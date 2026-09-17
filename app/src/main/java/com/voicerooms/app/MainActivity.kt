package com.voicerooms.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.voicerooms.app.ui.theme.VoiceRoomsTheme
import com.voicerooms.app.ui.VoiceRoomsApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoiceRoomsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    VoiceRoomsApp()
                }
            }
        }
    }
}
