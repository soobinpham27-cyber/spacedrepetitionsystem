package com.example.spacedrepetitionsystem.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spacedrepetitionsystem.data.local.dao.FlashcardDao
import com.example.spacedrepetitionsystem.data.local.entity.FlashcardEntity

@Database(entities = [FlashcardEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
}