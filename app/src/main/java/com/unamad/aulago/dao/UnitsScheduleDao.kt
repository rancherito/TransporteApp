package com.unamad.aulago.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unamad.aulago.models.database.UnitsScheduleModel

@Dao
interface UnitsScheduleDao {
    //fun for delete all
    @Query("DELETE FROM UnitsSchedule")
    fun deleteAll()
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(unitsScheduleModel: List<UnitsScheduleModel>)

    @Query("SELECT * FROM UnitsSchedule")
    fun listStream(): LiveData<List<UnitsScheduleModel>>
}