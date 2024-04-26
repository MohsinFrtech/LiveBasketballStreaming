package com.depvt.live.football.tv.ui.app.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.adsData.AdManager
import com.depvt.live.football.tv.databinding.HomeFragmentBinding
import com.depvt.live.football.tv.models.Event
import com.depvt.live.football.tv.room.RoomDatabase
import com.depvt.live.football.tv.room.RoomTable
import com.depvt.live.football.tv.ui.app.adapters.EventAdapter
import com.depvt.live.football.tv.utils.interfaces.AdManagerListener
import com.depvt.live.football.tv.utils.interfaces.NavigateData
import com.depvt.live.football.tv.utils.objects.Constants
import com.depvt.live.football.tv.viewModel.OneViewModel
import com.unity3d.scar.adapter.common.Utils
import java.util.*

class EventFragment : Fragment(), View.OnClickListener, NavigateData, AdManagerListener {

    private val modelEvent by lazy {
        activity?.let { ViewModelProvider(it)[OneViewModel::class.java] }
    }
    private var adManager: AdManager? = null
    private var adStatus = false
    private var liveChannelCount = 0
    private var navDirections: NavDirections? = null
    private var adProviderName = "none"
    private var nativeAdProviderName = "none"
    private var nativeFieldVal = ""
    private var notificationlist: MutableList<RoomTable> =
        ArrayList()
    private var bindingHome: HomeFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        bindingHome = DataBindingUtil.bind(view)
        bindingHome?.lifecycleOwner = this
        bindingHome?.model = modelEvent
        adManager = activity?.let { AdManager(requireContext(), it, this) }


        setUpViewModel()
        clickListener()
        bindingHome?.searchIconEvent?.setOnClickListener {
            if (bindingHome?.cancelIconEvent?.isVisible != true) {
                bindingHome?.cancelIconEvent?.visibility = View.VISIBLE
                bindingHome?.searchIconEvent?.visibility = View.INVISIBLE
                bindingHome?.searchLay?.visibility = View.VISIBLE
            }
        }

        bindingHome?.cancelIconEvent?.setOnClickListener {
            if (bindingHome?.searchIconEvent?.isVisible != true) {
                bindingHome?.eventEdittext?.setText("")
                bindingHome?.cancelIconEvent?.visibility = View.INVISIBLE
                bindingHome?.searchIconEvent?.visibility = View.VISIBLE
                bindingHome?.searchLay?.visibility = View.GONE


            }
        }

        return view
    }

    private fun clickListener() {

        bindingHome?.bellIcon?.setOnClickListener { findNavController().navigate(R.id.notification) }
    }

    private fun setUpViewModel() {
        try {
            bindingHome?.let { getListOfNotifications(requireContext(), it) }
        } catch (e: Exception) {
            Log.d("Exception", "" + e.message)
        }
        modelEvent?.dataModelList?.observe(viewLifecycleOwner) {
            if (!it.extra_3.isNullOrEmpty()) {
                nativeFieldVal = it.extra_3!!
            }

            if (!it.app_ads.isNullOrEmpty()) {
                adProviderName =
                    adManager?.checkProvider(it.app_ads!!, Constants.adMiddle).toString()

                adManager?.loadAdProvider(
                    adProviderName, Constants.adMiddle, bindingHome?.adView, bindingHome?.fbAdView,
                    bindingHome?.unityBannerView, bindingHome?.startAppBanner
                )

                nativeAdProviderName =
                    adManager?.checkProvider(it.app_ads!!, Constants.nativeAdLocation).toString()

                val bannerProvider = adManager?.checkProvider(it.app_ads!!, Constants.adLocation1)

                if (bannerProvider != null) {
                    bindingHome?.adView?.let { it1 ->
                        bindingHome?.fbAdView?.let { it2 ->
                            bindingHome?.startAppBanner?.let { it3 ->
                                adManager?.loadAdProvider(
                                    bannerProvider, Constants.adLocation1,
                                    it1, it2, bindingHome?.unityBannerView, it3
                                )
                            }
                        }
                    }
                }

            }


            if (!it.events.isNullOrEmpty()) {

                val liveEvents: MutableList<Event> =
                    ArrayList<Event>()

                for (event in it.events!!) {
                    liveChannelCount = 0
                    if (event.live == true) {

                        if (!event.channels.isNullOrEmpty()) {

                            for (channel in event.channels!!) {
                                if (channel.live == true) {
                                    liveChannelCount++
                                }
                            }

                            if (liveChannelCount > 0) {

                                liveEvents.add(event)
                            }
                        }


                    }
                }

                ///if channel list is not empty.....


                if (liveEvents.isNotEmpty()) {
                    bindingHome?.noEventText?.visibility = View.GONE
                    bindingHome?.eventRecycler?.visibility = View.VISIBLE
                    liveEvents.sortBy { it1 ->
                        it1.priority
                    }
                    setAdapter(liveEvents)
                } else {
                    bindingHome?.noEventText?.visibility = View.VISIBLE
                    bindingHome?.eventRecycler?.visibility = View.GONE
                    //if event list is empty....
                }

            } else {
                bindingHome?.noEventText?.visibility = View.VISIBLE
                bindingHome?.eventRecycler?.visibility = View.GONE
                ///if event list is empty from backend...
            }

        }
    }


    private fun setAdapter(liveEvents: MutableList<Event>) {

        val listAdapter = adManager?.let { it1 ->
            EventAdapter(
                requireContext(), this, liveEvents, nativeAdProviderName,
                it1
            )
        }
        bindingHome?.eventRecycler?.layoutManager = GridLayoutManager(requireContext(), 2)
        bindingHome?.eventRecycler?.adapter = listAdapter
        listAdapter?.submitList(liveEvents)
        searchEvents(liveEvents)

    }


    ////Function to return list of events with empty positions.....
    private fun searchEvents(list: List<Event>) {
        bindingHome?.eventEdittext?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                search(s.toString(), list as MutableList<Event>)
            }
        })
    }

    ///Return list of all notifications and set to adapter.....
    private fun getListOfNotifications(context: Context?, binding: HomeFragmentBinding) {

        Thread {
            // do your stuff
            val database by lazy { context?.let { RoomDatabase.getDatabase(it) } }
            if (notificationlist.isNotEmpty()) {
                notificationlist.clear()
            }

            @Suppress("UNCHECKED_CAST")
            notificationlist = database?.wordDao()?.getAll() as MutableList<RoomTable>
            Utils.runOnUiThread {
                if (notificationlist.isNotEmpty()) {

                    binding.bellIcon.setImageDrawable(context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.notification1
                        )
                    })


                } else {
                    binding.bellIcon.setImageDrawable(context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.notification1
                        )
                    })

                }
                // do onPostExecute stuff
            }
        }.start()

    }


    private fun search(
        text: String,
        liveAndNonemptyEvents: MutableList<Event>
    ) {
        //new array list that will hold the filtered data
        val localList: MutableList<Event> =
            ArrayList<Event>()
        //looping through existing elements
        for (s in liveAndNonemptyEvents) {
            //if the existing elements contains the search input
            if (s.name?.trim()?.lowercase(Locale.ROOT)?.contains(text.lowercase(Locale.ROOT))!!) {
                //adding the element to filtered list
                localList.add(s)
            }
        }

        val listAdapter = adManager?.let { it1 ->
            EventAdapter(
                requireContext(), this, localList, nativeAdProviderName,
                it1
            )
        }
        bindingHome?.eventRecycler?.layoutManager = LinearLayoutManager(requireContext())
        bindingHome?.eventRecycler?.adapter = listAdapter
        listAdapter?.submitList(localList)
    }


    override fun onClick(v: View?) {

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

    override fun onAdLoad(value: String) {

        adStatus = value.equals("success", true)
    }

    override fun onAdFinish() {

        if (navDirections != null) {
            findNavController().navigate(navDirections!!)
        }

    }

}