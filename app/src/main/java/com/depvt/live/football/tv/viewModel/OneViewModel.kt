package com.depvt.live.football.tv.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.depvt.live.football.tv.utils.objects.Constants.baseUrlChannel
import com.depvt.live.football.tv.utils.objects.Constants.baseUrlDemo
import com.depvt.live.football.tv.utils.objects.Constants.buildNo
import com.depvt.live.football.tv.utils.objects.Constants.cementType
import com.depvt.live.football.tv.utils.objects.Constants.myUserCheck1
import com.depvt.live.football.tv.utils.objects.Constants.passVal
import com.depvt.live.football.tv.utils.objects.Constants.passphraseVal
import com.depvt.live.football.tv.utils.objects.Constants.stringId
import com.depvt.live.football.tv.utils.objects.Constants.userBaseExtraDel1
import com.depvt.live.football.tv.utils.objects.Constants.userBaseExtraDel2
import com.depvt.live.football.tv.utils.objects.Constants.userIp
import com.depvt.live.football.tv.utils.objects.Constants.userLinkVal
import com.depvt.live.football.tv.utils.objects.Defamation
import com.depvt.live.football.tv.utils.objects.Defamation.stoneDel
import com.depvt.live.football.tv.utils.objects.InternetUtil
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.depvt.live.football.tv.models.AddUser
import com.depvt.live.football.tv.models.AddUser2
import com.depvt.live.football.tv.models.DataModel
import com.depvt.live.football.tv.models.StoneFile
import com.depvt.live.football.tv.models.countryLeagueModel
import com.depvt.live.football.tv.network.RetrofitController
import com.depvt.live.football.tv.utils.objects.Constants
import com.depvt.live.football.tv.utils.objects.Constants.channel_url_val
import com.depvt.live.football.tv.utils.objects.Constants.tokenCountry
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.await


class OneViewModel(application: Application?) : AndroidViewModel(application!!) {

    private val app: Application? = application
    private val tags: String = "OneViewModel"
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)
    val navigationCheck = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val isInternet = MutableLiveData<Boolean>()
    var userLink = MutableLiveData<Boolean>()
    var baseValue = MutableLiveData<Boolean>()
    var userLinkStatus = MutableLiveData<Boolean>()



    private val _dataModelList = MutableLiveData<DataModel>()
    val dataModelList: LiveData<DataModel>
        get() = _dataModelList

    init {
        isLoading.value = true
        navigationCheck.value = "nothing"
        isInternet.value = true
        userLink.value = false
        baseValue.value = true
    }

    fun onRefreshFixtures() {

        getApiData()
    }

    fun getApiData() {

        isLoading.value = true

        if (InternetUtil.isInternetOn(app)) {
            if (baseUrlChannel != "") {

                isInternet.value = true

                coroutineScope.launch {
                    val addUser = StoneFile()
                    addUser.id = stringId
                    addUser.auth_token = cementType
                    addUser.build_no = buildNo.toString()
                    val body = Gson().toJson(addUser)
                        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                    val getResponse = RetrofitController.apiServiceChannel.getChannelsAsync(
                        body
                    )

                    try {

                        val responseResult = getResponse.await()
                        withContext(Dispatchers.Main) {

                            responseResult.let {


                                try {

                                    val stringValue = it.data?.let { it1 -> stoneDel(it1, passVal) }
                                    var jobj: JSONObject? = JSONObject()
                                    jobj = stringValue?.let { JSONObject(it) }
                                    val gson: Gson = GsonBuilder()
                                        .setLenient()
                                        .create()


                                    val date = gson.fromJson(jobj.toString(), DataModel::class.java)



                                    _dataModelList.value = date

                                    if (userIp.equals("userIp", true)) {
                                        getIP()
                                    }

                                    if (date.extra_1.toString().isNotEmpty()) {
                                        date.extra_1 = Defamation.decryptBase6(date.extra_1)
                                        val encrypt = date.extra_1.toString().trim()
                                        val yourArray: List<String> =
                                            encrypt.split(userBaseExtraDel2)
                                        myUserCheck1 = yourArray[0].trim()
                                        val myRVal: List<String> =
                                            yourArray[1].split(userBaseExtraDel1)
                                        passphraseVal = myRVal[0].trim()
                                    }


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

                            Log.d(tags, "getChannelsData api catch")

                        }
                    }
                }

            } else {
                baseValue.value = false
            }

        } else {
            isLoading.value = false
            isInternet.value = false

        }

    }

    fun getDemoData() {
        isLoading.value = true

        if (baseUrlDemo != "") {
            if (InternetUtil.isInternetOn(app)) {
                isInternet.value = true
                val addUser = AddUser()
                addUser.passphrase = passphraseVal
                addUser.channel_url = channel_url_val
                val body = Gson().toJson(addUser)
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())


                coroutineScope.launch {
                    val getResponse = RetrofitController.apiServiceDemo.getDemoDataAsync(
                        body
                    )

                    try {
                        val responseResult = getResponse.await()
                        withContext(Dispatchers.Main) {
                            responseResult.let {
                                if (!it?.url.isNullOrEmpty()) {

                                    userLinkVal = it?.url.toString()
                                    userLinkStatus.value = true

                                }

                            }
                            isLoading.value = false
                        }

                    } catch (e: Exception) {
                        Log.d(tags, "getDemoData Exception ..." + e.localizedMessage)

                        withContext(Dispatchers.Main) {

                            isLoading.value = false
                            Log.d(tags, "getDemoData getDemoData api catch")
                        }

                    }

                }

            } else {
                Log.d(tags, "getDemoData api catch2")
                isInternet.value = false
                isLoading.value = false
            }
        } else {
            baseValue.value = false
        }
    }


    private fun getIP() {
        if (InternetUtil.isInternetOn(app)) {
            coroutineScope.launch {
                val getResponse = RetrofitController.apiServiceIP.getIPAsync()
                try {
                    val responseResult = getResponse?.await()
                    withContext(Dispatchers.Main) {
                        if (responseResult != null) {
                            userIp = responseResult.toString()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("getDemoData", "" + "ip.. " + e.localizedMessage)
                }
            }

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