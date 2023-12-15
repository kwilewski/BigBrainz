package com.narrowstudio.bigbrainz.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "game_3_table")
@Parcelize
data class G3DBEntry(
    val gameID: Int,
    val time: Int,
    // stores the length of test sample
    val testSample: Int,
    // stores the amount of all clicks (accurate and not)
    val taps: Int,
    val saveDate: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
    ): Parcelable{
    val saveDateFormatted: String
    get() = DateFormat.getDateTimeInstance().format(saveDate)



}
