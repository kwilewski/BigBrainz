package com.narrowstudio.bigbrainz.di

import android.app.Application
import androidx.room.Room
import com.narrowstudio.bigbrainz.data.GameDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: GameDatabase.Callback
    ) = Room.databaseBuilder(app, GameDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    fun provideG2Dao(db: GameDatabase) = db.g2Dao()

    @Provides
    fun provideG1Dao(db: GameDatabase) = db.g1Dao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope