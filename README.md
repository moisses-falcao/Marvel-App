# Marvel-App
App que desenvolvi em Kotlin. 
O App faz o consumo de uma API da Marvel com Retrofit, utiliza a arquitetura MVVM com Hilt para injeção de dependência e Room para a persistência de dados. 

Funcionalidades: O Aplicativo permite a pesquisa de personagens da Marvel Comics, os exibindo em uma lista. 
Ao selecionar um personagem é possível ver sua descrição e também a lista das revistas em quadrinhos em que o personagem já apareceu. 
O App também conta com o recurso "Favoritar", que permite guardar e excluir seus personagens prediletos uma lista de favoritos.  

O App utiliza Flow ao invés do LiveData, fazendo com que resultados sejam emitidos continuamente.


  
    ![video-app-marvel_k5A22egC (1)](https://user-images.githubusercontent.com/95506261/157915196-834fdb0a-d7d4-488d-a0df-d7ddd1be8406.gif)


