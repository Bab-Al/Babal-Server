<div align="center">
  
  # 🍙 Bab-Al 🍙 
  **AI-based Healthy Eating Habits Management Platform**  
  
  <br>
  <img src="https://github.com/user-attachments/assets/ed8c04ef-7b31-4ec4-b3ba-c794927d6fe5" alt="로고" width="200">   

  2024.03.06 ~ 2024.11.29

  <br>

  🔽🔽🔽  
  <a href="https://github.com/Bab-Al">Redirect to Main README</a>
  
</div>

<br>

## 🎀 Architecture
<div align="center">
  <img src="https://github.com/user-attachments/assets/e6c5c182-eaab-41b8-8e78-341b3253d45b">
</div>

<br>

## 🎭 TroubleShooting
<details>
<summary><b>🍽️ Improve accuracy and increase diversity of recommendations</b></summary>
  
> **Problem** : The content-based filtering algorithm using the cosine similarity comparison previously implemented has a limitation in that the accuracy of recommendations is poor and various recommendations cannot be made.
>
> **Solution** : Using the recbole framework, recommendations based on the neural graph collaborative filtering (NGCF) model provide recipe items that other users like.
>
> **Tech Blog**
> <ul>
>   <li><a href="https://alsrudalsrudalsrud.tistory.com/303">Analysis of recommendation system research trends 1 : Previously recommended systems</a></li>
>   <li><a href="https://alsrudalsrudalsrud.tistory.com/304">Analysis of recommendation system research trends 2 : GNN-based</a></li>
>   <li><a href="https://alsrudalsrudalsrud.tistory.com/305">Analysis of recommendation system research trends 3 : GNN-model</a></li>
> </ul>
  
</details>

<details>
<summary><b>🐍 Using Python in SpringBoot</b></summary>
  
> **Problem** : It was complicated to operate multiple servers for backend code and ai code when developed in an on-premises environment.
>
> **Solution** : Integrating back-end and ai servers, and add a 'jython' dependency to use python in spring boot, a back-end framework.
>
> **Tech Blog**
> <ul>
>   <li><a href="https://alsrudalsrudalsrud.tistory.com/336">Jython</a></li>
> </ul>
  
</details>

<details>
<summary><b>📜 Requirements Installation</b></summary>
  
> **Problem** : After I clone the RecBole framework to use in Spring Boot, the torch installation fails while executing the command 'pipe install -e. --verbose'.
>
> **Solution** : The Python version should be between 3.8 and 3.10, the Python and Pip versions must match, and the installed Python should be 64-bit.
>
> **Tech Blog**
> <ul>
>   <li><a href="https://alsrudalsrudalsrud.tistory.com/337">Could not find a version that satisfies the requirement torch>=1.10.0 (from recbole) (from versions: none)</a></li>
> </ul>
  
</details>

<details>
<summary><b>🦾 Preparations for using AI models</b></summary>
  
> **Problem** : I tried to train the Recbole dataset, but I kept getting this error despite the torch module. 'ModuleNotFoundError: No module named 'torch''.
>
> **Solution** : Install CUDA and Nvidia Driver, then reinstall PyTorch compatible with the corresponding CUDA version.
>
> **Tech Blog**
> <ul>
>   <li><a href="https://alsrudalsrudalsrud.tistory.com/341">error: subprocess-exited-with-error ~ ModuleNotFoundError: No module named 'torch'</a></li>
> </ul>
  
</details>
