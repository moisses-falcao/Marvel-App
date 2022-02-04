package com.example.marvelapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelapp.data.model.character.CharacterModelResponse
import com.example.marvelapp.repository.MarvelRepository
import com.example.marvelapp.ui.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ListCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
): ViewModel() {

    private val _list = MutableStateFlow<ResourceState<CharacterModelResponse>>(ResourceState.Loading())
    val list: StateFlow<ResourceState<CharacterModelResponse>> = _list

    init{
        fetch()
    }

    private fun fetch() = viewModelScope.launch {
        safeFetch()
    }

    private suspend fun safeFetch() {
        try {
            val response = repository.list()
            _list.value = handleResponse(response)
        }catch (t: Throwable){
            when(t){
                is IOException -> _list.value = ResourceState.Error("Erro de conexão com a internet")
                else -> _list.value = ResourceState.Error("Falha na conversão de dados")
            }
        }
    }

    private fun handleResponse(response: Response<CharacterModelResponse>): ResourceState<CharacterModelResponse> {
        if(response.isSuccessful){
            response.body()?.let{ values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }
}