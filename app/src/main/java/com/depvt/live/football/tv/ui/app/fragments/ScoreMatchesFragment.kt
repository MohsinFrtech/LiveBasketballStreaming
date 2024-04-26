package com.depvt.live.football.tv.ui.app.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.adsData.AdManager
import com.depvt.live.football.tv.databinding.ScoreMatchesFragmentBinding
import com.depvt.live.football.tv.models.Channel
import com.depvt.live.football.tv.models.Game
import com.depvt.live.football.tv.room.RoomDatabase
import com.depvt.live.football.tv.room.RoomTable
import com.depvt.live.football.tv.ui.app.adapters.MatchesAdapter
import com.depvt.live.football.tv.utils.interfaces.AdManagerListener
import com.depvt.live.football.tv.utils.interfaces.NavigateData
import com.depvt.live.football.tv.utils.objects.Constants
import com.depvt.live.football.tv.viewModel.OneViewModel
import com.depvt.live.football.tv.viewModel.ScoresViewModel
import com.unity3d.scar.adapter.common.Utils
import java.util.ArrayList
import java.util.Locale

class ScoreMatchesFragment : Fragment(), AdManagerListener, NavigateData {

    private val modelCountry by lazy {
        ViewModelProvider(requireActivity())[ScoresViewModel::class.java]
    }
    private var adManager: AdManager? = null
    private var bindingCategory: ScoreMatchesFragmentBinding? = null
    private var gameslist: MutableList<Game> =
        ArrayList()
    private var listWithAd: List<Game?> =
        ArrayList<Game?>()
    private var adStatus = false
    private val modelEvent by lazy {
        ViewModelProvider(requireActivity())[OneViewModel::class.java]
    }
    private var navDirections: NavDirections? = null
    private var adProviderName = "none"
    private var nativeAdProviderName = "none"
    private var notificationlist: MutableList<RoomTable> =
        ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.score_matches_fragment, container, false)
        bindingCategory = DataBindingUtil.bind(view)
        bindingCategory?.lifecycleOwner = this
        bindingCategory?.model = modelCountry
        adManager = activity?.let { AdManager(requireContext(), it, this) }
        setUpViewModel()


        return view
    }


    private fun setUpViewModel() {
        modelEvent.dataModelList?.observe(viewLifecycleOwner) {

            if (!it.app_ads.isNullOrEmpty()) {
                adProviderName =
                    adManager?.checkProvider(it.app_ads!!, Constants.adMiddle).toString()

                adManager?.loadAdProvider(
                    adProviderName,
                    Constants.adMiddle, bindingCategory?.adView, bindingCategory?.fbAdView,
                    bindingCategory?.unityBannerView, bindingCategory?.startAppBanner
                )

                nativeAdProviderName =
                    adManager?.checkProvider(it.app_ads!!, Constants.nativeAdLocation).toString()

                val bannerProvider = adManager?.checkProvider(it.app_ads!!, Constants.adLocation1)

                if (bannerProvider != null) {
                    bindingCategory?.adView?.let { it1 ->
                        bindingCategory?.fbAdView?.let { it2 ->
                            bindingCategory?.startAppBanner?.let { it3 ->
                                adManager?.loadAdProvider(
                                    bannerProvider, Constants.adLocation1,
                                    it1, it2, bindingCategory?.unityBannerView, it3
                                )
                            }
                        }
                    }
                }

            }


        }
        modelCountry.countryLeagueList.observe(viewLifecycleOwner) {
            gameslist.clear()

            if (it != null) {
                it.countries?.map { it1 ->
                    it1.leagues?.map { it2 ->
                        it2.games?.map { it3 ->
                            if (it3.teams != null) {

                                gameslist.add(it3)

                            }

                        }
                    }
                }

                if (gameslist.isNotEmpty()) {

                    setGameAdapter(gameslist)
                }

            }
        }
    }


    private fun checkNativeAd(list: MutableList<Game>): MutableList<Game?> {
        val tempChannels: MutableList<Game?> =
            ArrayList()
        for (i in list.indices) {
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
        return tempChannels
    }


    private fun setGameAdapter(gameslist: MutableList<Game>) {


        listWithAd = if (nativeAdProviderName != "none") {
            checkNativeAd(gameslist)
        } else {
            gameslist
        }


        val listAdapter =
            adManager?.let {
                MatchesAdapter(
                    requireContext(),
                    this,
                    listWithAd,
                    nativeAdProviderName,
                    it,
                    "score"
                )
            }
        bindingCategory?.matchesRecycler?.layoutManager = LinearLayoutManager(requireContext())
        bindingCategory?.matchesRecycler?.adapter = listAdapter
        listAdapter?.submitList(listWithAd)
    }

    override fun onAdLoad(value: String) {
        adStatus = value.equals("success", true)
    }

    override fun onAdFinish() {
        if (navDirections != null) {
            findNavController().navigate(navDirections!!)
        }
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


}