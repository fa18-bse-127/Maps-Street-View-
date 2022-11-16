package com.example.mapsstreetview.DatabaseHandler

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MytrackDao {

    @Insert
    suspend fun insertTrack(track:MytrackEntity)

    @Update
    suspend fun updateTrack(track:MytrackEntity)

    @Query("DELETE FROM savedRouteRecords WHERE id=:recordId ")
    suspend fun deleteTrack(recordId: Int)

/*    @Query("SELECT * from savedRouteRecords ")
     fun getRecord():LiveData<ArrayList<RecordEntity>>*/

    @Query("select * from myTracks")
    fun getTrack():List<MytrackEntity>

    @Query("SELECT * FROM myTracks WHERE id=:trackRecordId")
    fun getTrackbyId(trackRecordId:Int):MytrackEntity

}