package com.example.spacedrepetitionsystem.data.repository

import android.util.Log
import com.example.spacedrepetitionsystem.data.local.dao.FlashcardDao
import com.example.spacedrepetitionsystem.data.local.entity.toDomain
import com.example.spacedrepetitionsystem.data.local.entity.toEntity
import com.example.spacedrepetitionsystem.domain.model.Flashcard
import com.example.spacedrepetitionsystem.domain.repository.FlashcardRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FlashcardRepositoryImpl @Inject constructor(
    private val dao: FlashcardDao,
    private val firestore: FirebaseFirestore
) : FlashcardRepository {

    override fun getAllFlashcards(userId: String): Flow<List<Flashcard>> {
        return dao.getAllFlashcards(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getDueFlashcards(userId: String, currentTimestamp: Long): Flow<List<Flashcard>> {
        return dao.getDueFlashcards(userId, currentTimestamp).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // 🚀 SỬA TẠI ĐÂY: Đồng bộ hóa "Mạnh mẽ"
    override suspend fun insertFlashcard(flashcard: Flashcard) {
        // 1. Luôn lưu vào Room trước để UI hiện ra ngay lập tức (Offline-first)
        dao.insertFlashcard(flashcard.toEntity())

        // 2. Đẩy lên Firestore (Lưu vào collection "flashcards")
        if (flashcard.userId.isNotBlank()) {
            try {
                firestore.collection("flashcards")
                    .document(flashcard.id)
                    .set(flashcard.toEntity()) // Dùng Entity để Firebase dễ parse
                    .await()
                Log.d("FirebaseSync", "Đã lưu thẻ lên Cloud thành công!")
            } catch (e: Exception) {
                Log.e("FirebaseSync", "Lỗi đẩy dữ liệu lên Cloud: ${e.message}")
            }
        }
    }

    override suspend fun updateFlashcard(flashcard: Flashcard) {
        dao.updateFlashcard(flashcard.toEntity())
        if (flashcard.userId.isNotBlank()) {
            try {
                firestore.collection("flashcards")
                    .document(flashcard.id)
                    .set(flashcard.toEntity())
                    .await()
            } catch (e: Exception) {
                Log.e("FirebaseSync", "Lỗi cập nhật Cloud: ${e.message}")
            }
        }
    }

    override suspend fun deleteFlashcard(flashcard: Flashcard) {
        dao.deleteFlashcard(flashcard.toEntity())
        if (flashcard.userId.isNotBlank()) {
            try {
                firestore.collection("flashcards")
                    .document(flashcard.id)
                    .delete()
                    .await()
            } catch (e: Exception) {
                Log.e("FirebaseSync", "Lỗi xóa trên Cloud: ${e.message}")
            }
        }
    }

    override suspend fun countDueCardsSynchronously(userId: String, currentTimestamp: Long): Int {
        return dao.countDueCardsSynchronously(userId, currentTimestamp)
    }

    // 🔄 Hàm tải dữ liệu về khi người dùng đổi máy (Sync)
    override suspend fun syncWithBackend(userId: String) {
        try {
            val snapshot = firestore.collection("flashcards")
                .whereEqualTo("userId", userId) // Chỉ lấy thẻ của đúng user này
                .get()
                .await()

            val entities = snapshot.toObjects(com.example.spacedrepetitionsystem.data.local.entity.FlashcardEntity::class.java)

            // Cập nhật lại Database cục bộ
            entities.forEach { dao.insertFlashcard(it) }
            Log.d("FirebaseSync", "Đã tải thành công ${entities.size} thẻ từ Cloud về máy!")
        } catch (e: Exception) {
            Log.e("FirebaseSync", "Lỗi đồng bộ về máy: ${e.message}")
        }
    }
}