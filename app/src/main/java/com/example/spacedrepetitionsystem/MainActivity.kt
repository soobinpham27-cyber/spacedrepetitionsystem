package com.example.spacedrepetitionsystem

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spacedrepetitionsystem.ui.add.AddFlashcardScreen
import com.example.spacedrepetitionsystem.ui.deck.DeckListScreen
import com.example.spacedrepetitionsystem.ui.review.ReviewScreen
import com.example.spacedrepetitionsystem.ui.theme.SpacedRepetitionSystemTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔐 BƯỚC 1: Khởi tạo Firebase Auth để có User ID đồng bộ chéo thiết bị
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnSuccessListener {
                    Log.d("Firebase", "Đăng nhập ẩn danh thành công: ${it.user?.uid}")
                }
                .addOnFailureListener {
                    Log.e("Firebase", "Lỗi đăng nhập: ${it.message}")
                }
        }

        setContent {
            SpacedRepetitionSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "deck_list"
                    ) {
                        // 1. Màn hình Danh sách thẻ (Deck List)
                        composable("deck_list") {
                            DeckListScreen(
                                onAddClick = { navController.navigate("add_flashcard") },
                                onReviewClick = { navController.navigate("review") },
                                onEditClick = { cardId ->
                                    // cardId ở đây đã là String, truyền trực tiếp vào Route
                                    navController.navigate("add_flashcard?cardId=$cardId")
                                }
                            )
                        }

                        // 2. Màn hình Thêm/Sửa (Đã đổi hoàn toàn sang String ID)
                        composable(
                            route = "add_flashcard?cardId={cardId}",
                            arguments = listOf(
                                navArgument("cardId") {
                                    type = NavType.StringType // Ép kiểu String để khớp với Firestore UUID
                                    nullable = true
                                    defaultValue = null // Null nghĩa là chế độ Thêm mới
                                }
                            )
                        ) { backStackEntry ->
                            // Lấy cardId ra dưới dạng String?
                            val cardId = backStackEntry.arguments?.getString("cardId")

                            AddFlashcardScreen(
                                cardId = cardId, // Truyền sang Screen để ViewModel xử lý
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // 3. Màn hình Ôn tập (Tích hợp SM-2 và Google TTS)
                        composable("review") {
                            ReviewScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}