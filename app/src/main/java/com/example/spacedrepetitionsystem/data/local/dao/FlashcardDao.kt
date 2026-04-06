package com.example.spacedrepetitionsystem.data.local.dao

import androidx.room.*
import com.example.spacedrepetitionsystem.data.local.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    // 1. Lấy toàn bộ thẻ của CHÍNH người dùng đó (Để đồng bộ chéo thiết bị)
    @Query("SELECT * FROM flashcards WHERE userId = :userId ORDER BY nextReviewDate ASC")
    fun getAllFlashcards(userId: String): Flow<List<FlashcardEntity>>

    // 2. Lấy thẻ đến hạn ôn tập (SM-2) của CHÍNH người dùng đó
    @Query("SELECT * FROM flashcards WHERE userId = :userId AND nextReviewDate <= :currentTimestamp")
    fun getDueFlashcards(userId: String, currentTimestamp: Long): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity)

    @Update
    suspend fun updateFlashcard(flashcard: FlashcardEntity)

    @Delete
    suspend fun deleteFlashcard(flashcard: FlashcardEntity)

    // 3. Đếm số thẻ cần học của CHÍNH người dùng đó (Dành cho thông báo WorkManager)
    @Query("SELECT COUNT(*) FROM flashcards WHERE userId = :userId AND nextReviewDate <= :currentTimestamp")
    suspend fun countDueCardsSynchronously(userId: String, currentTimestamp: Long): Int
}