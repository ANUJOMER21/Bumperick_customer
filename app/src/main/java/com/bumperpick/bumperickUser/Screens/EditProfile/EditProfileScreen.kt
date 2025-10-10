package com.bumperpick.bumperickUser.Screens.EditProfile

import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.bumperpick.bumperickUser.API.New_model.profile_model
import com.bumperpick.bumperickUser.R
import com.bumperpick.bumperickUser.Screens.Home.AccountViewmodel
import com.bumperpick.bumperickUser.Screens.Home.UiState
import com.bumperpick.bumperickUser.ui.theme.BtnColor
import com.bumperpick.bumperickUser.ui.theme.grey
import com.bumperpick.bumperickUser.ui.theme.satoshi_regular
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun getFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "picked_image_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        file
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(
    onBackClick: () -> Unit,
    AccountViewmodel: AccountViewmodel = koinViewModel()
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
  //  var city by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var imageFile by remember { mutableStateOf<File?>(null) }
    var imageurl by remember { mutableStateOf("") }
    var enabled_mobile by remember { mutableStateOf(false) }

    // Validation states
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var cityError by remember { mutableStateOf<String?>(null) }
    var dobError by remember { mutableStateOf<String?>(null) }
    var showImagePickerError by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    val profileState by AccountViewmodel.profileState.collectAsState()
    val updateProfileState by AccountViewmodel.updateProfileState.collectAsState()

    // Handle update result
    LaunchedEffect(updateProfileState) {
        when (updateProfileState) {
            is UiState.Success -> {
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                onBackClick()
            }
            is UiState.Error -> {
                Toast.makeText(context, (updateProfileState as UiState.Error).message ?: "Update failed", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val file = getFileFromUri(context, it)
            if (file != null) {
                imageFile = file
                showImagePickerError = false
            } else {
                showImagePickerError = true
                Toast.makeText(context, "Failed to process selected image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) { AccountViewmodel.getProfile() }

    // Load profile data
    LaunchedEffect(profileState) {
        when (profileState) {
            is UiState.Success -> {
                val data = (profileState as UiState.Success<profile_model>).data.data
                name = data.name ?: ""
                email = data.email ?: ""
                mobile = data.phone_number ?: ""
                //city = data.city ?: ""
                dateOfBirth = data.dob?.let { dob ->
                    try {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        val date = inputFormat.parse(dob)
                        outputFormat.format(date)
                    } catch (e: Exception) {
                        ""
                    }
                } ?: ""
                enabled_mobile = mobile.isEmpty()
                imageurl = data.image_url ?: ""
            }
            is UiState.Error -> {
                Toast.makeText(context, "Failed to load profile", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    // --- Validation functions ---
    fun validateName(value: String): String? =
        when {
            value.isBlank() -> "Full name is required"
            value.length < 2 -> "Name must be at least 2 characters"
            else -> null
        }

    fun validateEmail(value: String): String? =
        when {
            value.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Please enter a valid email address"
            else -> null
        }

    fun validateCity(value: String): String? =
        if (value.isBlank()) "City is required" else null

    fun validateDateOfBirth(value: String): String? =
        when {
            value.isBlank() -> "Date of birth is required"
            else -> {
                try {
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    dateFormat.isLenient = false
                    val selectedDate = dateFormat.parse(value)
                    val currentDate = Date()
                    when {
                        selectedDate == null -> "Please enter a valid date"
                        selectedDate.after(currentDate) -> "Date of birth cannot be in the future"
                        else -> {
                            val age = Calendar.getInstance().apply {
                                time = selectedDate
                            }.get(Calendar.YEAR).let { selectedYear ->
                                Calendar.getInstance().get(Calendar.YEAR) - selectedYear
                            }
                            if (age > 120) "Please enter a valid date of birth" else null
                        }
                    }
                } catch (e: Exception) {
                    "Please enter date in DD-MM-YYYY format"
                }
            }
        }

    fun validateForm(): Boolean {
        nameError = validateName(name)
        emailError = validateEmail(email)

        dobError = validateDateOfBirth(dateOfBirth)
        return nameError == null && emailError == null  && dobError == null
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color(0xFFA40006), darkIcons = false)
    }

    // --- Date Picker ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(millis))
                        dateOfBirth = formattedDate
                        dobError = null
                    }
                    showDatePicker = false
                }) { Text("OK", color = BtnColor) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel", color = BtnColor) }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    selectedDayContainerColor = BtnColor,
                    todayDateBorderColor = BtnColor
                )
            )
        }
    }

    Scaffold(containerColor = grey) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Surface(color = Color.White, shadowElevation = 2.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onBackClick() }
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(
                            text = "Edit profile",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = satoshi_regular,
                            color = Color.Black
                        )
                    }
                }

                // Content Scroll
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(grey)
                        .padding(24.dp)
                ) {
                    // Profile Image
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier =  Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(
                                    width = 3.dp,
                                    color = if (showImagePickerError) Color.Red.copy(0.5f) else Color.Transparent,
                                    shape = CircleShape
                                )
                        ) {
                            AsyncImage(
                                model = imageFile?.toUri() ?: imageurl.ifEmpty { R.drawable.image_1 },
                                contentDescription = "Profile picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                error = painterResource(R.drawable.image_1),
                                placeholder = painterResource(R.drawable.image_1)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                                    .clickable { launcher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.pencil_svgrepo_com),
                                    contentDescription = "Change photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("Tap to change photo", fontSize = 14.sp, color = Color.Gray)
                    }

                    Spacer(Modifier.height(32.dp))

                    // --- Input Fields ---
                    Field(label = "Full name", value = name, error = nameError, icon = Icons.Outlined.Person) {
                        name = it; nameError = null
                    }
                    Field(label = "Date of birth", value = dateOfBirth, error = dobError, icon = Icons.Outlined.DateRange, isEnabled = false, onClick = {
                        showDatePicker = true
                    }) {

                    }
                    Field(label = "Mobile number", value = mobile, icon = Icons.Outlined.Phone, isEnabled = enabled_mobile) {
                        mobile = it
                    }
                    Field(label = "Email address", value = email, error = emailError, icon = Icons.Outlined.Email) {
                        email = it; emailError = null
                    }
                   /* Field(label = "City", value = city, error = cityError, icon = Icons.Outlined.Info) {
                        city = it; cityError = null
                    }*/

                    Spacer(Modifier.height(80.dp))
                }
            }

            // Bottom Button
            EnhancedButton(
                text = if (updateProfileState is UiState.Loading) "Updating..." else "Update profile",
                isLoading = updateProfileState is UiState.Loading,
                enabled = updateProfileState !is UiState.Loading,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                    if (validateForm()) {
                        AccountViewmodel.updateProfile(imageFile, name, email, mobile, dateOfBirth,"")
                    }
                }
            )
        }
    }
}

@Composable
private fun Field(
    label: String,
    value: String,
    error: String? = null,
    icon: ImageVector,
    isEnabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    Text(
        text = buildAnnotatedString {
            append(label)
            withStyle(SpanStyle(color = Color.Red)) { append(" *") }
        },
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = satoshi_regular,
        color = Color.Black
    )
    Spacer(Modifier.height(8.dp))
    Box(modifier = Modifier.clickable(enabled = onClick != null) { onClick?.invoke() }) {
        EnhancedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = "Enter $label",
            isError = error != null,
            leadingIcon = icon,
            isEnabled = isEnabled
        )
    }
    error?.let {
        Text(text = it, fontSize = 12.sp, color = Color.Red, modifier = Modifier.padding(top = 4.dp))
    }
    Spacer(Modifier.height(20.dp))
}

@Composable
fun EnhancedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontSize = 16.sp
            )
        },
        modifier = modifier.fillMaxWidth(),
        enabled = isEnabled,
        isError = isError,
        leadingIcon = leadingIcon?.let { icon ->
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isEnabled) {
                        if (isError) Color.Red else Color.Gray
                    } else Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isError) Color.Red else BtnColor,
            unfocusedBorderColor = if (isError) Color.Red else Color.Gray.copy(alpha = 0.3f),
            disabledBorderColor = Color.Gray.copy(alpha = 0.2f),
            disabledTextColor = Color.Gray,
            focusedTextColor = Color.Black,
            unfocusedContainerColor = Color(0xFFF5F5F5),
            focusedContainerColor = Color(0xFFF5F5F5),
            disabledContainerColor = Color(0xFFF5F5F5),
            cursorColor =  BtnColor,
            unfocusedTextColor = Color.Black


        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = satoshi_regular
        )
    )
}

@Composable
fun EnhancedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = BtnColor,
            disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontFamily = satoshi_regular
        )
    }
}