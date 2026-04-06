package com.example.academyday3.task3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academyday3.task2.ItemCard
import com.example.academyday3.task2.MyData

@Composable
fun MyItemList(
    items: List<MyData>,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { it.id }) { item ->
            ItemCard(item = item, onClick = { onItemClick(item.id) })
        }
    }
}
