package com.narrowstudio.bigbrainz.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface G3Dao {

    @Query("SELECT * FROM game_3_table")
    fun getEntries(): Flow<List<G3DBEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: com.narrowstudio.bigbrainz.data.G3DBEntry)

    @Update
    suspend fun update(entry: G3DBEntry)

    @Delete
    suspend fun delete(entry: G3DBEntry)
}