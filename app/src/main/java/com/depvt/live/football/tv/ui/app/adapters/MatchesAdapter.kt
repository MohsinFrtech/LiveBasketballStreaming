package com.depvt.live.football.tv.ui.app.adapters

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.LiveScoreItemsBinding
import com.depvt.live.football.tv.databinding.NativeAdLayoutBinding
import com.depvt.live.football.tv.models.Game
import com.depvt.live.football.tv.ui.app.fragments.ScoreMatchesFragmentDirections
import com.depvt.live.football.tv.ui.app.fragments.leagues.LeagueDetailsDirections
import com.depvt.live.football.tv.utils.interfaces.NavigateData
import com.depvt.live.football.tv.utils.objects.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class MatchesAdapter(
    val context: Context, private val navigateData: NavigateData, val list: List<Game?>,
    private val adType: String, private val adManager: com.depvt.live.football.tv.adsData.AdManager,private val destination: String
) : ListAdapter<Game, RecyclerView.ViewHolder>(
    ChannelAdapterDiffUtilCallback
) {

    private var liveScoreBinding: LiveScoreItemsBinding? = null
    private val nativeAdsLayout = 1
    private val simpleMenuLayout = 0
    private var binding2: NativeAdLayoutBinding? = null

    object ChannelAdapterDiffUtilCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
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
                    .inflate(R.layout.live_score_items, parent, false)
                liveScoreBinding = DataBindingUtil.bind(view)
                MatchAdapterViewHolder(view)
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
                liveScoreBinding?.scoreData = currentList[position]
                if (currentList[position].date.isNotEmpty())
                {
                    dateAndTime(currentList[position].date)
                }



                if (!currentList[position].status.short.equals("NS",true))
                {
                    liveScoreBinding?.scoresOfTeams?.text=""+ currentList[position].scores?.home?.total+
                            "  VS  "+currentList[position].scores?.away?.total

                }
                else
                {
                    liveScoreBinding?.scoresOfTeams?.text="VS"

                }


                holder.itemView.setOnClickListener {

                    if (destination.equals("score",true))
                    {
                        val direction=ScoreMatchesFragmentDirections.actionCategoriesFragmentToMatchInfoFragment(currentList[position])
                        navigateData.navigation(direction)

                    }
                    else {

                    val direction=LeagueDetailsDirections.actionStandingDetailsFragmentToMatchInfoFragment(currentList[position])
                    navigateData.navigation(direction)
                    }


                }


            }

        }

    }



    private fun dateAndTime(channelDate: String?) {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone("UTC")
        val date = channelDate?.let { df.parse(it) }
        df.timeZone = TimeZone.getDefault()
        val formattedDate = date?.let { df.format(it) }
        val date2: Date? = formattedDate?.let { df.parse(it) }
        val cal = Calendar.getInstance()
        if (date2 != null) {
            cal.time = date2
        }
        var hours = cal[Calendar.HOUR_OF_DAY]
        val minutes = cal[Calendar.MINUTE]
        var timeInAmOrPm = ""

        if (hours > 0) {
            timeInAmOrPm = if (hours >= 12) {
                "PM"
            } else {
                "AM"
            }
        }


        if (hours > 0) {
            if (hours >= 12) {
                if (hours == 12) {
                    /////
                } else {
                    hours -= 12
                }
            }
        }

        if (hours == 0) {
            hours = 12
            timeInAmOrPm = "AM"
        }

        val dayOfTheWeek =
            DateFormat.format("EEEE", date) as String

        val day = DateFormat.format("dd", date) as String

        val monthString =
            DateFormat.format("MMM", date) as String
        val year = DateFormat.format("yyyy", date) as String


        if (minutes < 9) {
            liveScoreBinding?.dateOfTeams?.text =
                "$dayOfTheWeek, $day $monthString, $year"
        } else {
            liveScoreBinding?.dateOfTeams?.text =
                "$dayOfTheWeek, $day $monthString, $year"

        }



    }



    class MatchAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    ///View holder class
    class NativeChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

}