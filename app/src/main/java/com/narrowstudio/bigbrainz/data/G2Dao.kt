package com.narrowstudio.bigbrainz.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface G2Dao {

    @Query("SELECT * FROM game_2_table")
    fun getEntry(): Flow<List<G2Dao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: G2Dao)

    @Update
    suspend fun update(entry: G2Dao)

    @Delete
    suspend fun delete(entry: G2Dao)
}