package com.bumperpick.bumperickUser.Screens.Contest

import ContestViewmodel
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.bumperpick.bumperickUser.API.New_model.contest_details
import com.bumperpick.bumperickUser.Screens.Home.UiState
import com.bumperpick.bumperickUser.ui.theme.BtnColor
import com.bumperpick.bumperickUser.ui.theme.blueColor
import com.bumperpick.bumperickUser.ui.theme.grey
import com.bumperpick.bumperickUser.ui.theme.satoshi_medium
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File
import android.content.Context
import androidx.compose.ui.text.AnnotatedString
import com.bumperpick.bumperickUser.Navigation.show_toast
import com.bumperpick.bumperickUser.R

import com.bumperpick.bumperickUser.Screens.Component.withRedAsterisk


// Replace this with your actual color and font references


fun Context.getFileName(uri: Uri): String {
    var name = "PDF File"
    try {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex != -1) {
                name = cursor.getString(nameIndex)
            }
        }
    } catch (e: Exception) {
        Log.e("FilePicker", "Error reading file name", e)
    }
    return name
}
fun Context.isPdf(uri: Uri): Boolean {
    // Try using MIME type first
    val mimeType = contentResolver.getType(uri)
    if (mimeType != null) {
        return mimeType.equals("application/pdf", ignoreCase = true)
    }

    // Fallback: check file extension from display name
    val cursor = contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            return name.endsWith(".pdf", true)
        }
    }
    return false
}

@Composable
fun SimpleFilePicker(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    onFileSelected: (Uri?) -> Unit = {}, // works for any file
    selectedFileUri: Uri? = null,
) {
    val context = LocalContext.current
    var selectedUri by remember { mutableStateOf<Uri?>(selectedFileUri) }

    // ---- FILE PICKER ----
    val fileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val size = context.contentResolver.openInputStream(it)?.available()?.toLong() ?: 0L
                if (size <= 3 * 1024 * 1024L) {
                    selectedUri = it
                    onFileSelected(it)
                } else {
                    Toast.makeText(context, "File size exceeds 3 MB", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error reading file", Toast.LENGTH_SHORT).show()
                Log.e("FilePicker", "Error reading file", e)
            }
        }
    }

    // ---- UI ----
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = satoshi_medium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(1.dp, BtnColor, RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .clickable { fileLauncher.launch("*/*") }
                .semantics { contentDescription = "Upload File" },
            contentAlignment = Alignment.Center
        ) {
            if (selectedUri == null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.vector),
                        contentDescription = "Upload Icon",
                        tint = Color.Gray
                    )
                    Text("Upload", fontSize = 18.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Maximum file size is 3 MB or paste your link below\nFormats: any (jpg, jpeg, png, pdf, mp4, etc.)",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            } else {
                val mimeType = context.contentResolver.getType(selectedUri!!) ?: ""
                val isImage = mimeType.startsWith("image/")
                val fileName = context.getFileName(selectedUri!!)

                if (isImage) {
                    AsyncImage(
                        model = selectedUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(125.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text(
                        text = fileName,
                        fontSize = 14.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    Icons.Outlined.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clickable {
                            selectedUri = null
                            onFileSelected(null)
                        }
                )
            }
        }
    }
}



@Composable
fun ContestSubmissionForm(
    contestName: String,
    contestId: String,
    onBackClick: () -> Unit,
    onSubmissionSuccess: () -> Unit = {},
    viewmodel: ContestViewmodel = koinViewModel()
) {
    // State variables
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var pastedLink by remember { mutableStateOf("") }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var imageError by remember { mutableStateOf<String?>(null) }
    var linkError by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var ContestDetail by remember { mutableStateOf<contest_details?>(null) }
    val context=LocalContext.current
    LaunchedEffect(Unit) {
        ContestDetail= viewmodel.fetchContestDetail(contestId)
    }

    val statusBarColor = Color(0xFFA40006)
    val systemUiController = rememberSystemUiController()

    // Change status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
    }

    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val contestSubmission by viewmodel.contestSubmission.collectAsState()

    LaunchedEffect(contestSubmission) {
        when (contestSubmission) {
            is UiState.Success -> {
                isSubmitting = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Your entry submitted successfully")
                }
                onSubmissionSuccess()
            }
            is UiState.Error -> {
                isSubmitting = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar((contestSubmission as UiState.Error).message)
                }
            }
            is UiState.Loading -> {
                isSubmitting = true
            }
            else -> {
                isSubmitting = false
            }
        }
    }

    // Clear errors when user starts typing/selecting
    LaunchedEffect(description) {
        if (descriptionError != null) descriptionError = null
    }

    // Clear URL and error when image is selected
    LaunchedEffect(selectedImageUri) {
        if (imageError != null) imageError = null
        if (linkError != null) linkError = null
        if (selectedImageUri != null) {
            pastedLink = "" // Clear the URL when image is selected
        }
    }

    // Clear image selection and error when URL is entered
    LaunchedEffect(pastedLink) {
        if (linkError != null) linkError = null
        if (imageError != null) imageError = null
        if (pastedLink.isNotBlank()) {
            selectedImageUri = null // Clear the image when URL is entered
        }
    }

    // Form validation
    fun validateForm(dont_check_uri: Boolean = false): Boolean {
        var isValid = true

        // Clear previous errors
        descriptionError = null
        imageError = null
        linkError = null

        // Image or Link validation (at least one required)
        if (!dont_check_uri) {
            if (selectedImageUri == null && pastedLink.isBlank()) {
                imageError = "Please provide either an image or a link"
                linkError = "Please provide either an image or a link"
                isValid = false
            }
        }

        // Description validation
        when {
            description.isBlank() -> {
                descriptionError = "Description is required"
                isValid = false
            }
            description.length < 10 -> {
                descriptionError = "Description must be at least 10 characters"
                isValid = false
            }
            description.length > 500 -> {
                descriptionError = "Description must be less than 500 characters"
                isValid = false
            }
        }

        return isValid
    }

    // Handle form submission
    fun handleSubmit() {
        Log.d("Contest Detail", "handleSubmit: $ContestDetail")
        if(ContestDetail==null) show_toast("Failed to submit contest entry",context = context)
        ContestDetail?.let {
            if (it.data.contest.is_required_file == 1) {
                if (validateForm() && !isSubmitting) {
                    // Submit with image if available, otherwise with link
                    if (selectedImageUri != null) {
                        viewmodel.submitContestEntry(contestId, description, selectedImageUri)
                    } else {
                        viewmodel.submitContestEntry(contestId, description, null, pastedLink)
                    }
                }
            }
            else{
                if (validateForm(true) && !isSubmitting) {
                    viewmodel.submitContestEntry(contestId, description, null, pastedLink)
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 20.dp,
                                bottom = 18.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { onBackClick() }
                                .padding(4.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = contestName,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Contest submission",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (ContestDetail?.data?.contest?.is_participate == 0) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = grey
                        ),
                    )
                    {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "You have submitted your contest entry.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = blueColor
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .background(grey)
                            .padding(24.dp)
                    )
                    {
                        // Form Instructions
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        )
                        {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = null,
                                    tint = blueColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Please provide relevant details for the entry submission.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = blueColor
                                )
                            }
                        }

                        // Form Fields
                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        )
                        {
                            if (ContestDetail?.data?.contest?.is_required_file == 1) {
                                Column {
                                    // Image Picker - Disabled when URL is entered
                                    Box(
                                        modifier = Modifier.alpha(if (pastedLink.isNotBlank()) 0.5f else 1f)
                                    ) {
                                        val htmlText = "Upload your entry".withRedAsterisk()
                                        SimpleFilePicker(
                                            text = htmlText,
                                            onFileSelected = { uri ->
                                                if (pastedLink.isBlank()) {
                                                    selectedImageUri = uri
                                                }
                                            },
                                            selectedFileUri = selectedImageUri,
                                            //enabled = pastedLink.isBlank() // Add this parameter to your SimpleImagePicker if it supports it
                                        )
                                    }

                                    // Error message for image
                                    imageError?.let { error ->
                                        Text(
                                            text = error,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                        )
                                    }

                                    // OR Divider
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        HorizontalDivider(
                                            modifier = Modifier.weight(1f),
                                            thickness = 1.dp,
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                        )
                                        Text(
                                            text = "OR",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                        HorizontalDivider(
                                            modifier = Modifier.weight(1f),
                                            thickness = 1.dp,
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                        )
                                    }

                                    if (selectedImageUri == null){
                                    Column {
                                        Text(
                                            text = "Paste your link ".withRedAsterisk(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            color = if (selectedImageUri != null)
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                            else
                                                MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )

                                        OutlinedTextField(
                                            value = pastedLink,
                                            onValueChange = {
                                                if (selectedImageUri == null) {
                                                    pastedLink = it
                                                }
                                            },
                                            placeholder = {
                                                Text(
                                                    text = "https://example.com/your-entry",
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            textStyle = MaterialTheme.typography.bodyMedium,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = BtnColor,
                                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(
                                                    alpha = 0.5f
                                                ),
                                                errorBorderColor = MaterialTheme.colorScheme.error,
                                                focusedLabelColor = BtnColor,
                                                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(
                                                    alpha = 0.3f
                                                ),
                                                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(
                                                    alpha = 0.38f
                                                )
                                            ),
                                            isError = linkError != null,
                                            enabled = selectedImageUri == null, // Disable when image is selected
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Uri,
                                                imeAction = ImeAction.Next
                                            )
                                        )

                                        // Error message for link
                                        linkError?.let { error ->
                                            Text(
                                                text = error,
                                                color = MaterialTheme.colorScheme.error,
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                            )
                                        }
                                    }
                                        }
                                }
                            }

                            // Description Field
                            Column {
                                Text(
                                    text = "Description ".withRedAsterisk(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = description,
                                    onValueChange = {
                                        if (it.length <= 500) {
                                            description = it
                                        }
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Describe your contest entry",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .padding(0.dp),
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BtnColor,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(
                                            alpha = 0.5f
                                        ),
                                        errorBorderColor = MaterialTheme.colorScheme.error,
                                        focusedLabelColor = BtnColor
                                    ),
                                    isError = descriptionError != null,
                                    maxLines = 5,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Default
                                    )
                                )

                                // Character count
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Error message
                                    descriptionError?.let { error ->
                                        Text(
                                            text = error,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    } ?: Spacer(modifier = Modifier.weight(1f))

                                    // Character count
                                    Text(
                                        text = "${description.length}/500",
                                        color = if (description.length > 450)
                                            MaterialTheme.colorScheme.error
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        // Additional spacing before submit button area
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                color = Color.White,
                tonalElevation = 8.dp
            )
            {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Button(
                        onClick = { handleSubmit() },
                        enabled = !isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BtnColor,
                            disabledContainerColor = BtnColor.copy(alpha = 0.6f)
                        )
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submitting...")
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.send_svgrepo_com),
                                tint = Color.White,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Submit entry",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}