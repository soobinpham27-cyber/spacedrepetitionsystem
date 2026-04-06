package com.example.spacedrepetitionsystem.ui.deck

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Bộ thẻ của tôi") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Chưa có bộ thẻ nào. Hãy thêm thẻ mới!")
            // Sau này sẽ dùng LazyColumn để hiển thị danh sách
        }
    }
}