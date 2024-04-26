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
import com.depvt.live.football.tv.databinding.ItemLayoutCategoriesBinding
import com.depvt.live.football.tv.databinding.NativeAdLayoutBinding
import com.depvt.live.football.tv.date.ProcessingFile
import com.depvt.live.football.tv.models.Channel
import com.depvt.live.football.tv.ui.app.fragments.ChannelFragmentDirections
import com.depvt.live.football.tv.ui.app.fragments.EventFragmentDirections
import com.depvt.live.football.tv.utils.interfaces.NavigateData
import com.depvt.live.football.tv.utils.objects.Constants
import com.depvt.live.football.tv.utils.objects.Constants.channel_url_val
import com.depvt.live.football.tv.utils.objects.Constants.defaultString
import com.depvt.live.football.tv.utils.objects.Constants.passphraseVal
import com.depvt.live.football.tv.utils.objects.Constants.sepUrl
import com.depvt.live.football.tv.utils.objects.Constants.userType1
import com.depvt.live.football.tv.utils.objects.Constants.userType2
import com.depvt.live.football.tv.utils.objects.Defamation


class RecommendedAdapter(val context: Context, private val navigateData: NavigateData, val list: List<Channel?>,
                         private val adType:String, private val adManager: com.depvt.live.football.tv.adsData.AdManager, private val localVal:String,
                         val destinationClass:String): ListAdapter<Channel, RecyclerView.ViewHolder>(
    ChannelAdapterDiffUtilCallback
) {


    private var channelAdapterBinding: ItemLayoutCategoriesBinding?=null
    private val nativeAdsLayout = 1
    private val simpleMenuLayout = 0
    private var binding2: NativeAdLayoutBinding? = null

    object ChannelAdapterDiffUtilCallback: DiffUtil.ItemCallback<Channel>()
    {
        override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {

            return oldItem.name==newItem.name
        }

        override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem==newItem
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
                    .inflate(R.layout.item_layout_categories, parent, false)
                channelAdapterBinding = DataBindingUtil.bind(view)
                ChannelAdapterViewHolder(view)
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
                ///To set remaining events
                channelAdapterBinding?.modelCategory=currentList[position]

                holder.itemView.setOnClickListener{

                    try {

                        if (currentList[position]?.channel_type.equals(
                                userType1, true)) {

                              val processingFile= ProcessingFile()
                              defaultString = processingFile.getChannelType(localVal)

                            val token= currentList[position].url?.let { it1 -> Defamation.improveDeprecatedCode(it1) }
                            val linkAppend = currentList[position].url + token
                            if (destinationClass.equals("event",true))
                            {
                                val channelDirection=EventFragmentDirections.actionEventToPlayer(currentList[position].url, linkAppend,
                                    userType1)
                                navigateData.navigation(channelDirection)
                            }
                            else
                            {
                                val channelDirection=ChannelFragmentDirections.actionChannelToPlayer(currentList[position].url, linkAppend,
                                    userType1)
                                navigateData.navigation(channelDirection)
                            }

                        }
                        else
                        {

                            if (currentList[position]?.url?.contains(sepUrl) == true
                                && passphraseVal.isNotEmpty()
                            ) {

                                val separatedPart =
                                    currentList[position]?.url?.split(sepUrl)

                                channel_url_val = separatedPart?.get(1).toString()

                                if (destinationClass.equals("event",true))
                                {
                                    val channelDirection =
                                        EventFragmentDirections.actionEventToPlayer(
                                            "baseLink",
                                            "linkAppend", userType2
                                        )
                                    navigateData.navigation(channelDirection)
                                }
                                else
                                {
                                    val channelDirection =
                                        ChannelFragmentDirections.actionChannelToPlayer(
                                            "baseLink",
                                            "linkAppend", userType2
                                        )
                                    navigateData.navigation(channelDirection)
                                }


                            }
                        }


                    }
                    catch (e:Exception)
                    {
                        Log.d("Token","exception"+e.message)
                    }

                }


            }

        }

    }


    class ChannelAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
    ///View holder class
    class NativeChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

}