package com.unamad.aulago.dao.studentDao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unamad.aulago.models.database.HomeworkStudentModel
import com.unamad.aulago.models.query.HomeworkStudentQueryModel

@Dao
interface HomeworkStudentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(list: List<HomeworkStudentModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(list: List<HomeworkStudentModel>)

    @Delete
    fun delete(list: List<HomeworkStudentModel>)

    @Query(
        """
        SELECT HW.* FROM HomeworkStudents HW
        INNER JOIN SystemData SY ON SY.currentUserId = HW.studentUserId
    """
    )
    suspend fun list(): List<HomeworkStudentModel>

    /*@Query(
        """
        SELECT 
            HW.homeworkTitle, HW.homeworkDescription, HW.sectionId, show, HW.dateBegin, HW.dateEnd, HW.unitName, HW.attempts, HW.isGroup, HW.usedAttempts,
            S.colorNumber, (C.code || '-' ||S.code) courseSectionCode, C.name courseName
        FROM HomeworkStudents HW
        INNER JOIN Sections S ON S.id = HW.sectionId
        INNER JOIN Courses C ON C.id = S.courseId
        INNER JOIN SystemData SY ON SY.currentUserId = HW.studentUserId
        ORDER BY HW.dateEnd DESC
    """
    )
    fun listStream(): LiveData<List<HomeworkStudentQueryModel>>*/
}
