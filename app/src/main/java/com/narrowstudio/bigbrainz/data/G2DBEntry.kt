package com.narrowstudio.bigbrainz.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "game_2_table")
@Parcelize
data class G2DBEntry(
    val gameID: Int,
    //val time: List<Long>,
    val averageTime: Long,
    val saveDate: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
    ): Parcelable{
    val saveDateFormatted: String
    get() = DateFormat.getDateTimeInstance().format(saveDate)



}
