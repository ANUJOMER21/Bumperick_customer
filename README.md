# BumperPick Customer App

<div align="center">
  <img src="app/src/main/ic_launcher_bp-playstore.png" alt="BumperPick Logo" width="120" height="120"/>

  [![Documentation](https://img.shields.io/badge/docs-website-purple.svg)](https://anujomer21.github.io/Bumperick_customer/)
  <h3>🎯 A Modern Android Customer App for Offers, Events, Contests & Campaigns</h3>
  
  [![Android](https://img.shields.io/badge/Android-28%2B-green.svg)](https://developer.android.com)
  [![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg)](https://kotlinlang.org)
  [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2025.05.00-orange.svg)](https://developer.android.com/jetpack/compose)
  [![API Level](https://img.shields.io/badge/API%20Level-28--34-lightgrey.svg)](https://developer.android.com/guide/topics/manifest/uses-sdk-element)
</div>

## 📱 Overview

BumperPick is a comprehensive customer-facing Android application built with modern technologies that connects users with local businesses through offers, events, contests, and marketing campaigns. The app provides a seamless experience for discovering deals, participating in contests, attending events, and engaging with promotional campaigns.

## ✨ Key Features

### 🏠 **Home & Discovery**
- **Trending Offers**: Discover popular deals and promotions
- **Banner Slider**: Interactive promotional banners
- **Category-based Navigation**: Browse offers by business categories
- **Location-based Services**: Find nearby offers and events
- **Smart Filtering**: Filter offers by distance, category, and preferences

### 🎯 **Offers & Deals**
- **Offer Details**: Comprehensive offer information with images and descriptions
- **Favorites System**: Save favorite offers for later
- **Offer History**: Track previously viewed and used offers
- **Cart Functionality**: Add offers to cart for easy management
- **Share & Report**: Share offers with friends or report issues

### 🎪 **Events & Campaigns**
- **Event Discovery**: Browse upcoming events by city
- **Event Registration**: Register for events with personal details
- **Campaign Participation**: Join marketing campaigns
- **Event Details**: Detailed event information and requirements
- **Location-based Events**: Find events near your location

### 🏆 **Contests & Competitions**
- **Contest Listings**: View available contests and competitions
- **Contest Registration**: Register for contests
- **Submission System**: Submit contest entries with media
- **Winner Announcements**: View contest results and winners
- **Contest History**: Track your contest participation

### 👤 **User Management**
- **Phone Authentication**: OTP-based phone number verification
- **Google Sign-in**: Quick authentication with Google accounts
- **Profile Management**: Update personal information and profile picture
- **Location Services**: Automatic location detection and updates
- **Notification Management**: Push notifications for offers and events

### 🛠 **Support & Utilities**
- **FAQ Section**: Frequently asked questions and answers
- **Support Tickets**: Create and manage support tickets
- **Referral System**: Refer friends and earn rewards
- **Search Functionality**: Search for offers, events, and businesses
- **Deep Linking**: Support for app deep links and web integration

## 🏗 Architecture & Technology Stack

### **Core Technologies**
- **Language**: Kotlin 2.0.0
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Koin 3.5.3
- **Navigation**: Navigation Compose 2.8.6

### **Backend Integration**
- **API Client**: Retrofit 2.9.0 with Gson
- **Network**: OkHttp with interceptors
- **Base URL**: `http://65.0.78.200/`
- **Authentication**: JWT tokens with refresh mechanism

### **Data Management**
- **Local Storage**: DataStore Preferences 1.1.6
- **Image Loading**: Coil 2.5.0
- **State Management**: Compose State with ViewModels

### **Location & Maps**
- **Maps**: Google Maps SDK 18.2.0
- **Location**: Google Play Services Location 21.0.1
- **Places**: Google Places API 3.3.0
- **Maps Compose**: Maps Compose 4.3.3

### **Firebase Integration**
- **Messaging**: Firebase Cloud Messaging 25.0.0
- **Authentication**: Firebase Auth
- **Analytics**: Firebase Analytics (via BOM 34.0.0)

### **Media & Content**
- **Video Player**: ExoPlayer 1.5.0
- **QR Code**: ZXing 3.5.1
- **WebView**: Android WebKit 1.7.0

### **Security & Permissions**
- **Credentials**: AndroidX Credentials 1.3.0
- **Google ID**: Google Identity Services 1.1.1
- **Permissions**: Runtime permission handling

## 📁 Project Structure

```
app/src/main/java/com/bumperpick/bumperickUser/
├── Animation/                    # Custom animations and transitions
│   ├── AnimatedComponents.kt
│   ├── AnimationConstants.kt
│   ├── AnimationUtils.kt
│   └── ScreenTransitions.kt
├── API/                         # API models and services
│   ├── Model/                   # Legacy API models
│   ├── New_model/              # Current API models
│   └── Provider/               # API service interface
├── DI/                         # Dependency injection
│   ├── AppModule.kt           # Koin module configuration
│   └── BumperPickUserApp.kt   # Application class
├── Misc/                      # Utility classes
│   ├── DataStoreManager.kt    # Local data storage
│   ├── LocationHelper.kt      # Location services
│   └── MyFirebaseMessagingService.kt
├── Navigation/                # App navigation
│   ├── Navigation.kt         # Navigation setup
│   └── Screens.kt           # Screen definitions
├── Repository/               # Data repositories
│   ├── AuthRepository.kt
│   ├── OfferRepository.kt
│   ├── Event_campaign_Repository.kt
│   └── SupportRepository.kt
├── Screens/                  # UI screens
│   ├── Campaign/            # Campaign-related screens
│   ├── Component/           # Reusable UI components
│   ├── Contest/             # Contest screens
│   ├── Event/               # Event screens
│   ├── Home/                # Home screen components
│   ├── Login/               # Authentication screens
│   ├── OTP/                 # OTP verification
│   ├── Splash/              # Splash screen
│   └── Support/             # Support screens
└── ui/theme/                # UI theme and styling
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Arctic Fox or later
- **JDK**: 8 or higher
- **Android SDK**: API Level 28-34
- **Kotlin**: 2.0.0
- **Gradle**: 8.7.3

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Bumperick
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. **Configure API Keys**
   - Add your Google Maps API key in `AndroidManifest.xml`
   - Configure Firebase in `google-services.json`
   - Update API base URL if needed in `AppModule.kt`

4. **Sync Project**
   ```bash
   ./gradlew build
   ```

5. **Run the App**
   - Connect an Android device or start an emulator
   - Click "Run" in Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

### Build Configuration

The app is configured to build with the following settings:

- **Application ID**: `com.bumperpick.bumperickUser`
- **Min SDK**: 28 (Android 9.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 35
- **Version Code**: 1
- **Version Name**: 1.0

## 🔧 Configuration

### API Configuration

Update the base URL in `app/src/main/java/com/bumperpick/bumperickUser/DI/AppModule.kt`:

```kotlin
single {
    Retrofit.Builder()
        .baseUrl("http://65.0.78.200/") // Update this URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
```

### Google Maps Setup

1. Get a Google Maps API key from [Google Cloud Console](https://console.cloud.google.com)
2. Add the key to `AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_API_KEY_HERE" />
   ```

### Firebase Configuration

1. Create a Firebase project
2. Add your app to the project
3. Download `google-services.json`
4. Place it in the `app/` directory

## 📱 Screens & Navigation

### Main Navigation Flow

```
Splash → StartScreen → Login/OTP → HomePage
                                    ├── Home (Offers, Banners, Categories)
                                    ├── Categories (Offer Categories)
                                    ├── Contest (Contest Listings)
                                    └── More (Account, Settings, Support)
```

### Key Screens

- **Splash Screen**: App initialization and authentication check
- **Start Screen**: Welcome screen with login options
- **Login/OTP**: Phone number authentication
- **Home Page**: Main dashboard with offers and navigation
- **Offer Details**: Detailed offer information
- **Event Screen**: Event listings and registration
- **Contest Screen**: Contest participation
- **Profile**: User account management
- **Support**: Help and ticket system

## 🔐 Authentication

The app supports multiple authentication methods:

1. **Phone Number + OTP**
   - Send OTP to phone number
   - Verify OTP for authentication
   - Resend OTP functionality

2. **Google Sign-in**
   - One-tap Google authentication
   - Automatic profile creation
   - Seamless user experience

3. **Token Management**
   - JWT token storage
   - Automatic token refresh
   - Secure token handling

## 📍 Location Services

### Features
- **Automatic Location Detection**: Get user's current location
- **Location-based Offers**: Show offers near user's location
- **Address Resolution**: Convert coordinates to readable addresses
- **Permission Handling**: Graceful permission requests
- **Fallback Support**: Handle location service failures

### Implementation
- Uses Google Play Services Location API
- Implements both fine and coarse location permissions
- Provides geocoding for address resolution
- Handles location service availability

## 🔔 Notifications

### Push Notifications
- **Firebase Cloud Messaging**: Real-time push notifications
- **Topic Subscriptions**: Subscribe to relevant topics
- **Notification Channels**: Organized notification categories
- **Custom Notifications**: Rich notification content

### In-App Notifications
- **Notification Screen**: View all notifications
- **Notification Management**: Mark as read, delete notifications
- **Real-time Updates**: Live notification updates

## 🎨 UI/UX Design

### Design System
- **Material 3**: Modern Material Design components
- **Custom Theme**: Brand-specific color scheme
- **Typography**: Custom font family (Satoshi)
- **Animations**: Smooth transitions and micro-interactions

### Color Scheme
- **Primary**: #A40006 (Red)
- **Secondary**: #3B82F6 (Blue)
- **Background**: White
- **Surface**: Light gray variants

### Components
- **Custom Buttons**: Animated button components
- **Image Sliders**: Auto-playing banner sliders
- **Cards**: Material 3 card components
- **Bottom Navigation**: Custom navigation bar
- **Search**: Advanced search functionality

## 🧪 Testing

### Test Structure
```
app/src/
├── androidTest/          # Instrumented tests
└── test/                 # Unit tests
```

### Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

## 📦 Build Variants

### Debug
- **APK Name**: `BumperPick Customer-debug.apk`
- **Minification**: Disabled
- **Logging**: Enabled
- **Debugging**: Enabled

### Release
- **Minification**: Enabled (ProGuard)
- **Logging**: Disabled
- **Optimization**: Enabled

## 🚀 Deployment

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

### APK Location
- Debug: `app/build/outputs/apk/debug/`
- Release: `app/build/outputs/apk/release/`

## 🔧 Troubleshooting

### Common Issues

1. **Build Errors**
   - Ensure all dependencies are properly synced
   - Check API keys and configuration
   - Verify SDK versions

2. **Location Issues**
   - Check location permissions
   - Verify Google Play Services
   - Test on physical device

3. **Network Issues**
   - Verify API base URL
   - Check network security config
   - Ensure proper internet permissions

4. **Firebase Issues**
   - Verify `google-services.json`
   - Check Firebase project configuration
   - Ensure proper package name

### Debug Tips
- Enable logging in debug builds
- Use Android Studio's debugger
- Check Logcat for error messages
- Test on multiple devices and API levels

## 📚 API Documentation

### Key Endpoints

#### Authentication
- `POST /api/customer/send-otp` - Send OTP
- `POST /api/customer/verify-otp` - Verify OTP
- `POST /api/customer/auth-google` - Google authentication

#### Offers
- `POST /api/customer/offers` - Get offers
- `GET /api/customer/offers-details/{id}` - Offer details
- `GET /api/customer/offers-history` - Offer history

#### Events & Campaigns
- `GET /api/customer/events` - Get events
- `GET /api/customer/campaigns` - Get campaigns
- `POST /api/customer/campaign-registers/store` - Register for campaign

#### Contests
- `GET /api/customer/contests` - Get contests
- `GET /api/customer/contests/{id}` - Contest details
- `POST /api/customer/contest-registerations/create` - Register for contest

#### User Management
- `GET /api/customer/profile` - Get profile
- `POST /api/customer/profile/update` - Update profile
- `GET /api/customer/favourites/my` - Get favorites

## 🤝 Contributing

### Development Guidelines
1. Follow Kotlin coding conventions
2. Use meaningful variable and function names
3. Add proper documentation
4. Write unit tests for new features
5. Follow the existing architecture patterns

### Code Style
- Use 4 spaces for indentation
- Follow Material Design guidelines
- Implement proper error handling
- Use dependency injection (Koin)
- Follow MVVM architecture

## 📄 License

This project is proprietary software. All rights reserved.

## 🔄 Version History

### Version 1.0 (Current)
- Initial release
- Core features implementation
- Authentication system
- Offers and events functionality
- Contest participation
- Location-based services
- Push notifications
- Support system

---

<div align="center">
  <p><strong>Built with ❤️ using Kotlin & Jetpack Compose</strong></p>
  <p>© 2024 BumperPick. All rights reserved.</p>
</div>
