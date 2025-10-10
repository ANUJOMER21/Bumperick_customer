package com.bumperpick.bumperickUser.Repository

import DataStoreManager
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import com.bumperpick.bumperickUser.API.New_model.Constest_Model
import com.bumperpick.bumperickUser.API.New_model.contest_details
import com.bumperpick.bumperickUser.API.New_model.contestregistered
import com.bumperpick.bumperickUser.API.New_model.error_model
import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.API.Provider.ApiResult
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.API.Provider.safeApiCall
import com.bumperpick.bumperpickvendor.API.Provider.toMultipartPart
import com.google.gson.Gson

import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

class ContestRepositoryImpl(val dataStoreManager: DataStoreManager,val apiService: ApiService,val context: Context):
    Contest_Repository {
    override suspend fun getAllContest(): Result<Constest_Model> {
        val token = dataStoreManager.getToken.firstOrNull() ?: return Result.Error("Token not found")

        val response = safeApiCall(
            context = context,
            api = { apiService.contest(token) },
            errorBodyParser = {
                try {
                    Gson().fromJson(it, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $it")
                }
            }
        )

        return when (response) {
            is ApiResult.Success -> {
                if (response.data.code == 200) Result.Success(response.data)
                else Result.Error(response.data.message)
            }
            is ApiResult.Error -> Result.Error(response.error.message)
        }
    }

    override suspend fun ContestDetails(id: String): Result<contest_details> {
        val token = dataStoreManager.getToken.firstOrNull() ?: return Result.Error("Token not found")

        val response = safeApiCall(
            context = context,
            api = { apiService.contest_details(id, token) },
            errorBodyParser = {
                try {
                    Gson().fromJson(it, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $it")
                }
            }
        )

        return when (response) {
            is ApiResult.Success -> {
                if (response.data.code == 200) Result.Success(response.data)
                else Result.Error(response.data.message)
            }
            is ApiResult.Error -> Result.Error(response.error.message)
        }
    }

    override suspend fun ContestRegister(
        id: String,
        name: String,
        email: String,
        phone: String
    ): Result<success_model> {
        val token = dataStoreManager.getToken.firstOrNull() ?: return Result.Error("Token not found")

        val fieldMap = mapOf(
            "contest_id" to id,
            "name" to name,
            "email" to email,
            "phone" to phone,
            "token" to token
        )

        val response = safeApiCall(
            context = context,
            api = { apiService.contest_register(fieldMap) },
            errorBodyParser = {
                try {
                    Gson().fromJson(it, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $it")
                }
            }
        )

        return when (response) {
            is ApiResult.Success -> {
                if (response.data.code == 200) Result.Success(response.data)
                else Result.Error(response.data.message)
            }
            is ApiResult.Error -> Result.Error(response.error.message)
        }
    }

    override suspend fun getRegisteredContest(): Result<contestregistered> {
        val token = dataStoreManager.getToken.firstOrNull() ?: return Result.Error("Token not found")

        val response = safeApiCall(
            context = context,
            api = { apiService.contest_registered(token) },
            errorBodyParser = {
                try {
                    Gson().fromJson(it, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $it")
                }
            }
        )

        return when (response) {
            is ApiResult.Success -> {
                if (response.data.code == 200) Result.Success(response.data)
                else Result.Error(response.data.message)
            }
            is ApiResult.Error -> Result.Error(response.error.message)
        }
    }

    override suspend fun deleteContest(id: String): Result<success_model> {
        val token = dataStoreManager.getToken.firstOrNull() ?: return Result.Error("Token not found")

        val response = safeApiCall(
            context = context,
            api = { apiService.contest_delete(id, token) },
            errorBodyParser = {
                try {
                    Gson().fromJson(it, error_model::class.java)
                } catch (e: Exception) {
                    error_model(message = "Unknown error format: $it")
                }
            }
        )

        return when (response) {
            is ApiResult.Success -> {
                if (response.data.code == 200) Result.Success(response.data)
                else Result.Error(response.data.message)
            }
            is ApiResult.Error -> Result.Error(response.error.message)
        }
    }
    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null

        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        fileName = it.getString(displayNameIndex)
                    }
                }
            }
        }

        if (fileName == null) {
            fileName = uri.path
            val cut = fileName?.lastIndexOf('/')
            if (cut != -1 && cut != null) {
                fileName = fileName.substring(cut + 1)
            }
        }

        return fileName
    }
    fun uriToMultipartBodyPart(
        context: Context,
        uri: Uri,
        partName: String = "file"
    ): MultipartBody.Part? {
        return try {
            val contentResolver = context.contentResolver

            // Get the input stream from URI
            val inputStream = contentResolver.openInputStream(uri)
                ?: return null

            // Read the file content
            val bytes = inputStream.readBytes()
            inputStream.close()

            // Get the MIME type
            val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
            Log.d("mimeType",mimeType)

            // Get the file name
            val fileName = getFileName(context, uri) ?: "file"

            // Create RequestBody
            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())

            // Create MultipartBody.Part
            MultipartBody.Part.createFormData(partName, fileName, requestBody)

        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    override suspend fun contest_submissions(
        id: String,
        text_content: String,
        file: Uri?,
        file_url: String
    ): Result<success_model> {
        val token = dataStoreManager.getToken.firstOrNull() ?: return Result.Error("Token not found")

        val partMap = mutableMapOf<String, RequestBody>()
        partMap["contest_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), id)
        partMap["text_content"] = RequestBody.create("text/plain".toMediaTypeOrNull(), text_content)
        partMap["token"] = RequestBody.create("text/plain".toMediaTypeOrNull(), token)
        if(file_url.isNotEmpty()) {
            partMap["file_url"] = RequestBody.create("text/plain".toMediaTypeOrNull(), file_url)

        }

            val response = safeApiCall(
                context = context,
                api = {
                    if(file!=null){
                        Log.d("file_path",file.path.toString())
                        apiService.contest_submission(
                            partMap,
                            uriToMultipartBodyPart(context, file, "file_path")
                        )
                    }
                    else {
                        apiService.contest_submission(partMap)
                    }
                      },
                errorBodyParser = {
                    try {
                        Gson().fromJson(it, error_model::class.java)
                    } catch (e: Exception) {
                        error_model(message = "Unknown error format: $it")
                    }
                }

            )

            return when (response) {
                is ApiResult.Success -> {
                    if (response.data.code == 200) Result.Success(response.data)
                    else Result.Error(response.data.message)
                }

                is ApiResult.Error -> Result.Error(response.error.message)
            }


    }


}