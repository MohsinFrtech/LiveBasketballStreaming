package com.depvt.live.football.tv.ui.app.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.depvt.live.football.tv.ui.app.fragments.leagues.MatchesListFragment
import com.depvt.live.football.tv.ui.app.fragments.leagues.StandingsListFragment
import com.depvt.live.football.tv.ui.app.fragments.leagues.TeamsListFragment

class StandingsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private val gamesGson: String) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MatchesListFragment.newInstance(gamesGson)
            1 -> TeamsListFragment()
            2 -> StandingsListFragment()
            else -> {
                MatchesListFragment()
            }
        }
    }

}