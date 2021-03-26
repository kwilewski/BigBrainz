package com.narrowstudio.bigbrainz.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat

@Entity(tableName = "game_2_table")
data class G2DBEntry(
    val gameID: Int,
    val time: ArrayList<Long>,
    val averageTime: Long,
    val saveDate: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
    ) {
    val saveDateFormatted: String
    get() = DateFormat.getDateTimeInstance().format(saveDate)



}
