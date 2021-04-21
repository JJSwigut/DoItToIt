package com.jjswigut.data.local.di

import android.app.Application
import androidx.room.Room
import com.jjswigut.data.local.MainDatabase
import com.jjswigut.data.local.dao.Dao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideMainDatabase(application: Application): MainDatabase {
        return Room
            .databaseBuilder(application, MainDatabase::class.java, "MainDatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: MainDatabase): Dao {
        return appDatabase.dao()
    }
}