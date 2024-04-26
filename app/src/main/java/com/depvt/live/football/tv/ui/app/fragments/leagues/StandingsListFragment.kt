package com.depvt.live.football.tv.ui.app.fragments.leagues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.FragmentStandingsListBinding
import com.depvt.live.football.tv.models.GroupStandingModel
import com.depvt.live.football.tv.models.StandingModel
import com.depvt.live.football.tv.ui.app.adapters.StandingsListNewAdapter
import com.depvt.live.football.tv.viewModel.ScoresViewModel

class StandingsListFragment : Fragment() {


    private val modelStanding by lazy {
        ViewModelProvider(requireActivity())[ScoresViewModel::class.java]
    }
    private var diff = 0


    private var groupStandingList: MutableList<GroupStandingModel> = ArrayList()

    var binding: FragmentStandingsListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_standings_list, container, false)
        binding = DataBindingUtil.bind(view)
        binding?.lifecycleOwner = this
        setUpStandingData()


        return binding?.root
    }

    private fun setUpStandingData() {

        groupStandingList.clear()

        modelStanding.leagueStandingList.observe(viewLifecycleOwner) {

            it?.groups?.map { it1 ->

                val standingList: MutableList<StandingModel> =
                    ArrayList()

                standingList.clear()

                it1.standings.map { it2 ->

                    if (it2.games?.played != null) {
                        if (it2.games?.played!! > 0) {

                            diff = it2.games?.played!! - it2.games?.win?.total!!
                        }
                    }



                    standingList.add(
                        StandingModel(
                            it2.position,
                            it2.team?.logo,
                            it2.team?.name,
                            it2.games?.played,
                            it2.games?.win?.total,
                            it2.games?.lose?.total,
                            diff,
                            it2.games?.win?.percentage
                        )
                    )

                }

                groupStandingList.add(GroupStandingModel(it1.name, standingList))


            }

            setStandingAdapter(groupStandingList)

        }
    }

    private fun setStandingAdapter(standingList: MutableList<GroupStandingModel>) {


        val listAdapter = StandingsListNewAdapter(standingList)
        binding?.standingRecyclerList?.layoutManager = LinearLayoutManager(requireContext())
        binding?.standingRecyclerList?.adapter = listAdapter
        //listAdapter.submitList(standingList)


    }

}