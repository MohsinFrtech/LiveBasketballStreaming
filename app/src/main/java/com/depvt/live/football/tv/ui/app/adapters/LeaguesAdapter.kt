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
import com.depvt.live.football.tv.databinding.ItemLayoutLeaguesBinding
import com.depvt.live.football.tv.databinding.NativeAdLayoutBinding
import com.depvt.live.football.tv.models.League
import com.depvt.live.football.tv.ui.app.fragments.LeagueFragmentDirections
import com.depvt.live.football.tv.utils.interfaces.NavigateData
import com.depvt.live.football.tv.utils.objects.Constants


class LeaguesAdapter(
    val context: Context,
    private val navigateData: NavigateData,
    val list: List<League?>,
    private val adType: String,
    private val adManager: com.depvt.live.football.tv.adsData.AdManager,
) : ListAdapter<League, RecyclerView.ViewHolder>(
    CountryAdapterDiffUtilCallback
) {


    private var leagueBinding: ItemLayoutLeaguesBinding? = null
    private val nativeAdsLayout = 1
    private val simpleMenuLayout = 0
    private var binding2: NativeAdLayoutBinding? = null

    object CountryAdapterDiffUtilCallback : DiffUtil.ItemCallback<League>() {
        override fun areItemsTheSame(oldItem: League, newItem: League): Boolean {

            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: League, newItem: League): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            nativeAdsLayout -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.native_ad_layout, parent, false)
                binding2 = DataBindingUtil.bind(view)
                NativeChannelViewHolder(view)
            }

            else -> {

                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_layout_leagues, parent, false)
                leagueBinding = DataBindingUtil.bind(view)
                CountryAdapterViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position] == null) {
            return nativeAdsLayout
        }
        return simpleMenuLayout
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        when (getItemViewType(position)) {
            nativeAdsLayout -> {
                ////For native ads if ads_provider provide native ads..
                val viewHolder: NativeChannelViewHolder = holder as NativeChannelViewHolder

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

                leagueBinding?.dataModel = currentList[position]

                holder.itemView.setOnClickListener {
                    val direction = LeagueFragmentDirections.actionLeagueToStandings(
                        currentList[position],
                    )
                    navigateData.navigation(direction)
                    /*val direction = CountryFragmentDirections.actionCountriesToLeagues(
                        currentList[position].leagues?.toTypedArray(),
                    )
                    navigateData.navigation(direction)*/

                }


            }

        }

    }


    class CountryAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    ///View holder class
    class NativeChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}