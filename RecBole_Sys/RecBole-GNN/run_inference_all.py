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

    # 예측할 모든 아이템 ID 오름차순 정렬 후 상위 _개 추출
    all_item_ids = np.arange(len(item_id2token))

    model.eval()
    top_n = 1
    recommendations = []  # 추천 결과를 저장할 리스트

    for item_id in all_item_ids:
        print(f"{item_id + 1}/{len(item_id2token)}", end='\r', flush=True)  # \r로 동일 줄에 출력

        users_who_liked_item = matrix[:, item_id].nonzero()[0]  # 특정 아이템을 평가한 사용자 ID 찾기

        # 각 사용자별 상호작용한 아이템 인덱스 가져오기
        for user in users_who_liked_item:
            interaction = {
                'user_id': torch.tensor([user]).to(device),
                'item_id': torch.tensor(all_item_ids).to(device)
            }

            # 모델 추론
            scores = model.full_sort_predict(interaction).cpu().data.numpy()
            scores[item_id] = -np.inf  # 사용자가 이미 평가한 아이템 점수 무효화

            # 상위 1개 아이템 추천
            recommended_item_index = np.argsort(scores)[-top_n:][::-1][0]
            recommendations.append((item_id2token[item_id], item_id2token[recommended_item_index]))  # 아이템 ID와 추천된 아이템 ID 저장
            break  # 첫 번째 사용자만 사용하여 추론 후 반복 중단

    # 추천 결과를 DataFrame으로 변환
    recommendation_df = pd.DataFrame(recommendations, columns=["item_id", "recommended_item_id"])

    # CSV로 저장
    recommendation_df.to_csv('RecBole_Sys/saved/recommendation.csv', index=False)
    print('Recommendation CSV saved successfully!')
