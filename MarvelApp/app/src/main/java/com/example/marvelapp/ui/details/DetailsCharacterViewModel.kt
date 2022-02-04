package com.example.marvelapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.example.marvelapp.data.model.character.CharacterModel
import com.example.marvelapp.data.model.comic.ComicModel
import com.example.marvelapp.data.model.comic.ComicModelResponse
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
class DetailsCharacterViewModel @Inject constructor(private val repository: MarvelRepository): ViewModel() {

    private val _details = MutableStateFlow<ResourceState<ComicModelResponse>>(ResourceState.Loading())
    val details: StateFlow<ResourceState<ComicModelResponse>> = _details

    fun fetch(characterId: Int) = viewModelScope.launch {
        safefetch(characterId)
    }

    private suspend fun safefetch(characterId: Int) {
        _details.value = ResourceState.Loading()
        try {
            val response = repository.getComics(characterId)
            _details.value = handleResponse(response)
        } catch (t: Throwable){
            when(t){
                is IOException -> _details.value = ResourceState.Error("Erro de conexão com a internet")
                else -> _details.value = ResourceState.Error("Erro na conversão")
            }
        }

    }

    private fun handleResponse(response: Response<ComicModelResponse>): ResourceState<ComicModelResponse> {
        if (response.isSuccessful){
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }

    fun insert(characterModel: CharacterModel) = viewModelScope.launch {
        repository.insert(characterModel)
    }

}