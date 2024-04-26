package com.depvt.live.football.tv.ui.app.activities

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.depvt.live.football.tv.BuildConfig
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.ActivityMainBinding
import com.depvt.live.football.tv.date.ScreenRotation
import com.depvt.live.football.tv.date.ScreenUtil
import com.depvt.live.football.tv.models.ApplicationConfiguration
import com.depvt.live.football.tv.utils.Logger
import com.depvt.live.football.tv.utils.interfaces.DialogListener
import com.depvt.live.football.tv.utils.objects.Constants
import com.depvt.live.football.tv.utils.objects.Constants.IpApi
import com.depvt.live.football.tv.utils.objects.Constants.authToken
import com.depvt.live.football.tv.utils.objects.Constants.baseUrlChannel
import com.depvt.live.football.tv.utils.objects.Constants.baseUrlDemo
import com.depvt.live.football.tv.utils.objects.Constants.cementData
import com.depvt.live.football.tv.utils.objects.Constants.cementMainData
import com.depvt.live.football.tv.utils.objects.Constants.cementMainType
import com.depvt.live.football.tv.utils.objects.Constants.cementType
import com.depvt.live.football.tv.utils.objects.Constants.countryLeagueBase
import com.depvt.live.football.tv.utils.objects.Constants.emptyCheck
import com.depvt.live.football.tv.utils.objects.Constants.passVal
import com.depvt.live.football.tv.utils.objects.Constants.tokenCountry
import com.depvt.live.football.tv.utils.objects.CustomDialogue
import com.depvt.live.football.tv.utils.objects.DebugChecker
import com.depvt.live.football.tv.utils.objects.Defamation
import com.depvt.live.football.tv.utils.objects.SharedPreference
import com.depvt.live.football.tv.viewModel.OneViewModel
import com.depvt.live.football.tv.viewModel.ScoresViewModel
import com.facebook.ads.AdSettings
import com.getkeepsafe.relinker.ReLinker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener,
    View.OnClickListener,
    DialogListener {


    var binding: ActivityMainBinding? = null
    private var preference: SharedPreference? = null
    private var context: Context? = null
    private val logger = Logger()
    private var backBoolean = false
    val screenUtil = ScreenUtil()
    private var booleanVpn: Boolean? = false
    private val tAG = "MainActivityClass"
    private var time = "0"
    private var replaceChar = "mint"
    private var intentLink: String = ""
    private val viewModel by lazy {
        ViewModelProvider(this)[OneViewModel::class.java]
    }

    private val viewModelScores by lazy {
        ViewModelProvider(this)[ScoresViewModel::class.java]
    }

    private external fun getStringArray1(): Array<String?>?
    private external fun getStringArray2(): Array<String?>?
    private external fun getStringArray3(): Array<String?>?
    private external fun getStringArray4(): Array<String?>?
    private external fun getStringArray5(): Array<String?>?
    private external fun getStringArray6(): Array<String?>?
    private external fun getStringArray7(): Array<String?>?
    private external fun getStringArray8(): Array<String?>?
    private external fun getStringArray9(): Array<String?>?
    private external fun getStringArray10(): Array<String?>?
    private external fun getStringArray11(): Array<String?>?
    private external fun getStringArray12(): Array<String?>?
    private external fun getStringArray13(): Array<String?>?
    private external fun getStringArray14(): Array<String?>?
    private external fun getStringArray15(): Array<String?>?
    private external fun getStringArray16(): Array<String?>?
    private external fun getStringArray17(): Array<String?>?
    private external fun getStringArray18(): Array<String?>?
    private external fun getStringArray19(): Array<String?>?
    private external fun getStringArray20(): Array<String?>?
    private external fun getStringArray21(): Array<String?>?
    private external fun getStringArray22(): Array<String?>?
    private external fun getStringArray23(): Array<String?>?
    private external fun getStringArray24(): Array<String?>?
    private external fun getStringArray25(): Array<String?>?
    private external fun getStringArray26(): Array<String?>?
    private external fun getStringArray27(): Array<String?>?
    private external fun getStringArray28(): Array<String?>?
    private external fun getStringArray29(): Array<String?>?
    private external fun getStringArray30(): Array<String?>?
    private external fun getStringArray31(): Array<String?>?
    private external fun getStringArray32(): Array<String?>?
    private external fun getStringArray33(): Array<String?>?
    private external fun getStringArray34(): Array<String?>?
    private external fun getStringArray35(): Array<String?>?
    private external fun getStringArray36(): Array<String?>?
    private external fun getStringArray37(): Array<String?>?
    private external fun getStringArray38(): Array<String?>?
    private external fun getStringArray39(): Array<String?>?
    private external fun getStringArray40(): Array<String?>?


    //    companion object {
//        // Used to load the 'cppproject' library on application startup.
//        init {
//            try {
//                System.loadLibrary("cppproject")
//            }
//            catch (e:Exception)
//            {
//                 Log.d("Exception","mess"+e.message)
//            }
//
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        context = this
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        preference = SharedPreference(context)
        sliderRotation()
        setUpActionBar()
        binding?.lifecycleOwner = this
        binding?.modelMain = viewModel
        AdSettings.addTestDevice("b6340138-8432-48a2-9a50-204e9d94103f")
        setUpNavigationGraph()


//        inAppRating()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (backBoolean) {
                    if (!isFinishing) {
                        showDialogue()
                    }
                } else {

                    binding?.navHostFragment?.findNavController()?.popBackStack()

                }

            }
        })

    }


    private fun sliderRotation() {
        ReLinker.loadLibrary(context, "cppproject", object : ReLinker.LoadListener {
            override fun success() {

                lifecycleScope.launch(Dispatchers.Main) {
                    val screenUtil = ScreenUtil()
                    val numberFile = getProjectConcat(screenUtil.reMem())
                    authToken = numberFile?.get(screenUtil.reMem2()).toString()
                    passVal = numberFile?.get(screenUtil.reMem4()).toString()
                    baseUrlChannel = numberFile?.get(screenUtil.reMem3()).toString()
                    emptyCheck = numberFile?.get(screenUtil.reMem5()).toString()
                    baseUrlDemo = numberFile?.get(screenUtil.reMem6()).toString()
                    countryLeagueBase = numberFile?.get(screenUtil.reMem4()).toString()
                    tokenCountry = numberFile?.get(screenUtil.reMem7()).toString()
                    IpApi = numberFile?.get(screenUtil.reMem8()).toString()
                    getIndexValue("chint")
                }
            }

            override fun failure(t: Throwable) {
                showFailedCppDialog()
            }
        })

    }


    private fun showFailedCppDialog() {
        CustomDialogue(this).showDialog(
            this, "title", getString(R.string.cpp_file_error),
            "", "Exit", "eventValue"
        )
    }

    private fun getIndexValue(fitX: String) {
        try {
            var ml1 = ""
            var xLimit = 40
            var sendValue = "tpcidfg&%45"
            if (replaceChar.equals("mint", true)) {
                val tripleVal = sendValue
                sendValue = emptyCheck
            } else {
                sendValue = fitX
            }

            getApiBaseUrl(replaceChar)


            val (array1, array2, array3) = screenUtil.dateFunction(sendValue)
            val sizeMain = screenUtil.returnValueOfSize()
            for (x in array3.indices) {

                var final = xLimit.minus(array3[x].toInt())
                if (final > 0) {
                    ///
                } else {
                    final = 40
                }

                val numberFile = getProjectConcat(final)
                if (array2[x].toInt() in 0..9) {

                    val indexValue = numberFile?.get(array2[x].toInt())
                    val finalVal = indexValue?.toCharArray()?.get(array1[x].toInt())
                    xLimit = final
                    ml1 += StringBuilder().append(finalVal).toString()
                }


            }

            if (replaceChar.equals("mint", true)) {
                Log.d("valueafter", "val" + ml1)
                passVal = ml1
                getStoneValues()
            } else {

                val getFileNumberAt2nd = getProjectConcat(sizeMain)
                val rotation = ScreenRotation()
                rotation.templateFile(ml1, sizeMain, getFileNumberAt2nd)
            }


        } catch (e: Exception) {
            logger.printLog(tAG, "message" + e.message)
        }


    }

    private fun getApiBaseUrl(replaceChar: String) {

        try {
            val baseValue = Defamation.encryptBase64(replaceChar)
            val getSecretValue = Defamation.decryptPRNG(baseValue)
            Defamation.convertCementData(getSecretValue)
        } catch (e: Exception) {

            Log.d("Exception", "message" + e.message)
        }
    }

    private fun getProjectConcat(x: Int): Array<String?>? {
        return when (x) {
            1 -> {
                getStringArray1()
            }

            2 -> {
                getStringArray2()
            }

            3 -> {
                getStringArray3()
            }

            4 -> {
                getStringArray4()
            }

            5 -> {
                getStringArray5()
            }

            6 -> {
                getStringArray6()
            }

            7 -> {
                getStringArray7()
            }

            8 -> {
                getStringArray8()
            }

            9 -> {
                getStringArray9()
            }

            10 -> {
                getStringArray10()
            }

            11 -> {
                getStringArray11()
            }

            12 -> {
                getStringArray12()
            }

            13 -> {
                getStringArray13()
            }

            14 -> {
                getStringArray14()
            }

            15 -> {
                getStringArray15()
            }

            16 -> {
                getStringArray16()
            }

            17 -> {
                getStringArray17()
            }

            18 -> {
                getStringArray18()
            }

            19 -> {
                getStringArray19()
            }

            20 -> {
                getStringArray20()
            }

            21 -> {
                getStringArray21()
            }

            22 -> {
                getStringArray22()
            }

            23 -> {
                getStringArray23()
            }

            24 -> {
                getStringArray24()
            }

            25 -> {
                getStringArray25()
            }

            26 -> {
                getStringArray26()
            }

            27 -> {
                getStringArray27()
            }

            28 -> {
                getStringArray28()
            }

            29 -> {
                getStringArray29()
            }

            30 -> {
                getStringArray30()
            }

            31 -> {
                getStringArray31()
            }

            32 -> {
                getStringArray32()
            }

            33 -> {
                getStringArray33()
            }

            34 -> {
                getStringArray34()
            }

            35 -> {
                getStringArray35()
            }

            36 -> {
                getStringArray36()
            }

            37 -> {
                getStringArray37()
            }

            38 -> {
                getStringArray38()
            }

            39 -> {
                getStringArray39()
            }

            40 -> {
                getStringArray40()
            }

            else -> {
                return null
            }
        }
    }


    private fun showDialogue() {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.custom_layout2)

        val textExit = dialog?.findViewById(R.id.yes) as Button
        val textRate2 = dialog.findViewById(R.id.no) as Button
        val textRate3 = dialog.findViewById(R.id.icon_clcik) as ImageView
        textExit.setOnClickListener {
            rateClicked()
        }

        textRate2.setOnClickListener {
            Constants.app_update_dialog = false
            dialog.dismiss()
            finishAffinity()

        }

        textRate3.setOnClickListener {
            rateClicked()
        }

        if (!isFinishing) {
            dialog.show()
        }

    }


    private fun showSplashMethod(applicationConfigurations: List<ApplicationConfiguration>?) {
        var splashScreenStatus = false
        if (applicationConfigurations != null) {
            ////if configuration array is not empty
            if (applicationConfigurations.isNotEmpty()) {
                val refresh = Handler(Looper.getMainLooper())
                refresh.post {
                    run {
                        for (configuration in applicationConfigurations) {

                            ///For setting time
                            if (configuration.key?.equals("Time", true)!!) {
                                if (configuration.value != null) {
                                    time = configuration.value!!
                                }

                                if (splashScreenStatus) {
                                    if (!Constants.splash_status) {
                                        Constants.splash_status = true
                                        try {
                                            var timer: Int = time.toInt()
                                            timer *= 1000
                                            binding?.splashLayout?.visibility = View.VISIBLE
                                            binding?.splashButton?.setOnClickListener {
                                                try {
                                                    val uri =
                                                        Uri.parse(intentLink)
                                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                                    startActivity(intent)
                                                } catch (e: Exception) {
                                                    Log.d("Exception", "" + e.message)

                                                }
                                            }
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                binding?.splashLayout?.visibility = View.GONE
                                            }, timer.toLong())
                                        } catch (e: NumberFormatException) {

                                            Log.d("Exception", "" + e.message)

                                        }

                                    }
                                }


                            }

                            ///For setting button text
                            if (configuration.key.equals("ButtonText", true)) {
                                if (configuration.value != null) {
                                    binding?.splashButton?.text = configuration.value
                                }

                            }

                            ///For setting heading
                            if (configuration.key.equals("Heading", true)) {
                                if (configuration.value != null) {
                                    binding?.splashHeading?.text = configuration.value
                                }

                            }

                            ///For setting link
                            if (configuration.key.equals("ButtonLink", true)) {
                                if (configuration.value != null) {
                                    intentLink = configuration.value!!
                                }

                            }

                            ///For setting body
                            if (configuration.key.equals("DetailText", true)) {
                                if (configuration.value != null) {
                                    binding?.splashBody?.text = configuration.value
                                }

                            }

                            ///For setting show button
                            if (configuration.key.equals("ShowButton", true)) {
                                if (configuration.value != null) {
                                    if (configuration.value.equals("True", true)) {
                                        binding?.splashButton?.visibility = View.VISIBLE
                                    } else {
                                        binding?.splashButton?.visibility = View.GONE
                                    }

                                }

                            }

                            ///For checking splash is on and off
                            if (configuration.key.equals("ShowSplash", true)) {
                                if (configuration.value.equals("true", true)) {
                                    if (!splashScreenStatus) {
                                        splashScreenStatus = true
                                    }

                                } else {
                                    splashScreenStatus = false
                                    binding?.splashLayout?.visibility = View.GONE
                                }

                            }


                        }////loop to iterate through configuration array
                    }
                }


            }


        }


    }

    private fun showAppUpdateDialog(appUpdateText: String?, permanent: Boolean?) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.app_update_layout)
        val textExit = dialog?.findViewById(R.id.no_thanks) as Button
        val textRate2 = dialog.findViewById(R.id.update) as Button
        val textUpdate = dialog.findViewById(R.id.app_update_txt) as TextView

        if (permanent == true) {
            dialog.setCancelable(false)
            textExit.text = resources.getString(R.string.Exit)
        } else {
            textExit.text = resources.getString(R.string.noThanks)
            dialog.setCancelable(true)
        }

        if (appUpdateText != null) {
            textUpdate.text = appUpdateText
        }

        textExit.setOnClickListener {
            if (permanent == true) {
                Constants.app_update_dialog = false
                dialog.dismiss()
                finishAffinity()
            } else {

                dialog.dismiss()

            }
        }

        textRate2.setOnClickListener {
            rateClicked()
        }


        dialog.show()
    }

    private fun rateClicked() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context?.packageName)
                )
            )
        } catch (e: ActivityNotFoundException) {

            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context?.packageName)
                    )
                )
            } catch (e: ActivityNotFoundException) {
                logger.printLog("Exception", "Exception" + e.message)
            }

        }
    }


    private fun setUpActionBar() {
        setSupportActionBar(binding?.toolbar1)
    }

    private fun setUpViewModel() {
        cementData = authToken
        authToken = "bfhwebfefbhbefjk"
        cementType = cementData
        cementData = "hb87y87y7"

        cementMainData = baseUrlChannel
        baseUrlChannel = "https://play.google.com/store/apps"
        cementMainType = cementMainData
        cementMainData = "https://play.google.com/store/apps/details"
        viewModel.getApiData()
        viewModel.isLoading.observe(this) {

            if (it) {
                binding?.lottieHome?.visibility = View.VISIBLE

            } else {
                binding?.lottieHome?.visibility = View.GONE

            }


        }

        viewModel.isInternet.observe(this)
        {
            if (!it) {

                CustomDialogue(this).showDialog(
                    this, "Alert", getString(R.string.againVal),
                    "Retry", "Exit", "isInternet"
                )
            }

        }

        viewModel.dataModelList.observe(this)
        {

            if (!it.extra_2.isNullOrEmpty()) {
                replaceChar = "goi"

                getIndexValue(it.extra_2!!)
            }

            if (!it.app_version.isNullOrEmpty()) {
                try {

                    val version = BuildConfig.VERSION_CODE
                    try {
                        val parsedInt = it.app_version!!.toInt()
                        if (parsedInt > version) {
                            if (!Constants.app_update_dialog) {
                                showAppUpdateDialog(it.app_update_text, it.is_permanent_dialog)
                                Constants.app_update_dialog = true
                            }
                        }
                    } catch (nfe: java.lang.NumberFormatException) {

                        logger.printLog(tAG, "Exception" + nfe.message)
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    logger.printLog(tAG, "Exception" + e.message)
                }


            }

            if (!it.application_configurations.isNullOrEmpty()) {
                showSplashMethod(it.application_configurations)
            }

        }

        // TODO :- calling scores api
        viewModelScores.onRefreshScoresData()
    }


    override fun onResume() {
        super.onResume()
        if (!DebugChecker.checkDebugging(this)) {

            ///usb debugging is on.
        }
        checkVpn()
    }

    private fun checkVpn() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    val booleanVpnCheck = hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    booleanVpn = booleanVpnCheck == true
                }
            } else {
                booleanVpn = false
            }
        }

        if (booleanVpn != null) {
            if (booleanVpn!!) {
                if (binding?.adblockLayout?.isVisible!!) {
                    /////////

                } else {
                    binding?.adblockLayout?.visibility = View.VISIBLE

                }
            } else {
                binding?.adblockLayout?.visibility = View.GONE

            }
        }

    }

    private fun getStoneValues() {
        try {

            setUpViewModel()
        } catch (e: Exception) {
            logger.printLog("Exception", "" + e.message)
        }
    }

    ///Navigation graph setup ......
    private fun setUpNavigationGraph() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding?.bottomNav?.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)

        binding?.bottomNav?.setOnItemSelectedListener { item ->
            if (item.itemId != navController.currentDestination?.id) {
                NavigationUI.onNavDestinationSelected(item, navController)
            }
            false
        }

    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        backBoolean = destination.id == R.id.event

    }


    override fun onClick(v: View?) {

    }

    override fun onPositiveDialogText(key: String) {
        when (key) {
            "baseValue" -> viewModel.getApiData()
            "isInternet" -> viewModel.getApiData()
            "eventValue" -> viewModel.getApiData()
        }
    }

    override fun onNegativeDialogText(key: String) {
        when (key) {
            "baseValue" -> finishAffinity()
            "isInternet" -> finishAffinity()
            "eventValue" -> finishAffinity()

        }
    }
}