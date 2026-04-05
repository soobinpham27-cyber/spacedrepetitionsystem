package com.example.spacedrepetitionsystem.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.spacedrepetitionsystem.data.local.entity.FlashcardEntity

@Dao
interface FlashcardDao {
    // Lấy tất cả các thẻ có ngày ôn tập bé hơn hoặc bằng thời gian hiện tại
    @Query("SELECT * FROM flashcards WHERE nextReviewDate <= :currentTimestamp")
    suspend fun getDueFlashcards(currentTimestamp: Long): List<FlashcardEntity>

    // Thêm thẻ mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity)

    // Cập nhật thẻ sau khi học xong
    @Update
    suspend fun updateFlashcard(flashcard: FlashcardEntity)
}