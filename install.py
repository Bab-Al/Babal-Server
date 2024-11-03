import os

# PyTorch와 관련 패키지 설치
os.system('pip install torch==2.4.0 torchvision==0.19.0 torchaudio==2.4.0 --index-url https://download.pytorch.org/whl/cu121')
os.system('pip install torch_scatter -f https://pytorch-geometric.com/whl/torch-2.4.0+cu121.html')
os.system('pip install torch_sparse -f https://pytorch-geometric.com/whl/torch-2.4.0+cu121.html')
os.system('pip install torch_cluster -f https://pytorch-geometric.com/whl/torch-2.4.0+cu121.html')
os.system('pip install torch_spline_conv -f https://pytorch-geometric.com/whl/torch-2.4.0+cu121.html')
os.system('pip install torch-geometric -f https://pytorch-geometric.com/whl/torch-2.4.0+cu121.html')