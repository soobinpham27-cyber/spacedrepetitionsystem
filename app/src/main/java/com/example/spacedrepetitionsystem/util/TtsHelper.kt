package com.example.spacedrepetitionsystem.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

class TtsHelper(context: Context) : TextToSpeech.OnInitListener {
    // Ép sử dụng Engine của Google để có giọng đọc chuẩn nhất
    private var tts: TextToSpeech = TextToSpeech(context, this, "com.google.android.tts")
    private var isReady = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isReady = true
        }
    }

    /**
     * @param isEnglish: true đọc giọng Anh-Mỹ, false đọc giọng Việt
     */
    fun speak(text: String, isEnglish: Boolean = true) {
        if (isReady && text.isNotBlank()) {
            // Chuyển đổi Locale ngay trước khi đọc
            val locale = if (isEnglish) Locale.US else Locale("vi", "VN")
            tts.language = locale

            // QUEUE_FLUSH để ngắt câu cũ và đọc câu mới ngay lập tức
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TTS_ID")
        }
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}