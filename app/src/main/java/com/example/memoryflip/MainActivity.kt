package com.example.memoryflip
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MemoryGame() }
    }
}

@Composable
fun MemoryGame() {
    val emojis = listOf("🐶","🐱","🦊","🐻","🐼","🐨","🦁","🐯")
    var cards by remember { mutableStateOf((emojis + emojis).shuffled()) }
    var flipped by remember { mutableStateOf(listOf<Int>()) }
    var matched by remember { mutableStateOf(setOf<Int>()) }
    var moves by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }

    LaunchedEffect(matched.size) {
        while(matched.size < 16) { delay(1000); seconds++ }
    }
    LaunchedEffect(flipped) {
        if(flipped.size == 2) {
            moves++
            val (a,b) = flipped
            if(cards[a] == cards[b]) matched = matched + flipped
            delay(800)
            flipped = emptyList()
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("لعبة الذاكرة", fontSize = 24.sp)
        Text("الحركات: $moves  |  الوقت: ${seconds}ث", fontSize = 16.sp)
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(4), modifier = Modifier.weight(1f)) {
            items(16) { i ->
                Card(Modifier.padding(6.dp).aspectRatio(1f).clickable {
                    if(i !in flipped && i !in matched && flipped.size < 2) flipped = flipped + i
                }, elevation = CardDefaults.cardElevation(4.dp)) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(if(i in flipped || i in matched) cards[i] else "❓", fontSize = 32.sp)
                    }
                }
            }
        }
        if(matched.size == 16) {
            Text("🎉 أحسنت! أنهيتها في $moves حركة", color = MaterialTheme.colorScheme.primary)
        }
        Button(onClick = {
            cards = (emojis + emojis).shuffled()
            flipped = emptyList()
            matched = emptySet()
            moves = 0
            seconds = 0
        }) { Text("إعادة اللعب") }
    }
}
