package com.example.mapsstreetview.DatabaseHandler

import android.content.Context
import androidx.room.*

@Database(entities = [RecordEntity::class,MytrackEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class RoomAppDb : RoomDatabase(){

    abstract fun recordDao():RecordDao?
    abstract fun mytrackDao():MytrackDao?

    companion object{
        @Volatile
        private var INSTANCE:RoomAppDb?=null

        fun getAppDatabase(context: Context):RoomAppDb?{
            if (INSTANCE==null){

                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, RoomAppDb::class.java, "AppDb"
                    ).fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE
        }
    }

}