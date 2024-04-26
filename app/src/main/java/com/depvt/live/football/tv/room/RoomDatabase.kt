package com.depvt.live.football.tv.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(RoomTable::class), version = 1, exportSchema = false)
public abstract class RoomDatabase : RoomDatabase() {

    abstract fun wordDao(): RoomInterface

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: com.depvt.live.football.tv.room.RoomDatabase? = null

        fun getDatabase(context: Context): com.depvt.live.football.tv.room.RoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    com.depvt.live.football.tv.room.RoomDatabase::class.java,
                    "word_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


}