package com.bumperpick.bumperickUser.Repository

import android.net.Uri
import com.bumperpick.bumperickUser.API.New_model.Constest_Model
import com.bumperpick.bumperickUser.API.New_model.*
import com.bumperpick.bumperpickvendor.API.Model.success_model

interface Contest_Repository {
    suspend fun getAllContest(): Result<Constest_Model>
    suspend fun ContestDetails(id: String): Result<contest_details>
    suspend fun ContestRegister(id: String,name: String,email: String,phone: String): Result<success_model>
    suspend fun getRegisteredContest(): Result<contestregistered>
    suspend fun deleteContest(id: String): Result<success_model>
    suspend fun contest_submissions(id: String,text_content: String,file: Uri?,file_url:String):Result<success_model>

}