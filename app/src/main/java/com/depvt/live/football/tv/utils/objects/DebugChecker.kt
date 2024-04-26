package com.depvt.live.football.tv.utils.objects

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import com.depvt.live.football.tv.BuildConfig
import com.depvt.live.football.tv.R

object DebugChecker{

    fun checkDebugging(context: Context?): Boolean {
        if (!BuildConfig.DEBUG)
        {
            if (Settings.Secure.getInt(
                    context?.contentResolver,
                    Settings.Global.ADB_ENABLED,
                    0
                ) == 1
            ) {
                showDialog(context)
                return true
            }
        }

        return false
    }

    private fun showDialog(context: Context?) {

        try {
            context?.let {
                androidx.appcompat.app.AlertDialog.Builder(it)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Error!")
                    .setCancelable(false)
                    .setMessage(context.getString(R.string.usb_debugging))
                    .setPositiveButton(
                        "Goto Disable"
                    ) { _, _ ->
                        context.startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
                    }
//                .setNegativeButton(
//                    "Exit"
//                ) { _, _ -> //"exit"
//                }
                    .show()
            }

        }
        catch (e:Exception)
        {
            Log.d("Exception","msg"+e.message)
        }

    }

}