package com.example.spacedrepetitionsystem.ui.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFlashcardScreen(
    cardId: String? = null, // ĐỔI SANG String? ĐỂ HẾT LỖI DÒNG 72 TRONG MAINACTIVITY
    onBack: () -> Unit,
    viewModel: AddFlashcardViewModel = hiltViewModel()
) {
    // Chế độ sửa dựa trên việc cardId có dữ liệu (không null và không trống)
    val isEditMode = !cardId.isNullOrBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Sửa kiến thức" else "Thêm kiến thức mới") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.frontText,
                onValueChange = { viewModel.frontText = it },
                label = { Text("Mặt trước (Tiếng Anh/Câu hỏi)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ví dụ: Spaced Repetition") }
            )

            OutlinedTextField(
                value = viewModel.backText,
                onValueChange = { viewModel.backText = it },
                label = { Text("Mặt sau (Tiếng Việt/Đáp án)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ví dụ: Lặp lại ngắt quãng") }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // ViewModel sẽ dùng UID từ Firebase để lưu thẻ đồng bộ
                    viewModel.saveFlashcard { onBack() }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                // Chỉ cho lưu khi đã nhập đủ thông tin
                enabled = viewModel.frontText.isNotBlank() && viewModel.backText.isNotBlank()
            ) {
                Text(
                    text = if (isEditMode) "Cập nhật và Đồng bộ" else "Lưu vào hệ thống",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}