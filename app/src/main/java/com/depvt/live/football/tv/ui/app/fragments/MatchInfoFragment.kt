package com.depvt.live.football.tv.ui.app.fragments

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.MatchInfoScreenBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MatchInfoFragment:Fragment() {


    private var bindingMatchInfoFragment:MatchInfoScreenBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.match_info_screen,container,false)
        bindingMatchInfoFragment=DataBindingUtil.bind(view)
        setUpData()
        bindingMatchInfoFragment?.infoBackIcon?.setOnClickListener {
            findNavController().popBackStack()
        }
        return  view
    }

    private fun setUpData() {
        val matchInfo: MatchInfoFragmentArgs by navArgs()

        if (matchInfo.gameData!=null)
        {
            bindingMatchInfoFragment?.lifecycleOwner=this
            bindingMatchInfoFragment?.matchInfoData=matchInfo.gameData

            if (!matchInfo.gameData?.status?.short.equals("NS",true))
            {

                bindingMatchInfoFragment?.matchStatusValue?.visibility=View.GONE
                bindingMatchInfoFragment?.mainScoreInfoHeader?.visibility=View.VISIBLE
                bindingMatchInfoFragment?.scoresOfTeams?.text=""+ matchInfo.gameData?.scores?.home?.total+
                        "  VS  "+matchInfo.gameData?.scores?.away?.total

            }
            else
            {

                bindingMatchInfoFragment?.scoresOfTeams?.text="VS"
                bindingMatchInfoFragment?.matchStatusValue?.visibility=View.VISIBLE
                bindingMatchInfoFragment?.mainScoreInfoHeader?.visibility=View.GONE

            }



        }

        if(matchInfo.gameData?.date!=null)
        {
            dateAndTime(matchInfo.gameData?.date)

        }



    }


    private fun dateAndTime(channelDate: String?) {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone("UTC")
        val date = channelDate?.let { df.parse(it) }
        df.timeZone = TimeZone.getDefault()
        val formattedDate = date?.let { df.format(it) }
        val date2: Date? = formattedDate?.let { df.parse(it) }
        val cal = Calendar.getInstance()
        if (date2 != null) {
            cal.time = date2
        }
        var hours = cal[Calendar.HOUR_OF_DAY]
        val minutes = cal[Calendar.MINUTE]
        var timeInAmOrPm = ""

        if (hours > 0) {
            timeInAmOrPm = if (hours >= 12) {
                "PM"
            } else {
                "AM"
            }
        }


        if (hours > 0) {
            if (hours >= 12) {
                if (hours == 12) {
                    /////
                } else {
                    hours -= 12
                }
            }
        }

        if (hours == 0) {
            hours = 12
            timeInAmOrPm = "AM"
        }

        val dayOfTheWeek =
            DateFormat.format("EEEE", date) as String

        val day = DateFormat.format("dd", date) as String

        val monthString =
            DateFormat.format("MMM", date) as String
        val year = DateFormat.format("yyyy", date) as String


        if (minutes < 9) {
            bindingMatchInfoFragment?.dateOfTeams?.text =
                "$dayOfTheWeek, $day $monthString, $year"
            bindingMatchInfoFragment?.timeValue?.text="$hours: 0$minutes$timeInAmOrPm"
        } else {
            bindingMatchInfoFragment?.dateOfTeams?.text =
                "$dayOfTheWeek, $day $monthString, $year"
            bindingMatchInfoFragment?.timeValue?.text="$hours: $minutes$timeInAmOrPm"

        }



    }

}