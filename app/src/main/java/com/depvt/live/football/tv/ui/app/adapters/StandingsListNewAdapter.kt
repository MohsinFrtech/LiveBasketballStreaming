package com.depvt.live.football.tv.ui.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.ItemLayoutGroupsBinding
import com.depvt.live.football.tv.databinding.ItemLayoutParentBinding
import com.depvt.live.football.tv.databinding.ItemLayoutStandingsBinding
import com.depvt.live.football.tv.databinding.ItemLayoutTeamChildBinding
import com.depvt.live.football.tv.models.GroupStandingModel
import com.depvt.live.football.tv.models.StandingModel
import com.depvt.live.football.tv.utils.objects.Constants

class StandingsListNewAdapter(val list: MutableList<GroupStandingModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var bindingParent: ItemLayoutGroupsBinding? = null
    private var bindingChild: ItemLayoutStandingsBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == Constants.STANDINGS_PARENT) {
            val rowView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_groups, parent, false)
            bindingParent = DataBindingUtil.bind(rowView)
            GroupViewHolder(rowView)
        } else {
            val rowView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_standings, parent, false)
            bindingChild = DataBindingUtil.bind(rowView)
            ChildViewHolder(rowView)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val dataList = list[position]
        if (dataList.type == Constants.STANDINGS_PARENT) {
            holder as GroupViewHolder
            holder.apply {
                bindingParent?.groupName?.text = dataList.groupName
                itemView.setOnClickListener {
                    expandOrCollapseParentItem(dataList, position)
                }
            }
        } else {
            holder as ChildViewHolder

            holder.apply {
                val singleService = dataList.teamsInGroup.first()
                bindingChild?.rankId?.text = singleService.teamPosition.toString()
                bindingChild?.teamName?.text = singleService.teamName
                bindingChild?.teamPoints?.text = singleService.teamPoints.toString()
                bindingChild?.teamWin?.text = singleService.teamWin.toString()
                bindingChild?.teamLose?.text = singleService.teamLose.toString()
                bindingChild?.teamDiff?.text = singleService.teamDiff.toString()
                bindingChild?.teamPct?.text = singleService.teamPct.toString()
                loadImage(bindingChild!!.teamLogo, singleService.teamLogo)
            }
        }
    }

    private fun expandOrCollapseParentItem(singleBoarding: GroupStandingModel, position: Int) {

        if (singleBoarding.isExpanded) {
            collapseParentRow(position)
            //bindingParent?.imageViewArrow?.animate()?.rotationBy(180.0F)?.setDuration(50)?.start()
            bindingParent?.imageViewArrow?.setImageResource(R.drawable.right_arrow)
            bindingParent?.layoutTeams?.visibility = View.GONE

        } else {
            expandParentRow(position)
            //bindingParent?.imageViewArrow?.animate()?.rotationBy(180.0F)?.setDuration(50)?.start();
            bindingParent?.imageViewArrow?.setImageResource(R.drawable.arrow_down)
            bindingParent?.layoutTeams?.visibility = View.VISIBLE
        }
    }

    private fun expandParentRow(position: Int) {
        val currentBoardingRow = list[position]
        val services = currentBoardingRow.teamsInGroup
        currentBoardingRow.isExpanded = true
        var nextPosition = position
        if (currentBoardingRow.type == Constants.STANDINGS_PARENT) {

            services.forEach { service ->
                val subList: ArrayList<StandingModel> = ArrayList()
                subList.add(service)
                val parentModel = GroupStandingModel(type = Constants.STANDINGS_CHILD, teamsInGroup = subList)
                list.add(++nextPosition, parentModel)
            }
            notifyDataSetChanged()
        }
    }

    private fun collapseParentRow(position: Int) {
        val currentBoardingRow = list[position]
        val services = currentBoardingRow.teamsInGroup
        list[position].isExpanded = false
        if (list[position].type == Constants.STANDINGS_PARENT) {
            services.forEach { _ ->
                list.removeAt(position + 1)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int = list[position].type

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class GroupViewHolder(row: View) : RecyclerView.ViewHolder(row)

    class ChildViewHolder(row: View) : RecyclerView.ViewHolder(row)
}



