package com.example.mapsstreetview.DatabaseHandler

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecordDao {

    @Insert
    suspend fun insertRecord(record:RecordEntity)



    @Update
    suspend fun updateRecord(record:RecordEntity)

    @Query("DELETE FROM savedRouteRecords WHERE id=:recordId ")
    suspend fun deleteRecord(recordId: Int)

/*    @Query("SELECT * from savedRouteRecords ")
     fun getRecord():LiveData<ArrayList<RecordEntity>>*/

     @Query("select * from savedRouteRecords")
     fun getRecodes():List<RecordEntity>




}