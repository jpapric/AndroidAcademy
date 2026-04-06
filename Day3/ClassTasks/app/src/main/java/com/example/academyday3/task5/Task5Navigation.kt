package com.example.academyday3.task5

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.academyday3.task1.CustomButton
import com.example.academyday3.task1.TitleText
import com.example.academyday3.task2.MyData
import com.example.academyday3.task3.MyItemList
import com.example.academyday3.task4.Task4State
import com.example.academyday3.task4.rememberTask4State

sealed class Screen(val route: String) {
    data object List : Screen("list")

    data object Detail : Screen("detail/{id}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}

@Composable
fun AppRoot(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val state = rememberTask4State()

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.List.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.List.route) {
                ListScreen(
                    state = state,
                    onItemClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id")
                val selectedItem = state.items.firstOrNull { it.id == id }
                DetailScreen(
                    item = selectedItem,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun ListScreen(
    state: Task4State,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleText(text = "My Data List")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.query,
            onValueChange = state::updateQuery,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search by topic") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomButton(
            text = "Shuffle items",
            onClick = state::shuffleItems,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        MyItemList(
            items = state.filteredItems,
            onItemClick = onItemClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun DetailScreen(
    item: MyData?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CustomButton(text = "Back", onClick = onBack)
        Spacer(modifier = Modifier.height(16.dp))
        if (item == null) {
            Text(text = "Item not found.")
        } else {
            TitleText(text = item.title)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
