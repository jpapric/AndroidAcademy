package com.example.androidacademyapi.ui.productdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.androidacademyapi.AppContainer
import com.example.androidacademyapi.ui.common.ErrorStateScreen
import com.example.androidacademyapi.ui.common.NoInternetScreen
import androidx.compose.ui.unit.dp

@Composable
fun ProductDetailsScreen(navController: NavController,productId: Int){
    val viewModel: ProductDetailsViewModel =
        viewModel(factory = ProductDetailsViewModelFactory(AppContainer.productRepository,productId))
    val productDetailsUIState = viewModel.productDetailsUIState.value

    ProductDetailsContent(
        productDetailsUIState = productDetailsUIState,
        onNavigateBack = { navController.popBackStack() },
        onRetryClick = viewModel::getProductDetails
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsContent(
    productDetailsUIState: ProductDetailsUIState,
    onNavigateBack:()-> Unit,
    onRetryClick: () -> Unit
){
    val title = when (productDetailsUIState) {
        is ProductDetailsUIState.Success -> productDetailsUIState.product.title
        else -> "Product details"
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)

            )
        }
    ) { paddingValues ->
        when (productDetailsUIState) {
            is ProductDetailsUIState.Error -> {
                ErrorStateScreen(
                    title = "Couldn't load product",
                    description = productDetailsUIState.message,
                    actionLabel = "Retry",
                    onAction = onRetryClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            ProductDetailsUIState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            ProductDetailsUIState.NoInternet -> {
                NoInternetScreen(
                    onRetry = onRetryClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is ProductDetailsUIState.InvalidProductId -> {
                ErrorStateScreen(
                    title = "Invalid product id",
                    description = productDetailsUIState.message,
                    actionLabel = "Go back",
                    onAction = onNavigateBack,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is ProductDetailsUIState.Success -> {
                ProductDetailsSuccessContent(
                    product = productDetailsUIState.product,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun ProductDetailsSuccessContent(
    product: com.example.androidacademyapi.data.model.Product,
    modifier: Modifier = Modifier
) {
    val imageUrl = product.thumbnail ?: product.images.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No image available")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Product id: ${product.id}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = product.description ?: "No description available.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (product.images.isNotEmpty()) {
            Text(
                text = "Gallery",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                product.images.forEach { image ->
                    AsyncImage(
                        model = image,
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
