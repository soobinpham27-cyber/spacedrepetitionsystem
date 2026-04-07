package com.example.spacedrepetitionsystem.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spacedrepetitionsystem.domain.model.Flashcard

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey
    val id: String, // Khớp kiểu String UUID với Domain
    val userId: String, // Thêm trường này để lọc thẻ theo người dùng
    val front: String,
    val back: String,
    val easiness: Double,
    val interval: Int,
    val repetitions: Int,
    val nextReviewDate: Long
)

/**
 * Extension functions để chuyển đổi qua lại giữa Database và Giao diện
 */

// 1. Chuyển từ Database (Entity) -> Giao diện (Domain Model)
fun FlashcardEntity.toDomain(): Flashcard {
    return Flashcard(
        id = id,
        userId = userId,
        front = front,
        back = back,
        easiness = easiness,
        interval = interval,
        repetitions = repetitions,
        nextReviewDate = nextReviewDate
    )
}

// 2. Chuyển từ Giao diện (Domain Model) -> Database (Entity)
fun Flashcard.toEntity(): FlashcardEntity {
    return FlashcardEntity(
        id = id,
        userId = userId,
        front = front,
        back = back,
        easiness = easiness,
        interval = interval,
        repetitions = repetitions,
        nextReviewDate = nextReviewDate
    )
}