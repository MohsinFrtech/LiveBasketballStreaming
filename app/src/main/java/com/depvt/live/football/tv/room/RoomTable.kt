package com.depvt.live.football.tv.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


////Class used for local database
@Entity
data class RoomTable(

    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "description")
    val desc: String?,

    @ColumnInfo(name = "image_url")
    val image_url: String?,

    @ColumnInfo(name = "url")
    val specific_url: String,

    @PrimaryKey(autoGenerate = true) val id: Int=0


)