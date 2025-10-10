package com.bumperpick.bumperickUser.Screens.Contest

import ContestViewmodel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperickUser.API.New_model.Contest_reg
import com.bumperpick.bumperickUser.API.New_model.contest_details
import com.bumperpick.bumperickUser.API.New_model.my_status
import com.bumperpick.bumperickUser.R
import com.bumperpick.bumperickUser.Screens.Home.UiState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel


@Composable
fun winnerlist(
    contestId: String,
    contestName: String,
    onBackClick: () -> Unit,
    viewmodel: ContestViewmodel = koinViewModel()
) {
    val statusBarColor = Color(0xFFA40006)
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
    }

    val contest_details by viewmodel.contestDetails.collectAsState()
    LaunchedEffect(Unit) {
        viewmodel.fetchContestDetails(contestId)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAFAFA))
        ) {
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
                        .padding(bottom = 12.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

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
                                text = contestName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (contest_details) {
                UiState.Empty -> {}
                is UiState.Error -> {}
                UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFA40006))
                    }
                }
                is UiState.Success<contest_details> -> {
                    val mydata = (contest_details as UiState.Success<contest_details>).data.data.my_status
                    val registration = (contest_details as UiState.Success<contest_details>).data.data.contest_registrations

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            MyStatusCard(mydata)
                        }

                        item {
                            AllWinnersCard(registration)
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyStatusCard(data: my_status) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, Color(0xFFE57373)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Name: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000)
                )
                Text(
                    text = data.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Email: ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF9E9E9E)
                )
                Text(
                    text = data.email,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF9E9E9E)
                )
            }

            if (data.winner != 0) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Winner: ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.badge),
                        contentDescription = "Winner Badge",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Position: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000)
                )
                Text(
                    text = data.position.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000)
                )
            }
        }
    }
}

@Composable
fun AllWinnersCard(registrations: List<Contest_reg>) {
    val winnerCount = registrations.filter { it.winner != 0 }.size

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, Color(0xFFE57373)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "All winners",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            registrations.forEach { registration ->
                ContestRegistrationItem(registration)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ContestRegistrationItem(data: Contest_reg) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Name: ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000)
                    )
                    Text(
                        text = data.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Email: ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF9E9E9E)
                    )
                    Text(
                        text = data.email,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            PositionDisplay(data.position, data.winner != 0)
        }

        if (data != data) { // Check if not last item (you can adjust this logic)
            Divider(
                modifier = Modifier.padding(top = 16.dp),
                color = Color(0xFFEEEEEE),
                thickness = 1.dp
            )
        }
    }
}

@Composable
fun PositionDisplay(position: Int, isWinner: Boolean) {
    when (position) {
        1 -> {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.first_pos),
                    contentDescription = "1st Position",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        2 -> {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.second_pos),
                    contentDescription = "2nd Position",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        3 -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isWinner) {
                    Image(
                        painter = painterResource(id = R.drawable.badge),
                        contentDescription = "Winner Badge",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3F2FD)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = position.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                }
            }
        }
        else -> {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = position.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
            }
        }
    }
}