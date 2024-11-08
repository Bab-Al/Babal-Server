import argparse
import torch
import numpy as np
import pandas as pd
import shutil
import os

from recbole.quick_start import load_data_and_model

if __name__ == '__main__':

    # module.py 덮어쓰기
    source_path = "/content/Babal-Server/module.py" # colab 경로
    target_path = os.path.join(torch.__path__[0], "nn/modules/module.py")
    shutil.copyfile(source_path, target_path)

    parser = argparse.ArgumentParser()
    parser.add_argument('--model_path', '-m', type=str, default='saved/model.pth', help='name of models')
    parser.add_argument('--item_id', '-i', type=int, required=True, help='item ID for recommendation')
    # python RecBole_Sys/RecBole-GNN/run_inference.py --model_path=RecBole_Sys/saved/NGCF-Oct-16-2024_19-14-27.pth 로 실행
    # C:\Users\alsrud\Downloads\Babal-Server\Babal-Server\RecBole\saved\NGCF-Oct-16-2024_19-14-27.pth

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

    # 특정 사용자의 아이템 ID
    liked_item_id = args.item_id  # 예시: 사용자가 좋아하는 아이템 ID

    # 사용자가 좋아하는 아이템을 좋아하는 사용자 ID 찾기
    users_who_liked_item = matrix[:, liked_item_id].nonzero()[0]

    # 예측할 사용자 ID 및 아이템 ID 설정
    user_ids = users_who_liked_item
    all_item_ids = np.arange(len(item_id2token))  # 모든 아이템 ID

    # 사용자-아이템 상호작용 객체 생성
    interaction = {
        'user_id': torch.tensor(user_ids).to(device),
        'item_id': torch.tensor(all_item_ids).to(device)
    }

    model.eval()
    # 모델을 사용해 추론
    scores = model.full_sort_predict(interaction)

    # 예측 결과 처리
    rating_pred = scores.cpu().data.numpy()  # 1차원 배열로 예상 점수 가져오기

    final_result = []

    # 각 사용자에 대해 상위 N개의 추천 아이템 추출
    top_n = 1
    for user in users_who_liked_item:
        # 사용자의 상호작용한 아이템 인덱스 가져오기
        interacted_indices = matrix[user].indices  # 해당 사용자의 상호작용한 아이템 인덱스
        rating_pred[interacted_indices] = 0  # 이미 좋아하는 아이템의 점수 0으로 설정

        # 상위 N개의 추천 아이템 인덱스 추출
        recommended_item_indices = np.argsort(rating_pred)[-top_n:][::-1]

        for item_index in recommended_item_indices:
            # 추천된 아이템이 유효한지 확인
            if item_index < len(item_id2token):
                # 추천된 아이템의 실제 ID 가져오기
                original_item_seq = item_id2token[item_index]
                final_result.append((user, original_item_seq))

    # 추천 결과를 DataFrame으로 변환하고 CSV 파일로 저장
    final_results = []
    for user, item in final_result:
        original_user_seq = user_id2token[user]
        final_results.append((original_user_seq, item))

    final_dataframe = pd.DataFrame(final_results, columns=["user", "recommended_item"])
    final_dataframe.to_csv('RecBole_Sys/saved/recommendation.csv', index=False)
    print('Recommendations saved to CSV!')