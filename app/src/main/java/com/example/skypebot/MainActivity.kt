package com.example.skypebot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val botViewModel: BotViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkypeBotApp(botViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkypeBotApp(viewModel: BotViewModel) {
    val history by viewModel.history.collectAsState()
    var userInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("SkypeBot") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                reverseLayout = true
            ) {
                items(history.reversed()) { entry ->
                    if (entry.startsWith("You:")) {
                        UserMessage(entry.removePrefix("You: "))
                    } else if (entry.startsWith("Bot:")) {
                        BotMessage(entry.removePrefix("Bot: "))
                    } else {
                        Text(entry) // fallback, in case
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Type a command...") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        viewModel.processCommand(userInput)
                        userInput = ""
                    }
                ) {
                    Text("Send")
                }
            }
        }
    }
}

@Composable
fun UserMessage(text: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun BotMessage(text: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
