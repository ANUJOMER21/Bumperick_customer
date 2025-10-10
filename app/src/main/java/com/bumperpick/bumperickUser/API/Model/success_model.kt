package com.bumperpick.bumperpickvendor.API.Model

import com.bumperpick.bumperickUser.API.New_model.errors

data class success_model(
    val code: Int,
    val message: String,
    val status:String?=null,
    val errors: errors,
)