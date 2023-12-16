package com.unamad.aulago.dao.studentDao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unamad.aulago.models.database.StudentSectionModel
import com.unamad.aulago.models.query.CourseStudentTeachersQueryModel
import com.unamad.aulago.models.query.StudentSectionQueryModel

@Dao
interface StudentDao {

    //REGION SECTION STUDENT

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStudentSection(studentSectionModel: List<StudentSectionModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateStudentSection(studentSectionModel: List<StudentSectionModel>)

    @Query(
        """
        SELECT 
            SS.sectionId, C.code || '-' || S.code courseSectionCode, C.name courseName,s.colorNumber, S.externalGroupLink, S.representativeStudentUserId,
            U.sex, U.phoneNumber, U.email, (U.paternalSurname || ' ' || U.maternalSurname || ', ' || U.name) studentFullName
        FROM StudentSections SS
        INNER JOIN Sections S ON S.id = SS.sectionId
        INNER JOIN Courses C ON C.id = S.courseId
        INNER JOIN SystemData SY ON SY.currentUserId = SS.studentUserId
        INNER JOIN StudentSections SS2 ON SS2.sectionId = SS.sectionId
        INNER JOIN Users U ON U.id = SS2.studentUserId        
        """
    )
    fun listStudentSectionInfoStream(): LiveData<List<StudentSectionQueryModel>>

    @Delete
    fun deleteStudentSection(studentSectionModel: List<StudentSectionModel>)

    @Query("SELECT * FROM StudentSections SS")
    fun listStudentSection(): List<StudentSectionModel>
    //ENDREGION

    @Query(
        """
        SELECT 
            TS.teacherUserId, TS.isPrincipal, (TU.name || ' ' || TU.paternalSurname || ' ' || TU.maternalSurname) AS teacherFullName,
            TU.email,TU.phoneNumber, TU.sex, c.name courseName, c.code || '-' || s.code curseSectionCode, s.colorNumber, SS.sectionId
        FROM StudentSections SS
        INNER JOIN Sections S ON S.id = SS.sectionId
        INNER JOIN SystemData SD ON SD.currentUserId = SS.studentUserId
        INNER JOIN Terms T ON T.id = S.termId
        INNER JOIN Courses C ON C.id = S.courseId
        LEFT JOIN TeacherSections TS ON TS.sectionId = ss.sectionId
        LEFT JOIN Users TU ON TU.id = TS.teacherUserId
        WHERE T.status = 1"""
    )
    fun listStudentTeacherStream(): LiveData<List<CourseStudentTeachersQueryModel>>

}