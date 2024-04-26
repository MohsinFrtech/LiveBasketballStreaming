package com.depvt.live.football.tv.utils.objects

import android.content.Context
import android.content.SharedPreferences
import com.depvt.live.football.tv.R

class SharedPreference(context: Context?) {

    private var mPref: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null

    init {
        mPref = context?.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
        mEditor = mPref?.edit()
    }

    fun saveString(key: String, value: String) {
        mEditor?.putString(key, value)
        mEditor?.commit()
    }

    fun getString(key: String):String? {
        return mPref?.getString(key, "light")
    }

    fun saveBool(key: String, value: Boolean) {
        mEditor?.putBoolean(key, value)
        mEditor?.commit()
    }

    fun getBool(key: String):Boolean? {
       return mPref?.getBoolean(key, true)
    }

}