package com.depvt.live.football.tv.ui.app.fragments.leagues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.FragmentLeagueDetailsBinding
import com.depvt.live.football.tv.models.Game
import com.depvt.live.football.tv.ui.app.adapters.StandingsPagerAdapter
import com.depvt.live.football.tv.ui.app.adapters.loadImage
import com.depvt.live.football.tv.viewModel.ScoresViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class LeagueDetails : Fragment() {

    private var binding: FragmentLeagueDetailsBinding? = null
    private var listGames: List<Game> = emptyList()
    private val modelCountry by lazy {
        ViewModelProvider(requireActivity())[ScoresViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_league_details, container, false)
        binding = DataBindingUtil.bind(view)
        binding?.lifecycleOwner = this
        binding?.infoBackIcon?.setOnClickListener {
            findNavController().popBackStack()
        }


        val args: LeagueDetailsArgs by navArgs()

        if (args.getLeague != null) {
            val data = args.getLeague
            binding?.leagueNameVal?.text = data?.name
            loadImage(binding!!.ivLeague, data!!.games?.get(0)?.league?.logo)
            listGames = data.games!!
            setUpViewPager()
            modelCountry.getLeagueDetails(data.id, data.season)
        }

        return view
    }

    private fun setUpViewPager() {
        val rankingsArray = resources.getStringArray(R.array.standings)
        val fragmentAdapter = StandingsPagerAdapter(
            childFragmentManager, this.lifecycle,
            Gson().toJson(listGames)
        )
        binding?.viewPager?.isUserInputEnabled = true
        binding?.tabsStandings?.tabGravity = TabLayout.GRAVITY_FILL
        binding?.viewPager?.adapter = fragmentAdapter
        binding?.tabsStandings.let {
            binding?.viewPager.let { it1 ->
                if (it != null) {
                    if (it1 != null) {
                        TabLayoutMediator(
                            it, it1
                        ) { tab: TabLayout.Tab, position: Int ->
                            tab.text = rankingsArray[position]
                        }.attach()
                    }
                }
            }
        }

        //hideLoading()

        binding?.tabsStandings?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}