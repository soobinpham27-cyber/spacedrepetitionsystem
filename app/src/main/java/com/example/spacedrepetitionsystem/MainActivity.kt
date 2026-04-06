package com.example.spacedrepetitionsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.spacedrepetitionsystem.ui.deck.DeckListScreen
import com.example.spacedrepetitionsystem.ui.theme.SpacedRepetitionSystemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpacedRepetitionSystemTheme {
                // Surface giúp quản lý nền của ứng dụng
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DeckListScreen()
                }
            }
        }
    }
}