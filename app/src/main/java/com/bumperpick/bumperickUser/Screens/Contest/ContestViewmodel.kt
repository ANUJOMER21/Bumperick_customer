
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperickUser.API.New_model.*
import com.bumperpick.bumperickUser.Repository.Contest_Repository
import com.bumperpick.bumperickUser.Repository.OfferRepository

import com.bumperpick.bumperpickvendor.API.Model.success_model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import     com.bumperpick.bumperickUser.Repository.Result
import com.bumperpick.bumperickUser.Screens.Home.Map.LocationData
import com.bumperpick.bumperickUser.Screens.Home.UiState


class ContestViewmodel(
    private val offerRepository: OfferRepository,
    private val contestRepository: Contest_Repository
): ViewModel()
{

    // Location state
    private val _getLocation=MutableStateFlow<UiState<LocationData>>(
        UiState.Empty)
    val getLocation:StateFlow<UiState<LocationData>> =_getLocation

    fun fetchLocation(){
        viewModelScope.launch {
            _getLocation.value=when(val result=offerRepository.get_locationData()){
                is Result.Error -> UiState.Error(result.message)
                Result.Loading -> UiState.Loading
                is Result.Success-> UiState.Success(result.data)
            }
        }
    }

    // All contests state
    private val _allContests = MutableStateFlow<UiState<Constest_Model>>(UiState.Empty)
    val allContests: StateFlow<UiState<Constest_Model>> = _allContests

    // Contest details state
    private val _contestDetails = MutableStateFlow<UiState<contest_details>>(UiState.Empty)
    val contestDetails: StateFlow<UiState<contest_details>> = _contestDetails

    // Contest registration state
    private val _contestRegistration = MutableStateFlow<UiState<success_model>>(UiState.Empty)
    val contestRegistration: StateFlow<UiState<success_model>> = _contestRegistration

    // Registered contests state
    private val _registeredContests = MutableStateFlow<UiState<contestregistered>>(UiState.Empty)
    val registeredContests: StateFlow<UiState<contestregistered>> = _registeredContests

    // Delete contest state
    private val _deleteContest = MutableStateFlow<UiState<success_model>>(UiState.Empty)
    val deleteContest: StateFlow<UiState<success_model>> = _deleteContest

    // Contest submission state
    private val _contestSubmission = MutableStateFlow<UiState<success_model>>(UiState.Empty)
    val contestSubmission: StateFlow<UiState<success_model>> = _contestSubmission

    /**
     * Fetch user location data
     */


    /**
     * Fetch all available contests
     */
    fun fetchAllContests() {
        viewModelScope.launch {
            _allContests.value = UiState.Loading
            _allContests.value = when (val result = contestRepository.getAllContest()) {
                is Result.Error -> UiState.Error(result.message)
                is Result.Success ->UiState .Success(result.data)
                else -> {
                    UiState.Empty
                }

            }
        }
    }

    /**
     * Fetch details for a specific contest
     * @param contestId The ID of the contest
     */
    suspend fun fetchContestDetail(contestId: String):contest_details? {
        return when(contestRepository.ContestDetails(contestId)){
            is Result.Error -> null
            is Result.Success -> (contestRepository.ContestDetails(contestId) as Result.Success<contest_details>).data
            else -> null
        }


    }

    fun fetchContestDetails(contestId: String) {
        viewModelScope.launch {
            _contestDetails.value = UiState.Loading
            _contestDetails.value = when (val result = contestRepository.ContestDetails(contestId)) {
                is Result.Error -> UiState.Error(result.message)
                is Result.Success -> UiState.Success(result.data)
                else -> {
                    UiState.Empty
                }
            }
        }
    }

    /**
     * Register for a contest
     * @param contestId The ID of the contest
     * @param name User's name
     * @param email User's email
     * @param phone User's phone number
     */
    fun registerForContest(
        contestId: String,
        name: String,
        email: String,
        phone: String,

    ) {
        viewModelScope.launch {
            _contestRegistration.value = UiState.Loading
            _contestRegistration.value = when (val result = contestRepository.ContestRegister(
                id = contestId,
                name = name,
                email = email,
                phone = phone
            )) {
                is Result.Error -> UiState.Error(result.message)
                is Result.Success -> UiState.Success(result.data)
                else -> {
                    UiState.Empty
                }
            }
        }
    }

    /**
     * Fetch contests that the user has registered for
     */
    fun fetchRegisteredContests() {
        viewModelScope.launch {
            _registeredContests.value = UiState.Loading
            _registeredContests.value = when (val result = contestRepository.getRegisteredContest()) {
                is Result.Error -> UiState.Error(result.message)
                is Result.Success -> UiState.Success(result.data)
                else -> {
                    UiState.Empty
                }
            }
        }
    }

    /**
     * Delete a contest registration
     * @param contestId The ID of the contest to delete
     */
    fun deleteContestRegistration(contestId: String) {
        viewModelScope.launch {
            _deleteContest.value = UiState.Loading
            _deleteContest.value = when (val result = contestRepository.deleteContest(contestId)) {
                is Result.Error -> UiState.Error(result.message)
                is Result.Success -> UiState.Success(result.data)
                else -> {
                    UiState.Empty
                }
            }
        }
    }

    /**
     * Submit content for a contest
     * @param contestId The ID of the contest
     * @param textContent Text content for the submission
     * @param fileUri URI of the file to submit
     */
    fun submitContestEntry(
        contestId: String,
        textContent: String,
        fileUri: Uri?,
        pasteLink: String=""
    ) {
        viewModelScope.launch {
            Log.d("submitContestEntry", "submitContestEntry: $fileUri")
            Log.d("submitContestEntry", "pasteLink: $pasteLink")
            _contestSubmission.value = UiState.Loading
            _contestSubmission.value = when (val result = contestRepository.contest_submissions(
                id = contestId,
                text_content = textContent,
                file = fileUri,
                file_url = pasteLink
            )) {

                is Result.Error -> UiState.Error(result.message)
                is Result.Success -> UiState.Success(result.data)
                else -> {
                    UiState.Empty
                }
            }
        }
    }

    /**
     * Clear contest details state
     */
    fun clearContestDetails() {
        _contestDetails.value = UiState.Empty
    }

    /**
     * Clear contest registration state
     */
    fun clearContestRegistration() {
        _contestRegistration.value = UiState.Empty
    }

    /**
     * Clear delete contest state
     */
    fun clearDeleteContest() {
        _deleteContest.value = UiState.Empty
    }

    /**
     * Clear contest submission state
     */
    fun clearContestSubmission() {
        _contestSubmission.value = UiState.Empty
    }

    /**
     * Refresh all contest data
     */
    fun refreshAllData() {
        fetchAllContests()
        fetchRegisteredContests()
        fetchLocation()
    }






}