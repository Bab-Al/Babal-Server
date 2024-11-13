import numpy as np
import pandas as pd
from scipy.sparse import csr_matrix
from recbole.quick_start import load_data_and_model

def save_popular_items_csv(model_path, top_n=100, output_file='popular_items.csv'):
    # Load model and dataset
    config, model, dataset, train_data, valid_data, test_data = load_data_and_model(model_path)

    # Get item_id -> token mapping
    item_id2token = dataset.field2id_token['item_id']

    # Get user-item interaction matrix in CSR format
    matrix = dataset.inter_matrix(form='csr')

    # Calculate each item's interaction count (popularity)
    item_counts = matrix.getnnz(axis=0)  # Number of non-zero entries for each item (consumption count)

    # Find top N items by consumption count
    top_items_indices = np.argsort(item_counts)[-top_n:][::-1]  # Indices of top N items in descending order
    top_items = [(item_id2token[item_id], item_counts[item_id]) for item_id in top_items_indices]

    # Convert to DataFrame
    popular_items_df = pd.DataFrame(top_items, columns=["item_id", "consumption_count"])

    # Save to CSV
    popular_items_df.to_csv(output_file, index=False)
    print(f'Popular items saved to {output_file} successfully!')

# Usage
model_path = 'RecBole_Sys/saved/NGCF-Oct-16-2024_19-14-27.pth'  # Replace with your model path
save_popular_items_csv(model_path, top_n=100, output_file='RecBole_Sys/saved/popular_items.csv')
