package com.example.spacedrepetitionsystem.di

import android.content.Context
import androidx.room.Room
import com.example.spacedrepetitionsystem.data.local.AppDatabase
import com.example.spacedrepetitionsystem.data.local.dao.FlashcardDao
import com.example.spacedrepetitionsystem.data.repository.FlashcardRepositoryImpl
import com.example.spacedrepetitionsystem.domain.repository.FlashcardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "flashcard_db"
        ).build()
    }

    @Provides
    fun provideFlashcardDao(database: AppDatabase): FlashcardDao {
        return database.flashcardDao()
    }

    @Provides
    @Singleton
    fun provideFlashcardRepository(dao: FlashcardDao): FlashcardRepository {
        return FlashcardRepositoryImpl(dao)
    }
}