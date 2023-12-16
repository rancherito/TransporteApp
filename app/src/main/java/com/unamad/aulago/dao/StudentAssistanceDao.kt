package com.unamad.aulago.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.unamad.aulago.models.database.StudentAssistanceModel
import com.unamad.aulago.models.query.StudentAssistanceQuery

@Dao
interface StudentAssistanceDao {
    @Delete
    suspend fun delete(studentAssistance: List<StudentAssistanceModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(studentSectionModel: List<StudentAssistanceModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(studentSectionModel: List<StudentAssistanceModel>)

    @Query(
        """
        SELECT * FROM StudentAssistance P
    """
    )
    suspend fun list(): List<StudentAssistanceModel>

    @Query(
        """
        SELECT course, total, maxAbsences, dictated, assisted, absences, P.code, C.colorNumber 
        FROM StudentAssistance P
        JOIN Sections C ON P.sectionId = C.id
        JOIN SystemData D ON D.currentUserId = P.userId
    """
    )
    fun listStream(): LiveData<List<StudentAssistanceQuery>>
}
