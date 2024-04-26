package com.depvt.live.football.tv.ui.app.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.NotificationLayoutBinding
import com.depvt.live.football.tv.room.RoomTable

///Adapter for showing notification....
class NotificationAdapter(context: Context, list_notification: List<RoomTable>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationAdapterViewHolder>() {

    ///Local class variables....
    val context_notification = context
    var list_notification2 = list_notification
    var binding: NotificationLayoutBinding? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapterViewHolder {
        val view: View = LayoutInflater.from(context_notification)
            .inflate(R.layout.notification_layout, parent, false)
        binding = DataBindingUtil.bind(view)
        return NotificationAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list_notification2.size
    }

    override fun onBindViewHolder(holder: NotificationAdapterViewHolder, position: Int) {
        binding?.notiTitle?.text = list_notification2.get(position).title
        binding?.notiDes?.text = list_notification2.get(position).desc
        if (!list_notification2.get(position).image_url.isNullOrEmpty())
        {
            binding?.channelImageShow?.let {
                Glide.with(context_notification).load(list_notification2.get(position).image_url)
                    .placeholder(R.drawable.app_icon)
                    .into(it)
            }
        }

        ////Itemview click listener....
        holder.itemView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val position1=holder.bindingAdapterPosition

                if (list_notification2.get(position1).specific_url.contains("https://") || list_notification2.get(position1).specific_url.contains("http://"))
                {
                    val url: String = list_notification2.get(position1).specific_url
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    context_notification.startActivity(i)
                }
            }


        })
    }

    class NotificationAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

}