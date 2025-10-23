package com.bumperpick.bumperickUser.Screens.Component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.text.Spannable
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.animation.core.tween
import com.bumperpick.bumperickUser.Animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperickUser.R
import com.bumperpick.bumperickUser.ui.theme.BtnColor
import com.bumperpick.bumperickUser.ui.theme.satoshi_regular

import androidx.compose.material3.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.bumperpick.bumperickUser.API.New_model.Category
import com.bumperpick.bumperickUser.API.New_model.DataXX
import com.bumperpick.bumperickUser.API.New_model.Media
import com.bumperpick.bumperickUser.API.New_model.Offer
import com.bumperpick.bumperickUser.API.New_model.eventSlider
import com.bumperpick.bumperickUser.Navigation.DEEP_LINK_BASE_URL
import com.bumperpick.bumperickUser.Screens.Home.HomePageViewmodel
import com.bumperpick.bumperickUser.Screens.Home.Map.LocationData
import com.bumperpick.bumperickUser.Screens.Home.UiState
import com.bumperpick.bumperickUser.Screens.Home.shareReferral
import com.bumperpick.bumperickUser.ui.theme.blueColor

import com.bumperpick.bumperickUser.ui.theme.grey
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
fun String.withRedAsterisk(): AnnotatedString {
    return buildAnnotatedString {
        append(this@withRedAsterisk)
        append(" ")
        withStyle(style = SpanStyle(color = Color.Red)) {
            append("*")
        }
    }
}
@Composable
fun LocationCard(
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onEventCity:()-> Unit={},
    onFavClick: () -> Unit = {},
    locationDetails: UiState<LocationData>,
    content: @Composable ColumnScope.() -> Unit = {}
) {

    // Simple state management
    var locationTitle by remember { mutableStateOf( "Current Location")}
    var locationSubtitle by remember { mutableStateOf( "Tap to select location")}
    LaunchedEffect(locationDetails) {
        when(locationDetails) {
            UiState.Empty -> {}
            is UiState.Error -> {

            }

            UiState.Loading -> {}
            is UiState.Success -> {
                locationTitle = locationDetails.data.area
                locationSubtitle = locationDetails.data.city
            }
        }
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFA40006), Color(0xFFA40006)),
                        radius = 800f
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 0.dp, end = 0.dp, bottom = 0.dp)
            ) {
                Column {
                   Spacer(modifier= Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onLocationClick() }
                            .padding(vertical = 4.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        // Location Info
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = "Location",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = locationTitle,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Icon(
                                        imageVector = Icons.Outlined.ArrowDropDown,
                                        contentDescription = "Expand",
                                        tint = Color.White.copy(alpha = 0.8f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Text(
                                    text = locationSubtitle,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Action Icons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            IconButton(
                                onClick = onEventCity,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.lightnings_flash_svgrepo_com),
                                    contentDescription = "city_event",

                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            IconButton(
                                onClick = onCartClick,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ShoppingCart,
                                    contentDescription = "Cart",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            IconButton(
                                onClick = onFavClick,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.icon),
                                    contentDescription = "Favorites",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            IconButton(
                                onClick = onNotificationClick,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }


                    content()
                }
            }

        }
    }
}
@Composable
fun TextFieldView(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Placeholder...",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = LocalTextStyle.current,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFFF5F5F5), // Default light gray background
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = textStyle.copy(color = Color.Gray)
            )
        },
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        modifier = modifier,
        singleLine = singleLine,
        enabled = isEnabled,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = containerColor,
            disabledTextColor = Color.Black,
            focusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            cursorColor =  BtnColor,
            focusedBorderColor =  BtnColor,
            unfocusedBorderColor = Color.Gray,

        )
    )
}

@Composable
fun ButtonView(text:String,
               enabled:Boolean=true,
               modifier: Modifier=Modifier, textColor: Color=Color.White, color: Color=BtnColor, horizontal_padding:Dp=16.dp, onClick:()->Unit) {
    var isPressed by remember { mutableStateOf(false) }
    
    Button(
        onClick = { 
            isPressed = true
            onClick() 
        },
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(bottom = 20.dp, start = horizontal_padding, end = horizontal_padding)
            .animateScale(isPressed = isPressed),

        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(16.dp)

    ) {
        Text(text, color = textColor, fontFamily = satoshi_regular, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
    
    // Reset pressed state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
fun Google_SigInButton(modifier: Modifier=Modifier,onCLick:()->Unit) {
    var isPressed by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { 
            isPressed = true
            onCLick()
        },
        border = BorderStroke(0.dp, Color.Transparent),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(
                color = Color(0xFFF0F0F0),
                shape = RoundedCornerShape(16.dp)
            )
            .animateScale(isPressed = isPressed)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.google_icon_logo_svgrepo_com),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .width(18.dp)
                    .height(18.dp)
            )
            Text(
                "Sign in with Google",
                color = Color(0xFF212427),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
    
    // Reset pressed state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}
@Composable
fun OtpView(
    numberOfOtp: Int,
    value: String,
    onValueChange: (String) -> Unit,
    otpCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequesters = List(numberOfOtp) { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(numberOfOtp) { index ->
            val char = if (index < value.length) value[index].toString() else ""
            OutlinedTextField(
                value = char,
                onValueChange = { newChar ->
                    if (newChar.length <= 1) {
                        val newValue = buildString {
                            append(value.take(index))
                            append(newChar)
                            append(value.drop(index + 1))
                        }.take(numberOfOtp)
                        onValueChange(newValue)
                        if (newChar.isNotEmpty() && index < numberOfOtp - 1) {
                            focusRequesters[index + 1].requestFocus()
                        } else if (newChar.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                        if (newValue.length == numberOfOtp) {
                            otpCompleted(newValue)
                            keyboardController?.hide()
                        }
                    }
                },
                modifier = Modifier
                    .width(48.dp)
                    .focusRequester(focusRequesters[index]),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BtnColor,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    disabledBorderColor = Color.Black,
                    focusedContainerColor = grey,
                    unfocusedContainerColor = grey,
                    disabledContainerColor = grey,
                    cursorColor = BtnColor
                ),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (index < numberOfOtp - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                ),
                singleLine = true
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector? = null,
    val painter: Painter? = null,
    val contentDescription: String,
)
@Composable
fun BottomNavigationBar(
    items: List<NavigationItem>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.shadow(0.dp)
    ) {
        items.forEachIndexed { index, item ->
            var isPressed by remember { mutableStateOf(false) }
            
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { 
                    isPressed = true
                    onTabSelected(index) 
                },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Icon itself
                        item.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = item.contentDescription,
                                tint = if (selectedTab == index) Color(0xFF3B82F6) else Color.Gray,
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .animateScale(isPressed = isPressed)
                            )
                        }
                        item.painter?.let {
                            Icon(
                                painter = it,
                                contentDescription = item.contentDescription,
                                tint = if (selectedTab == index) Color(0xFF3B82F6) else Color.Gray,
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .animateScale(isPressed = isPressed)
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selectedTab == index) Color(0xFF3B82F6) else Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == index) FontWeight.Medium else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
                modifier = if (selectedTab == index) {
                    Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF3B82F6).copy(alpha = 0.1f),
                                    Color.White
                                )
                            )
                        )
                        .animateScale(isPressed = isPressed)
                } else Modifier.animateScale(isPressed = isPressed)
            )
            
            // Reset pressed state after animation
            LaunchedEffect(isPressed) {
                if (isPressed) {
                    delay(100)
                    isPressed = false
                }
            }
        }
    }
}



@Composable
fun CategoryItem(category: Category,onClick: (Category) -> Unit) {
    Log.d("category", category.image_url ?: "")
    var isPressed by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF830001)),
        border = BorderStroke(0.5.dp, Color(0xFFFFD700)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(90.dp)
            .width(80.dp)
            .clickable {
                isPressed = true
                onClick(category)
            }
            .animateScale(isPressed = isPressed)
            .animateElevation(isHovered = isPressed)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = category.image_url,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (2).dp, y = 2.dp)
            )

            Column(
                modifier = Modifier
                    .padding(start = 6.dp, top = 4.dp, end = 4.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = category.name?:"",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
    
    // Reset pressed state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}


@Composable
private fun ImageSliderItem(
    imageUrl: String,
    modifier: Modifier = Modifier
) {


    Log.d("ImageSliderItem",imageUrl)
    AsyncImage(
        model=imageUrl,
        contentDescription = "Slider Image",
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )

}

@Composable
fun AutoImageSlider(
    imageUrls: List<String>,
    height:Dp=150.dp,
    modifier: Modifier = Modifier,
    autoSlideInterval: Long = 5000000L, // 5 seconds
    slideAnimationDuration: Int = 800 // milliseconds
) {
    if (imageUrls.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { imageUrls.size }
    )

    // Auto-scroll effect
    LaunchedEffect(pagerState) {
        while (true) {
            delay(autoSlideInterval)
            val nextPage = (pagerState.currentPage + 1) % imageUrls.size
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(durationMillis = slideAnimationDuration)
            )
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),

        ) {
        // Image Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(height),
            pageSpacing = 8.dp
        ) { page ->
            ImageSliderItem(
                imageUrl = imageUrls[page],
                modifier = Modifier.fillMaxSize()
            )
        }



        // Red Dot Indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            repeat(imageUrls.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) BtnColor else BtnColor.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
}
@Composable
fun  AutoImageSlider_clickable(
    images: List<eventSlider>,
    height:Dp=150.dp,
    modifier: Modifier = Modifier,
    autoSlideInterval: Long = 5000000L, // 5 seconds
    slideAnimationDuration: Int = 800,
    image_click: (id: Int) -> Unit
) {
    val imageUrls =images.map {it.url }
    if (imageUrls.isEmpty()) return
    Log.d("AutoImageSlider_clickable", images.toString())
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { imageUrls.size }
    )

    // Auto-scroll effect
    LaunchedEffect(pagerState) {
        while (true) {
            delay(autoSlideInterval)
            val nextPage = (pagerState.currentPage + 1) % imageUrls.size
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(durationMillis = slideAnimationDuration)
            )
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),

        ) {
        // Image Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(height),
            pageSpacing = 8.dp
        ) { page ->
            ImageSliderItem(
                imageUrl = imageUrls[page],
                modifier = Modifier.fillMaxSize().clickable {
                    image_click(images[page].id)

                }
            )
        }



        // Red Dot Indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            repeat(imageUrls.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) BtnColor else BtnColor.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
}




@Composable
fun ChipRowWithSelectiveIcons() {
    val chips = listOf(
        ChipData("Filter", Icons.Default.List),
        ChipData("Sort by", Icons.Default.ArrowDropDown),
        ChipData("Offers", null),
        ChipData("Distance", null)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.forEach { chip ->
            ChipWithOptionalIcon(label = chip.label, icon = chip.icon)
        }
    }
}

@Composable
fun ChipWithOptionalIcon(label: String, icon: ImageVector?) {
    Surface(
        modifier = Modifier.height(36.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.White ,
        border = BorderStroke(1.dp, Color.Gray),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clickable { /* Handle click */ },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$label icon",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

data class ChipData(val label: String, val icon: ImageVector?)
enum class OfferValidation{
    Valid,Expired
}
enum class MarketingOption(val title: String) {
    OFFERS("Offers"),
    CUSTOMER_ENGAGEMENT("Customer engagement"),
    CONTEST_FOR_CUSTOMERS("Contest for customers"),
    SCRATCH_AND_WIN("Scratch & win"),
    LUCKY_DRAW("Lucky draw"),
    CAMPAIGNS("Campaigns"),
    EVENTS("Events");

    companion object {
        val allOptions = values().toList()
    }
}

private fun drawHorizontalDots(
    drawScope: DrawScope,
    color: Color,
    dotSizePx: Float,
    spacingPx: Float,
    dotCount: Int?
) {
    val width = drawScope.size.width
    val height = drawScope.size.height
    val centerY = height / 2
    val radius = dotSizePx / 2

    val totalDotWidth = dotSizePx + spacingPx
    val calculatedDotCount = dotCount ?: (width / totalDotWidth).toInt()
    val actualWidth = calculatedDotCount * totalDotWidth - spacingPx
    val startX = (width - actualWidth) / 2

    repeat(calculatedDotCount) { index ->
        val x = startX + index * totalDotWidth + radius
        drawScope.drawCircle(
            color = color,
            radius = radius,
            center = Offset(x, centerY)
        )
    }
}

private fun drawVerticalDots(
    drawScope: DrawScope,
    color: Color,
    dotSizePx: Float,
    spacingPx: Float,
    dotCount: Int?
) {
    val width = drawScope.size.width
    val height = drawScope.size.height
    val centerX = width / 2
    val radius = dotSizePx / 2

    val totalDotHeight = dotSizePx + spacingPx
    val calculatedDotCount = dotCount ?: (height / totalDotHeight).toInt()
    val actualHeight = calculatedDotCount * totalDotHeight - spacingPx
    val startY = (height - actualHeight) / 2

    repeat(calculatedDotCount) { index ->
        val y = startY + index * totalDotHeight + radius
        drawScope.drawCircle(
            color = color,
            radius = radius,
            center = Offset(centerX, y)
        )
    }
}
@Composable
fun DottedDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray,
    dotSize: Dp = 2.dp,
    spacing: Dp = 4.dp,
    isVertical: Boolean = false,
    dotCount: Int? = null
) {
    val density = LocalDensity.current

    Canvas(modifier = modifier) {
        val dotSizePx = with(density) { dotSize.toPx() }
        val spacingPx = with(density) { spacing.toPx() }

        if (isVertical) {
            drawVerticalDots(
                drawScope = this,
                color = color,
                dotSizePx = dotSizePx,
                spacingPx = spacingPx,
                dotCount = dotCount
            )
        } else {
            drawHorizontalDots(
                drawScope = this,
                color = color,
                dotSizePx = dotSizePx,
                spacingPx = spacingPx,
                dotCount = dotCount
            )
        }
    }
}
@Composable
fun MediaSlider(
    mediaList: List<Media>,
    height: Dp = 150.dp,
    modifier: Modifier = Modifier
) {
    if (mediaList.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { mediaList.size }
    )

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        // Media Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(height),
            pageSpacing = 8.dp
        ) { page ->
            MediaSliderItem(
                media = mediaList[page],
                modifier = Modifier.fillMaxSize()
            )
        }

        // Dot Indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            repeat(mediaList.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) BtnColor else BtnColor.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
}

@Composable
fun MediaSliderItem(
    media: Media,
    modifier: Modifier = Modifier
) {
    when (media.type.lowercase()) {
        "image" -> {
            ImageSliderItem(
                imageUrl = media.url,
                modifier = modifier
            )
        }
        "video" -> {
            VideoSliderItem(
                videoUrl = media.url,
                modifier = modifier
            )
        }
        else -> {
            // Fallback to image for unknown types
            ImageSliderItem(
                imageUrl = media.url,
                modifier = modifier
            )
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoSliderItem(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context)

            .build()
            .apply {
                // Mute the video
                volume = 0f
                // Set to loop
                repeatMode = Player.REPEAT_MODE_ONE
            }
    }

    // Prepare media source
    LaunchedEffect(videoUrl) {
        val mediaItem = MediaItem.fromUri(videoUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    // Handle lifecycle
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    exoPlayer.play()
                }

                Lifecycle.Event.ON_STOP -> {
                    exoPlayer.pause()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    // Hide controls for cleaner look
                    useController = false
                    // Set resize mode
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
            modifier = Modifier.fillMaxSize()
        )


    }
}

@ExperimentalMaterial3Api
@Composable
fun ReportBottomSheet(
    reportModel: reportModel,
    onDismiss: () -> Unit,
    onSubmit: (reportModel, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var reason by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
        ) {
            // Title - "Report This Offer"
            Text(
                text = "Report this offer",
                fontSize = 24.sp,
                fontFamily = satoshi_regular,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Product name
            Text(
                text = reportModel.title,
                fontSize = 18.sp,
                fontFamily = satoshi_regular,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // "Reason" label
            Text(
                text = "Reason",
                fontSize = 16.sp,
                fontFamily = satoshi_regular,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Reason TextField
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                placeholder = {
                    Text(
                        text = "",
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 16.sp,
                        fontFamily = satoshi_regular
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                maxLines = 6,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = satoshi_regular,
                    color = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE0E0E0),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color(0xFF1976D2)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cancel Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE8E8E8),
                        contentColor = Color.Black
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 18.sp,
                        fontFamily = satoshi_regular,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Submit Button
                Button(
                    onClick = {
                        if (reason.isNotBlank()) {
                            onSubmit(reportModel, reason.trim())
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB71C1C),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFB71C1C).copy(alpha = 0.5f),
                        disabledContentColor = Color.White.copy(alpha = 0.7f)
                    ),
                    enabled = reason.isNotBlank(),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Text(
                        text = "Submit",
                        fontSize = 18.sp,
                        fontFamily = satoshi_regular,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun share(
    message: String,
    context: Context,
    modifier: Modifier= Modifier, ){
    Surface(modifier=modifier, color = Color.Transparent) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                .background(
                    color = Color.Gray.copy(0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    0.5.dp,
                    blueColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    Log.d("shareoffer", "click")
                    shareReferral(context,message)
                }
                .padding(4.dp),
        )
        {
            Icon(
                Icons.Default.Share,
                contentDescription = "Share offer",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(26.dp),
                tint =

                    blueColor

            )
        }
    }

}

// Data class (already provided by user)
data class reportModel(val id: String,val title: String)
@Composable
fun HomeOfferView(offerModel: Offer,
                  offerClick:(String)->Unit,
                  liketheoffer:(String)->Unit={},
                  shareoffer:(String)-> Unit={},
                  reportClick:(reportModel)-> Unit ={}
                  ){


    Card (
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                offerModel.is_ads?.let {
                    if(!it){
                        offerClick(offerModel.id.toString())
                    }
                }
                       },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        ){
        Column() {

            Box(modifier = Modifier.fillMaxWidth()){
                val media_list: List<Media> = when {
                    offerModel.media.isNotEmpty() -> offerModel.media
                    !offerModel.brand_logo_url.isNullOrEmpty() -> listOf(
                        Media(0, type = "image", url = offerModel.brand_logo_url)
                    )
                    else -> emptyList()
                }

                if(offerModel.is_ads == true){
                    Log.d("Media list ad",offerModel.media.toString())
                }

                MediaSlider(if(offerModel.is_ads == true) offerModel.media else media_list, height = 180.dp)


                if(offerModel.is_ads==false) {
                    Column(modifier = Modifier.align(Alignment.TopEnd)) {
                        Box(
                            modifier = Modifier
                                .padding(start=16.dp,end=16.dp,top=16.dp, bottom = 8.dp)
                                .background(
                                    color = Color.Gray.copy(0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    0.5.dp,
                                   blueColor,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { liketheoffer(offerModel.id.toString()) }
                                .padding(4.dp),
                        ) {
                            Icon(
                                if (offerModel.is_favourited==true) {
                                    Icons.Outlined.Favorite
                                } else {
                                    Icons.Outlined.FavoriteBorder
                                },
                                contentDescription = "Like offer",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(26.dp),
                                tint =
                                    if (offerModel.is_favourited==true) {
                                        BtnColor
                                    } else {
                                        blueColor
                                    }
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(start=16.dp,end=16.dp,top=8.dp, bottom = 16.dp)
                                .background(
                                    color = Color.Gray.copy(0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    0.5.dp,
                                    blueColor,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    Log.d("shareoffer","click")
                                    shareoffer("Check out this amazing offer: ${offerModel.title}\nLink: ${DEEP_LINK_BASE_URL}offer/${offerModel.id}")
                                    }
                                .padding(4.dp),
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Share offer",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(26.dp),
                                tint =

                                       blueColor

                            )
                        }


                    }


                
                
                    Box(
                        modifier = Modifier

                            .clip(
                                RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 12.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            )
                            .background(Color.White)
                            .align(Alignment.BottomStart)
                    ) {
                        Text(
                            text  = if(offerModel?.is_unlimited==1){
                                "While stocks last"
                            }else "${offerModel?.quantity} left",
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
                        )

                    }
                }




            }

            if(offerModel.is_ads==false) {
                Column(modifier = Modifier.padding(12.dp))
                {
                    Spacer(Modifier.height(5.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    )
                    {
                        Text(
                            text = offerModel.title ?: "",
                            fontSize = 22.sp,
                            fontFamily = satoshi_regular,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        if (offerModel.average_rating != null && offerModel.average_rating > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            )
                            {
                                repeat(5) { index ->
                                    Box(modifier = Modifier.size(26.dp)) {
                                        // Black outline star
                                        Icon(
                                            imageVector = Icons.TwoTone.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFAFAFAF),
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        // Golden filled star (only if rating covers this star)

                                        if (index < (offerModel.average_rating ?: 0.0)) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Star ${index + 1}",
                                                tint = Color(0xFFFFD700),
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(1.dp) // Small padding to show black outline
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    offerModel.description?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            fontFamily = satoshi_regular,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    DottedDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,

                        )
                    Spacer(modifier = Modifier.height(12.dp))


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    )
                    {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.percentage_red),
                                    contentDescription = "Discount percentage",
                                    modifier = Modifier.size(24.dp)
                                )

                                offerModel.discount?.let { discount ->
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = discount,
                                        fontSize = 15.sp,
                                        fontFamily = satoshi_regular,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Image(
                                painter = painterResource(R.drawable.caution_mark_svgrepo_com),
                                contentDescription = "Report offer",
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable {
                                        reportClick(
                                            reportModel(
                                                id = offerModel.id.toString(),
                                                title = offerModel.title.orEmpty()
                                            )
                                        )
                                    }
                            )
                        }


                    }


                }
            }



        }


    }


}
@Composable
fun CartOfferView(offerModel: DataXX, openQr: (id: String) -> Unit,deleteCart:(String)->Unit,showOpenQrBtn:Boolean=true,onCardClick:(id:String)->Unit={}) {

    val context= LocalContext.current
    var loading by remember { mutableStateOf(false) }

    val offer = offerModel.offer
    Log.d("offer",offer.toString())

    val media_list: List<Media> = when {
        offer!!.media.isNotEmpty() -> offer!!.media
        offer.brand_logo_url.isNotEmpty() -> listOf(
            Media(0, type = "image", url = offer.brand_logo_url)
        )
        else -> emptyList()
    }


    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).clickable { onCardClick(offer?.id.toString()) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        if(loading){
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(color = BtnColor)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Deleting...", color = BtnColor)
            }

        }
        else {
            Column {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Delete button


                    // Image slider
                    MediaSlider(media_list, height = 180.dp)
                    if(showOpenQrBtn) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(24.dp)
                                .background(
                                    color = blueColor.copy(0.1f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    0.5.dp,
                                    Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                        ) {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = "Delete offer",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .clickable {
                                        deleteCart(offerModel.id.toString())
                                    }
                                    .padding(4.dp)
                                    .size(30.dp),
                                tint = blueColor
                            )
                        }
                    }
                    // Quantity badge
                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 12.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            )
                            .background(Color.White)
                            .align(Alignment.BottomStart)
                    ) {

                        Text(
                            text = if(offer?.is_unlimited==1){
                                        "While stocks last"
                            }else "${offer?.quantity} left",
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
                        )
                    }
                }

                Column(modifier = Modifier.padding(12.dp)) {
                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = offer?.title?:"",
                        fontSize = 22.sp,
                        fontFamily = satoshi_regular,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = offer?.description?:"",
                        fontSize = 14.sp,
                        fontFamily = satoshi_regular,
                        color = Color.Black,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(modifier = Modifier.height(12.dp))

                    DottedDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Discount row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.percentage_red),
                                contentDescription = "percentage",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = offer?.discount?:"",
                                fontSize = 15.sp,
                                fontFamily = satoshi_regular,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                if(showOpenQrBtn) {
                    ButtonView(
                        text = "Open QR",
                        color = BtnColor.copy(alpha = 1f),
                        textColor = Color.White,
                        modifier = Modifier.padding(vertical = 0.dp)
                    ) {
                        openQr(offer?.id.toString())
                    }
                }
            }
        }
    }
}
fun generateQRCodeBitmap(content: String): Bitmap {
    val size = 512
    val bits = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

    for (x in 0 until size) {
        for (y in 0 until size) {
            val color = if (bits.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            bitmap.setPixel(x, y, color)
        }
    }

    return bitmap
}
@Composable
fun CartBottomSheet(jsonData: String,offerId: String,onBack:()->Unit){
    val bitmap = remember(jsonData) { generateQRCodeBitmap(jsonData) }
    val imageBitmap = remember(bitmap) { bitmap.asImageBitmap() }
    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                bitmap = imageBitmap,
                contentDescription = "QR Code",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Present this QR code at the outlet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

                ButtonView("Go back") {
                    onBack()

            }

        }
    }
}

@Composable
fun QRCodeBottomSheet(jsonData: String,offerId:String, onAddToCart: () -> Unit,goback:()->Unit,is_saved:Boolean=false) {
    val context = LocalContext.current
    val bitmap = remember(jsonData) { generateQRCodeBitmap(jsonData) }
    var showloading by remember { mutableStateOf(false) }
    val imageBitmap = remember(bitmap) { bitmap.asImageBitmap() }
    val viewnodel:HomePageViewmodel= koinViewModel()
    val cart=viewnodel.add_to_cart_uiState.collectAsState().value
    Log.d("is_saved",is_saved.toString())
    LaunchedEffect(cart) {
        when(cart){
            UiState.Empty -> {
                Log.d("error","empty")
            }
            is UiState.Error -> {
                showloading=false
                Toast.makeText(context, cart.message, Toast.LENGTH_SHORT).show()
            }
            UiState.Loading ->{
                showloading=true
            }
            is UiState.Success ->{
                showloading=false
                Toast.makeText(context,"Offer saved to the cart", Toast.LENGTH_SHORT).show()
                Log.d("success","s")
                    viewnodel.resetaddtocart()
                    onAddToCart()
            }
        }
    }

    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                bitmap = imageBitmap,
                contentDescription = "QR Code",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if(is_saved)"Present this QR code at the outlet to avail the offer" else "QR code generated",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))


            Spacer(modifier = Modifier.height(24.dp))

            if(showloading){
                CircularProgressIndicator(color = BtnColor)
            }
            else {
                ButtonView(if (!is_saved) "Save to the cart" else "Go back") {
                    if(!is_saved) {
                        viewnodel.addToCart(offerId)
                        }
                    else{
                        goback()
                    }
                }
            }

        }
    }
}
@Composable
fun SearchCard(
    title:String="",
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Search",
    onSearch: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Card(
        modifier = modifier
            .fillMaxWidth()

            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(width = 1.dp, color = Color.Gray.copy(alpha = 0.3f)),
          colors = CardDefaults.cardColors(containerColor = grey)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray,
                modifier = Modifier.padding(start = 12.dp).size(30.dp)
            )


            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("$hint $title", color = Color.Gray) },
                textStyle = TextStyle(fontSize = 18.sp),

                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch()
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                )
            )

            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear text",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun SignOutDialog(

    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Sign out",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to sign out?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "Sign out",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )

}



@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun <T> ResponsiveLazyList(
    items: List<T>,
    modifier: Modifier = Modifier,
    breakpoint: Int = 600, // width dp to switch from column to grid
    gridColumns: Int = 2,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    itemContent: @Composable (T) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        if (maxWidth < breakpoint.dp) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = contentPadding
            ) {
                items(items) { item ->
                    itemContent(item)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    itemContent(item)
                }
            }
        }
    }
}
@Preview
@Composable
fun TestResponsiveList() {
    val data = List(20) { "Item $it" }

    ResponsiveLazyList(items = data) { item ->
        Text(
            text = item,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}