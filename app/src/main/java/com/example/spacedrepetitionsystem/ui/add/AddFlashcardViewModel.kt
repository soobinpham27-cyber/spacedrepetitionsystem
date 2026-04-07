package com.example.spacedrepetitionsystem.ui.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacedrepetitionsystem.domain.model.Flashcard
import com.example.spacedrepetitionsystem.domain.repository.FlashcardRepository
import com.google.firebase.auth.FirebaseAuth // Dùng để định danh người dùng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull

@HiltViewModel
class AddFlashcardViewModel @Inject constructor(
    private val repository: FlashcardRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 1. Lấy cardId dưới dạng String? (Khớp với Navigation và Firestore)
    private val cardId: String? = savedStateHandle["cardId"]
    private val auth = FirebaseAuth.getInstance()

    var frontText by mutableStateOf("")
    var backText by mutableStateOf("")

    private var existingCard: Flashcard? = null

    init {
        // Nếu cardId không null và không trống, Linh đang ở chế độ Sửa
        if (!cardId.isNullOrBlank()) {
            loadExistingCard(cardId)
        }
    }

    private fun loadExistingCard(id: String) {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: ""
            // Lấy danh sách thẻ của chính User này để tìm thẻ cần sửa
            repository.getAllFlashcards(uid).collect { cards ->
                val card = cards.find { it.id == id }
                if (card != null) {
                    existingCard = card
                    frontText = card.front
                    backText = card.back
                }
            }
        }
    }

    fun saveFlashcard(onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: ""

        if (frontText.isNotBlank() && backText.isNotBlank() && uid.isNotBlank()) {
            viewModelScope.launch {
                if (cardId.isNullOrBlank()) {
                    // --- CHẾ ĐỘ THÊM MỚI ---
                    val newCard = Flashcard(
                        userId = uid, // Gán UID để Backend biết thẻ này của ai
                        front = frontText,
                        back = backText
                    )
                    repository.insertFlashcard(newCard)
                } else {
                    // --- CHẾ ĐỘ CẬP NHẬT ---
                    existingCard?.let { card ->
                        val updatedCard = card.copy(
                            front = frontText,
                            back = backText
                        )
                        repository.updateFlashcard(updatedCard)
                    }
                }
                onSuccess()
            }
        }
    }
}