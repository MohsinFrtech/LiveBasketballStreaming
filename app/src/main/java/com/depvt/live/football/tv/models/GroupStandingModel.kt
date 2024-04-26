package com.depvt.live.football.tv.models

import com.depvt.live.football.tv.utils.objects.Constants

data class GroupStandingModel(
    val groupName: String? = null,var teamsInGroup: List<StandingModel>,
    var type: Int = Constants.STANDINGS_PARENT, var isExpanded: Boolean = false
)