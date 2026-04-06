package com.example.spacedrepetitionsystem.data.repository

import com.example.spacedrepetitionsystem.data.local.dao.FlashcardDao
import com.example.spacedrepetitionsystem.data.local.entity.toEntity
import com.example.spacedrepetitionsystem.domain.model.Flashcard
import com.example.spacedrepetitionsystem.domain.repository.FlashcardRepository

class FlashcardRepositoryImpl(
    private val dao: FlashcardDao
) : FlashcardRepository {

    override suspend fun getDueFlashcards(currentTimestamp: Long): List<Flashcard> {
        // Lấy danh sách Entity từ Database và chuyển đổi (map) sang Domain Model
        return dao.getDueFlashcards(currentTimestamp).map { it.toDomainModel() }
    }

    override suspend fun insertFlashcard(flashcard: Flashcard) {
        dao.insertFlashcard(flashcard.toEntity())
    }

    override suspend fun updateFlashcard(flashcard: Flashcard) {
        dao.updateFlashcard(flashcard.toEntity())
    }
}