package com.example.marvelapp.data.local

import androidx.room.*
import com.example.marvelapp.data.model.character.CharacterModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MarvelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (characterModel: CharacterModel) : Long

    @Query("SELECT * FROM charactermodel ORDER BY id")
    fun getAll(): Flow<List<CharacterModel>>

    @Delete
    suspend fun delete(characterModel: CharacterModel)

}