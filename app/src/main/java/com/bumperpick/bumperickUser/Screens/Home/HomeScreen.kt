package com.bumperpick.bumperickUser.Screens.Home

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import com.bumperpick.bumperickUser.Animation.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.bumperpick.bumperickUser.R
import com.bumperpick.bumperickUser.Screens.Component.BottomNavigationBar
import com.bumperpick.bumperickUser.Screens.Component.LocationPermissionScreen
import com.bumperpick.bumperickUser.Screens.Component.NavigationItem
import com.bumperpick.bumperickUser.Screens.Contest.Contest
import com.bumperpick.bumperickUser.Screens.Contest.ContestClick
import com.google.accompanist.systemuicontroller.rememberSystemUiController

import org.koin.androidx.compose.koinViewModel
enum class PermissionState {
    CHECKING,
    GRANTED,
    NOT_GRANTED,
    DENIED
}

@Composable
fun Homepage(
    onHomeClick: (HomeClick) -> Unit,
    open_subID: (sub_cat_id: String, sub_cat_name: String, cat_id: String) -> Unit,
    onAccountClick: (AccountClick) -> Unit,
    onEventClick: () -> Unit,
    onCampaignClick: () -> Unit,
    onContestClick: (ContestClick) -> Unit
) {
    val context = LocalContext.current
    var permissionState by remember { mutableStateOf(PermissionState.CHECKING) }
    var showPermissionScreen by remember { mutableStateOf(false) }
    val statusBarColor = Color(0xFFA40006)
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
    }
    // Check permission status on composition
    LaunchedEffect(Unit) {
        val locationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        permissionState = if (locationPermission == PackageManager.PERMISSION_GRANTED) {
            PermissionState.GRANTED
        } else {
            PermissionState.NOT_GRANTED
        }

        showPermissionScreen = permissionState == PermissionState.NOT_GRANTED
    }

    // Show home screen
    HomeScreen(
        viewmodel = koinViewModel(),
        onHomeClick,
        onEventClick,
        onContestClick,
        onCampaignClick,
        onAccountClick,
        open_subID
    )

    // Show permission screen only when needed
    if (showPermissionScreen && permissionState == PermissionState.NOT_GRANTED) {
        LocationPermissionScreen(
            onPermissionGranted = {
                permissionState = PermissionState.GRANTED
                showPermissionScreen = false
                println("Location permission granted")
            },
            onPermissionDenied = {
                permissionState = PermissionState.DENIED
                showPermissionScreen = false
                println("Location permission denied")
            }
        )
    }
}
class homeScreenViewmodel(): ViewModel(){
    var selectedTab by mutableStateOf(0)
    fun onTabSelected(index:Int){
        selectedTab=index
    }

}
@Composable
fun HomeScreen(
    viewmodel: homeScreenViewmodel,
    onHomeClick: (HomeClick)->Unit,
    onEventClick:()->Unit,
    onContestClick:(ContestClick)->Unit,
    onCampaignClick:()-> Unit,
    onAccountClick: (AccountClick) -> Unit, open_subID:(sub_cat_id:String,sub_cat_name:String,cat_id:String)->Unit,) {
   val selectedTab = viewmodel.selectedTab

    Column(
        modifier = Modifier
            .fillMaxSize()


            .background(Color.White)
    ) {



        val navItems = listOf(
            NavigationItem("Home", icon = Icons.Outlined.Home, contentDescription = "Home"),
            NavigationItem("Categories", icon = Icons.Default.List, contentDescription = "Create offers"),
            NavigationItem("Contest", painter = painterResource(R.drawable.contest), contentDescription = "Contest"),
            NavigationItem("More", painter = painterResource(R.drawable.more_horizontal_square_svgrepo_com), contentDescription = "Account")
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),

        ) {

                when(selectedTab){
                    0->{
                        Home(homeClick = onHomeClick, gotoEvent = {
                            onEventClick()
                        }, gotoCampaign = {onCampaignClick()},)
                    }
                    1->{
                        OfferScreen(homeClick = onHomeClick, openSubCategory = open_subID)
                    }
                    2->{
                        Contest(homeclick = onHomeClick,onContestClick = onContestClick)
                    }
                    3->{
                        AccountScreen(accountclick = onAccountClick)
                    }

                }


        }




        BottomNavigationBar(
            items = navItems,
            selectedTab = selectedTab,
            onTabSelected = {viewmodel.onTabSelected(it)}
        )

    }
}





