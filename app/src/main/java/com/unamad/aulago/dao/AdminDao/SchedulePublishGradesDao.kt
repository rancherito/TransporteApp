package com.unamad.aulago.dao.AdminDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.unamad.aulago.models.database.SchedulePublishGradesModel

@Dao
interface SchedulePublishGradesDao {
    //delete
    @Query("DELETE FROM schedulePublishGrades")
    suspend fun clear()

    @Delete
    suspend fun delete(schedulePublishGradesModel: List<SchedulePublishGradesModel>)

    //insert
    @Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    suspend fun insert(schedulePublishGradesModel: List<SchedulePublishGradesModel>)

    @Update(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    suspend fun update(schedulePublishGradesModel: List<SchedulePublishGradesModel>)

    //list
    @Query("SELECT * FROM schedulePublishGrades")
    suspend fun list(): List<SchedulePublishGradesModel>

    @Query("SELECT * FROM schedulePublishGrades")
    fun listStream(): LiveData<List<SchedulePublishGradesModel>>

    @Query("SELECT * FROM schedulePublishGrades WHERE sectionId = :sectionId")
    fun listStream(sectionId: String): LiveData<List<SchedulePublishGradesModel>>
}