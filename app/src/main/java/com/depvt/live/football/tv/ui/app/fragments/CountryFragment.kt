package com.depvt.live.football.tv.ui.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.adsData.AdManager
import com.depvt.live.football.tv.databinding.ContriesFragmentBinding
import com.depvt.live.football.tv.models.Country
import com.depvt.live.football.tv.ui.app.adapters.CountriesAdapter
import com.depvt.live.football.tv.utils.interfaces.AdManagerListener
import com.depvt.live.football.tv.utils.interfaces.NavigateData
import com.depvt.live.football.tv.utils.objects.Constants
import com.depvt.live.football.tv.viewModel.ScoresViewModel

class CountryFragment : Fragment(), AdManagerListener, NavigateData {

    private val modelCountry by lazy {
        ViewModelProvider(requireActivity())[ScoresViewModel::class.java]
    }
    private var adManager: AdManager? = null
    private var binding: ContriesFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.contries_fragment, container, false)
        binding = DataBindingUtil.bind(view)
        binding?.lifecycleOwner = this
        binding?.model = modelCountry
        adManager = AdManager(requireContext(), requireActivity(), this)
        setUpViewModel()

        return view
    }

    private fun setUpViewModel() {

        //modelCountry?.onRefreshScoresData()
        modelCountry.countryLeagueList.observe(viewLifecycleOwner) {
            if (it != null) {
                setAdapter(it.countries)
            }
        }
    }

    private fun setAdapter(countries: List<Country>?) {

        val listWithAd = countries?.let { checkNativeAd(it) }

        val adapter = listWithAd?.let {
            CountriesAdapter(
                requireContext(), this, it,
                Constants.nativeAdProviderNameVal, adManager!!
            )
        }

        binding?.countryRecycler?.layoutManager = LinearLayoutManager(context)
        binding?.countryRecycler?.adapter = adapter
        adapter?.submitList(listWithAd)

    }

    private fun checkNativeAd(list: List<Country>): MutableList<Country?> {
        val tempChannels: MutableList<Country?> =
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