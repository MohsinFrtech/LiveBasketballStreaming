package com.depvt.live.football.tv.ui.app.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.NotificationsLayoutBinding
import com.depvt.live.football.tv.room.RoomDatabase
import com.depvt.live.football.tv.room.RoomTable
import com.depvt.live.football.tv.ui.app.adapters.NotificationAdapter
import com.unity3d.scar.adapter.common.Utils.runOnUiThread

///Notification class...
class NotificationFragment : Fragment() {


    var binding: NotificationsLayoutBinding? = null
    private var notificationlist: MutableList<RoomTable> =
        ArrayList()

    ///OnCreateView function.....
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.notifications_layout, container, false)
        binding = DataBindingUtil.bind(view)
        setUpNotificationData(binding)

        ////clear text click listener.....
        binding?.clear?.setOnClickListener { clearAllNotification(binding!!) }
        binding?.backIcon2?.setOnClickListener{
            findNavController().popBackStack()
        }
        return view
    }

    ////Clear all notifications.....
    private fun clearAllNotification(binding: NotificationsLayoutBinding) {
        clearallnotifications(context, binding)
//        context?.let { ClearNotifications(it, binding).execute() }
    }

    private fun clearallnotifications(context: Context?, binding: NotificationsLayoutBinding) {

        Thread {
            // do your stuff
            val databaseclear by lazy { context?.let { RoomDatabase.getDatabase(it) } }
            databaseclear?.wordDao()?.deleteAll()

            runOnUiThread {
                getlistofnotifications(context, binding)
                // do onPostExecute stuff
            }
        }.start()

    }

    ///Setting notification data....
    private fun setUpNotificationData(binding2: NotificationsLayoutBinding?) {
        getlistofnotifications(context, binding2)
//        context?.let { GetList(it, binding2).execute() }
    }


    ///Return list of all notifications and set to adapter.....
    private fun getlistofnotifications(context: Context?, bindinglay: NotificationsLayoutBinding?) {

        Thread {
            // do your stuff
            val database by lazy { context?.let { RoomDatabase.getDatabase(it) } }
            if (notificationlist.isNotEmpty()) {
                notificationlist.clear()
            }

            @Suppress("UNCHECKED_CAST")
            notificationlist = database?.wordDao()?.getAll() as MutableList<RoomTable>
            runOnUiThread {
                if (notificationlist.isNotEmpty()) {
                    bindinglay?.noNotificationAvailable?.visibility = View.GONE
                    bindinglay?.notificationRecycler?.layoutManager = LinearLayoutManager(context)
                    val adapter3 = context?.let { NotificationAdapter(it, notificationlist) }
                    bindinglay?.notificationRecycler?.adapter = adapter3
                } else {
                    bindinglay?.notificationRecycler?.layoutManager = LinearLayoutManager(context)
                    val adapter3 = notificationlist.let {
                        context?.let { it1 ->
                            NotificationAdapter(
                                it1, it
                            )
                        }
                    }
                    bindinglay?.notificationRecycler?.adapter = adapter3
                    bindinglay?.noNotificationAvailable?.visibility = View.VISIBLE
                }
                // do onPostExecute stuff
            }
        }.start()

    }

}