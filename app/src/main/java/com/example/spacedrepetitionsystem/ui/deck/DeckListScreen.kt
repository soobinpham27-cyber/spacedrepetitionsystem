package com.example.spacedrepetitionsystem.ui.deck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spacedrepetitionsystem.domain.model.Flashcard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen(
    onAddClick: () -> Unit,
    onReviewClick: () -> Unit,
    onEditClick: (String) -> Unit, // Đã khớp kiểu String cho Backend
    viewModel: DeckListViewModel = hiltViewModel()
) {
    val flashcards by viewModel.flashcards.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kho kiến thức của Linh") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm thẻ mới")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // --- 1. THANH TÌM KIẾM ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Tìm kiếm thẻ...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Xóa")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // --- 2. DASHBOARD THỐNG KÊ ---
            if (flashcards.isNotEmpty() || searchQuery.isNotEmpty()) {
                val currentTimestamp = System.currentTimeMillis()
                val dueCount = flashcards.count { it.nextReviewDate <= currentTimestamp }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatItem(label = "Tổng số thẻ", value = "${flashcards.size}")
                            StatItem(
                                label = "Cần ôn ngay",
                                value = "$dueCount",
                                valueColor = if (dueCount > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onReviewClick,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = dueCount > 0,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (dueCount > 0) "Bắt đầu ôn tập ($dueCount thẻ)" else "Hôm nay đã xong!")
                        }
                    }
                }
            }

            // --- 3. DANH SÁCH THẺ ---
            Box(modifier = Modifier.fillMaxSize()) {
                if (flashcards.isEmpty()) {
                    EmptyStateView(isSearching = searchQuery.isNotEmpty())
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp, start = 16.dp, end = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = flashcards,
                            key = { it.id }
                        ) { card ->
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    when (value) {
                                        SwipeToDismissBoxValue.EndToStart -> {
                                            viewModel.deleteCard(card)
                                            true
                                        }
                                        SwipeToDismissBoxValue.StartToEnd -> {
                                            onEditClick(card.id) // ✅ Đã khớp String
                                            false
                                        }
                                        else -> false
                                    }
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = {
                                    val direction = dismissState.dismissDirection
                                    val color = when (direction) {
                                        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                                        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.tertiaryContainer
                                        else -> Color.Transparent
                                    }
                                    val alignment = if (direction == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
                                    val icon = if (direction == SwipeToDismissBoxValue.StartToEnd) Icons.Default.Edit else Icons.Default.Delete

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color, shape = RoundedCornerShape(12.dp))
                                            .padding(horizontal = 24.dp),
                                        contentAlignment = alignment
                                    ) {
                                        Icon(imageVector = icon, contentDescription = null)
                                    }
                                }
                            ) {
                                // GỌI COMPONENT RIÊNG Ở ĐÂY ĐỂ CODE SẠCH
                                FlashcardItem(
                                    flashcard = card,
                                    onEditClick = { onEditClick(it) },
                                    onDeleteClick = { viewModel.deleteCard(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- CÁC COMPONENT PHỤ (NẰM NGOÀI HÀM CHÍNH) ---

@Composable
fun FlashcardItem(
    flashcard: Flashcard,
    onEditClick: (String) -> Unit,
    onDeleteClick: (Flashcard) -> Unit
) {
    Surface(
        onClick = { onEditClick(flashcard.id) },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = flashcard.front,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Bấm để sửa hoặc xem đáp án",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatItem(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onSecondaryContainer) {
    Column {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
fun EmptyStateView(isSearching: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = if (isSearching) "🔍" else "🎉", style = MaterialTheme.typography.displayLarge)
        Text(text = if (isSearching) "Không thấy gì hết!" else "Trống trải quá Linh ơi!", fontWeight = FontWeight.Bold)
    }
}