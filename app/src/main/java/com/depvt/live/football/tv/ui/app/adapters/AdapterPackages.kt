package com.depvt.live.football.tv.ui.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.billing.RecyclerViewClickListener
import com.depvt.live.football.tv.billing.SkuRowData
import com.depvt.live.football.tv.databinding.ItemLayoutPackagesBinding
import com.depvt.live.football.tv.utils.Logger
import com.depvt.live.football.tv.utils.objects.Constants.oldSkuList
import java.util.*

class AdapterPackages(
    private val data: List<SkuRowData>,
    private val clickListener: RecyclerViewClickListener
) : RecyclerView.Adapter<AdapterPackages.ViewHolder>() {

    private var binding: ItemLayoutPackagesBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_packages, parent, false)
        binding = DataBindingUtil.bind(view)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        binding?.tvDesc?.text = data[position].description
        binding?.tvPrice?.text = data[position].price

        if (data[position].title.contains("-")) {
            ///if string contain - the indication of period......
            val index = data[position].title.indexOf("-")
            val subString = data[position].title.substring(index + 1, index + 4)
            if (subString == " 1 ") {
                binding?.period?.text = "$subString Month"
            } else {
                binding?.period?.text = "$subString Months"
            }
        }

        val mainString = data[position].title.lowercase(Locale.getDefault())
        if (mainString.contains("off")) {
            binding?.saveLayout?.visibility = View.VISIBLE
            val index3 = mainString.indexOf("(")
            val index4 = mainString.indexOf("%")
            val subString = mainString.substring(index3 + 1, index4)
            binding?.savePercentage?.text = "Save $subString%"
        } else {
            binding?.saveLayout?.visibility = View.GONE
        }


        if (oldSkuList.isNotEmpty()) {
            for (j in oldSkuList.indices) {
                if (oldSkuList[j].equals(data[position].sku, ignoreCase = true)) {
                    binding?.subBtn?.text = "Subscribed"
                }
            }
        }

        binding?.subBtn?.setOnClickListener {
            clickListener.recyclerViewListClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}