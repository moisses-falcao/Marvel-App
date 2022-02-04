package com.example.marvelapp.repository

import com.example.marvelapp.data.local.MarvelDao
import com.example.marvelapp.data.model.character.CharacterModel
import com.example.marvelapp.data.remote.ServiceApi
import javax.inject.Inject

class MarvelRepository @Inject constructor(
    private val api: ServiceApi,
    private val dao: MarvelDao

) {
    suspend fun list(nameStatsWith: String? = null) = api.list(nameStatsWith)
    suspend fun getComics(characterId: Int) = api.getComics(characterId)

    suspend fun insert(characterModel: CharacterModel) = dao.insert(characterModel)
    suspend fun delete(characterModel: CharacterModel) = dao.delete(characterModel)
    fun getAll() = dao.getAll()

}