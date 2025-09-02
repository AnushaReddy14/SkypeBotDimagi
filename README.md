SkypeBotDimagi  

This project was built as part of the Dimagi coding exercise.  
It is an Android application (built with Kotlin and Jetpack Compose) that simulates a simple bot which accepts text-based commands and responds accordingly.  

---

Features Implemented
- Ping → Check if the bot is active.  
- Help → List all available commands.  
- History → View command history, with optional filtering.  
- Whatdayis → Get today’s date, parse weekdays, or convert a given date (e.g., `8/9`) to the day of the week.  
- Say → Bot repeats the text provided.  
- Hello → Quick greeting response.  
- InOffice → Mock feature to list sample employees and their network.  
- Lunch → Suggests a random restaurant.  
- Calculator → Perform basic arithmetic (`+`, `-`, `*`, `/`).  
- To-do List → Add, list, and complete tasks.  
- Songs → Save a favorite song (`mynewsongis`) and play it later (`playmysong`).  
- Lorem Ipsum Generator → Generate random placeholder text.  
- Play → Pretend to play a given song.  
- Volume → Simulated volume control.  
- Twitter(mock) → Pretend to fetch tweets for a given query.  
- Dictionary(API call) → Look up word definitions using the free Dictionary API.  

---

Tech Stack
- Kotlin for Android development  
- Jetpack Compose for UI  
- ViewModel + StateFlow for state management  
- Retrofit for API calls (Dictionary API)  
- Coroutines for async operations  

---

Demo
The app allows users to type commands in a chat-like interface and see the bot’s responses in real time.  

---

Project Structure
- `MainActivity.kt` → Sets up the UI with Compose and links to the ViewModel.  
- `BotViewModel.kt` → Handles all command processing logic.  
- `build.gradle.kts` → Gradle setup with required dependencies.  

---

Future Improvements
- Replace mock data (`InOffice`, `Twitter`) with real APIs or a database.  
- Improve UI styling and add animations.  
- Add persistence with Room Database for to-do tasks and history.  


