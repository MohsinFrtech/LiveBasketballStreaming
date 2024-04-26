package com.depvt.live.football.tv.date

import com.depvt.live.football.tv.utils.objects.Constants
import com.depvt.live.football.tv.utils.objects.Constants.userRepAlgo

class ScreenRotation {

    fun templateFile(strCon:String,sizeVal: Int,mainIndex:Array<String?>?)
    {
        var string1 = strCon
        val string2Pick = (sizeVal / 4)
        val char2Pick = (sizeVal * 0.7).toInt()

        if (string2Pick in 0..9)
        {
            val getFileNumberAt2nd2= mainIndex
            val char1ToReplace = getFileNumberAt2nd2?.get(string2Pick)?.toCharArray()?.get(char2Pick)
            val rep = userRepAlgo.toRegex()
            string1 = rep.replace(string1, char1ToReplace.toString())
            Constants.myUserLock1=string1
        }

    }
}