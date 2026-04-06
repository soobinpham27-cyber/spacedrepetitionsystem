package com.example.spacedrepetitionsystem.domain.repository

import com.example.spacedrepetitionsystem.domain.model.Flashcard

interface FlashcardRepository {
    suspend fun getDueFlashcards(currentTimestamp: Long): List<Flashcard>
    suspend fun insertFlashcard(flashcard: Flashcard)
    suspend fun updateFlashcard(flashcard: Flashcard)
}