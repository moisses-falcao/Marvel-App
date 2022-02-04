package com.example.marvelapp.ui.search

import androidx.lifecycle.ViewModel
import com.example.marvelapp.data.model.character.CharacterModelResponse
import com.example.marvelapp.repository.MarvelRepository
import com.example.marvelapp.ui.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SearchCharacterViewModel @Inject constructor(private val repository: MarvelRepository ) : ViewModel() {

    private val _searchCharacter = MutableStateFlow<ResourceState<CharacterModelResponse>>(ResourceState.Empty())
    val searchCharacter: MutableStateFlow<ResourceState<CharacterModelResponse>> = _searchCharacter

    suspend fun fetch(nameStartsWith: String){
        _searchCharacter.value = ResourceState.Loading()
        try {
            val response = repository.list(nameStartsWith)
            _searchCharacter.value = handleResponse(response)
        } catch (t: Throwable){
            when (t){
                is IOException -> _searchCharacter.value = ResourceState.Error("Erro na rede")
                else -> _searchCharacter.value = ResourceState.Error("Erro na conversão")
                }
            }
        }

    private fun handleResponse(response: Response<CharacterModelResponse>): ResourceState<CharacterModelResponse>{

        if (response.isSuccessful){
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }

}