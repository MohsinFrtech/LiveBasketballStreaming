package com.depvt.live.football.tv.room

import androidx.room.*

@Dao
interface RoomInterface {

    @Query("SELECT * FROM RoomTable")
    fun getAll(): List<RoomTable?>?

    @Insert
    fun insert(task: RoomTable?)

    @Delete
    fun delete(task: RoomTable?)

    @Query("DELETE FROM RoomTable")
    fun deleteAll()

    @Update
    fun update(task: RoomTable?)
}