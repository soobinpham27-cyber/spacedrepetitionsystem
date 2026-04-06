package com.example.spacedrepetitionsystem.ui.review

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spacedrepetitionsystem.ui.components.FlipCard
import com.example.spacedrepetitionsystem.util.TtsHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    onBack: () -> Unit,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val ttsHelper = remember { TtsHelper(context) }

    // Dọn dẹp bộ nhớ TTS khi thoát màn hình
    DisposableEffect(Unit) {
        onDispose { ttsHelper.shutdown() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ôn tập kiến thức") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is ReviewUiState.Loading -> { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
                is ReviewUiState.Empty -> { ReviewEmptyView(onBack) }
                is ReviewUiState.Success -> {
                    val currentCard = state.cards.first()

                    // TỰ ĐỘNG ĐỌC MẶT TRƯỚC (TIẾNG ANH) KHI CHUYỂN THẺ
                    LaunchedEffect(currentCard) {
                        ttsHelper.speak(currentCard.front, isEnglish = true)
                    }

                    val total = state.totalInitialCount
                    val remaining = state.cards.size
                    val completed = total - remaining
                    val progressValue = if (total > 0) completed.toFloat() / total.toFloat() else 0f
                    val animatedProgress by animateFloatAsState(targetValue = progressValue, label = "")

                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 1. THANH TIẾN TRÌNH [cite: 25]
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp))
                        )

                        // 2. NÚT LOA SONG NGỮ (Xanh: Anh, Xanh lá: Việt)
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = { ttsHelper.speak(currentCard.front, isEnglish = true) }) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color(0xFF2196F3))
                            }
                            IconButton(onClick = { ttsHelper.speak(currentCard.back, isEnglish = false) }) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color(0xFF4CAF50))
                            }
                        }

                        // 3. TẤM THẺ 3D
                        Box(modifier = Modifier.weight(1f)) {
                            FlipCard(flashcard = currentCard)
                        }

                        // 4. HỆ THỐNG ĐÁNH GIÁ SM-2 [cite: 121]
                        Text("Bạn thấy kiến thức này thế nào?", modifier = Modifier.padding(vertical = 16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            RatingButton("Quên", Color(0xFFEF5350), 0, viewModel)
                            RatingButton("Mơ hồ", Color(0xFFFF9800), 3, viewModel)
                            RatingButton("Nhớ", Color(0xFF4CAF50), 5, viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewEmptyView(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🎉", fontSize = 80.sp)
        Text("Hoàn thành xuất sắc!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Button(onClick = onBack, modifier = Modifier.padding(top = 24.dp)) {
            Text("Quay lại danh sách")
        }
    }
}

@Composable
fun RowScope.RatingButton(text: String, color: Color, quality: Int, viewModel: ReviewViewModel) {
    Button(
        onClick = { viewModel.answerCard(quality) },
        modifier = Modifier.weight(1f).height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold, color = Color.White)
    }
}