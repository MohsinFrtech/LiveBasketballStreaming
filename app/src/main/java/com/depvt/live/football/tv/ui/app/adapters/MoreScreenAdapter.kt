package com.depvt.live.football.tv.ui.app.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.ItemLayoutMoreBinding
import com.depvt.live.football.tv.models.MoreDataModel
import com.depvt.live.football.tv.utils.interfaces.MoreScreenNavigate
import com.depvt.live.football.tv.utils.objects.Constants
import com.depvt.live.football.tv.utils.objects.SharedPreference
import com.google.firebase.messaging.FirebaseMessaging

class MoreScreenAdapter(
    val context: Context, private val moveData: MoreScreenNavigate
) : ListAdapter<MoreDataModel, MoreScreenAdapter.ChannelAdapterViewHolder>(
    ChannelAdapterDiffUtilCallback
) {

    private var bindingItem: ItemLayoutMoreBinding? = null
    private var preference: SharedPreference? = null

    init {
        preference = SharedPreference(context)
    }


    class ChannelAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    object ChannelAdapterDiffUtilCallback : DiffUtil.ItemCallback<MoreDataModel>() {
        override fun areItemsTheSame(oldItem: MoreDataModel, newItem: MoreDataModel): Boolean {
            //return oldItem. == newItem.textVal
            return true
        }

        override fun areContentsTheSame(oldItem: MoreDataModel, newItem: MoreDataModel): Boolean {
            //return oldItem.textVal == newItem.textVal
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelAdapterViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_layout_more, parent, false)
        bindingItem = DataBindingUtil.bind(view)
        return ChannelAdapterViewHolder(view)

    }

    override fun onBindViewHolder(holder: ChannelAdapterViewHolder, position: Int) {

        val getNotification = preference?.getBool(Constants.preferenceKey)

        bindingItem?.switchCompatNotification?.isChecked = getNotification == true


        val data = currentList[position]

        bindingItem?.ivItemMore?.setImageResource(data.image)
        if (data.text.equals("Notifications", true)) {
            bindingItem?.forwardIcon?.visibility = View.GONE
            bindingItem?.switchCompatNotification?.visibility = View.VISIBLE

        } else {
            bindingItem?.forwardIcon?.visibility = View.VISIBLE
            bindingItem?.switchCompatNotification?.visibility = View.GONE

        }
        bindingItem?.tvItemMore?.text = data.text


        bindingItem?.switchCompatNotification?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (data.text.equals("Notification", true)) {
                    preference?.saveBool(Constants.preferenceKey, true)

                }
                @Suppress("DEPRECATION")
                bindingItem?.switchCompatNotification?.thumbDrawable?.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    ), PorterDuff.Mode.MULTIPLY
                )
                @Suppress("DEPRECATION")
                bindingItem?.switchCompatNotification?.trackDrawable?.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    ), PorterDuff.Mode.MULTIPLY
                )
                ///Subscribe to particular event.....
                //FirebaseMessaging.getInstance().subscribeToTopic("event")

            } else {
                if (data.text.equals("Notification", true)) {
                    preference?.saveBool(Constants.preferenceKey, false)
                }
                @Suppress("DEPRECATION")
                bindingItem?.switchCompatNotification?.thumbDrawable?.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    ), PorterDuff.Mode.MULTIPLY
                )
                @Suppress("DEPRECATION")
                bindingItem?.switchCompatNotification?.trackDrawable?.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.cardColor
                    ), PorterDuff.Mode.SRC_IN
                )
                //FirebaseMessaging.getInstance().unsubscribeFromTopic("event")


            }
        }



        holder.itemView.setOnClickListener {
            moveData.moveTo(position)
        }
    }

}
