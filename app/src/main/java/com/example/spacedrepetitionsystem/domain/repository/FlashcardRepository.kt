package com.example.spacedrepetitionsystem.domain.repository

import com.example.spacedrepetitionsystem.domain.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {

    // 1. Lấy toàn bộ thẻ của một người dùng cụ thể để đồng bộ
    fun getAllFlashcards(userId: String): Flow<List<Flashcard>>

    // 2. Lấy các thẻ đến hạn ôn tập của người dùng [cite: 120]
    fun getDueFlashcards(userId: String, currentTimestamp: Long): Flow<List<Flashcard>>

    // 3. Thêm thẻ mới (Sẽ được đồng bộ lên Firestore)
    suspend fun insertFlashcard(flashcard: Flashcard)

    // 4. Cập nhật tiến độ SM-2 (Quan trọng nhất để đồng bộ tiến độ học)
    suspend fun updateFlashcard(flashcard: Flashcard)

    // 5. Xóa thẻ khỏi cả máy và Cloud
    suspend fun deleteFlashcard(flashcard: Flashcard)

    // 6. Dành cho Worker: Đếm số thẻ cần học để gửi thông báo (WorkManager) [cite: 122]
    suspend fun countDueCardsSynchronously(userId: String, currentTimestamp: Long): Int

    // 7. HÀM MỚI: Kích hoạt đồng bộ thủ công giữa Room và Firestore
    suspend fun syncWithBackend(userId: String)
}