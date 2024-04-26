package com.depvt.live.football.tv.ui.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.ItemLayoutPopularBinding
import com.depvt.live.football.tv.models.Event
import com.depvt.live.football.tv.ui.app.fragments.EventFragmentDirections
import com.depvt.live.football.tv.utils.interfaces.NavigateData

class PopularEventAdapter(val context: Context, private val navigateData: NavigateData, val list: List<Event?>,
                          private val adType:String, private val adManager: com.depvt.live.football.tv.adsData.AdManager):
    ListAdapter<Event, RecyclerView.ViewHolder>(PopularAdapterDiffUtilCallback) {

    private var bindingEvent: ItemLayoutPopularBinding?=null

    object PopularAdapterDiffUtilCallback: DiffUtil.ItemCallback<Event>()
    {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem==newItem
        }

    }



    class PopularEventAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout_popular,parent,false)
        bindingEvent= DataBindingUtil.bind(view)
        return PopularEventAdapterViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindingEvent?.modelData=currentList[position]



        holder.itemView.setOnClickListener {

            val direction=EventFragmentDirections.actionEventToChannel(currentList[position],null,null)
            navigateData.navigation(direction)

        }
    }
}