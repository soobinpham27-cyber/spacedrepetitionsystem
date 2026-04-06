package com.example.spacedrepetitionsystem.ui.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacedrepetitionsystem.domain.model.Flashcard
import com.example.spacedrepetitionsystem.domain.repository.FlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckListViewModel @Inject constructor(
    private val repository: FlashcardRepository
) : ViewModel() {

    // Flow để quan sát danh sách thẻ từ Database
    private val _flashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val flashcards = _flashcards.asStateFlow()

    init {
        loadFlashcards()
    }

    private fun loadFlashcards() {
        viewModelScope.launch {
            // Lấy danh sách thẻ cần học (hết hạn ôn tập)
            val dueCards = repository.getDueFlashcards(System.currentTimeMillis())
            _flashcards.value = dueCards
        }
    }
}