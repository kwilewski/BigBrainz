package com.narrowstudio.bigbrainz.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface G1Dao {

    @Query("SELECT * FROM game_1_table")
    fun getEntries(): Flow<List<G1DBEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: com.narrowstudio.bigbrainz.data.G1DBEntry)

    @Update
    suspend fun update(entry: G1DBEntry)

    @Delete
    suspend fun delete(entry: G1DBEntry)
}