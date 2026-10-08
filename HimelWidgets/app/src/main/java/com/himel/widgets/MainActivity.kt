package com.himel.widgets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Simple launcher activity.
 * The real value of this app is the three home-screen widgets.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
private fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Himel Widgets",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Three native home-screen widgets\nported from your portfolio site",
            fontSize = 15.sp,
            color = Color(0xFF33691E),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(28.dp))
        Text(
            text = "How to add them",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1B5E20)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "1. Long-press on your home screen\n" +
                    "2. Tap “Widgets”\n" +
                    "3. Find “Himel Widgets”\n" +
                    "4. Drag Clock / Calendar / Weather onto the screen",
            fontSize = 14.sp,
            color = Color(0xFF424242),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = "Clock  ·  BD Calendar  ·  Weather",
            fontSize = 13.sp,
            color = Color(0xFF689F38)
        )
    }
}
