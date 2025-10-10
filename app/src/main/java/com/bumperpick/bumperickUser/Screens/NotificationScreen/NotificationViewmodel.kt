package com.bumperpick.bumperickUser.Screens.NotificationScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperickUser.Repository.OfferRepository
import com.bumperpick.bumperickUser.Repository.Result
import com.bumperpick.bumperickUser.Screens.Home.UiState
import com.bumperpick.bumperickUser.Screens.Home.UiState.*
import com.bumperpick.bumperpick_Vendor.API.FinalModel.Notification_model
import com.bumperpick.bumperpickvendor.API.Model.success_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class NotificationViewmodel(val offerRepository: OfferRepository): ViewModel() {
private val _notifiacation= MutableStateFlow<UiState<Notification_model>>(UiState.Empty)
    val notification: MutableStateFlow<UiState<Notification_model>> =_notifiacation
    private val _destroY_notification= MutableStateFlow<UiState<success_model>>(UiState.Empty)
    val destroy_notification: MutableStateFlow<UiState<success_model>> =_destroY_notification

    fun deleteNotification(id:String){
        viewModelScope.launch {
            val result=offerRepository.notification_destroy(id)
            _destroY_notification.value= when(result){
                is Result.Error -> UiState.Error(result.message)
                    Result.Loading -> UiState.Loading
                is Result.Success -> {
                    fetchNotifcation()
                    UiState.Success(result.data)}
            }


        }
    }
    fun fetchNotifcation(){
        viewModelScope.launch {
            val result=offerRepository.notification()
            _notifiacation.value=when(result){
                is Result.Error -> UiState.Error(result.message)
                Result.Loading ->UiState.Loading
                is Result.Success -> UiState.Success(result.data)
            }
        }
    }

}
