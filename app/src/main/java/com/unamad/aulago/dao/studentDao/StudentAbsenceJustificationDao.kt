package com.unamad.aulago.dao.studentDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.unamad.aulago.models.database.StudentAbsenceJustificationModel
@Dao
interface StudentAbsenceJustificationDao {
    @Delete
    suspend fun delete(studentAbsenceJustification: List<StudentAbsenceJustificationModel>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(studentAbsenceJustification: List<StudentAbsenceJustificationModel>)
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(studentAbsenceJustification: List<StudentAbsenceJustificationModel>)
    @Query(
        """
        SELECT * FROM StudentAbsenceJustification P
    """
    )
    suspend fun list(): List<StudentAbsenceJustificationModel>

    @Query(
        """
        SELECT P.* FROM StudentAbsenceJustification P
        JOIN Terms T ON T.id = P.termId
        WHERE T.status = 1
    """
    )
    fun listStream(): LiveData<List<StudentAbsenceJustificationModel>>
}
