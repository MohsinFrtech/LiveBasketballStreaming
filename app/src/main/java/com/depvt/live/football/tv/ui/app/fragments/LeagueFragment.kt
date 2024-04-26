package com.depvt.live.football.tv.ui.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.adsData.AdManager
import com.depvt.live.football.tv.databinding.LeaguesFragmentBinding
import com.depvt.live.football.tv.models.League
import com.depvt.live.football.tv.ui.app.adapters.LeaguesAdapter
import com.depvt.live.football.tv.utils.interfaces.AdManagerListener
import com.depvt.live.football.tv.utils.interfaces.NavigateData
import com.depvt.live.football.tv.utils.objects.Constants

class LeagueFragment : Fragment(), AdManagerListener, NavigateData {

    private var adManager: AdManager? = null
    private var binding: LeaguesFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.leagues_fragment, container, false)
        binding = DataBindingUtil.bind(view)
        binding?.lifecycleOwner = this
        adManager = AdManager(requireContext(), requireActivity(), this)


        binding?.ivBackIcon?.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val args: LeagueFragmentArgs by navArgs()

        val name = args.countryName
        binding?.appText?.text = name
        if (args.getLeaguesList != null) {

            setAdapter(args.getLeaguesList)

        }
    }


    private fun setAdapter(countries: Array<League>?) {

        val listWithAd = countries?.let { checkNativeAd(it) }
        val adapter = listWithAd?.let {
            LeaguesAdapter(
                requireContext(), this, it,
                Constants.nativeAdProviderNameVal, adManager!!
            )
        }

        binding?.countryRecycler?.layoutManager = LinearLayoutManager(context)
        binding?.countryRecycler?.adapter = adapter
        adapter?.submitList(listWithAd)

    }

    private fun checkNativeAd(list: Array<League>): MutableList<League?> {
        val tempChannels: MutableList<League?> =
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

    override fun onAdLoad(value: String) {
        TODO("Not yet implemented")
    }

    override fun onAdFinish() {
        TODO("Not yet implemented")
    }

    override fun navigation(viewId: NavDirections) {
        if (viewId.actionId != null) {
            findNavController().navigate(viewId)
        }
    }
}