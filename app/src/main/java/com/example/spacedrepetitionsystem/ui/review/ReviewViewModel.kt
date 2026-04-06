package com.example.spacedrepetitionsystem.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacedrepetitionsystem.domain.model.Flashcard
import com.example.spacedrepetitionsystem.domain.repository.FlashcardRepository
import com.google.firebase.auth.FirebaseAuth // Thêm import này để định danh người dùng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository: FlashcardRepository
) : ViewModel() {

    // 1. Khởi tạo Firebase Auth để lấy chìa khóa userId
    private val auth = FirebaseAuth.getInstance()
    private val _uiState = MutableStateFlow<ReviewUiState>(ReviewUiState.Loading)
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    private var initialSessionCount = 0
    private var isInitialCountSet = false

    init {
        loadReviewCards()
    }

    private fun loadReviewCards() {
        viewModelScope.launch {
            // Lấy UID của Linh từ Firebase
            val uid = auth.currentUser?.uid ?: ""
            val currentTime = System.currentTimeMillis()

            // ✅ ĐÃ SỬA: Truyền cả uid (String) và currentTime (Long) cho Repository
            repository.getDueFlashcards(uid, currentTime).collect { cards ->

                if (!isInitialCountSet && cards.isNotEmpty()) {
                    initialSessionCount = cards.size
                    isInitialCountSet = true
                }

                _uiState.update {
                    if (cards.isEmpty()) {
                        ReviewUiState.Empty
                    } else {
                        ReviewUiState.Success(
                            cards = cards,
                            totalInitialCount = initialSessionCount
                        )
                    }
                }
            }
        }
    }

    fun answerCard(quality: Int) {
        val currentState = _uiState.value
        if (currentState is ReviewUiState.Success) {
            val currentCard = currentState.cards.first()
            viewModelScope.launch {
                // 1. Tính toán SM-2 (Hệ thống tự động cập nhật tiến độ)
                val updatedCard = currentCard.calculateNextReview(quality)

                // 2. Cập nhật vào DB và Đồng bộ lên Cloud Firestore
                repository.updateFlashcard(updatedCard)
            }
        }
    }
}

// Giữ nguyên Sealed Class để UI hiển thị Progress Bar
sealed class ReviewUiState {
    object Loading : ReviewUiState()
    object Empty : ReviewUiState()
    data class Success(
        val cards: List<Flashcard>,
        val totalInitialCount: Int
    ) : ReviewUiState()
}