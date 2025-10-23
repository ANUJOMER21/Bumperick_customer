package com.bumperpick.bumperickUser.Screens.Campaign

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperickUser.API.New_model.DataXXXXXX
import com.bumperpick.bumperickUser.API.New_model.EventModel
import com.bumperpick.bumperickUser.R
import com.bumperpick.bumperickUser.Screens.Component.ResponsiveLazyList
import com.bumperpick.bumperickUser.Screens.Component.share
import com.bumperpick.bumperickUser.Screens.Contest.toFormattedDate
import com.bumperpick.bumperickUser.ui.theme.BtnColor
import com.bumperpick.bumperickUser.ui.theme.blueColor
import com.bumperpick.bumperickUser.ui.theme.satoshi_bold
import com.bumperpick.bumperickUser.ui.theme.satoshi_regular
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel

// Define UiState sealed class if not already defined


@Composable
fun EventScreen(
    onBackClick: () -> Unit,
    onFavClick:()-> Unit,
    onNotificationClick: () -> Unit = {},
    gotoEventRegister: (DataXXXXXX) -> Unit = {},
    viewmodel: EventScreenViewmodel = koinViewModel(),
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val events by viewmodel.eventstate.collectAsState()
    LaunchedEffect (Unit){
        viewmodel.getEvents()
    }

    // Load events when the screen is first composed or tab changes
    val statusBarColor = Color(0xFFA40006) // Your desired color
    val systemUiController = rememberSystemUiController()    // Change status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // true for dark icons on light background
        )
    }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            Color.White.copy(alpha = 0.05f),
            Color.White.copy(alpha = 0.07f),
            Color.White.copy(alpha = 0.1f),
            Color.White.copy(alpha = 0.2f)
        )
    )
    val transparentBrush = Brush.horizontalGradient(
        colors = listOf(Color.Transparent, Color.Transparent)
    )
    Scaffold {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .background(Color(0xFFFAFAFA))
    )
    {
        var size by remember { mutableStateOf(IntSize.Zero) }
        val backgroundModifier = remember(size) {
            if (size.width > 0 && size.height > 0) {
                val radius = maxOf(size.width, size.height) / 1.5f
                Modifier.background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFA40006), Color(0xFFA40006)),
                        center = Offset(size.width / 2f, size.height / 2f),
                        radius = radius
                    )
                )
            } else {
                Modifier.background(Color(0xFFA40006))
            }
        }

        // Header Card with improved padding and structure
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size = it },
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 24.dp,
                bottomEnd = 24.dp
            ),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(backgroundModifier)
                    .padding(bottom = 0.dp)
            )
            {
                Spacer(modifier = Modifier.height(20.dp))

                // Top App Bar with improved spacing
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Campaigns",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    }

                    // Action Icons with improved spacing and backgrounds
               /*     Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 4.dp)
                    )
                    {
                        // Custom Icon Button
                        IconButton(
                            onClick = onFavClick,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.icon),
                                contentDescription = "Custom Action",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Notification Icon
                        IconButton(
                            onClick = onNotificationClick,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }*/
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Search Field with improved styling
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = Color.Gray.copy(alpha = 0.7f)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = Color.Gray.copy(alpha = 0.7f)
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Search campaigns",
                            color = Color.Gray.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedBorderColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Tab Row with improved styling
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .height(4.dp)
                                .padding(horizontal = 0.dp),
                            color = Color.White
                        )
                    },
                    modifier = Modifier.padding(horizontal = 0.dp)
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Text(
                                "Ongoing campaigns",
                                fontSize = 16.sp,
                                fontWeight = if (selectedTabIndex == 0) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier
                            .background(if (selectedTabIndex == 0) gradientBrush else transparentBrush)
                    )

                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Text(
                                "Your campaigns",
                                fontSize = 16.sp,
                                fontWeight = if (selectedTabIndex == 1) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier
                            .background(if (selectedTabIndex == 1) gradientBrush else transparentBrush)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (events) {
        UiState.Empty -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "No campaigns available",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                    color = BtnColor
                )
            }
        }
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = (events as UiState.Error).message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                    color = BtnColor
                )
            }
        }
        UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = BtnColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        is UiState.Success -> {
            val eventList = (events as UiState.Success<EventModel>).data.data
            val selectedlist= eventList
                .filter {if(selectedTabIndex==1)it.is_registered else !it.is_registered}
                .filter { !it.expire }

            val filteredList = selectedlist.filter {
                it.title.contains(searchQuery, ignoreCase = true)
            }
            Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                Icon(painter = painterResource(R.drawable.left),
                    tint = blueColor,
                    contentDescription = null, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(10.dp))
                val ongoing_head_test = if (selectedlist.size == 0) "No ongoing campaign" else if(selectedlist.size==1) "1 ongoing campaign" else "${selectedlist.size} campaigns"
                val my_head_test = if (selectedlist.size == 0) "No campaign" else if(selectedlist.size==1) "1 campaign" else "${selectedlist.size} campaigns"

                // Events count with improved styling
                Text(
                    text = if (selectedTabIndex == 0) ongoing_head_test else my_head_test,
                    letterSpacing = 2.sp, // Use sp for text spacing, not dp
                    color = blueColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(painter = painterResource(R.drawable.right),
                    tint = blueColor,contentDescription = null, modifier = Modifier.size(12.dp))


            }
           Spacer(modifier = Modifier.height(12.dp))

                // Events List with improved padding

//            ResponsiveLazyList(items=filteredList) {
//                    event ->
//                EventCard(event) {
//                    gotoEventRegister(it)
//            }
//                }
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                )
                {
                    if (filteredList.isEmpty()) {
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(24.dp).fillMaxSize()
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.artwork),
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    "No campaign found",
                                    modifier = Modifier.padding(horizontal = 0.dp).fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = blueColor
                                )
                            }
                        }
                    } else {
                        items(filteredList) { event ->
                            EventCard(event) {
                                gotoEventRegister(it)
                            }
                        }
                    }
                }
            }
        }


    }
    }
}

@Composable
fun EventCard(events: DataXXXXXX, onClick: (DataXXXXXX) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Banner Image with Badge
            Box {
                AsyncImage(
                    model = events.banner_image_url,
                    contentDescription = "CAMPAIGNS Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
                share(modifier = Modifier.align(Alignment.TopEnd), message = "Check out this amazing campaign", context = LocalContext.current)

            }

            // Event Details
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = events.title,
                    fontFamily = satoshi_bold,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                InfoRow(
                    icon = painterResource(R.drawable.home),
                    text = events.description
                )

                InfoRow(
                    icon = painterResource(R.drawable.loading),
                    text = "Last date: ${(events.end_date).toFormattedDate()}"
                )

                InfoRow(
                    icon = Icons.Outlined.LocationOn,
                    text = events.address,
                    vectorIcon = true
                )
                InfoRow(
                    icon = Icons.Outlined.Person,
                    text = "Maximum participants: ${events.number_of_participant}",
                    vectorIcon = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Gray.copy(alpha = 0.15f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Registration / Expiry Info
                if (events.is_registered) {
                    InfoRow(
                        icon = painterResource(R.drawable.check),
                        text = "You have registered for this campaign.",
                        iconTint = blueColor,
                        textColor = blueColor
                    )
                } else {
                    OutlinedButton(
                        onClick = { onClick(events) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        border = BorderStroke(1.dp, BtnColor),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Register for campaign",
                            color = BtnColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: Any,
    text: String,
    vectorIcon: Boolean = false,
    iconTint: Color = Color.Gray,
    textColor: Color = Color.Black
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        if (vectorIcon && icon is ImageVector) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = iconTint
            )
        } else if (icon is Painter) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = iconTint
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = satoshi_regular,
            fontWeight = FontWeight.Medium,
            color = textColor.copy(alpha = 0.8f)
        )
    }
}
