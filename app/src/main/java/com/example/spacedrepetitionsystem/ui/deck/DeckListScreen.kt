package com.example.spacedrepetitionsystem.ui.deck

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spacedrepetitionsystem.ui.components.FlipCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen(
    viewModel: DeckListViewModel = viewModel() // Hilt sẽ tự động "bơm" ViewModel vào đây
) {
    // Quan sát danh sách flashcards từ ViewModel
    val flashcards by viewModel.flashcards.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thẻ cần ôn tập hôm nay") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        if (flashcards.isEmpty()) {
            // Hiển thị khi không có thẻ nào cần học
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Tuyệt vời! Bạn đã hoàn thành hết bài tập hôm nay.")
            }
        } else {
            // Hiển thị danh sách các thẻ
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(flashcards) { card ->
                    // Triệu hồi linh kiện lật thẻ đã tạo ở bước trước
                    FlipCard(flashcard = card)
                }
            }
        }
    }
}