package com.bumperpick.bumperickUser.Screens.Contest

import ContestViewmodel
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperickUser.API.New_model.Constest_Model
import com.bumperpick.bumperickUser.API.New_model.DataXXXXXXXXXXXXXX
import com.bumperpick.bumperickUser.API.New_model.contestregistered
import com.bumperpick.bumperickUser.R
import com.bumperpick.bumperickUser.Screens.Campaign.InfoRow
import com.bumperpick.bumperickUser.Screens.Component.LocationCard
import com.bumperpick.bumperickUser.Screens.Component.share
import com.bumperpick.bumperickUser.Screens.Home.HomeClick
import com.bumperpick.bumperickUser.Screens.Home.UiState
import com.bumperpick.bumperickUser.ui.theme.BtnColor
import com.bumperpick.bumperickUser.ui.theme.blueColor
import com.bumperpick.bumperickUser.ui.theme.satoshi_bold
import com.bumperpick.bumperickUser.ui.theme.satoshi_regular
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// UiState sealed class
sealed class ContestClick(){
    data class RegisterClick(val contest: DataXXXXXXXXXXXXXX) : ContestClick()
    data class ParticipateClick(val contest: DataXXXXXXXXXXXXXX) : ContestClick()

    data class WinnerClick(val contest: DataXXXXXXXXXXXXXX) : ContestClick()
}
@Composable
fun Contest(
    homeclick: (HomeClick) -> Unit,
    onContestClick: (ContestClick) -> Unit,
    viewModel: ContestViewmodel = koinViewModel(),
) {
    var searchContest by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }


    LaunchedEffect(Unit) {
        viewModel.fetchLocation()
        viewModel.fetchAllContests()
        viewModel.fetchRegisteredContests()
    }

    val locationDetails by viewModel.getLocation.collectAsState()
    val allContests by viewModel.allContests.collectAsState()
    val registeredContests by viewModel.registeredContests.collectAsState()
    Scaffold(containerColor = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top=it.calculateTopPadding())
                .background(Color(0xFFFAFAFA))
        )
        {
            LocationCard(
                locationDetails = locationDetails,
                onEventCity = {  homeclick(HomeClick.EventIncity)},
                onCartClick = { homeclick(HomeClick.CartClick) },
                onFavClick = { homeclick(HomeClick.FavClick) },
                onLocationClick = { homeclick(HomeClick.LocationClick) },
                onNotificationClick = { homeclick(HomeClick.NotifyClick) },
                content = {
                    Column {
                        // Search TextField
                        OutlinedTextField(
                            value = searchContest,
                            onValueChange = { searchContest = it },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Search",
                                    tint = Color.Gray
                                )
                            },
                            trailingIcon = {
                                if (searchContest.isNotEmpty()) {
                                    IconButton(
                                        onClick = { searchContest = "" }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                            },
                            placeholder = { Text(text = "Search for contests", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Black,
                                focusedBorderColor = BtnColor,
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White
                            )
                        )


                        // Tab Row
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = BtnColor,
                            contentColor = Color.White,
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                    color = Color.White
                                )
                            }
                        ) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = {
                                    Text(
                                        text = "Ongoing contests",
                                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = {
                                    Text(
                                        text = "My contests",
                                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))



            // Content based on selected tab

                    // Ongoing Contest Tab
                    when (allContests) {
                        is UiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = BtnColor)
                            }
                        }

                        is UiState.Success -> {
                            // Access the data property from Constest_Model
                            val contestsList =
                                (allContests as UiState.Success<Constest_Model>).data.data // Assuming Constest_Model has a data property
                            var filteredContests = if (searchContest.isEmpty()) {
                                contestsList
                            } else {
                                contestsList.filter { contest ->
                                    contest.title.contains(searchContest, ignoreCase = true)
                                }
                            }
                            filteredContests= if(selectedTab==0) filteredContests.filter { it.is_registered==0 } else filteredContests.filter { it.is_registered==1 }

                            Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                                Icon(painter = painterResource(R.drawable.left),
                                    tint = blueColor,
                                    contentDescription = null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(10.dp))

                                // Events count with improved styling
                                val ongoing_head_test = if (filteredContests.size == 0) "No ongoing contest" else if(filteredContests.size==1) "1 ongoing contest" else "${filteredContests.size} contests"
                                val my_head_test = if (filteredContests.size == 0) "No contest" else if(filteredContests.size==1) "1 contest" else "${filteredContests.size} contests"

                                Text(
                                    text = if (selectedTab == 0) ongoing_head_test else my_head_test,
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
                            if (filteredContests.isEmpty()) {
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
                                        text = if (searchContest.isEmpty()) "No ongoing contests available" else "No contests found for '$searchContest'",
                                        modifier = Modifier.padding(horizontal = 0.dp).fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = blueColor
                                    )
                                }
                            }
                            else {

                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {

                                    items(filteredContests) { contest ->
                                        ContestCard(
                                            contest = contest,
                                            onRegisterClick = {
                                              onContestClick(ContestClick.RegisterClick(it))
                                            },
                                            onParticipateClick = {
                                                onContestClick(ContestClick.ParticipateClick(it))
                                            },
                                            onResultClick = {
                                                onContestClick(ContestClick.WinnerClick(it))
                                            }
                                        )
                                    }
                                    item {
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }
                                }
                            }
                        }

                        is UiState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Error: ${(allContests as UiState.Error).message}",
                                        color = Color.Red,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { viewModel.fetchAllContests() },
                                        colors = ButtonDefaults.buttonColors(containerColor = BtnColor)
                                    ) {
                                        Text("Retry", color = Color.White)
                                    }
                                }
                            }
                        }

                        is UiState.Empty -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "No contests loaded",
                                        fontSize = 16.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { viewModel.fetchAllContests() },
                                        colors = ButtonDefaults.buttonColors(containerColor = BtnColor)
                                    ) {
                                        Text("Load contests", color = Color.White)
                                    }
                                }
                            }
                        }
                    }



            }
        }
    }

fun String.toFormattedDate(): String {
    return LocalDate.parse(this).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
}

@Composable
fun ContestCard(
    contest: DataXXXXXXXXXXXXXX,
    onRegisterClick: (DataXXXXXXXXXXXXXX) -> Unit,
    onParticipateClick: (DataXXXXXXXXXXXXXX) -> Unit,
    onResultClick: (DataXXXXXXXXXXXXXX) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Banner Image with Badge
            Box {
                AsyncImage(
                    model = contest.media?.firstOrNull()?.url ?: "",
                    contentDescription = "Contest Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                )
                share(modifier = Modifier.align(Alignment.TopEnd), message = "Check out this amazing contest", context = LocalContext.current)


            }

            // Contest Details
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = contest.title,
                        fontFamily = satoshi_bold,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )


                    if (contest.show_winner) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "View result",
                            modifier = Modifier.clickable {
                                onResultClick(contest)
                            },
                            fontSize = 16.sp,
                            fontFamily = satoshi_regular,
                            fontWeight = FontWeight.SemiBold,
                            color = BtnColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                InfoRow(
                    icon = painterResource(R.drawable.home),
                    text = contest.description ?: ""
                )

                InfoRow(
                    icon = painterResource(R.drawable.loading),
                    text = "Last date: ${(contest.end_date).toFormattedDate()}"
                )
                InfoRow(
                    icon = Icons.Outlined.Person,
                    text = "Maximum participants: ${
                        if(contest.is_unlimited==1) "unlimited" else 
                        contest.no_of_participants}",
                    vectorIcon = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Gray.copy(alpha = 0.15f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Action Button based on contest state
                if (contest.is_registered == 0) {
                    // User is NOT registered - Show Register button
                    OutlinedButton(
                        onClick = { onRegisterClick(contest) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        border = BorderStroke(1.dp, BtnColor),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Register for contest",
                            color = BtnColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else if (contest.is_registered == 1 && contest.is_participated == 0) {
                    // User is registered but NOT participated - Show Participate button
                    Button(
                        onClick = { onParticipateClick(contest) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BtnColor),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Participate now",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    // User has already participated - Show confirmation message
                    InfoRow(
                        icon = painterResource(R.drawable.check),
                        text = "You have participated in this contest",
                        iconTint = blueColor,
                        textColor = blueColor
                    )
                }
            }
        }
    }
}
