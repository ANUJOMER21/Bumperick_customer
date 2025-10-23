package com.bumperpick.bumperickUser.API.New_model

data class DataXXXXXXXXXXXXXX(
    val approval: String,
    val description: String?,
    val end_date: String,
    val expire: Boolean,
    val id: Int,
    val is_participated: Int,
    val is_registered: Int,
    val is_required_file: Int,
    val media: List<MediaXXXXX>,
    val no_of_participants: Int,
    val is_unlimited: Int,
    val start_date: String,
    val status: String,
    val terms: String,
    val title: String,
    val vendor_id: Int,
    var show_winner: Boolean=false,
    val is_winner_announced:Int
)