package com.example.skypebot

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class BotViewModel : ViewModel() {

    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history = _history.asStateFlow()

    private val todos = mutableListOf<String>()
    private val userSongs = mutableMapOf<String, String>()

    fun processCommand(input: String) {
        if (input.isBlank()) return

        val command = input.trim().lowercase()
        val response = when {
            // Simple ping check
            command == "ping" -> "Bot: I am active!"

            // Show available commands
            command == "help" -> """
                Bot: Available commands →
                ping, help, history [filter],
                whatdayis [today|date|weekday],
                say <text>, hello,
                inoffice, lunch,
                calculator <a> <+|-|*|/> <b>,
                todo add <task>, todo list, todo done <task>,
                mynewsongis <song>, playmysong,
                lorem [count], play <song>,
                vol <0-100>, twitter <query>, define <word>
            """.trimIndent()

            // Show history or filter it
            command.startsWith("history") -> {
                val parts = input.split(" ")
                if (parts.size == 1) {
                    if (_history.value.isEmpty()) "Bot: No history yet."
                    else "Bot: Previous commands → ${_history.value.joinToString()}"
                } else {
                    val filter = parts[1]
                    val filtered = _history.value.filter { it.contains(filter, ignoreCase = true) }
                    if (filtered.isEmpty()) "Bot: No history matches \"$filter\""
                    else "Bot: Filtered history → ${filtered.joinToString()}"
                }
            }

            // Date lookups
            command.startsWith("whatdayis") -> {
                val parts = input.split(" ")
                when {
                    parts.size == 1 || parts[1] == "today" -> {
                        val today = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
                        "Bot: Today is $today"
                    }
                    parts[1].contains("/") -> {
                        try {
                            val formatter = SimpleDateFormat("M/d", Locale.getDefault())
                            val date = formatter.parse(parts[1])
                            if (date != null) {
                                val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
                                "Bot: ${parts[1]} is a $dayOfWeek"
                            } else "Bot: Invalid date format. Use M/d like 8/9"
                        } catch (e: Exception) {
                            "Bot: Invalid date format. Use M/d like 8/9"
                        }
                    }
                    else -> {
                        val targetDay = parts[1].lowercase()
                        val days = listOf("sunday","monday","tuesday","wednesday","thursday","friday","saturday")
                        if (targetDay in days) {
                            "Bot: Next $targetDay is on ${getNextDayOfWeek(targetDay)}"
                        } else "Bot: Unknown format. Try today, 8/9, or a weekday name."
                    }
                }
            }

            // Echo text back
            command.startsWith("say ") -> {
                val text = input.removePrefix("say ").trim()
                "Bot says: $text"
            }

            // hello
            command == "hello" -> "Bot: Hello there! How can I help you?"

            // Example mock data
            command == "inoffice" -> "Bot: Danny - WiFi, Carter - Wired"

            // Pick a random restaurant
            command == "lunch" -> {
                val options = listOf("Chipotle", "Panera Bread", "Subway", "Shake Shack", "Taco Bell")
                "Bot: How about ${options.random()} today?"
            }

            // Simple calculator
            command.startsWith("calculator") -> {
                val parts = input.split(" ")
                if (parts.size == 4) {
                    val a = parts[1].toDoubleOrNull()
                    val op = parts[2]
                    val b = parts[3].toDoubleOrNull()
                    if (a != null && b != null) {
                        when (op) {
                            "+" -> "Bot: ${a + b}"
                            "-" -> "Bot: ${a - b}"
                            "*" -> "Bot: ${a * b}"
                            "/" -> if (b != 0.0) "Bot: ${a / b}" else "Bot: Cannot divide by zero"
                            else -> "Bot: Unknown operator. Use + - * /"
                        }
                    } else {
                        "Bot: Invalid numbers. Example: calculator 5 + 7"
                    }
                } else {
                    "Bot: Invalid format. Use: calculator <a> <+|-|*|/> <b>"
                }
            }

            // Todo list commands
            command.startsWith("todo add") -> {
                val task = input.removePrefix("todo add").trim()
                if (task.isNotEmpty()) {
                    todos.add(task)
                    "Bot: Task added → $task"
                } else {
                    "Bot: Please provide a task."
                }
            }
            command == "todo list" -> {
                if (todos.isEmpty()) {
                    "Bot: No tasks yet."
                } else {
                    todos.mapIndexed { index, t -> "${index + 1}. $t" }
                        .joinToString("\n", prefix = "Bot: Tasks →\n")
                }
            }
            command.startsWith("todo done") -> {
                val task = input.removePrefix("todo done").trim()
                if (todos.remove(task)) {
                    "Bot: Task completed → $task"
                } else {
                    "Bot: Task not found."
                }
            }

            // Save and play user's favorite song
            command.startsWith("mynewsongis") -> {
                val song = input.removePrefix("mynewsongis").trim()
                if (song.isNotEmpty()) {
                    userSongs["defaultUser"] = song
                    "Bot: Got it! Your new song is \"$song\""
                } else "Bot: Please provide a song name."
            }
            command == "playmysong" -> {
                val song = userSongs["defaultUser"]
                if (song != null) {
                    "Bot: Pretending to play your saved song \"$song\""
                } else "Bot: You don’t have a song saved yet. Use mynewsongis <song>"
            }

            // Random lorem ipsum text
            command.startsWith("lorem") -> {
                val words = listOf("lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit")
                val count = input.removePrefix("lorem").trim().toIntOrNull() ?: 10
                val generated = (1..count).map { words.random() }.joinToString(" ")
                "Bot: $generated"
            }

            // Pretend to play music
            command.startsWith("play") -> {
                val song = input.removePrefix("play").trim()
                if (song.isNotEmpty()) {
                    "Bot: Pretending to play \"$song\" from YouTube."
                } else {
                    "Bot: Please provide a song name."
                }
            }

            // Fake volume control
            command.startsWith("vol") -> {
                val parts = input.split(" ")
                if (parts.size == 2) {
                    val level = parts[1].toIntOrNull()
                    if (level != null && level in 0..100) {
                        "Bot: Volume set to $level%"
                    } else {
                        "Bot: Please enter a number between 0 and 100"
                    }
                } else {
                    "Bot: Invalid format. Use vol <0–100>"
                }
            }

            // Mock twitter endpoint
            command.startsWith("twitter") -> {
                val query = input.removePrefix("twitter").trim()
                if (query.isNotEmpty()) {
                    "Bot: Pretending to fetch tweets about \"$query\"..."
                } else "Bot: Please provide a search term. Example: twitter android"
            }

            // Mock dictionary definition (no API)
            command.startsWith("define") -> {
                val word = input.removePrefix("define").trim()
                if (word.isNotEmpty()) {
                    "Bot: Pretending to fetch definition of \"$word\"... For demo purposes."
                } else {
                    "Bot: Please provide a word. Example: define hello"
                }
            }

            else -> "Bot: Command not recognized."
        }

        _history.value = _history.value + "You: $input" + response
    }

    private fun getNextDayOfWeek(day: String): String {
        val days = listOf("sunday","monday","tuesday","wednesday","thursday","friday","saturday")
        val todayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
        val targetIndex = days.indexOf(day)
        val diff = (targetIndex - todayIndex + 7) % 7
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, if (diff == 0) 7 else diff)
        return SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(cal.time)
    }
}
