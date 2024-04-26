package com.depvt.live.football.tv.ui.app.fragments.leagues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.adsData.AdManager
import com.depvt.live.football.tv.databinding.FragmentMatchesListBinding
import com.depvt.live.football.tv.models.Country
import com.depvt.live.football.tv.models.Game
import com.depvt.live.football.tv.ui.app.adapters.MatchesAdapter
import com.depvt.live.football.tv.utils.interfaces.AdManagerListener
import com.depvt.live.football.tv.utils.interfaces.NavigateData
import com.depvt.live.football.tv.utils.objects.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class MatchesListFragment : Fragment(), AdManagerListener, NavigateData {


    companion object {
        fun newInstance(state: String): MatchesListFragment {
            val matchesFragment = MatchesListFragment()
            val bundle = Bundle()
            bundle.putString("gamesList", state)
            matchesFragment.arguments = bundle
            return matchesFragment
        }
    }

    var binding: FragmentMatchesListBinding? = null

    private var adManager: AdManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_matches_list, container, false)
        binding = DataBindingUtil.bind(view)
        binding?.lifecycleOwner = this

        adManager = AdManager(requireContext(), requireActivity(), this)


        if (arguments != null) {
            val gameArgs = requireArguments().getString("gamesList", "")
            if (gameArgs.isNotEmpty()) {
                val type = object : TypeToken<List<Game>>() {

                }.type
                val listGames: List<Game> = Gson().fromJson(gameArgs, type)

                setAdapter(listGames)
            }

        }

        return binding?.root
    }

    private fun setAdapter(listGames: List<Game>) {

        val listWithAd = checkNativeAd(listGames)

        val listAdapter = MatchesAdapter(
                requireContext(),
                this,
                listWithAd,
                Constants.nativeAdProviderNameVal,
                adManager!!,"leagueMatch"
            )

        binding?.matchesRecycler?.layoutManager = LinearLayoutManager(requireContext())
        binding?.matchesRecycler?.adapter = listAdapter
        listAdapter.submitList(listWithAd)
    }

    private fun checkNativeAd(list: List<Game>): MutableList<Game?> {
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