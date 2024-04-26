package com.depvt.live.football.tv.utils
import android.util.Log

class Logger {

   private val isLoggerEnabled= true


    fun printLog(tag:String,message: String)
    {
        if (isLoggerEnabled)
        {
            Log.d(tag,message)
        }
    }

}