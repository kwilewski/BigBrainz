package com.narrowstudio.bigbrainz.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.narrowstudio.bigbrainz.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [G2DBEntry::class, G1DBEntry::class, G3DBEntry::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun g2Dao(): G2Dao
    abstract fun g1Dao(): G1Dao
    abstract fun g3Dao(): G3Dao

    class Callback @Inject constructor(
        private val database: Provider<GameDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao1 = database.get().g1Dao()
            val dao2 = database.get().g2Dao()
            val dao3 = database.get().g3Dao()

            applicationScope.launch {
            }
        }
    }
}