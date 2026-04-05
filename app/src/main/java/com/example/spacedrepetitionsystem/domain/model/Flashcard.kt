package com.example.spacedrepetitionsystem.domain.model

import kotlin.math.roundToInt

data class Flashcard(
    val id: Int = 0, // ID tự tăng khi lưu vào Database
    val front: String, // Mặt trước (Câu hỏi)
    val back: String, // Mặt sau (Câu trả lời)
    val easiness: Double = 2.5, // Hệ số dễ (E-Factor), khởi điểm luôn là 2.5 theo chuẩn SM-2
    val interval: Int = 0, // Khoảng cách (số ngày) cho lần ôn tiếp theo
    val repetitions: Int = 0, // Số lần lặp lại thành công liên tiếp
    val nextReviewDate: Long = System.currentTimeMillis() // Thời gian ôn tập tiếp theo (lưu dưới dạng mili-giây)
) {
    /**
     * Thuật toán SuperMemo-2 (SM-2)
     * @param quality: Điểm chất lượng do người dùng tự đánh giá từ 0 đến 5.
     * 0: Quên hoàn toàn
     * 1: Sai, nhưng nhớ ra khi xem đáp án
     * 2: Sai, có cảm giác quen thuộc
     * 3: Đúng, nhưng tốn nhiều thời gian và khó khăn
     * 4: Đúng, có chút do dự
     * 5: Đúng, nhớ lại hoàn hảo và ngay lập tức
     */
    fun calculateNextReview(quality: Int): Flashcard {
        var newEasiness = easiness
        var newInterval = interval
        var newRepetitions = repetitions

        // 1. Tính toán số lần lặp (repetitions) và khoảng thời gian (interval)
        if (quality >= 3) {
            // Trả lời đúng (từ mức 3 trở lên)
            newRepetitions += 1
            newInterval = when (newRepetitions) {
                1 -> 1 // Lần đúng đầu tiên -> Ôn lại sau 1 ngày
                2 -> 6 // Lần đúng thứ hai -> Ôn lại sau 6 ngày
                else -> (newInterval * newEasiness).roundToInt() // Từ lần 3 trở đi
            }
        } else {
            // Trả lời sai (dưới mức 3) -> Quên bài, reset lại chu kỳ
            newRepetitions = 0
            newInterval = 1
        }

        // 2. Cập nhật hệ số dễ (easiness / E-Factor)
        newEasiness += 0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02)
        if (newEasiness < 1.3) {
            newEasiness = 1.3 // Hệ số tối thiểu không bao giờ được nhỏ hơn 1.3
        }

        // 3. Tính toán mốc thời gian ôn tập tiếp theo
        // Công thức: Thời gian hiện tại + (Khoảng cách ngày * số mili-giây trong 1 ngày)
        val oneDayInMillis = 24 * 60 * 60 * 1000L
        val nextDate = System.currentTimeMillis() + (newInterval * oneDayInMillis)

        // 4. Trả về một bản sao mới của Flashcard với các thông số đã được cập nhật
        return this.copy(
            easiness = newEasiness,
            interval = newInterval,
            repetitions = newRepetitions,
            nextReviewDate = nextDate
        )
    }
}