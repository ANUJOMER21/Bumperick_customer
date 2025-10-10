package com.bumperpick.bumperickUser.Screens.Home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperickUser.API.New_model.Category
import com.bumperpick.bumperickUser.API.New_model.sub_categories
import com.bumperpick.bumperickUser.R
import com.bumperpick.bumperickUser.Screens.Component.LocationCard
import com.bumperpick.bumperickUser.Screens.Home.Map.LocationData
import com.bumperpick.bumperickUser.Screens.Home.UiState

import com.bumperpick.bumperickUser.ui.theme.BtnColor
import com.bumperpick.bumperickUser.ui.theme.blueColor
import org.koin.androidx.compose.koinViewModel// ImprovedOfferScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferScreen(
    homeClick: (HomeClick) -> Unit,
    openSubCategory: (subCatId: String, subCatName: String, catId: String) -> Unit,
    viewModel: CategoryViewModel = koinViewModel()
) {
    // State management
    var showSubCategories by rememberSaveable { mutableStateOf(false) }
    var selectedCategoryId by rememberSaveable { mutableStateOf<Int?>(null) }
    var selectedCategoryName by rememberSaveable { mutableStateOf("") }
    var categorySearchQuery by rememberSaveable { mutableStateOf("") }
    var subCategorySearchQuery by rememberSaveable { mutableStateOf("") }

    val categoriesState by viewModel.categoriesUiState.collectAsState()
    val subCategoriesState by viewModel.subCategoriesUiState.collectAsState()
    val locationDetails by viewModel.getLocation.collectAsState()

    // Initial data fetch
    LaunchedEffect(Unit) {
        viewModel.getCategories()
        viewModel.fetchLocation()
    }

    // Fetch subcategories when category is selected
    LaunchedEffect(selectedCategoryId) {
        selectedCategoryId?.let { viewModel.fetchSubCategories(it) }
    }

    // Handle back navigation
    BackHandler(enabled = showSubCategories) {
        showSubCategories = false
        selectedCategoryId = null
        selectedCategoryName = ""
        subCategorySearchQuery = ""
    }


        Box(modifier = Modifier.fillMaxSize()) {
            // Categories View
            AnimatedVisibility(
                visible = !showSubCategories,
                enter = fadeIn(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                        slideInHorizontally(
                            animationSpec = tween(400, easing = FastOutSlowInEasing),
                            initialOffsetX = { -it }
                        ),
                exit = fadeOut(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                        slideOutHorizontally(
                            animationSpec = tween(400, easing = FastOutSlowInEasing),
                            targetOffsetX = { -it }
                        )
            ) {
                Scaffold {paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                                  .padding(paddingValues)
                            .background(Color(0xFFFAFAFA))
                    ) {
                        LocationCard(
                            locationDetails = locationDetails,
                            onEventCity = {  homeClick(HomeClick.EventIncity)},
                            onCartClick = { homeClick(HomeClick.CartClick) },
                            onFavClick = { homeClick(HomeClick.FavClick) },
                            onLocationClick = { homeClick(HomeClick.LocationClick) },
                            onNotificationClick = { homeClick(HomeClick.NotifyClick) },
                            content = {
                                SearchBar(
                                    searchQuery = categorySearchQuery,
                                    onSearchQueryChange = { categorySearchQuery = it },
                                    placeholder = "Search for category"
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CategoriesSection(
                            categoriesState = categoriesState,
                            searchQuery = categorySearchQuery,
                            onCategoryClick = { category ->
                                showSubCategories = true
                                selectedCategoryId = category.id
                                selectedCategoryName = category.name.orEmpty()
                            },
                            onRetry = { viewModel.getCategories() }
                        )
                    }
                }
            }

            // SubCategories View
            AnimatedVisibility(
                visible = showSubCategories,
                enter = fadeIn(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                        slideInHorizontally(
                            animationSpec = tween(400, easing = FastOutSlowInEasing),
                            initialOffsetX = { it }
                        ),
                exit = fadeOut(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                        slideOutHorizontally(
                            animationSpec = tween(400, easing = FastOutSlowInEasing),
                            targetOffsetX = { it }
                        )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                     //   .padding(paddingValues)
                        .background(Color(0xFFFAFAFA))
                ) {
                    SubCategoryHeader(
                        categoryName = selectedCategoryName,
                        searchQuery = subCategorySearchQuery,
                        onSearchQueryChange = { subCategorySearchQuery = it },
                        onBackClick = {
                            showSubCategories = false
                            selectedCategoryId = null
                            selectedCategoryName = ""
                            subCategorySearchQuery = ""
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SubCategoriesContent(
                        subCategoriesState = subCategoriesState,
                        searchQuery = subCategorySearchQuery,
                        onSubCategoryClick = { subCategory ->
                            openSubCategory(
                                subCategory.id.toString(),
                                subCategory.name.orEmpty(),
                                selectedCategoryId.toString()
                            )
                        },
                        onRetry = {
                            selectedCategoryId?.let { viewModel.fetchSubCategories(it) }
                        }
                    )
                }
            }
        }

}



@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = searchQuery.isNotEmpty(),
                enter = fadeIn(tween(200)) + scaleIn(tween(200)),
                exit = fadeOut(tween(200)) + scaleOut(tween(200))
            ) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color.Gray
                    )
                }
            }
        },
        placeholder = {
            Text(text = placeholder, color = Color.Gray)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Black,
            focusedBorderColor = BtnColor,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),
        singleLine = true
    )
}

@Composable
private fun CategoriesSection(
    categoriesState: UiState<List<Category>>,
    searchQuery: String,
    onCategoryClick: (Category) -> Unit,
    onRetry: () -> Unit
) {
    when (categoriesState) {
        UiState.Empty -> {
            EmptyState(
                message = "No categories available",
                icon = Icons.Outlined.Close
            )
        }

        is UiState.Error -> {
            ErrorState(
                message = categoriesState.message,
                onRetry = onRetry
            )
        }

        UiState.Loading -> {
            LoadingState()
        }

        is UiState.Success -> {
            val filteredCategories = remember(categoriesState.data, searchQuery) {
                if (searchQuery.isBlank()) {
                    categoriesState.data
                } else {
                    categoriesState.data.filter {
                        it.name?.contains(searchQuery, ignoreCase = true) == true
                    }
                }
            }

            if (filteredCategories.isEmpty() && searchQuery.isNotEmpty()) {
                EmptyState(
                    message = "No categories found for \"$searchQuery\"",
                    icon = Icons.Outlined.Search
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(
                        items = filteredCategories,
                        key = { _, category -> category.id ?: category.hashCode() }
                    ) { index, category ->
                        val animationDelay = (index * 50).coerceAtMost(300)

                        AnimatedListItem(delay = animationDelay) {
                            CategoryListItem(
                                category = category,
                                onClick = { onCategoryClick(category) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedListItem(
    delay: Int = 0,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400, easing = FastOutSlowInEasing)) +
                slideInVertically(
                    animationSpec = tween(400, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 4 }
                ) +
                scaleIn(
                    animationSpec = tween(400, easing = FastOutSlowInEasing),
                    initialScale = 0.9f
                )
    ) {
        content()
    }
}

@Composable
private fun CategoryListItem(
    category: Category,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon/Image
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {


                AsyncImage(model = category.image_url, contentDescription = null)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Category Name
            Text(
                text = category.name.orEmpty(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            // Arrow Icon
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Open",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SubCategoryHeader(
    categoryName: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val backgroundModifier = remember(size) {
        if (size.width > 0 && size.height > 0) {
            val radius = maxOf(size.width, size.height) / 1.5f
            Modifier.background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFA40006), Color(0xFF8B0005)),
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = radius
                )
            )
        } else {
            Modifier.background(Color(0xFFA40006))
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { size = it },
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(backgroundModifier)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = categoryName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                placeholder = "Search subcategories",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = searchQuery.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color.Gray
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = Color(0xFFA40006),
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),
        singleLine = true
    )
}



@Composable
private fun CategoriesContent(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = categories,
            key = { it.id ?: it.hashCode() }
        ) { category ->
            CategoryCard(
                category = category,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Composable
private fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Add your category image/icon here
            Text(
                text = category.name.orEmpty(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
@Composable
private fun CategoriesContent(
    categoriesState: List<Category>,
    onCategoryClick: (Category) -> Unit,
    searchQuery: String
) {
    Column {
        // Header
        SectionHeader(
            title = "Categories",
            subtitle = "Choose your category"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show search results info
        if (searchQuery.isNotEmpty()) {
            Text(
                text = "${categoriesState.size} categories found",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontSize = 12.sp,
                color = Color(0xFF666666)
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (categoriesState.isEmpty() && searchQuery.isNotEmpty()) {
                item {
                    EmptyState(
                        message = "No categories found for \"$searchQuery\"",
                        icon = Icons.Outlined.Search
                    )
                }
            } else {
                items(
                    items = categoriesState,
                    key = { it.id }
                ) { category ->
                    CategoryItem(
                        category = category,
                        onClick = { onCategoryClick(category) }
                    )
                }
            }

            // Add bottom padding
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SubCategoriesContent(
    subCategoriesState: UiState<List<sub_categories>>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit={},
    onSubCategoryClick: (sub_categories) -> Unit,
    onRetry: () -> Unit
) {
    Column {
        // Search Bar for Subcategories



        // Header
        SectionHeader(
            title = "Subcategories",
            subtitle = "Select a subcategory"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // SubCategories List
        when (subCategoriesState) {
            UiState.Empty -> {
                EmptyState(
                    message = "No subcategories available",
                    icon = Icons.Outlined.Close
                )
            }

            is UiState.Error -> {
                ErrorState(
                    message = subCategoriesState.message,
                    onRetry = onRetry
                )
            }

            UiState.Loading -> {
                LoadingState()
            }

            is UiState.Success -> {
                val filteredSubCategories = subCategoriesState.data.filter {
                    it.name?.contains(searchQuery, ignoreCase = true) ?: false
                }



                // Show search results info
                if (searchQuery.isNotEmpty()) {
                    Text(
                        text = "${filteredSubCategories.size} subcategories found",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (filteredSubCategories.isEmpty() && searchQuery.isNotEmpty()) {
                        item {
                            EmptyState(
                                message = "No subcategories found for \"$searchQuery\"",
                                icon = Icons.Outlined.Search
                            )
                        }
                    } else {
                        items(
                            items = filteredSubCategories,
                            key = { it.id }
                        ) { subCategory ->
                            SubCategoryItem(
                                subCategory = subCategory,
                                onClick = { onSubCategoryClick(subCategory) }
                            )
                        }
                    }

                    // Add bottom padding
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String? = null
) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp
                )

                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = blueColor,
                    letterSpacing = 2.sp
                )

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp
                )
            }

            subtitle?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    fontSize = 12.sp,
                    color = blueColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFE8E8E8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Image
            Card(
                modifier = Modifier.size(58.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {
                AsyncImage(
                    model = category.image_url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Category Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                category.name?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2C2C2C),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Arrow Icon
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF999999)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubCategoryItem(
    subCategory: sub_categories,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            pressedElevation = 4.dp
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, Color(0xFFE8E8E8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon (optional)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                       blueColor.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = subCategory.name?.take(1)?.uppercase() ?: "",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color =blueColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Subcategory Name
            subCategory.name?.let {
                Text(
                    text = it,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C2C2C),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Arrow Icon
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = blueColor
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color(0xFF6B0221),
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading...",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.Warning,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = Color(0xFFE57373)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Oops! Something went wrong",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C2C2C),
            textAlign = TextAlign.Center
        )

        Text(
            text = message,
            fontSize = 14.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6B0221)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Retry",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun EmptyState(
    message: String,
    icon: ImageVector
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = Color(0xFFBDBDBD)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            fontSize = 16.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}