package com.bumperpick.bumperickUser.API.New_model

data class Contest_reg(
    val contest_id: Int,
    val created_at: String,
    val customer_id: Int,
    val email: String,
    val id: Int,
    val name: String,
    val phone: Any,
    val position: Int,
    val submissions: List<Submission>,
    val winner: Int
)