from flask import Flask, request, jsonify
import pandas as pd
import numpy as np
import ast
import os
import requests

app = Flask(__name__)

def checkFile(url, local_filename):
    # 파일이 로컬에 존재하는지 확인
    if not os.path.exists(local_filename):
        print(local_filename + "파일이 존재하지 않습니다. S3에서 다운로드 중...")
        response = requests.get(url)
        # 파일 다운로드 후 저장
        with open(local_filename, 'wb') as f:
            f.write(response.content)
        print(local_filename + "파일 다운로드 및 저장 완료!")
    else:
        print("로컬에 "+local_filename+" 파일이 이미 존재합니다. 저장된 파일을 불러옵니다.")


# 필요한 파일 다운로드
recipeUrl = "https://babal-bucket.s3.ap-northeast-2.amazonaws.com/RAW_recipes.csv"
localRecipe = "RAW_recipes.csv"
checkFile(recipeUrl, localRecipe)

userUrl = "https://babal-bucket.s3.ap-northeast-2.amazonaws.com/preferred_data.csv"
localUser = "preferred_data.csv"
checkFile(userUrl, localUser)

popularUrl = "https://babal-bucket.s3.ap-northeast-2.amazonaws.com/popular_items.csv"
localPopular = "popular_items.csv"
checkFile(popularUrl, localPopular)

recommendationUrl = "https://babal-bucket.s3.ap-northeast-2.amazonaws.com/recommendation.csv"
localRecommend = "recommendation.csv"
checkFile(recommendationUrl, localRecommend)


# 안전하게 열을 리스트로 변환
def safe_eval(val):
    try:
        # 리스트 형식으로 변환
        return ast.literal_eval(val)
    except (ValueError, SyntaxError):
        # 변환에 실패하면 그대로 반환
        return val


# 내 정보 기반 추천
def my_recommendation(age, gender, user_ingredients, user_tags):
    # 파일을 pandas DataFrame으로 읽기
    data = pd.read_csv("RAW_recipes.csv")

    # 안전하게 열을 리스트로 변환
    data['ingredients'] = data['ingredients'].apply(safe_eval)
    data['tags'] = data['tags'].apply(safe_eval)
    data['steps'] = data['steps'].apply(safe_eval)

    # 일치하는 재료 수 계산 함수
    def match_score(recipe_ingredients):
        if isinstance(recipe_ingredients, list):
            # 교집합을 통해 일치하는 재료 수를 계산
            return len(user_ingredients.intersection(recipe_ingredients))
        return 0

    # 태그 일치도 계산 함수
    def tag_score(recipe_tags):
        if isinstance(recipe_tags, list):
            if len(user_tags) == 0:  # user_tags가 비어 있는지 확인
                return 0  # 비어 있으면 0을 반환
            return len(set(recipe_tags).intersection(user_tags)) / len(user_tags)
        return 0

    # 각 레시피에 대해 일치하는 재료 수를 계산하여 새로운 열에 추가
    data['match_score'] = data['ingredients'].apply(match_score)
    data['tag_score'] = data['tags'].apply(tag_score)

    # 최종 점수를 계산하여 새로운 열에 추가 (가중치 조정 가능)
    tag_weight = 0.3
    data['final_score'] = data['match_score'] + (data['tag_score'] * tag_weight)

    # 최종 점수에 따라 정렬 및 필터링
    filtered_recipes = data[data['match_score'] > 0].sort_values(by='final_score', ascending=False)
    filtered_recipes = filtered_recipes.head(10)
    print(filtered_recipes)

    # nutrition 열을 리스트로 변환
    # nutrition : [칼로리(0), 당(1), 나트륨(2), 지방(3), 단백질(4), 포화 지방(5), 탄수화물(6)]
    filtered_recipes['nutrition'] = filtered_recipes['nutrition'].apply(lambda x: ast.literal_eval(x) if isinstance(x, str) else x)
    filtered_recipes['kcal'] = filtered_recipes['nutrition'].apply(lambda x: x[0] if isinstance(x, list) else None)
    filtered_recipes['carbo'] = filtered_recipes['nutrition'].apply(lambda x: x[6] if isinstance(x, list) else None)
    filtered_recipes['protein'] = filtered_recipes['nutrition'].apply(lambda x: x[4] if isinstance(x, list) else None)
    filtered_recipes['fat'] = filtered_recipes['nutrition'].apply(lambda x: x[3] if isinstance(x, list) else None)

    # 최대/최소 칼로리 값 계산
    kcal_max = filtered_recipes['kcal'].max()
    kcal_min = filtered_recipes['kcal'].min()

    carbo_max = filtered_recipes['carbo'].max()
    carbo_min = filtered_recipes['carbo'].min()

    protein_max = filtered_recipes['protein'].max()
    protein_min = filtered_recipes['protein'].min()

    fat_max = filtered_recipes['fat'].max()
    fat_min = filtered_recipes['fat'].min()

    # 정규화 표 생성하기
    normalized_data = filtered_recipes[['name', 'id', 'minutes', 'tags', 'nutrition', 'n_steps', 'steps', 'n_ingredients', 'ingredients', 'match_score']].copy()
    normalized_data['에너지(kcal)_정규화'] = (filtered_recipes['kcal'] - kcal_min) / (kcal_max - kcal_min)
    normalized_data['탄수화물(g)_정규화'] = (filtered_recipes['carbo'] - carbo_min) / (carbo_max - carbo_min)
    normalized_data['단백질(g)_정규화'] = (filtered_recipes['protein'] - protein_min) / (protein_max - protein_min)
    normalized_data['지방(g)_정규화'] = (filtered_recipes['fat'] - fat_min) / (fat_max - fat_min)

    # 사용자 벡터 구하기
    # 파일을 pandas DataFrame으로 읽기
    user_data = pd.read_csv("preferred_data.csv", encoding='cp949')

    if gender == 'WOMAN':
        gender = '여자'
    else:
        gender = '남자'

    if 1 <= age <= 5:
        age = '유아'

    filtered_user_data = user_data[(user_data['성별'] == gender) & (user_data['연령'] == age)]
    user_preffered_energy = filtered_user_data['필요 추정 에너지(kcal/일)'].values[0] / 3
    user_preffered_carbo = filtered_user_data['권장 섭취 탄수화물량(g/일)'].values[0] / 3
    user_preffered_protein = filtered_user_data['권장 섭취 단백질량(g/일)'].values[0] / 3
    user_preffered_fat = filtered_user_data['권장 섭취 지방량(g/일)'].values[0] / 3

    normalized_user_preffered_energy = (user_preffered_energy - kcal_min) / (kcal_max - kcal_min)
    normalized_user_preffered_carbo = (user_preffered_carbo - carbo_min) / (carbo_max - carbo_min)
    normalized_user_preffered_protein = (user_preffered_protein - protein_min) / (protein_max - protein_min)
    normalized_user_preffered_fat = (user_preffered_fat - fat_min) / (fat_max - fat_min)

    user_vector = np.array([normalized_user_preffered_energy, normalized_user_preffered_carbo, normalized_user_preffered_protein, normalized_user_preffered_fat])

    # 음식의 정규화된 특성 벡터
    food_vectors = normalized_data[['에너지(kcal)_정규화', '탄수화물(g)_정규화', '단백질(g)_정규화', '지방(g)_정규화']].values

    # 벡터의 크기 계산
    food_norms = np.linalg.norm(food_vectors, axis=1)
    user_norm = np.linalg.norm(user_vector)

    # 0으로 나눗셈을 방지하여 코사인 유사도 계산
    if user_norm == 0 or np.any(food_norms == 0):
        cosine_similarities = np.zeros(len(food_vectors))
    else:
        cosine_similarities = np.dot(food_vectors, user_vector) / (food_norms * user_norm)

    if len(cosine_similarities) == 0:
        return None, None;

    # 가장 유사한 음식 찾기 (top_n을 1로 설정)
    top_n = 1
    top_food_index = np.argsort(cosine_similarities)[::-1][:top_n][0]

    # JSON 응답 구성
    response01 = {
        "name": filtered_recipes.iloc[top_food_index]['name'],
        "minutes": str(filtered_recipes.iloc[top_food_index]['minutes']),
        "calories": float(filtered_recipes.iloc[top_food_index]['nutrition'][0]),
        "carbohydrates": float(filtered_recipes.iloc[top_food_index]['nutrition'][6]),
        "protein": float(filtered_recipes.iloc[top_food_index]['nutrition'][4]),
        "fat": float(filtered_recipes.iloc[top_food_index]['nutrition'][3]),
        "n_steps": int(filtered_recipes.iloc[top_food_index]['n_steps']),
        "steps": list(filtered_recipes.iloc[top_food_index]['steps']),
        "n_ingredients": int(filtered_recipes.iloc[top_food_index]['n_ingredients']),
        "ingredients": list(filtered_recipes.iloc[top_food_index]['ingredients']),
    }
    return response01, filtered_recipes.iloc[top_food_index]['id']


def others_recommendation(recommended_id):
    # 모든 id 에 대한 추천 들어있는 csv 파일 불러오기
    data = pd.read_csv("recommendation.csv")

    # 추천 데이터 매핑
    filtered_data = data.loc[data['item_id'] == recommended_id]

    # 비어 있는 경우 예외 처리
    if filtered_data.empty:
        return None

    recommendedData_id = filtered_data['recommended_item_id'].iloc[0]

    recipeData = pd.read_csv("RAW_recipes.csv")
    recommendedData = recipeData[recipeData['id']==recommendedData_id]

    # 리스트 데이터를 안전하게 변환
    recommendedData.loc[:, 'steps'] = recommendedData['steps'].apply(safe_eval)
    recommendedData.loc[:, 'ingredients'] = recommendedData['ingredients'].apply(safe_eval)

    # JSON 응답 구성
    nutrition_str = recommendedData.iloc[0]['nutrition']
    nutrition_list = ast.literal_eval(nutrition_str)
    response = {
        "name": recommendedData.iloc[0]['name'],
        "minutes": str(recommendedData.iloc[0]['minutes']),
        "calories": float(nutrition_list[0]),
        "carbohydrates": float(nutrition_list[6]),
        "protein": float(nutrition_list[4]),
        "fat": float(nutrition_list[3]),
        "n_steps": int(recommendedData.iloc[0]['n_steps']),
        "steps": list(recommendedData.iloc[0]['steps']),
        "n_ingredients": int(recommendedData.iloc[0]['n_ingredients']),
        "ingredients": list(recommendedData.iloc[0]['ingredients']),
    }
    return response


def popular_recommendation():
    # 파일을 pandas DataFrame으로 읽기
    data = pd.read_csv("popular_items.csv")

    # 'item_id' 컬럼에서 무작위로 하나를 선택합니다.
    random_item_id = data['item_id'].sample(1).iloc[0]

    # RAW_recipe.csv
    recipeData = pd.read_csv("RAW_recipes.csv")

    # 추천 데이터 매핑
    recommendedData = recipeData[recipeData['id']==random_item_id]
    recommendedData.loc[:, 'steps'] = recommendedData['steps'].apply(safe_eval)
    recommendedData.loc[:, 'ingredients'] = recommendedData['ingredients'].apply(safe_eval)

    # JSON 응답 구성
    nutrition_str = recommendedData.iloc[0]['nutrition']
    nutrition_list = ast.literal_eval(nutrition_str)
    response = {
        "name": recommendedData.iloc[0]['name'],
        "minutes": str(recommendedData.iloc[0]['minutes']),
        "calories": float(nutrition_list[0]),
        "carbohydrates": float(nutrition_list[6]),
        "protein": float(nutrition_list[4]),
        "fat": float(nutrition_list[3]),
        "n_steps": int(recommendedData.iloc[0]['n_steps']),
        "steps": list(recommendedData.iloc[0]['steps']),
        "n_ingredients": int(recommendedData.iloc[0]['n_ingredients']),
        "ingredients": list(recommendedData.iloc[0]['ingredients']),
    }
    return response


# 레시피 추천
@app.route('/recipeRecommendations', methods=['POST'])
def recipeRecommendations():
    # Spring 으로부터 전달받은 JSON 객체
    user_info = request.get_json()
    age = user_info.get('age')
    gender = user_info.get('gender')
    user_ingredients = set(user_info.get('ingredients', []))  # List를 Set으로 변환
    user_tags = set(user_info.get('tags', []))  # List를 Set으로 변환
    print(str(age) + "  " + str(gender) + "  " + str(user_ingredients) + "  " + str(user_tags))

    response = []
    # 1. 내 정보 기반 추천
    response01, recommended_id = my_recommendation(age, gender, user_ingredients, user_tags)
    if response01 is None:
        # 3. 인기 아이템 추천
        response03 = popular_recommendation()
        response.append(response03)
    else:
        response.append(response01)

    # 2. 내 정보 기반 다른 사람 추천
    response02 = others_recommendation(recommended_id)

    # response02가 None이면 response03을 실행해서 추가
    if response02 is None:
        # 3. 인기 아이템 추천
        response03 = popular_recommendation()
        response.append(response03)
    else:
        response.append(response02)

    # 응답 반환
    return jsonify(response)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)