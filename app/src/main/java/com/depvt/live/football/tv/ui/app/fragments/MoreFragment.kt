package com.depvt.live.football.tv.ui.app.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.depvt.live.football.tv.BuildConfig
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.MoreScreenLayoutBinding
import com.depvt.live.football.tv.models.MoreDataModel
import com.depvt.live.football.tv.ui.app.activities.SubscriptionScreen
import com.depvt.live.football.tv.ui.app.adapters.MoreScreenAdapter
import com.depvt.live.football.tv.utils.Logger
import com.depvt.live.football.tv.utils.interfaces.MoreScreenNavigate
import com.depvt.live.football.tv.utils.objects.SharedPreference

class MoreFragment : Fragment(), MoreScreenNavigate {

    private var preference: SharedPreference? = null
    private var binding: MoreScreenLayoutBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.more_screen_layout, container, false)
        binding = DataBindingUtil.bind(view)
        preference = SharedPreference(requireContext())
        ///Notification enable or disable

        val listMore = ArrayList<MoreDataModel>()
        listMore.add(
            MoreDataModel(
                R.drawable.rate_the_app,
                "Rate us",
                "Rate the app on Google Play Store."
            )
        )
        listMore.add(
            MoreDataModel(
                R.drawable.feedback,
                "Feedback",
                "Let us know if you have any issues."
            )
        )
        listMore.add(
            MoreDataModel(
                R.drawable.notification1,
                "Notifications",
                "Get Alerts for Latest Updates!"
            )
        )
        listMore.add(
            MoreDataModel(
                R.drawable.share,
                "Share App",
                "Share the app with your Friends."
            )
        )
        listMore.add(
            MoreDataModel(
                R.drawable.terms_and_conditions,
                "Terms of Use",
                "Get Alerts for Latest Updates!"
            )
        )
        listMore.add(
            MoreDataModel(
                R.drawable.privacy_policy,
                "Privacy Policy",
                "Don't want Ads?Let Remove them!"
            )
        )
        listMore.add(
            MoreDataModel(
                R.drawable.remove_ads,
                "Remove Ads",
                "Don't want Ads?Let Remove them!"
            )
        )


        setAdapter(listMore)

        binding?.versionTex?.text = "Version " + BuildConfig.VERSION_NAME

        return view

    }

    private fun setAdapter(listMore: ArrayList<MoreDataModel>) {

        val listAdapter = MoreScreenAdapter(requireContext(), this)
        binding?.rvMore?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvMore?.adapter = listAdapter
        listAdapter.submitList(listMore)
    }

    override fun moveTo(id: Int) {
        when (id) {
            0 -> {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + context?.packageName)
                        )
                    )
                } catch (e: ActivityNotFoundException) {

                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + context?.packageName)
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        Log.d("Exception", "" + e.message)
                    }

                }
            }

            1 -> {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, Array(1) { "mailto:android@sportsstream.org" })
                intent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    requireContext().resources.getString(R.string.appName)
                )
                startActivity(Intent.createChooser(intent, "Send Email..."))
            }

            3 -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(
                    Intent.EXTRA_TEXT, "Please download this app for live  streaming.\n" +
                            "https://play.google.com/store/apps/details?id=" + context?.packageName
                )
                intent.type = "text/plain"
                startActivity(intent)
            }

            2 -> {


            }

            4 -> {
                try {
                    val url = "http://sportsstream.org/#privacy"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                } catch (e: ActivityNotFoundException) {
                    Logger().printLog("Exception", "" + e.message)
                }
            }

            5 -> {
                try {
                    val url = "http://sportsstream.org/#privacy"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                } catch (e: ActivityNotFoundException) {
                    Logger().printLog("Exception", "" + e.message)
                }
            }

            6 -> {

                val intent = Intent(requireContext(), SubscriptionScreen::class.java)
                startActivity(intent)
            }
        }
    }


}