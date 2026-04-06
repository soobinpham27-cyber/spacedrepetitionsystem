package com.example.spacedrepetitionsystem.ui.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacedrepetitionsystem.domain.repository.FlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckListViewModel @Inject constructor(
    private val repository: FlashcardRepository
) : ViewModel() {
    // Sau này sẽ viết logic lấy danh sách thẻ ở đây
}