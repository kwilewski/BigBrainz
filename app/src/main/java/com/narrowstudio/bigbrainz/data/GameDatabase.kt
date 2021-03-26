package com.narrowstudio.bigbrainz.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.narrowstudio.bigbrainz.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [G2DBEntry::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun g2Dao(): G2Dao

    class Callback @Inject constructor(
        private val database: Provider<GameDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().g2Dao()

            applicationScope.launch {
                //dao.insert(G2DBEntry(201))
            }
        }
    }
}