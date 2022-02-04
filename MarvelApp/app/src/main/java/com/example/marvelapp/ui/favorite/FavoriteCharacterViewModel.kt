package com.example.marvelapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelapp.data.local.MarvelDao
import com.example.marvelapp.data.model.character.CharacterModel
import com.example.marvelapp.repository.MarvelRepository
import com.example.marvelapp.ui.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteCharacterViewModel@Inject constructor(private val repository: MarvelRepository): ViewModel() {

    private val _favorits = MutableStateFlow<ResourceState<List<CharacterModel>>>(ResourceState.Empty())
    val favorits: StateFlow<ResourceState<List<CharacterModel>>> = _favorits

    init {
       fetch()
    }

    fun fetch() = viewModelScope.launch {
        repository.getAll().collectLatest {
            if(it.isNullOrEmpty()){
                _favorits.value = ResourceState.Empty()
            }else{
                _favorits.value = ResourceState.Success(it)
            }
        }

    }


    fun delete(characterModel: CharacterModel) = viewModelScope.launch {
        repository.delete(characterModel)
    }

}