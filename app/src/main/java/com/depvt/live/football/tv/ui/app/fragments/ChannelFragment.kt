package com.depvt.live.football.tv.ui.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.FragmentChannelsBinding
import com.depvt.live.football.tv.models.Channel
import com.depvt.live.football.tv.ui.app.adapters.ChannelAdapter
import com.depvt.live.football.tv.utils.interfaces.AdManagerListener
import com.depvt.live.football.tv.utils.interfaces.NavigateData
import com.depvt.live.football.tv.utils.objects.Constants
import com.depvt.live.football.tv.viewModel.OneViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChannelFragment : Fragment(), NavigateData, AdManagerListener {


    private var bindingChannel: FragmentChannelsBinding? = null
    private var adManager: com.depvt.live.football.tv.adsData.AdManager? = null
    private var adStatus = false
    private var navDirections: NavDirections? = null
    private var adProviderName = "none"
    private var nativeAdProviderName = "none"
    private var nativeFieldVal = ""
    private val viewModel by lazy {
        activity?.let { ViewModelProvider(it)[OneViewModel::class.java] }
    }

    private var liveChannelsList: MutableList<Channel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_channels, container, false)
        bindingChannel = DataBindingUtil.bind(view)
        bindingChannel?.lifecycleOwner = this
        adManager = context?.let {
            activity?.let { it1 ->
                com.depvt.live.football.tv.adsData.AdManager(
                    it,
                    it1,
                    this
                )
            }
        }


        bindingChannel?.backIcon2?.setOnClickListener {
            findNavController().popBackStack()
        }



        return view
    }

    private fun getFilteredList(pos: Int): MutableList<Channel> {
        //getting current date
        val currentDate: String =
            SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(
                Date()
            )
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        val date: Date = format.parse(currentDate) as Date
        val cal = Calendar.getInstance()
        cal.time = date
        val hours = cal[Calendar.HOUR_OF_DAY]
        val minutes = cal[Calendar.MINUTE]
        val currentDayOfMonth = cal[Calendar.DAY_OF_MONTH]

        ///Iterating through list of live_channels
        val currentlyLiveList: MutableList<Channel> = ArrayList<Channel>()
        for (channel_live in liveChannelsList) {
            if (!channel_live.date.isNullOrEmpty()) {
                try {
                    val df =
                        SimpleDateFormat(
                            "yyyy/MM/dd HH:mm",
                            Locale.getDefault()
                        )
                    df.timeZone = TimeZone.getTimeZone("UTC")
                    val dateNormal = channel_live.date?.let { df.parse(it) }
                    df.timeZone = TimeZone.getDefault()
                    val formattedDate = dateNormal?.let { df.format(it) }

                    val date2: Date =
                        formattedDate?.let { df.parse(it) } as Date
                    val calNormal = Calendar.getInstance()
                    calNormal.time = date2
                    val hoursMatch = calNormal[Calendar.HOUR_OF_DAY]
                    val minutesMatch = calNormal[Calendar.MINUTE]
                    val channelDayOfMonth = calNormal[Calendar.DAY_OF_MONTH]

                    when (pos) {
                        1 -> {
                            if (currentDayOfMonth == channelDayOfMonth) {
                                if (hours > hoursMatch) {
                                    val hourDiff = hours - hoursMatch
                                    if (hourDiff <= 2) {

                                        if (hourDiff == 1) {
                                            val diff = (60 - minutesMatch)
                                            val finalDiff = diff + minutes
                                            if (finalDiff in 0..110) {
                                                currentlyLiveList.add(channel_live)
                                            }
                                        } else if (hourDiff == 2) {
                                            val diff = (60 - minutesMatch)
                                            val finalDiff = diff + minutes + 60
                                            if (finalDiff in 0..110) {
                                                currentlyLiveList.add(channel_live)
                                            }
                                        }
                                    }
                                } else if (hours == hoursMatch) {
                                    if (minutes > minutesMatch) {
                                        val diff = minutes - minutesMatch
                                        if (diff >= 0) {
                                            currentlyLiveList.add(channel_live)
                                        }
                                    }
                                    ///if hours and hour of match same....

                                }

                            }
                        }

                        2 -> {
                            if (currentDayOfMonth == channelDayOfMonth) {
                                ///
                                ///if match hour is equal or greater than current hour...
                                if (hoursMatch >= hours) {
                                    val diffHour = hoursMatch - hours

                                    if (diffHour == 0) {
                                        if (minutesMatch > minutes) {
                                            val minDiff = minutesMatch - minutes
                                            if (minDiff in 0..30) {
                                                currentlyLiveList.add(channel_live)
                                            }
                                        } else if (minutesMatch == minutes) {
                                            val minDiff2 = minutesMatch - minutes
                                            if (minDiff2 == 0) {
                                                currentlyLiveList.add(channel_live)
                                            }

                                        }
                                        ///means match starting and current device time is equal...

                                    } else if (diffHour == 1) {

                                        if (minutes <= 60) {
                                            val minDiff = (60 - minutes)
                                            val finalMin = minDiff + minutesMatch

                                            if (finalMin in 0..30) {
                                                currentlyLiveList.add(channel_live)
                                            }

                                        }

                                    }


                                }


                            }

                        }

                        3 -> {
                            if (currentDayOfMonth == channelDayOfMonth) {
                                if (hoursMatch >= hours) {
//                                                Log.d(
//                                                    "current_date_instance",
//                                                    "" + channel_live.name
//                                                )
                                    val hourDiff = hoursMatch - hours
                                    if (hourDiff == 1) {
                                        if (minutes <= 60) {
                                            val minDiff2 = (60 - minutes)
                                            val finalMin = minDiff2 + minutesMatch
                                            if (finalMin in 0..60) {
                                                currentlyLiveList.add(channel_live)
                                            }
                                        }

                                    }
                                }


                            }

                        }
                    }

                } catch (e: ParseException) {

                    Log.d("Exception", "" + e.message)
                }


            }

        }

        return currentlyLiveList

    }


    override fun onResume() {
        super.onResume()
        settingChannels()
    }

    ///set channels
    private fun settingChannels() {
        val channelData: ChannelFragmentArgs by navArgs()
        if (channelData.getEvent != null) {
            bindingChannel?.channel = channelData.getEvent
            bindingChannel?.eventNameChannel?.text = channelData.getEvent!!.name
            setChannelAdapter(channelData.getEvent?.channels)
        } else {
            if (channelData.channelList?.isNotEmpty() == true) {
                bindingChannel?.noChannelText?.visibility = View.GONE

                val list = channelData.channelList
                setChannelAdapter(list?.toList())

            } else {
                bindingChannel?.noChannelText?.visibility = View.VISIBLE
            }

        }
    }

    private fun setChannelAdapter(channels: List<Channel>?) {

        viewModel?.dataModelList?.observe(viewLifecycleOwner)
        {

            liveChannelsList = ArrayList<Channel>()

            if (!it.extra_3.isNullOrEmpty()) {
                nativeFieldVal = it.extra_3!!
            }
            if (!it.app_ads.isNullOrEmpty()) {
                adProviderName =
                    adManager?.checkProvider(it.app_ads!!, Constants.adBefore).toString()
                nativeAdProviderName =
                    adManager?.checkProvider(it.app_ads!!, Constants.nativeAdLocation).toString()

                Constants.nativeAdProviderNameVal = nativeAdProviderName

                Constants.location2TopProvider =
                    adManager?.checkProvider(it.app_ads!!, Constants.adLocation2top).toString()
                Constants.location2BottomProvider =
                    adManager?.checkProvider(it.app_ads!!, Constants.adLocation2bottom).toString()
                Constants.locationAfter =
                    adManager?.checkProvider(it.app_ads!!, Constants.adAfter).toString()
                if (adProviderName.equals(Constants.startApp, true)) {
                    if (Constants.videoFinish) {
                        Constants.videoFinish = false
                        adManager?.loadAdProvider(
                            adProviderName,
                            Constants.adBefore,
                            null,
                            null,
                            null,
                            null
                        )

                    }
                } else {
                    adManager?.loadAdProvider(
                        adProviderName,
                        Constants.adBefore,
                        null,
                        null,
                        null,
                        null
                    )

                }
            }

            if (channels?.isNotEmpty() == true) {
                bindingChannel?.noChannelText?.visibility = View.GONE

                val liveChannels: MutableList<Channel> =
                    ArrayList<Channel>()
                for (channel in channels!!) {
                    if (channel.live == true) {
                        liveChannelsList.add(channel)
                        liveChannels.add(channel)
                    }

                }

                if (liveChannels.isNotEmpty()) {
//                bindingChannel?.noChannelIcon?.visibility=View.GONE
                    bindingChannel?.noChannelText?.visibility = View.GONE
                    liveChannels.sortBy { it1 ->
                        it1.priority
                    }

                    val listWithAd = checkNativeAd(liveChannels)
                    val adapter = context?.let {
                        adManager?.let { it1 ->
                            ChannelAdapter(
                                it, this, listWithAd, nativeAdProviderName,
                                it1, nativeFieldVal, "channel"
                            )
                        }
                    }
                    bindingChannel?.recyclerChannels?.layoutManager = LinearLayoutManager(context)
                    bindingChannel?.recyclerChannels?.adapter = adapter
                    adapter?.submitList(listWithAd)
//                    adapter?.let { searchChannels(it,liveChannels) }
                } else {
                    bindingChannel?.recyclerChannels?.visibility = View.GONE
                    bindingChannel?.noChannelText?.visibility = View.VISIBLE

                }
            } else {
                bindingChannel?.recyclerChannels?.visibility = View.GONE
                bindingChannel?.noChannelText?.visibility = View.VISIBLE

            }


        }


    }


//    private fun searchChannels(adapter: ChannelAdapter, list: List<Channel?>) {
//        bindingChannel?.eventEdittext?.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                searchChannel(s.toString(), list as MutableList<Channel>, adapter)
//            }
//        })
//    }


    private fun searchChannel(
        text: String,
        liveAndNonemptyEvents: MutableList<Channel>,
        adapterEvent: ChannelAdapter?
    ) {
        //new array list that will hold the filtered data
        val localList: MutableList<Channel> =
            ArrayList<Channel>()


        //looping through existing elements
        for (s in liveAndNonemptyEvents) {
            //if the existing elements contains the search input
            if (s.name?.trim()?.lowercase(Locale.ROOT)?.contains(text.lowercase(Locale.ROOT))!!) {
                //adding the element to filtered list
                localList.add(s)
            }


        }


        val adapter = context?.let {
            adManager?.let { it1 ->
                ChannelAdapter(
                    it, this, localList, nativeAdProviderName,
                    it1, nativeFieldVal, "channel"
                )
            }
        }
        bindingChannel?.recyclerChannels?.layoutManager = LinearLayoutManager(context)
        bindingChannel?.recyclerChannels?.adapter = adapter
        adapter?.submitList(localList)


    }

    override fun navigation(viewId: NavDirections) {
        try {
            navDirections = viewId
            if (adStatus) {
                if (!adProviderName.equals("none", true)) {
                    adManager?.showAds(adProviderName)
                } else {
                    findNavController().navigate(viewId)
                }
            } else {
                findNavController().navigate(viewId)
            }
        } catch (e: Exception) {
            Log.d("Exception", "mess")
        }


    }


    ////Function to return list of events with empty positions.....
    private fun checkNativeAd(list: List<Channel>): List<Channel?> {
        val tempChannels: MutableList<Channel?> =
            ArrayList()
        for (i in list.indices) {
            if (list[i].live!!) {
                val diff = i % 5
                if (diff == 2) {

                    tempChannels.add(null)
                }
                tempChannels.add(list[i])
                if (list.size == 2) {
                    if (i == 1) {
                        tempChannels.add(null)

                    }
                }
            }
        }
        return tempChannels
    }

    override fun onAdLoad(value: String) {
        adStatus = value.equals("success", true)

    }

    override fun onAdFinish() {

        if (navDirections != null) {
            findNavController().navigate(navDirections!!)
        }
    }


}