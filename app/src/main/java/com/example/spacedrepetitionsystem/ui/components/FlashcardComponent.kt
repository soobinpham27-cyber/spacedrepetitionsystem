package com.example.spacedrepetitionsystem.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.spacedrepetitionsystem.domain.model.Flashcard

@Composable
fun FlipCard(flashcard: Flashcard) {
    var rotated by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "CardRotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable { rotated = !rotated },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                // Mặt trước
                Text(
                    text = flashcard.front,
                    style = MaterialTheme.typography.headlineMedium
                )
            } else {
                // Mặt sau (Xoay ngược lại để chữ không bị lộn ngược)
                Text(
                    text = flashcard.back,
                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}