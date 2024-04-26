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
import com.depvt.live.football.tv.databinding.FragmentTeamsListBinding
import com.depvt.live.football.tv.models.GroupTeamModel
import com.depvt.live.football.tv.models.Team
import com.depvt.live.football.tv.ui.app.adapters.TeamListAdapter
import com.depvt.live.football.tv.viewModel.ScoresViewModel

class TeamsListFragment : Fragment() {

    var binding: FragmentTeamsListBinding? = null
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[ScoresViewModel::class.java]
    }

    private var groupTeamListModel: MutableList<GroupTeamModel> =
        ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_teams_list, container, false)
        binding = DataBindingUtil.bind(view)
        binding?.lifecycleOwner = this
        setUpViewModel()

        return binding?.root
    }

    private fun setUpViewModel() {

        groupTeamListModel.clear()

        viewModel.leagueStandingList.observe(viewLifecycleOwner) {

            it?.groups?.map { it1 ->

                val groupTeamList: MutableList<Team> =
                    ArrayList()

                groupTeamList.clear()

                it1.standings.map { it2 ->

                    if (it2.team != null) {

                        groupTeamList.add(it2.team!!)
                    }
                }

                groupTeamListModel.add(GroupTeamModel(it1.name, groupTeamList))

                setGroupAdapter(groupTeamListModel)

            }


        }

    }

    private fun setGroupAdapter(groupTeamListModel: MutableList<GroupTeamModel>) {
        val listAdapter =
            TeamListAdapter(groupTeamListModel)

        binding?.groupRecycler?.layoutManager = LinearLayoutManager(requireContext())
        binding?.groupRecycler?.adapter = listAdapter
    }


}