package com.example.spacedrepetitionsystem.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spacedrepetitionsystem.domain.model.Flashcard

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val front: String,
    val back: String,
    val easiness: Double,
    val interval: Int,
    val repetitions: Int,
    val nextReviewDate: Long
) {
    // Hàm chuyển đổi từ Entity (Data) sang Model (Domain)
    fun toDomainModel(): Flashcard {
        return Flashcard(id, front, back, easiness, interval, repetitions, nextReviewDate)
    }
}

// Hàm chuyển đổi từ Model (Domain) sang Entity (Data)
fun Flashcard.toEntity(): FlashcardEntity {
    return FlashcardEntity(id, front, back, easiness, interval, repetitions, nextReviewDate)
}