package com.depvt.live.football.tv.ui.app.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.depvt.live.football.tv.ui.app.fragments.MoreFragment

class RecentlMatchesPagerAdapter (fm: Fragment, status:String): FragmentStateAdapter(fm) {
    var status: String? = null;

    init {
        this.status = status
    }


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> MoreFragment()
        1 -> MoreFragment()
        else -> MoreFragment()
    }
}