import argparse
import torch
import numpy as np
import pandas as pd
import shutil
import os

from recbole.quick_start import load_data_and_model

if __name__ == '__main__':

    # module.py 덮어쓰기
    source_path = "module.py"  # "/content/Babal-Server/module.py" # colab 경로
    target_path = os.path.join(torch.__path__[0], "nn/modules/module.py")
    shutil.copyfile(source_path, target_path)

    parser = argparse.ArgumentParser()
    parser.add_argument('--model_path', '-m', type=str, default='saved/model.pth', help='name of models')
    parser.add_argument('--item_id', '-i', type=int, required=True, help='item ID for recommendation')

    args, _ = parser.parse_known_args()

    # model, dataset 불러오기
    config, model, dataset, train_data, valid_data, test_data = load_data_and_model(args.model_path)

    # device 설정
    device = config.final_config_dict['device']

    # user, item id -> token 변환 array
    user_id2token = dataset.field2id_token['user_id']
    item_id2token = dataset.field2id_token['item_id']

    # user-item sparse matrix
    matrix = dataset.inter_matrix(form='csr')

    # 특정 사용자가 평가한 아이템
    liked_item_id = args.item_id
    users_who_liked_item = matrix[:, liked_item_id].nonzero()[0]  # 특정 아이템을 평가한 사용자 ID 찾기

    # 예측할 사용자 ID 및 아이템 ID 설정
    all_item_ids = np.arange(len(item_id2token))  # 모든 아이템 ID

    model.eval()
    top_n = 10
    recommendations = []  # 추천 결과를 저장할 리스트

    for user in users_who_liked_item:
        # 각 사용자별 상호작용한 아이템 인덱스 가져오기
        interacted_indices = matrix[user].indices  # 해당 사용자의 상호작용한 아이템 인덱스
        interaction = {
            'user_id': torch.tensor([user]).to(device),
            'item_id': torch.tensor(all_item_ids).to(device)
        }

        # 모델 추론
        scores = model.full_sort_predict(interaction).cpu().data.numpy()
        scores[liked_item_id] = 0  # 사용자가 이미 평가한 아이템 점수 0으로 설정

        # 상위 N개 아이템 추천
        recommended_item_indices = np.argsort(scores)[-top_n:][::-1]
        for item_index in recommended_item_indices:
            if item_index < len(item_id2token):
                recommendations.append((item_id2token[item_index], scores[item_index]))  # 아이템 ID와 점수 저장

    # 추천 점수 기준 상위 아이템 정렬 및 상위 아이템 출력
    sorted_recommendations = sorted(recommendations, key=lambda x: x[1], reverse=True)
    final_item = sorted_recommendations[0] if sorted_recommendations else None

    # 추천 아이템이 있을 경우 해당 아이템을 CSV로 저장, 없을 경우 빈 CSV 생성
    if final_item:
        final_item_id = final_item[0]  # 점수를 제외하고 ID만 가져오기
        final_dataframe = pd.DataFrame([[final_item_id]], columns=["recommended_item"])
    else:
        final_dataframe = pd.DataFrame(columns=["recommended_item"])  # 빈 데이터프레임 생성

    # CSV 저장
    final_dataframe.to_csv('RecBole_Sys/saved/recommendation.csv', index=False)
    print('Recommendation saved to CSV! (파일 생성 완료)')