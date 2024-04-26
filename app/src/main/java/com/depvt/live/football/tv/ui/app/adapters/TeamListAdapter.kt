package com.depvt.live.football.tv.ui.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.ItemLayoutParentBinding
import com.depvt.live.football.tv.databinding.ItemLayoutTeamChildBinding
import com.depvt.live.football.tv.models.GroupTeamModel
import com.depvt.live.football.tv.models.Team
import com.depvt.live.football.tv.utils.objects.Constants

class TeamListAdapter(val list: MutableList<GroupTeamModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var bindingParent: ItemLayoutParentBinding? = null
    private var bindingChild: ItemLayoutTeamChildBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == Constants.PARENT) {
            val rowView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_parent, parent, false)
            bindingParent = DataBindingUtil.bind(rowView)
            GroupViewHolder(rowView)
        } else {
            val rowView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_team_child, parent, false)
            bindingChild = DataBindingUtil.bind(rowView)
            ChildViewHolder(rowView)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val dataList = list[position]
        if (dataList.type == Constants.PARENT) {
            holder as GroupViewHolder
            holder.apply {
                bindingParent?.tvName?.text = dataList.groupName
                itemView.setOnClickListener {
                    expandOrCollapseParentItem(dataList, position)
                }
            }
        } else {
            holder as ChildViewHolder

            holder.apply {
                val singleService = dataList.teamsInGroup.first()
                bindingChild?.tvName?.text = singleService.name
                loadImage(bindingChild!!.img3, singleService.logo)
            }
        }
    }

    private fun expandOrCollapseParentItem(singleBoarding: GroupTeamModel, position: Int) {

        if (singleBoarding.isExpanded) {
            collapseParentRow(position)
            //bindingParent?.imageViewArrow?.animate()?.rotationBy(180.0F)?.setDuration(50)?.start()
            bindingParent?.imageViewArrow?.setImageResource(R.drawable.right_arrow)

        } else {
            expandParentRow(position)
            //bindingParent?.imageViewArrow?.animate()?.rotationBy(180.0F)?.setDuration(50)?.start();
            bindingParent?.imageViewArrow?.setImageResource(R.drawable.arrow_down)
        }
    }

    private fun expandParentRow(position: Int) {
        val currentBoardingRow = list[position]
        val services = currentBoardingRow.teamsInGroup
        currentBoardingRow.isExpanded = true
        var nextPosition = position
        if (currentBoardingRow.type == Constants.PARENT) {

            services.forEach { service ->
                val subList: ArrayList<Team> = ArrayList()
                subList.add(service)
                val parentModel = GroupTeamModel(type = Constants.CHILD, teamsInGroup = subList)
                list.add(++nextPosition, parentModel)
            }
            notifyDataSetChanged()

        }
    }

    private fun collapseParentRow(position: Int) {
        val currentBoardingRow = list[position]
        val services = currentBoardingRow.teamsInGroup
        list[position].isExpanded = false
        if (list[position].type == Constants.PARENT) {
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



