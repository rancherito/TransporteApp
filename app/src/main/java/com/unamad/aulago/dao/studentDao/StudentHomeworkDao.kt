package com.unamad.aulago.dao.studentDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.unamad.aulago.models.database.StudentHomeworkModel
import com.unamad.aulago.models.query.StudentHomeworkQueryModel

@Dao
interface StudentHomeworkDao {
    @Query("""
        SELECT SH.*, S.colorNumber FROM studentsHomeworks SH
        INNER JOIN SystemData SY ON SY.currentUserId = SH.studentUserId
        INNER JOIN Sections S ON S.id = SH.sectionId
        """)
    fun listStream(): LiveData<List<StudentHomeworkQueryModel>>

    //simple select
    @Query("""
        SELECT SH.* FROM studentsHomeworks SH
        INNER JOIN SystemData SY ON SY.currentUserId = SH.studentUserId
        """)
    suspend fun list(): List<StudentHomeworkModel>

    //update
    @Update(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    suspend fun update(list: List<StudentHomeworkModel>)

    //insert
    @Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    suspend fun insert(list: List<StudentHomeworkModel>)

    @Delete
    suspend fun delete(list: List<StudentHomeworkModel>)

}