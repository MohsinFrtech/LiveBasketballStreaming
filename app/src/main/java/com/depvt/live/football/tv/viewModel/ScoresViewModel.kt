package com.depvt.live.football.tv.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.depvt.live.football.tv.models.AddUser2
import com.depvt.live.football.tv.models.LeagueRequest
import com.depvt.live.football.tv.models.StandingsModel
import com.depvt.live.football.tv.models.countryLeagueModel
import com.depvt.live.football.tv.network.RetrofitController
import com.depvt.live.football.tv.utils.objects.Constants.tokenCountry
import com.depvt.live.football.tv.utils.objects.InternetUtil
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.await


class ScoresViewModel(application: Application?) : AndroidViewModel(application!!) {

    private val app: Application? = application
    private val tags: String = "ScoresViewModel"
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    val isLoading = MutableLiveData<Boolean>()
    val isInternet = MutableLiveData<Boolean>()


    private val _countryLeagueList = MutableLiveData<countryLeagueModel?>()
    val countryLeagueList: MutableLiveData<countryLeagueModel?>
        get() = _countryLeagueList

    private val _leagueStandingList = MutableLiveData<StandingsModel?>()
    val leagueStandingList: MutableLiveData<StandingsModel?>
        get() = _leagueStandingList


    init {
        isLoading.value = true
        isInternet.value = true
        _countryLeagueList.value = null
        _leagueStandingList.value = null
    }

    fun onRefreshScoresData() {

        countryLeagueData()
    }


    private fun countryLeagueData() {

        isLoading.value = true

        if (InternetUtil.isInternetOn(app)) {
            isInternet.value = true

            coroutineScope.launch {
                val addUser = AddUser2()
                addUser.token = tokenCountry

                val body = Gson().toJson(addUser)
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val getResponse = RetrofitController.apiServiceCountry.getCountryLeague(
                    body
                )

                try {

                    val responseResult = getResponse.await()
                    withContext(Dispatchers.Main) {

                        responseResult.let {
                            try {
                                _countryLeagueList.value = it

                            } catch (e: Exception) {

                                isLoading.value = false
                                isInternet.value = false
                                //e.printStackTrace();
                            }


                        }
                        isLoading.value = false

                    }

                } catch (e: Exception) {
                    Log.d("Exception", "" + "coming34......" + e.localizedMessage)

                    withContext(Dispatchers.Main) {
                        isLoading.value = false

                        Log.d(tags, "countryLeagueData api catch")

                    }
                }
            }


        } else {
            isLoading.value = false
            isInternet.value = false

        }

    }

    fun getLeagueDetails(id: Long, season: Any) {

        isLoading.value = true

        if (InternetUtil.isInternetOn(app)) {
            isInternet.value = true

            coroutineScope.launch {
                val leagueDetails = LeagueRequest()
                leagueDetails.token = tokenCountry
                leagueDetails.league_id = id
                leagueDetails.season = season

                val body = Gson().toJson(leagueDetails)
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val getResponse = RetrofitController.apiServiceStanding.getLeagueStandings(
                    body
                )

                try {

                    val responseResult = getResponse.await()
                    withContext(Dispatchers.Main) {

                        responseResult.let {

                            try {
                                _leagueStandingList.value = it

                            } catch (e: Exception) {

                                isLoading.value = false
                                isInternet.value = false
                                //e.printStackTrace();
                            }


                        }
                        isLoading.value = false

                    }

                } catch (e: Exception) {
                    Log.d("Exception", "" + "coming34......" + e.localizedMessage)

                    withContext(Dispatchers.Main) {
                        isLoading.value = false

                        Log.d(tags, "getLeagueDetails api catch")

                    }
                }
            }


        } else {
            isLoading.value = false
            isInternet.value = false

        }

    }


    // On ViewModel Cleared
    override fun onCleared() {
        super.onCleared()
        viewModelJob.let {
            viewModelJob.cancel()
        }

    }

}