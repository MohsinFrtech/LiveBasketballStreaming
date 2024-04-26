package com.depvt.live.football.tv.models

import com.depvt.live.football.tv.utils.objects.Constants

data class GroupTeamModel(
    val groupName: String? = null,var teamsInGroup: List<Team>,
    var type: Int = Constants.PARENT, var isExpanded: Boolean = false
)