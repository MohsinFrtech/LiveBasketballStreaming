package com.depvt.live.football.tv.ui.app.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.ItemLayoutEventsBinding
import com.depvt.live.football.tv.databinding.NativeAdLayoutBinding
import com.depvt.live.football.tv.models.Event
import com.depvt.live.football.tv.ui.app.fragments.EventFragmentDirections
import com.depvt.live.football.tv.utils.interfaces.NavigateData
import com.depvt.live.football.tv.utils.objects.Constants

class EventAdapter(val context: Context, private val navigateData: NavigateData, val list: List<Event?>,
                   private val adType:String, private val adManager: com.depvt.live.football.tv.adsData.AdManager
): ListAdapter<Event, RecyclerView.ViewHolder>(EventAdapterDiffUtilCallback) {



    private var bindingEvent: ItemLayoutEventsBinding?=null
    private val nativeAdsLayout = 1
    private val simpleMenuLayout = 0
    private var binding2: NativeAdLayoutBinding? = null
    object EventAdapterDiffUtilCallback: DiffUtil.ItemCallback<Event>()
    {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
           return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
           return oldItem==newItem
        }

    }



    class EventAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            nativeAdsLayout -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.native_ad_layout, parent, false)
                binding2 = DataBindingUtil.bind(view)
                NativeEventViewHolder(view)
            }
            else -> {
                val view =LayoutInflater.from(context).inflate(R.layout.item_layout_events,parent,false)
                bindingEvent=DataBindingUtil.bind(view)
                EventAdapterViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        when (getItemViewType(position)) {
            nativeAdsLayout -> {
                ////For native ads if ads_provider provide native ads..
                val viewHolder: NativeEventViewHolder = holder as NativeEventViewHolder

                if (adType.equals(Constants.facebook, true)) {

                    binding2?.fbNativeAdContainer?.let {
                        adManager.loadFacebookNativeAd(
                            it
                        )
                    }
                } else if (adType.equals(Constants.admob, true)) {

                    binding2?.nativeAdView?.let { adManager.loadAdmobNativeAd(viewHolder, it) }

                }

            }
            else -> {
                bindingEvent?.modelData=currentList[position]

                holder.itemView.setOnClickListener {

                    val direction=EventFragmentDirections.actionEventToChannel(currentList[position],null,null)
                    navigateData.navigation(direction)

                }
            }
        }



    }
    override fun getItemViewType(position: Int): Int {
        if (list[position] == null) {
            return nativeAdsLayout
        }
        return simpleMenuLayout
    }

    class NativeEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }
}