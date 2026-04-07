package com.example.spacedrepetitionsystem.ui.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacedrepetitionsystem.domain.model.Flashcard
import com.example.spacedrepetitionsystem.domain.repository.FlashcardRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi // Thêm cái này
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch // 🚀 CỰC KỲ QUAN TRỌNG: Để hết lỗi unresolved 'launch'
import javax.inject.Inject

@HiltViewModel
class DeckListViewModel @Inject constructor(
    private val repository: FlashcardRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class) // Cần thiết khi dùng flatMapLatest
    val flashcards: StateFlow<List<Flashcard>> = _searchQuery
        .debounce(300L)
        .flatMapLatest { query ->
            // Lấy UID thực tế từ Firebase
            val uid = auth.currentUser?.uid ?: ""
            repository.getAllFlashcards(uid).map { list ->
                if (query.isBlank()) list
                else list.filter { it.front.contains(query, ignoreCase = true) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    // ✅ ĐÃ FIX: Bao hàm trong viewModelScope.launch để hết lỗi đỏ
    fun deleteCard(card: Flashcard) {
        viewModelScope.launch {
            repository.deleteFlashcard(card)
        }
    }
}