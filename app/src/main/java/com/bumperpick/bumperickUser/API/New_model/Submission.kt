package com.bumperpick.bumperickUser.API.New_model

data class Submission(
    val contest_id: Any,
    val created_at: String,
    val customer_id: Int,
    val file_path: String,
    val id: Int,
    val status: Any,
    val text_content: String
)