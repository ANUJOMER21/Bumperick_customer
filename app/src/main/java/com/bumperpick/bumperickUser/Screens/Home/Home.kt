package com.bumperpick.bumperickUser.Screens.Home

import FilterSortScreen
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import com.bumperpick.bumperickUser.Animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bumperpick.bumperickUser.API.New_model.Category
import com.bumperpick.bumperickUser.API.New_model.DataXXXXXXXXXXXXX
import com.bumperpick.bumperickUser.API.New_model.Offer
import com.bumperpick.bumperickUser.API.New_model.eventSlider
import com.bumperpick.bumperickUser.Navigation.show_toast
import com.bumperpick.bumperickUser.R
import com.bumperpick.bumperickUser.Screens.Component.AutoImageSlider
import com.bumperpick.bumperickUser.Screens.Component.AutoImageSlider_clickable
import com.bumperpick.bumperickUser.Screens.Component.CategoryItem
import com.bumperpick.bumperickUser.Screens.Component.ChipRowWithSelectiveIcons

import com.bumperpick.bumperickUser.Screens.Component.HomeOfferView
import com.bumperpick.bumperickUser.Screens.Component.LocationCard
import com.bumperpick.bumperickUser.Screens.Component.ReportBottomSheet
import com.bumperpick.bumperickUser.Screens.Component.reportModel

import com.bumperpick.bumperickUser.ui.theme.BtnColor
import com.bumperpick.bumperickUser.ui.theme.blueColor
import com.bumperpick.bumperickUser.ui.theme.satoshi
import com.bumperpick.bumperpickvendor.API.Model.success_model
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel



sealed class HomeClick(){
    data class OfferClick(val offerId:String):HomeClick()
    object CartClick:HomeClick()
    object LocationClick:HomeClick()
    object SearchClick:HomeClick()
    object FavClick: HomeClick()
    object NotifyClick: HomeClick()
    object EventIncity: HomeClick()
    data class EventClick(val id: Int):HomeClick()
    data class CategoryClick(val cat:Category):HomeClick()

}


sealed class gotoPage(){
    object Event:gotoPage()
    object Campaign:gotoPage()
}
@Composable
fun CampaignSlider(goto: (gotoPage) -> Unit) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    // Auto-scroll


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()

        ) { page ->
            when (page) {
                1 -> EventCard({ goto(gotoPage.Event) })
                0-> CampaignCard { goto(gotoPage.Campaign) }
            }
        }

        // Indicator
        Row(
            modifier = Modifier.padding(top = 6.dp, bottom = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(2) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (pagerState.currentPage == index) 12.dp else 8.dp)
                        .background(
                            color = if (pagerState.currentPage == index)
                             BtnColor
                            else
                                Color.Gray,
                            shape = MaterialTheme.shapes.small
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    homeClick: (HomeClick) -> Unit,
    gotoEvent: () -> Unit,
    gotoCampaign: () -> Unit,
    viewModel: HomePageViewmodel = koinViewModel()
) {

    val offerDetails by viewModel.offer_uiState.collectAsState()
    val categoryDetails by viewModel.categories_uiState.collectAsState()
    val locationDetails by viewModel.getLocation.collectAsState()

    val context = LocalContext.current
    val fav_toogle by viewModel.fav_toogle_uiState.collectAsState()
    val fetch_banner by viewModel.banner.collectAsState()
    val event_banner by viewModel.event_banner.collectAsState()
    var hide_cat by remember { mutableStateOf(false) }
    val report_state by viewModel.reportOffer_uiState.collectAsState()
    var showReportSheet by remember { mutableStateOf(false) }
    var currentReportModel by remember { mutableStateOf<reportModel?>(null) }

    LaunchedEffect(report_state) {
       when(report_state){
           UiState.Empty ->{}
           is UiState.Error ->{
               Toast.makeText(context,(report_state as UiState.Error).message ,Toast.LENGTH_SHORT).show()
                viewModel.free_report_state()           }
           UiState.Loading ->{

           }
           is UiState.Success<success_model> -> {
               Toast.makeText(context, (report_state as UiState.Success<success_model>).data.message,Toast.LENGTH_SHORT).show()
           viewModel.free_report_state()
           }
       }

    }






    // Fetch data only once when the composable enters the composition
    LaunchedEffect(Unit) {
       viewModel.getOffers()
        viewModel.getCategory()
        viewModel.fetchEventBanner()
        //viewModel.clearFilter()
        viewModel.fetchLocation()
        viewModel.fetchBanner()
    }

    LaunchedEffect(fav_toogle) {
        when(fav_toogle){
            UiState.Empty -> {}
            is UiState.Error -> {
                show_toast((fav_toogle as UiState.Error).message,context)
            }
            UiState.Loading -> {}
            is UiState.Success -> {
                show_toast((fav_toogle as UiState.Success<success_model>).data.message,context)
                viewModel.free_fav()
            }
        }
    }
    Scaffold(containerColor = Color.White) {

        Column(
            modifier = Modifier
                .padding(top=it.calculateTopPadding())
                .fillMaxSize() // Only one scrollable column
        )
        {

            LocationCard(
                locationDetails = locationDetails,

                onCartClick = { homeClick(HomeClick.CartClick) },
                onEventCity = {homeClick(HomeClick.EventIncity)},
                onLocationClick = { homeClick(HomeClick.LocationClick) },
                onFavClick = { homeClick(HomeClick.FavClick) },
                onNotificationClick = { homeClick(HomeClick.NotifyClick) },
                modifier = Modifier.animateContentSize(
                    animationSpec = tween(durationMillis = 300),
                ),
            ) {
                // Search Card
                SearchCard(onSearchClick = { homeClick(HomeClick.SearchClick) })

                // Category List
                AnimatedVisibility(
                    visible = hide_cat,
                    enter = fadeInTransition() + slideInFromBottom(),
                    exit = fadeOutTransition() + slideOutToBottom(),
                    modifier = Modifier

                ) {
                    Column {

                        CategoryContent(
                            categoryDetails = categoryDetails,
                            onCategoryClick = { selectedCategory ->
                                homeClick(HomeClick.CategoryClick(selectedCategory))
                            }
                        )
                    }
                }


            }
            val scrollState = rememberScrollState()
            val thirdElementPosition = with(LocalDensity.current) { (150.dp * 2).toPx() }
            LaunchedEffect(scrollState.value) {
                hide_cat = scrollState.value < thirdElementPosition
            }
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            )
            {
                Spacer(modifier = Modifier.height(12.dp))


                // Trending Offers Header
                TrendingOffersHeader()
                Spacer(modifier = Modifier.height(12.dp))

                // Image Slider
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    border = BorderStroke(0.1.dp, Color.Black)
                ) {
                    when (fetch_banner) {
                        UiState.Empty -> {}
                        is UiState.Error -> {

                        }

                        UiState.Loading -> {
                            CircularProgressIndicator(color = BtnColor)
                        }

                        is UiState.Success<*> -> {
                            val banners =
                                (fetch_banner as UiState.Success<List<DataXXXXXXXXXXXXX>>).data.map { it.banner }
                            AutoImageSlider(banners, height = 160.dp)
                        }
                    }

                }
                Spacer(modifier = Modifier.height(12.dp))


                CampaignSlider {
                    when (it) {
                        gotoPage.Event -> gotoEvent()
                        gotoPage.Campaign -> gotoCampaign()
                    }
                }


                when (event_banner) {
                    UiState.Empty -> Unit

                    is UiState.Error -> {
                        Text(
                            text = "Failed to load banners",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Red
                        )
                    }

                    UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = BtnColor)
                        }
                    }

                    is UiState.Success<*> -> {
                        val banners = (event_banner as UiState.Success<List<eventSlider>>).data
                        if (banners.isEmpty()) {


                        }
                        else {
                            Column {
                                EventCityHeader() {
                                    homeClick(HomeClick.EventIncity)
                                }
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    border = BorderStroke(0.1.dp, Color.Black)
                                ) {

                                        // The slider first (background)
                                        AutoImageSlider_clickable(
                                            images = banners,
                                            height = 200.dp
                                        ) {
                                            homeClick(HomeClick.EventClick(it))

                                }


                                }
                            }


                        }
                    }

                }








                Spacer(modifier = Modifier.height(8.dp))

                // Filter and Sort
                // Pass the actual category data when it's available
                FilterSortScreen(
                    viewmodel = viewModel,
                    onFiltersApplied = {
                        viewModel.updateFilters(it)


                    },
                    onSortSelected = {
                        val id = it.id
                        viewModel.updateSortBy(id)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Offers Content
                OffersContent(
                    offerDetails = offerDetails,
                    onOfferClick = { offerId -> homeClick(HomeClick.OfferClick(offerId)) },
                    liketheoffer = { offerId ->
                        viewModel.toogle_fav(offerId, true)
                    },
                    shareoffer = {
                        Log.d("shareoffer", it)
                        shareReferral(context, it)

                    },
                    reportclick = {it->
                        showReportSheet=true
                        currentReportModel=it


                    }
                )

            }
            // Display Toast for errors
            DisplayErrorToast(uiState = categoryDetails, context = context)
            DisplayErrorToast(uiState = offerDetails, context = context)
        }


    }
    if (showReportSheet && currentReportModel != null) {
        ReportBottomSheet(
            reportModel = currentReportModel!!,
            onDismiss = {
                showReportSheet = false
                currentReportModel = null
            },
            onSubmit = { model, reason ->
                // Handle the report submission
                println("Reporting: ${model.id} - ${model.title}")
                println("Reason: $reason")

                viewModel.reportOffer(model, reason)

                // Call your API or ViewModel method here
                // viewModel.submitReport(model, reason)

                // Close the sheet
                showReportSheet = false
                currentReportModel = null
            }
        )
    }

}

// --- Private Composable Functions for Modularity ---

@Composable
private fun SearchCard(onSearchClick: () -> Unit) {
    Card(
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onSearchClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                tint = Color.Black,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search for reliance mart",
                color = Color.Gray,
                fontFamily = satoshi,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun CategoryContent(
    categoryDetails: UiState<List<Category>>,
    onCategoryClick: (Category) -> Unit
) {
    when (categoryDetails) {
        is UiState.Error -> {
            // Error handling for categories can be shown here, e.g., a Retry button
            Text("Failed to load categories: ${categoryDetails.message}", color = MaterialTheme.colorScheme.error)
        }
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BtnColor)
            }
        }
        is UiState.Success -> {
            if (categoryDetails.data.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    items(categoryDetails.data) { category ->
                        CategoryItem(category) { selectedCategory ->
                            onCategoryClick(selectedCategory)
                        }
                    }
                }
            } else {
                Text("No categories available.", modifier = Modifier.padding(horizontal = 16.dp), color = Color.White)
            }
        }
        is UiState.Empty -> {
            Text("No categories available.", modifier = Modifier.padding(horizontal = 16.dp), color = Color.White)
        }
    }
}

@Composable
private fun CampaignCard(gotoEvent: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor =  Color(0xff2F88FF).copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xff2F88FF)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 0.dp) // Apply padding here once
            .clickable { gotoEvent() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = R.drawable.speaker,
                    contentDescription = "Speaker Icon",
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = "Trending campaigns",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Outlined.ArrowForward,
                contentDescription = "Forward",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(24.dp),
                tint =  Color(0xff2F88FF)
            )
        }
    }
}
@Composable
private fun EventCard(gotoEvent: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xff2F88FF).copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xff2F88FF)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 0.dp)
            .clickable { gotoEvent() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = R.drawable.ticket_svgrepo_com,
                    contentDescription = "Speaker Icon",
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = "Trending events",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Outlined.ArrowForward,
                contentDescription = "Forward",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(24.dp),
                tint = Color(0xff2F88FF)
            )
        }
    }
}

@Composable
private fun EventCityHeader(
    text: String = "Events in the city",
    onViewAllClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        // Title - Centered
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(end = 40.dp) // Offset to account for "View All" button width
        ) {
            Icon(
                painter = painterResource(R.drawable.left),
                contentDescription = null,
                tint = blueColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = text,
                letterSpacing = 1.sp,
                color = blueColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                painter = painterResource(R.drawable.right),
                contentDescription = null,
                tint = blueColor,
                modifier = Modifier.size(12.dp)
            )
        }

        // View All Button - Always at end with 20.dp spacing from title
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(6.dp))
                .clickable { onViewAllClick() }
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "View All",
                letterSpacing = 0.3.sp,
                color = BtnColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Outlined.ArrowForward,
                contentDescription = "View All",
                tint = BtnColor,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}


@Composable
private fun TrendingOffersHeader( text: String="Brands & offers") {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            Icon(
                painter = painterResource(R.drawable.left), // Ensure these drawables exist
                contentDescription = null,
                tint = blueColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                letterSpacing = 3.sp,
                color = blueColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(R.drawable.right), // Ensure these drawables exist
                contentDescription = null,
                tint = blueColor,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Composable
fun OffersContent(
    offerDetails: UiState<List<Offer>>,
    onOfferClick: (String) -> Unit,
    liketheoffer:(String)->Unit={},
    shareoffer:(String)-> Unit={},
    reportclick:(reportModel)-> Unit={}
) {
    when (offerDetails) {
        is UiState.Error -> {
            // Error handling for offers, e.g., a message or a retry button
            Text("Failed to load offers: ${offerDetails.message}", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
        }
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BtnColor)
            }
        }
        is UiState.Success -> {

            val data = offerDetails.data.filter { it.expire==false || it.is_ads ==true }
            Log.d("HOME_OFFER",data.toString())

            if (data.isNotEmpty()) {
                Column { // Offers are shown in a column, not lazy
                    data.forEach { offer ->
                        HomeOfferView(
                            offerModel = offer,
                            offerClick = { onOfferClick(offer.id.toString()) },
                           liketheoffer = liketheoffer,
                            shareoffer={
                                Log.d("shareoffer",it)
                                shareoffer(it)
                            },
                            reportClick =reportclick
                        )
                    }
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Image(painter = painterResource(R.drawable.artwork), contentDescription = null, modifier = Modifier.size(100.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("No brands and offers available.", modifier = Modifier.padding(horizontal = 0.dp).fillMaxWidth(), textAlign = TextAlign.Center, color = BtnColor)
                }

            }
        }
        is UiState.Empty -> {
            Text("No brands and offers available.", modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
fun DisplayErrorToast(uiState: UiState<*>, context: android.content.Context) {
    if (uiState is UiState.Error) {
        LaunchedEffect(uiState.message) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        }
    }
}
