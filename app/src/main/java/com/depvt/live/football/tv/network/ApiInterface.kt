package com.depvt.live.football.tv.network

import com.depvt.live.football.tv.models.DataModel
import com.depvt.live.football.tv.models.DataStone
import com.depvt.live.football.tv.models.DataStone2
import com.depvt.live.football.tv.models.StandingsModel
import com.depvt.live.football.tv.models.countryLeagueModel
import com.depvt.live.football.tv.utils.objects.Constants.IpApi
import com.depvt.live.football.tv.utils.objects.Constants.channelApi
import com.depvt.live.football.tv.utils.objects.Constants.countryLeague
import com.depvt.live.football.tv.utils.objects.Constants.leagueStandings
import com.depvt.live.football.tv.utils.objects.Constants.userApi
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("/")
    fun getIPAsync(): Call<String?>?


    @POST(userApi)
    @Headers("Content-Type: application/json")
    fun getDemoDataAsync(
        @Body body: RequestBody
    ): Call<DataStone2?>

    @POST(channelApi)
    @Headers("Content-Type: application/json")
    fun getChannelsAsync(
        @Body body: RequestBody
    ): Call<DataStone>

    @POST(countryLeague)
    @Headers("Content-Type: application/json")
    fun getCountryLeague(
        @Body body: RequestBody
    ): Call<countryLeagueModel>


    @POST(leagueStandings)
    @Headers("Content-Type: application/json")
    fun getLeagueStandings(
        @Body body: RequestBody
    ): Call<StandingsModel>

}