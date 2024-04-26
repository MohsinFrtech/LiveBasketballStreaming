package com.depvt.live.football.tv.ui.app.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.ActivitySplashBinding
import com.depvt.live.football.tv.utils.interfaces.DialogListener
import com.depvt.live.football.tv.utils.objects.Constants
import com.depvt.live.football.tv.utils.objects.CustomDialogue
import com.depvt.live.football.tv.utils.objects.DebugChecker
import com.depvt.live.football.tv.utils.objects.InternetUtil
import com.depvt.live.football.tv.utils.objects.SharedPreference
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


///Home Screen ....
class HomeScreen:AppCompatActivity(), DialogListener {

    private var bindingHome: ActivitySplashBinding?=null
    private var preference: SharedPreference?=null
    private var permissionCount= 0

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {

//                bindingHome?.notificationLayout?.visibility=View.GONE
                preference?.saveBool(Constants.preferenceKey,true)

                // Permission is granted. Continue the action or workflow in your
                subscribeOrUnSubscribeTopic()

            } else {
                preference?.saveBool(Constants.preferenceKey,false)
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingHome=DataBindingUtil.setContentView(this, R.layout.activity_splash)
        //Initialize firebase instance...
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        preference= SharedPreference(this)

        //FirebaseApp.initializeApp(this)

        bindingHome?.retry?.setOnClickListener {
            if (!DebugChecker.checkDebugging(this))
            {
                checkEitherEmulatorOrNot()
            }


        }

        bindingHome?.enableBtn?.setOnClickListener{
            bindingHome?.notificationLayout?.visibility=View.GONE
            makePermission()
        }
        bindingHome?.skipBtn?.setOnClickListener{

//            requestPermissionLauncher.unregister()
            bindingHome?.notificationLayout?.visibility=View.GONE
            navigationToNextScreen()
        }

//        cipherAlgo()
    }


    private fun checkNotificationPermission() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    //subscribeOrUnSubscribeTopic()
                        navigationToNextScreen()

                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
                -> {
                    bindingHome?.notificationLayout?.visibility=View.VISIBLE

                }
                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    makePermission()

                }

            }
        }
        else
        {
            subscribeOrUnSubscribeTopic()
        }


    }


    private fun checkEitherEmulatorOrNot() {
        try {

            val isProbablyRunningOnEmulator: Boolean by lazy {
                // Android SDK emulator
                return@lazy ((Build.FINGERPRINT.startsWith("google/sdk_gphone_")
                        && Build.FINGERPRINT.endsWith(":user/release-keys")
                        && Build.MANUFACTURER == "Google" && Build.PRODUCT.startsWith("sdk_gphone_") && Build.BRAND == "google"
                        && Build.MODEL.startsWith("sdk_gphone_"))
                        //
                        || Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")
                        //bluestacks
                        || "QC_Reference_Phone" == Build.BOARD && !"Xiaomi".equals(Build.MANUFACTURER, ignoreCase = true) //bluestacks
                        || Build.MANUFACTURER.contains("Genymotion")
                        || Build.HOST.startsWith("Build") //MSI App Player
                        || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                        || Build.PRODUCT == "google_sdk"
                        || Build.FINGERPRINT.contains("generic")
                        // another Android SDK emulator check
                        )
            }

            if (isProbablyRunningOnEmulator)
            {

                CustomDialogue(this).showDialog(
                    this,"Alert!","Please use application on real device",
                    "","Ok","baseValue"
                )
            }
            else
            {

                checkNotificationPermission()
            }

        } catch (e: Exception) {
            Log.d("Exception", "" + e.message)

        }

    }


    private fun makePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (permissionCount>3)
            {
                bindingHome?.notificationLayout?.visibility=View.GONE

               navigationToNextScreen()

            }
            else if (permissionCount==2)
            {
                bindingHome?.notificationLayout?.visibility=View.GONE

                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts(
                    "package",
                    packageName, null
                )
                intent.data = uri
                startActivity(intent)
            }
            else
            {
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }

            permissionCount++

        }
    }



    private fun subscribeOrUnSubscribeTopic() {

        val getStatus=preference?.getBool(Constants.preferenceKey)


        if (getStatus == true)
        {

//            FirebaseMessaging.getInstance().subscribeToTopic("event")
//                .addOnCompleteListener { task ->
//                    if (task.isComplete) {
//                        //
//                        preference?.saveBool(Constants.preferenceKey,true)
//                    }
//
//                }
//                .addOnFailureListener {
//                    Log.d("valueData","data"+getStatus)
//                }

            Log.d("valueData","data"+getStatus)

            navigationToNextScreen()
            ///means already subscribe to topic...
        }
        else
        {
            navigationToNextScreen()
            preference?.saveBool(Constants.preferenceKey,false)
        }


    }

    override fun onResume() {
        super.onResume()


        if (!DebugChecker.checkDebugging(this))
        {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    checkEitherEmulatorOrNot()
//                    emulatorCheck()

                },
                2000
            )

        }

    }

//    private fun emulatorCheck() {
//        val emulatorDetector: EmulatorDetector = EmulatorDetector.Builder(this)
//            .checkSensors()
//            .checkProperties()
//            .build()
//
//        lifecycleScope.launch {
//            val state = emulatorDetector.getState()
//            getDeviceStateDescription(state)
//        }
//    }

//    private fun getDeviceStateDescription(state: DeviceState) {
//        if (state is DeviceState.Emulator) {
//
//            CustomDialogue(this).showDialog(
//                this,"Alert!","Please use application on real device",
//                "","Ok","baseValue"
//            )
//        } else {
//            checkNotificationPermission()
//        }
//    }
    override fun onDestroy() {
        super.onDestroy()
        requestPermissionLauncher.unregister()
    }
    ///Navigation to next Screen
    private fun navigationToNextScreen() {

        if (InternetUtil.isInternetOn(this))
        {
            bindingHome?.lottieAnimLayout?.visibility=View.GONE
            bindingHome?.noInternetText?.visibility=View.GONE
            bindingHome?.retry?.visibility=View.GONE
            moveToMainScreen()
        }
        else
        {
            bindingHome?.lottieAnimLayout?.visibility=View.VISIBLE
            bindingHome?.noInternetText?.visibility=View.VISIBLE
            bindingHome?.retry?.visibility=View.VISIBLE
            bindingHome?.homeAnimLayout?.visibility=View.GONE


        }
    }

    private fun moveToMainScreen() {
        bindingHome?.homeAnimLayout?.visibility=View.GONE
        if (isDeviceRooted())
        {
            CustomDialogue(this).showDialog(
                this,"Alert!","Please use application on real device",
                "","Ok","baseValue"
            )
        }
        else {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun isDeviceRooted(): Boolean {
        return checkForSuFile() || checkForSuCommand() ||
                checkForSuperuserApk() || checkForBusyBoxBinary() || checkForMagiskManager()
    }

    private fun checkForSuCommand(): Boolean {
        return try {
            // check if the device is rooted
            val file = File("/system/app/Superuser.apk")
            if (file.exists()) {
                return true
            }
            val command: Array<String> = arrayOf("/system/xbin/which", "su")
            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            if (reader.readLine() != null) {
                return true
            }
            return false
        } catch (e: Exception) {
            false
        }
    }

    private fun checkForSuFile(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )
        for (path in paths) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }

    private fun checkForSuperuserApk(): Boolean {
        val packageName = "eu.chainfire.supersu"
        val packageManager = packageManager
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
                true
            } else {
                packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                true
            }

        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun checkForMagiskManager(): Boolean {
        val packageName = "com.topjohnwu.magisk"
        val packageManager = packageManager
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
                true
            } else {
                packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                true
            }

        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun checkForBusyBoxBinary(): Boolean {
        val paths = arrayOf("/system/bin/busybox", "/system/xbin/busybox", "/sbin/busybox")
        try {
            for (path in paths) {
                val process = Runtime.getRuntime().exec(arrayOf("which", path))
                if (process.waitFor() == 0) {
                    return true
                }
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    override fun onPositiveDialogText(key: String) {

    }

    override fun onNegativeDialogText(key: String) {

        when (key) {
            "baseValue" -> finishAffinity()
            "isInternet" -> finishAffinity()
            "eventValue" -> startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))

        }
    }







}