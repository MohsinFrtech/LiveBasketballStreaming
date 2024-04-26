package com.depvt.live.football.tv.ui.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.ItemLayoutGroupsBinding
import com.depvt.live.football.tv.databinding.ItemLayoutStandingsBinding
import com.depvt.live.football.tv.databinding.ItemLayoutTeamsBinding
import com.depvt.live.football.tv.models.GroupTeamModel
import com.depvt.live.football.tv.models.StandingModel
import com.depvt.live.football.tv.models.Team
import com.depvt.live.football.tv.utils.interfaces.NavigateData

class TeamAdapter(val context: Context, val list: List<Team?>):
    ListAdapter<Team, RecyclerView.ViewHolder>(StandingAdapterDiffUtilCallback) {

    private var bindingString: ItemLayoutTeamsBinding?=null

    object StandingAdapterDiffUtilCallback: DiffUtil.ItemCallback<Team>()
    {
        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem==newItem
        }

    }



    class PopularEventAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout_teams,parent,false)
        bindingString= DataBindingUtil.bind(view)
        return PopularEventAdapterViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        bindingString?.teamData=currentList[position]

    }
}