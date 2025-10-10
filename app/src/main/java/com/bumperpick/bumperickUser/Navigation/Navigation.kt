package com.bumperpick.bumperickUser.Navigation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.bumperpick.bumperickUser.Animation.*
import com.bumperpick.bumperickUser.Screens.Campaign.EventForm
import com.bumperpick.bumperickUser.Screens.Campaign.EventScreen
import com.bumperpick.bumperickUser.Screens.Component.YouTubeLiveVideoPlayer
import com.bumperpick.bumperickUser.Screens.Contest.ContestClick
import com.bumperpick.bumperickUser.Screens.Contest.ContestSubmissionForm
import com.bumperpick.bumperickUser.Screens.Contest.contestForm
import com.bumperpick.bumperickUser.Screens.Contest.winnerlist
import com.bumperpick.bumperickUser.Screens.EditProfile.EditProfile
import com.bumperpick.bumperickUser.Screens.Event.EventCityScreenMain
import com.bumperpick.bumperickUser.Screens.Event.EventDetailScreen
import com.bumperpick.bumperickUser.Screens.Event.EventScreenMain
import com.bumperpick.bumperickUser.Screens.Faq.faq
import com.bumperpick.bumperickUser.Screens.Home.AccountClick
import com.bumperpick.bumperickUser.Screens.Home.Cart
import com.bumperpick.bumperickUser.Screens.Home.HomeClick
import com.bumperpick.bumperickUser.Screens.Home.Homepage
import com.bumperpick.bumperickUser.Screens.Home.Map.ChooseLocation
import com.bumperpick.bumperickUser.Screens.Home.OfferDetails
import com.bumperpick.bumperickUser.Screens.Home.OfferSearchScreen
import com.bumperpick.bumperickUser.Screens.Home.SubCategoryPage
import com.bumperpick.bumperickUser.Screens.Home.offer_subcat
import com.bumperpick.bumperickUser.Screens.Login.Login
import com.bumperpick.bumperickUser.Screens.OTP.OtpScreen
import com.bumperpick.bumperickUser.Screens.Splash.Splash
import com.bumperpick.bumperickUser.Screens.StartScreen.StartScreen
import com.bumperpick.bumperickUser.Screens.Support.SupportTicketsScreen
import com.bumperpick.bumperickUser.Screens.Support.TicketDetailsScreen
import com.bumperpick.bumperickUser.Screens.favourite.FavouriteScreen
import com.bumperpick.bumperpick_Vendor.Screens.NotificationScreen.NotificationScreen
import com.bumperpick.bumperpickvendor.Screens.OfferhistoryScreen.offerhistoryScreen

// Base URL for deep links
const val DEEP_LINK_BASE_URL = "bumperpick://"

fun show_toast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        // Splash
        composable(
            route = Screen.Splash.route,
            enterTransition = { fadeInTransition() },
            exitTransition = { fadeOutTransition() }
        ) {
            Splash(gotoScreen = {
                navController.navigate(it.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }

        // Start
        composable(
            route = Screen.StartScreen.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}start" }
            ),
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() }
        ) {
            StartScreen(gotoLogin = {
                navController.navigate(Screen.Login.route) {
                    launchSingleTop = true
                }
            }
            )
        }

        // Login
        composable(
            route = Screen.Login.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}login" }
            ),
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() }
        ) {
            Login(onLoginSuccess = { mobile, isMobile ->
                if (isMobile) {
                    navController.navigate(Screen.Otp.withMobile(mobile)) {
                        launchSingleTop = true
                    }
                } else {
                    navController.navigate(Screen.HomePage.route) {
                        popUpTo(Screen.StartScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            })
        }

        // OTP
        composable(
            route = Screen.Otp.route,
            arguments = listOf(navArgument(Screen.MOBILE_KEY) {
                type = NavType.StringType
            }),
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}otp/{${Screen.MOBILE_KEY}}" }
            ),
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() }
        ) { backStackEntry ->
            val mobile = backStackEntry.arguments?.getString(Screen.MOBILE_KEY) ?: ""

            OtpScreen(
                mobile = mobile,
                onBackClick = {
                    navController.popBackStack()
                },
                onOtpVerify = {
                    navController.navigate(Screen.HomePage.route) {
                        popUpTo(Screen.StartScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Offer Subcategory
        composable(
            route = Screen.Offer_subcat.route,
            arguments = listOf(
                navArgument(Screen.SUB_CAT_ID) { type = NavType.StringType },
                navArgument(Screen.SUB_CAT_NAME) { type = NavType.StringType },
                navArgument(Screen.CAT_ID) { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DEEP_LINK_BASE_URL}category/{${Screen.CAT_ID}}/subcategory/{${Screen.SUB_CAT_ID}}/{${Screen.SUB_CAT_NAME}}"
                }
            )
        ) { backStackEntry ->
            val sub_catid = backStackEntry.arguments?.getString(Screen.SUB_CAT_ID) ?: ""
            val catid = backStackEntry.arguments?.getString(Screen.CAT_ID) ?: ""
            val sub_catname = backStackEntry.arguments?.getString(Screen.SUB_CAT_NAME) ?: ""

            offer_subcat(
                subcatId = sub_catid,
                subcatName = sub_catname,
                cat_id = catid,
                onBackClick = { navController.popBackStack() },
                homeclick = {
                    when (it) {
                        HomeClick.CartClick -> {}
                        HomeClick.LocationClick -> {}
                        is HomeClick.OfferClick -> {
                            navController.navigate(Screen.OfferDetail.withOfferId(it.offerId))
                        }
                        HomeClick.SearchClick -> {}
                        is HomeClick.CategoryClick -> {}
                        HomeClick.FavClick -> {}
                        HomeClick.NotifyClick -> {
                            navController.navigate(Screen.Notification.route)
                        }

                        is HomeClick.EventClick -> {
                            navController.navigate(Screen.EventScreenDetail.withid(it.id))
                        }
                        HomeClick.EventIncity -> {
                            navController.navigate(Screen.EventCityScreen.route)
                        }
                    }
                }
            )
        }

        // HomePage
        composable(
            route = Screen.HomePage.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}home" },
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}" }
            ),
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() }
        ) {
            Homepage(
                onHomeClick = {
                    when (it) {
                        HomeClick.CartClick -> {
                            navController.navigate(Screen.Cart.route)
                        }
                        is HomeClick.OfferClick -> {
                            navController.navigate(Screen.OfferDetail.withOfferId(it.offerId))
                        }
                        HomeClick.LocationClick -> {
                            navController.navigate(Screen.Location.route)
                        }
                        HomeClick.SearchClick -> {
                            navController.navigate(Screen.Search.route)
                        }
                        is HomeClick.CategoryClick -> {
                            val category = it.cat
                            val categoryIdInt = category.id.toString()
                            navController.navigate(Screen.SubCatPage.witcatId(categoryIdInt, category.name ?: ""))
                        }
                        HomeClick.FavClick -> {
                            navController.navigate(Screen.FavouriteScreen.route)
                        }
                        HomeClick.NotifyClick -> {
                            navController.navigate(Screen.Notification.route)
                        }
                        is HomeClick.EventClick -> {
                            navController.navigate(Screen.EventScreenDetail.withid(it.id))
                        }
                        HomeClick.EventIncity -> {
                            navController.navigate(Screen.EventCityScreen.route)
                        }
                    }
                },
                open_subID = { sub_cat_id, sub_cat_name, cat_id ->
                    navController.navigate(Screen.Offer_subcat.withsubcatId(sub_cat_id, sub_cat_name, cat_id))
                },
                onEventClick = {
                    navController.navigate(Screen.EventScreen2.route)
                },
                onCampaignClick = {
                    navController.navigate(Screen.EventScreen.route)
                },
                onAccountClick = {
                    when (it) {
                        AccountClick.Logout -> {
                            navController.navigate(Screen.Splash.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        AccountClick.EditAccount -> {
                            navController.navigate(Screen.EditProfile.route)
                        }
                        AccountClick.OfferHistory -> {
                            navController.navigate(Screen.OfferHistoryScreen.route)
                        }
                        AccountClick.EventClick -> {
                            navController.navigate(Screen.EventScreen2.route)
                        }
                        AccountClick.CampaignClick -> {
                            navController.navigate(Screen.EventScreen.route)
                        }
                        AccountClick.EventCityClick -> navController.navigate(Screen.EventCityScreen.route)
                        AccountClick.FavClick -> {
                            navController.navigate(Screen.FavouriteScreen.route)
                        }
                        AccountClick.FaqClick -> {
                            navController.navigate(Screen.Faq.route)
                        }
                        AccountClick.mailToAdmin -> {
                            navController.navigate(Screen.emailadmin.route)
                        }
                    }
                },
                onContestClick = {
                    when (it) {
                        is ContestClick.ParticipateClick -> {
                            navController.navigate(
                                Screen.ContestSubmission.withid(it.contest.title, it.contest.id.toString())
                            )
                        }
                        is ContestClick.RegisterClick -> {
                            navController.navigate(
                                Screen.ContestForm.withid(it.contest.title, it.contest.id.toString())
                            )
                        }

                        is ContestClick.WinnerClick -> navController.navigate(Screen.ContestResult.withid(it.contest.title, it.contest.id.toString()))
                    }
                }
            )
        }

        // Edit Profile
        composable(
            route = Screen.EditProfile.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}profile/edit" }
            )
        ) {
            EditProfile(onBackClick = {
                navController.popBackStack()
            })
        }

        // Location
        composable(
            route = Screen.Location.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}location" }
            )
        ) {
            ChooseLocation(onBackClick = {
                navController.popBackStack()
            })
        }

        // Cart
        composable(
            route = Screen.Cart.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}cart" }
            ),
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() }
        ) {
            Cart {
                navController.popBackStack()
            }
        }

        // Offer Detail - Fixed to make is_offer_or_history optional with default value
        composable(
            route = Screen.OfferDetail.route,
            arguments = listOf(
                navArgument(Screen.OFFER_ID) { type = NavType.StringType },
                navArgument(Screen.is_offer_or_history) {
                    type = NavType.BoolType
                    defaultValue = false // Add default value to make it optional
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DEEP_LINK_BASE_URL}offer/{${Screen.OFFER_ID}}/{${Screen.is_offer_or_history}}"
                },
                navDeepLink {
                    uriPattern = "${DEEP_LINK_BASE_URL}offer/{${Screen.OFFER_ID}}"
                }
            )
        ) { navBackStackEntry ->
            val offerId = navBackStackEntry.arguments?.getString(Screen.OFFER_ID) ?: ""
            val is_offer_or_history = navBackStackEntry.arguments?.getBoolean(Screen.is_offer_or_history)

            OfferDetails(
                offerId,
                onBackClick = { navController.popBackStack() },
                is_offer_or_history ?: false,
                gotocart = {
                    navController.navigate(Screen.Cart.route)
                }
            )
        }

        // Search
        composable(
            route = Screen.Search.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}search" }
            ),
            enterTransition = { slideInFromBottom() },
            exitTransition = { slideOutToBottom() }
        ) {
            OfferSearchScreen(
                onBackClick = { navController.popBackStack() },
                homeClick = {
                    when (it) {
                        HomeClick.CartClick -> {}
                        HomeClick.LocationClick -> {}
                        is HomeClick.OfferClick -> {
                            navController.navigate(Screen.OfferDetail.withOfferId(it.offerId))
                        }
                        HomeClick.SearchClick -> {}
                        is HomeClick.CategoryClick -> {}
                        HomeClick.FavClick -> {}
                        HomeClick.NotifyClick -> {
                            navController.navigate(Screen.Notification.route)
                        }
                        is HomeClick.EventClick -> {
                            navController.navigate(Screen.EventScreenDetail.withid(it.id))
                        }
                        HomeClick.EventIncity -> {
                            navController.navigate(Screen.EventCityScreen.route)
                        }
                    }

                }
            )
        }

        // Subcategory Page
        composable(
            route = Screen.SubCatPage.route,
            arguments = listOf(
                navArgument(Screen.CAT_ID) { type = NavType.StringType },
                navArgument(Screen.CAT_NAME) { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DEEP_LINK_BASE_URL}category/{${Screen.CAT_ID}}/{${Screen.CAT_NAME}}"
                }
            )
        ) { navBackStackEntry ->
            val catid = navBackStackEntry.arguments?.getString(Screen.CAT_ID) ?: ""
            val catname = navBackStackEntry.arguments?.getString(Screen.CAT_NAME) ?: ""

            SubCategoryPage(
                cat_id = catid.toInt(),
                selectedCategoryName = catname,
                onBackClick = {
                    navController.popBackStack()
                },
                open_subID = { sub_cat_id, sub_cat_name, cat_id ->
                    navController.navigate(
                        Screen.Offer_subcat.withsubcatId(sub_cat_id, sub_cat_name, cat_id)
                    )
                }
            )
        }

        // Campaign Events
        composable(
            route = Screen.EventScreen.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}campaigns" }
            )
        ) {
            EventScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                gotoEventRegister = {
                    navController.navigate(Screen.EventForm.withid(eventId = it.id.toString(), eventName = it.title))
                },
                onFavClick = {
                    navController.navigate(Screen.FavouriteScreen.route)
                },
                onNotificationClick = {
                    navController.navigate(Screen.Notification.route)
                }
            )
        }

        // Campaign Event Form - Fixed to include both parameters in deep link
        composable(
            route = Screen.EventForm.route,
            arguments = listOf(
                navArgument(Screen.EVENT_NAME) { type = NavType.StringType },
                navArgument(Screen.EVENT_ID) { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DEEP_LINK_BASE_URL}campaign/{${Screen.EVENT_ID}}/{${Screen.EVENT_NAME}}/register"
                }
            )
        ) { navBackStackEntry ->
            val eventname = navBackStackEntry.arguments?.getString(Screen.EVENT_NAME) ?: ""
            val eventId = navBackStackEntry.arguments?.getString(Screen.EVENT_ID) ?: ""

            EventForm(
                eventName = eventname,
                eventId = eventId,
                onBackClick = {
                    navController.popBackStack()
                },
                onRegistrationSuccess = {
                    show_toast("Registered Successfully", context)
                    navController.popBackStack()
                }
            )
        }

        // Contest Form - Fixed to include both parameters in deep link
        composable(
            route = Screen.ContestForm.route,
            arguments = listOf(
                navArgument(Screen.EVENT_NAME) { type = NavType.StringType },
                navArgument(Screen.EVENT_ID) { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DEEP_LINK_BASE_URL}contest/{${Screen.EVENT_ID}}/{${Screen.EVENT_NAME}}/register"
                }
            )
        ) { navBackStackEntry ->
            val eventname = navBackStackEntry.arguments?.getString(Screen.EVENT_NAME) ?: ""
            val eventId = navBackStackEntry.arguments?.getString(Screen.EVENT_ID) ?: ""

            contestForm(
                contestName = eventname,
                contestId = eventId,
                onBackClick = {
                    navController.popBackStack()
                },
                onRegistrationSuccess = {
                    show_toast("Registered Successfully", context)
                    navController.popBackStack()
                }
            )
        }

        // Contest Submission - Fixed to include both parameters in deep link
        composable(
            route = Screen.ContestSubmission.route,
            arguments = listOf(
                navArgument(Screen.EVENT_NAME) { type = NavType.StringType },
                navArgument(Screen.EVENT_ID) { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DEEP_LINK_BASE_URL}contest/{${Screen.EVENT_ID}}/{${Screen.EVENT_NAME}}/participate"
                }
            )
        ) { navBackStackEntry ->
            val eventname = navBackStackEntry.arguments?.getString(Screen.EVENT_NAME) ?: ""
            val eventId = navBackStackEntry.arguments?.getString(Screen.EVENT_ID) ?: ""

            ContestSubmissionForm(
                contestName = eventname,
                contestId = eventId,
                onBackClick = {
                    navController.popBackStack()
                },
                onSubmissionSuccess = {
                    show_toast("Submission Successfully", context)
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.ContestResult.route,
            arguments = listOf(
                navArgument(Screen.EVENT_NAME) { type = NavType.StringType },
                navArgument(Screen.EVENT_ID) { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DEEP_LINK_BASE_URL}contest_result/{${Screen.EVENT_ID}}/{${Screen.EVENT_NAME}}/participate"
                }
            )
        ) { navBackStackEntry ->
            val eventname = navBackStackEntry.arguments?.getString(Screen.EVENT_NAME) ?: ""
            val eventId = navBackStackEntry.arguments?.getString(Screen.EVENT_ID) ?: ""

            winnerlist(
                contestName = eventname,
                contestId = eventId,
                onBackClick = {
                    navController.popBackStack()
                },

            )
        }

        // Offer History
        composable(
            route = Screen.OfferHistoryScreen.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}offers/history" }
            )
        ) {
            offerhistoryScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                openOfferDetail = {
                    navController.navigate(Screen.OfferDetail.withOfferId(it, true))
                }
            )
        }

        // Events Main Screen
        composable(
            route = Screen.EventScreen2.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}events" }
            )
        ) {
            EventScreenMain(
                onBackClick = {
                    navController.popBackStack()
                },
                onFavClick = {
                    navController.navigate(Screen.FavouriteScreen.route)
                },
                gotoEventDetail = {
                    navController.navigate(Screen.EventScreenDetail.withid(it))
                },
                onNotificationClick = {
                    navController.navigate(Screen.Notification.route)
                }
            )
        }
        composable(
            route = Screen.EventCityScreen.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}events" }
            )
        ) {
            EventCityScreenMain(
                onBackClick = {
                    navController.popBackStack()
                },
                onFavClick = {
                    navController.navigate(Screen.FavouriteScreen.route)
                },
                gotoEventDetail = {
                    navController.navigate(Screen.EventScreenDetail.withid(it))
                },
                onNotificationClick = {
                    navController.navigate(Screen.Notification.route)
                }
            )
        }

        // YouTube Video Player
        composable(
            route = Screen.YoutubeView.route,
            arguments = listOf(
                navArgument(Screen.Url) { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DEEP_LINK_BASE_URL}video?url={${Screen.Url}}"
                }
            )
        ) { navBackStackEntry ->
            val url = navBackStackEntry.arguments?.getString(Screen.Url) ?: ""
            YouTubeLiveVideoPlayer(url)
        }

        // Event Detail Screen
        composable(
            route = Screen.EventScreenDetail.route,
            arguments = listOf(
                navArgument(Screen.EVENT_ID) { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DEEP_LINK_BASE_URL}event/{${Screen.EVENT_ID}}"
                }
            )
        ) { navBackStackEntry ->
            val eventId = navBackStackEntry.arguments?.getString(Screen.EVENT_ID) ?: ""

            EventDetailScreen(
                onBackClick = { navController.popBackStack() },
                eventId = eventId.toInt(),
                onOpenWebView = {
                    Log.d("navUrl", it)
                    navController.navigate(Screen.YoutubeView.withurl(it))
                }
            )
        }

        // FAQ
        composable(
            route = Screen.Faq.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}faq" }
            )
        ) {
            faq(onBackClick = { navController.popBackStack() })
        }

        // Favourites
        composable(
            route = Screen.FavouriteScreen.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}favourites" }
            )
        ) {
            FavouriteScreen(
                onBackClick = { navController.popBackStack() },
                homeClick = {
                    when (it) {
                        HomeClick.CartClick -> {}
                        HomeClick.LocationClick -> {}
                        is HomeClick.OfferClick -> {
                            navController.navigate(Screen.OfferDetail.withOfferId(it.offerId))
                        }
                        HomeClick.SearchClick -> {}
                        is HomeClick.CategoryClick -> {}
                        HomeClick.FavClick -> {}
                        HomeClick.NotifyClick -> {
                            navController.navigate(Screen.Notification.route)
                        }
                        is HomeClick.EventClick -> {
                            navController.navigate(Screen.EventScreenDetail.withid(it.id))
                        }
                        HomeClick.EventIncity -> {
                            navController.navigate(Screen.EventCityScreen.route)
                        }
                    }
                }
            )
        }

        // Support/Email Admin
        composable(
            route = Screen.emailadmin.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}support" }
            )
        ) {
            SupportTicketsScreen(
                onBackPressed = { navController.popBackStack() },
                gototicketdetail = { id ->
                    navController.navigate(Screen.ticketdetail.withid(id))
                }
            )
        }

        // Ticket Details
        composable(
            route = Screen.ticketdetail.route,
            arguments = listOf(
                navArgument(Screen.TICKET_ID) { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DEEP_LINK_BASE_URL}support/ticket/{${Screen.TICKET_ID}}"
                }
            )
        ) { navBackStackEntry ->
            val ticketId = navBackStackEntry.arguments?.getString(Screen.TICKET_ID) ?: ""

            TicketDetailsScreen(
                ticketId = ticketId,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        // Notifications
        composable(
            route = Screen.Notification.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "${DEEP_LINK_BASE_URL}notifications" }
            )
        ) {
            NotificationScreen(onBackClick = {
                navController.popBackStack()
            })
        }
    }
}