package com.narrowstudio.bigbrainz.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "game_1_table")
@Parcelize
data class G1DBEntry(
    val gameID: Int,
    val score: Int,
    val saveDate: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
    ): Parcelable{
    val saveDateFormatted: String
    get() = DateFormat.getDateTimeInstance().format(saveDate)



}
