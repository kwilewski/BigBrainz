package com.narrowstudio.bigbrainz.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface G2Dao {

    @Query("SELECT * FROM game_2_table")
    fun getEntries(): Flow<List<G2DBEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: com.narrowstudio.bigbrainz.data.G2DBEntry)

    @Update
    suspend fun update(entry: G2DBEntry)

    @Delete
    suspend fun delete(entry: G2DBEntry)
}