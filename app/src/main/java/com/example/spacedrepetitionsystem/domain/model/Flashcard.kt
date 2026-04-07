package com.example.spacedrepetitionsystem.domain.model

import java.util.UUID
import kotlin.math.roundToInt

/**
 * Domain Model: Đại diện cho một thẻ Flashcard trong ứng dụng SRS[cite: 119].
 * Dùng String ID để hỗ trợ đồng bộ dữ liệu lên Firebase Firestore.
 */
data class Flashcard(
    val id: String = UUID.randomUUID().toString(), // Đổi sang String để tránh trùng lặp khi đồng bộ
    val userId: String, // ID người dùng từ Firebase Authentication để phân biệt dữ liệu
    val front: String,
    val back: String,
    val easiness: Double = 2.5,
    val interval: Int = 0,
    val repetitions: Int = 0,
    val nextReviewDate: Long = System.currentTimeMillis()
) {
    /**
     * Thuật toán SM-2: Tự động lên lịch ôn tập dựa trên độ khó.
     */
    fun calculateNextReview(quality: Int): Flashcard {
        var newEasiness = easiness
        var newInterval = interval
        var newRepetitions = repetitions

        // Nếu câu trả lời là Nhớ (3, 4, 5)
        if (quality >= 3) {
            newRepetitions += 1
            newInterval = when (newRepetitions) {
                1 -> 1
                2 -> 6
                else -> (newInterval * newEasiness).roundToInt()
            }
        } else {
            // Nếu Quên (0, 1, 2) - Reset lại từ đầu
            newRepetitions = 0
            newInterval = 1
        }

        // Cập nhật hệ số dễ (Easiness Factor) theo chuẩn SM-2
        newEasiness += 0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02)
        if (newEasiness < 1.3) newEasiness = 1.3

        // Tính toán ngày ôn tập tiếp theo (ms)
        val oneDayInMillis = 24 * 60 * 60 * 1000L
        val nextDate = System.currentTimeMillis() + (newInterval * oneDayInMillis)

        // Trả về bản sao thẻ mới với tiến độ đã cập nhật
        return this.copy(
            easiness = newEasiness,
            interval = newInterval,
            repetitions = newRepetitions,
            nextReviewDate = nextDate
        )
    }
}