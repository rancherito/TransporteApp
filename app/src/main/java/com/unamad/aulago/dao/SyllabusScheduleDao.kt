package com.unamad.aulago.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.unamad.aulago.models.database.SyllabusScheduleModel

@Dao
interface SyllabusScheduleDao {
    @Query("DELETE FROM SyllabusSchedule")
    suspend fun delete()
    @Insert
    suspend fun insert(syllabusScheduleModel: SyllabusScheduleModel)

    //QUERY UPDATED
    @Query("UPDATE SyllabusSchedule SET startDate = :startDate, endDate = :endDate, typeDescription = :type")
    suspend fun update(startDate: String, endDate: String, type: String)

    //QUERY GET
    @Query("SELECT * FROM SyllabusSchedule LIMIT 1")
    suspend fun get(): SyllabusScheduleModel?
    @Query("SELECT * FROM SyllabusSchedule LIMIT 1")
    fun getStream(): LiveData<SyllabusScheduleModel?>
}