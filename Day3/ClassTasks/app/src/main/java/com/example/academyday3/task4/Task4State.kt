package com.example.academyday3.task4

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.academyday3.R
import com.example.academyday3.task2.MyData

class Task4State(initialItems: List<MyData>) {
    var items by mutableStateOf(initialItems)
        private set
    var query by mutableStateOf("")
        private set

    val filteredItems: List<MyData>
        get() = if (query.isBlank()) items else items.filter {
            it.topic.contains(query, ignoreCase = true)
        }

    fun updateQuery(value: String) {
        query = value
    }

    fun shuffleItems() {
        items = items.shuffled()
    }
}

@Composable
fun rememberTask4State(initialItems: List<MyData> = sampleMyData()): Task4State {
    return remember { Task4State(initialItems) }
}

fun sampleMyData(): List<MyData> = listOf(
    MyData(
        id = 1,
        title = "Compose Basics",
        description = "Learn how composable functions build UI with less code and better clarity.",
        topic = "Compose",
        imageRes = R.drawable.ic_launcher_foreground
    ),
    MyData(
        id = 2,
        title = "Layouts",
        description = "Use Row, Column, and Box to create clean and responsive layouts.",
        topic = "Layout",
        imageRes = R.drawable.ic_launcher_foreground
    ),
    MyData(
        id = 3,
        title = "Cards",
        description = "Group content inside cards with elevation, padding, and rounded corners.",
        topic = "UI",
        imageRes = R.drawable.ic_launcher_foreground
    ),
    MyData(
        id = 4,
        title = "Lazy Lists",
        description = "Render large datasets efficiently using LazyColumn and item keys.",
        topic = "List",
        imageRes = R.drawable.ic_launcher_foreground
    ),
    MyData(
        id = 5,
        title = "State",
        description = "Hold UI state with remember and update it to trigger recomposition.",
        topic = "State",
        imageRes = R.drawable.ic_launcher_foreground
    ),
    MyData(
        id = 6,
        title = "Navigation",
        description = "Move between screens and pass parameters with a typed route.",
        topic = "Navigation",
        imageRes = R.drawable.ic_launcher_foreground
    )
)
