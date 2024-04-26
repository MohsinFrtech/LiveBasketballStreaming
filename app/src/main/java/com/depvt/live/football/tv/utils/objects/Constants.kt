package com.depvt.live.football.tv.utils.objects

import com.depvt.live.football.tv.BuildConfig


object Constants {

    //Ads
    const val unityTestMode = false
    const val admob = "admob"
    const val facebook = "facebook"
    const val chartBoost = "chartboost"
    const val unity = "unity"
    const val startApp = "startapp"

    var admobInterstitial = ""
    const val adUnitId = "Interstitial_Android"
    var facebookPlacementIdInterstitial = ""
    var fbPlacementIdBanner = ""
    var chartBoostAppID = ""
    var chartBoostAppSig = ""
    var nativeAdmob: String = ""
    var unityGameID = ""
    var startAppId=""
    var nativeFacebook = ""
    var admobBannerId=""

    //Ad Locations
    const val adMiddle="Middle"
    const val adBefore="BeforeVideo"
    const val adAfter="AfterVideo"
    const val adLocation1="Location1"
    const val adLocation2top="Location2Top"
    const val adLocation2bottom="Location2Bottom"
    const val nativeAdLocation="native"
    var nativeAdProviderNameVal="none"
    var location2TopProvider="none"
    var location2BottomProvider="none"
    var locationAfter="none"
    var isInitAdmobSdk=false
    var isInitFacebookSdk= false
    var isUnitySdkInit=false
    var isChartboostSdkInit=false
    var isStartAppSdkInit=false
    var modeCheckValue=""
    const val preferenceKey="Message"

    //DefamationClass
    var myUserLock1 = "locked"
    var myUserCheck1 = "myUserCheck1"

    const val  key = "nonenFootBall@Key"
    const val  salt = "Fit4533op"
    const val mySecretCheckDel: String = "&"
    const val mySecretSize = 16
    var userIp = "userIp"
    const val userBase = "?token="
    const val userBaseDel = "/"
    const val algoTypeS1 = "SHA-1"
    const val algoTypeS2 = "SHA-256"
    const val algoName = "iso-8859-1"

    var splash_status=false
    var app_update_dialog=false
    //Api data
    const val stringId = "14"
    var authToken = ""
    const val buildNo = BuildConfig.VERSION_CODE
    var passVal=""
    var emptyCheck=""
    var countryLeagueBase=""
    var tokenCountry=""
    var IpApi = ""
    var baseUrlChannel = ""
    var baseUrlDemo = ""
    var userLinkVal = ""

    const val userApi = "get_url"
    const val channelApi = "details"
    const val countryLeague = "games"
    const val leagueStandings = "standings"

    const val sepUrl = ".net"
    const val channelId = "id"
    const val channelAuth = "auth_token"
    const val channelBuild = "build_no"
    var passphraseVal = ""
    var channel_url_val = ""
    //playLand
    const val userBaseExtraDel1 = "999"
    const val userBaseExtraDel2 = "%"
    const val userRepAlgo = "[cCITS]"

    //userData
    const val userType1 = "flussonic"
    const val userType2 = "cdn"
    const val userType3 = "p2p"
    const val userType4 = "cdnp2p"
    const val phraseDel = "@"
    var userLink = ""
    var defaultString = ""
    //StoneData

    const val chName="UTF-8"
    const val asp="AES"
    const val instanceVal="PBKDF2WithHmacSHA1"
    const val transForm="AES/CBC/PKCS5Padding"

    //billing
    var removeAds = false
    var oldSkuList: ArrayList<String> = ArrayList()
    var videoFinish=false


    //splash
    var updateScreenStatus = false
    var mailId="apps.greek@gmail.com"
    var mailText="Send Email..."

    //userValues
    var cementData = "cementData"
    var cementType = "cementType"
    var cementMainData = "cementMainData"
    var cementMainType = "cementMainType"

    //expanded recyclerview
    const val PARENT=0
    const val CHILD=1

    //expanded recyclerview
    const val STANDINGS_PARENT=0
    const val STANDINGS_CHILD=1


}