package com.unamad.aulago.dao.teacherDao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unamad.aulago.models.database.TeacherSectionModel
import com.unamad.aulago.models.query.AcademicChargeQueryModel
import com.unamad.aulago.models.query.SimpleCourseSectionQueryModel
import com.unamad.aulago.models.query.TeacherSectionInfoQueryModel
import com.unamad.aulago.models.query.TeacherSectionQueryModel
import com.unamad.aulago.models.query.TeacherStudentQueryModel

@Dao
interface TeacherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTeacherSection(teacherSectionModel: List<TeacherSectionModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateTeacherSection(teacherSectionModel: List<TeacherSectionModel>)

    @Query("DELETE FROM TeacherSections")
    fun deleteTeacherSection()

    @Delete
    fun deleteTeacherSection(teacherSectionModel: List<TeacherSectionModel>)

    @Query("SELECT * FROM TeacherSections")
    suspend fun listTeacherSection(): List<TeacherSectionModel>

    @Query(
        """
        SELECT TS.isPrincipal, (U.paternalSurname || ' ' || U.maternalSurname || ', ' || U.name) AS fullName 
        FROM TeacherSections TS
        INNER JOIN Users U ON U.id = TS.teacherUserId
        WHERE TS.sectionId = :sectionId
        """
    )
    suspend fun listTeacherSectionFilter(sectionId: String): List<TeacherSectionQueryModel>

    @Query(
        """
        SELECT (U.paternalSurname || ' ' || U.maternalSurname || ', ' || U.name) fullName, U.email, U.sex, U.phoneNumber , U.userName, U.id userId
        FROM StudentSections SS
        INNER JOIN Sections S ON S.id = SS.sectionId
        INNER JOIN Users U ON U.id = SS.studentUserId
        WHERE SS.sectionId = :sectionId
        ORDER BY fullName
    """
    )
    fun listTeacherStudentsStream(sectionId: String): LiveData<List<TeacherStudentQueryModel>>

    @Query(
        """
        SELECT 
            (U.paternalSurname || ' ' || U.maternalSurname || ', ' || U.name) fullName, u.sex, u.phoneNumber, CA.code careerCode, CA.name careerName,
            s.isDirectedCourse, TS2.isPrincipal, C.name courseName, C.code || '-' || S.code courseSectionCode, S.colorNumber,
            (TS.teacherUserId = TS2.teacherUserId) isMe, TS.sectionId,
            (SELECT COUNT(*) FROM StudentSections SS WHERE SS.sectionId = S.Id) enrolledStudents
        FROM TeacherSections TS
        JOIN Sections S ON S.id = TS.sectionId
        JOIN Courses C ON C.id = S.courseId
        JOIN SystemData SD ON SD.currentUserId == TS.teacherUserId
        JOIN TeacherSections TS2 ON TS2.sectionId = S.id
        JOIN Users U ON U.id = TS2.teacherUserId
        LEFT JOIN Careers CA ON CA.id = C.careerId
        ORDER BY CA.name, C.name
    """
    )
    fun listAcademicChargeStream(): LiveData<List<AcademicChargeQueryModel>>


    @Query(
        """
        SELECT 
            (U.paternalSurname || ' ' || U.maternalSurname || ', ' || U.name) fullName, 
            u.sex, 
            u.phoneNumber, 
            CA.code careerCode, 
            CA.name careerName,
            s.isDirectedCourse, 
            TS.isPrincipal, 
            C.name courseName, 
            S.code sectionCode, 
            C.code courseCode,
            S.colorNumber,
            (TS.teacherUserId = TS.teacherUserId) isMe, 
            TS.sectionId,
            S.externalGroupLink,
            (SELECT COUNT(*) FROM StudentSections SS WHERE SS.sectionId = S.Id) totalEnrolled,
            (
                SELECT COUNT(*) FROM ClassSchedule CS 
                JOIN TeacherClasses TC ON TC.classScheduleId == CS.id
                WHERE CS.sectionId = S.Id AND TC.teacherUserId = TS.teacherUserId
            ) totalClasses            
        FROM TeacherSections TS
        JOIN Sections S ON S.id = TS.sectionId
        JOIN Courses C ON C.id = S.courseId
        JOIN SystemData SD ON SD.currentUserId == TS.teacherUserId
        JOIN Users U ON U.id = TS.teacherUserId
        JOIN Careers CA ON CA.id = C.careerId
        WHERE TS.sectionId = :sectionId
        ORDER BY CA.name, C.name
    """
    )
    fun geStream(sectionId: String): LiveData<TeacherSectionInfoQueryModel?>

    @Query(
        """
        SELECT 
            C.name  courseName, TS.sectionId, S.colorNumber, S.code sectionCode, CA.code careerCode, CA.name careerName, C.code courseCode,
            (
                SELECT  TC.startTime FROM TeacherClasses  TC 
                JOIN ClassSchedule CS ON TC.classScheduleId = CS.id                
                WHERE TC.teacherUserId = SD.currentUserId AND CS.sectionId = S.id  AND TC.startTime >= datetime('now') 
                ORDER BY TC.startTime
                LIMIT 1
            )  nextDatetime
            
        FROM SystemData SD
        JOIN TeacherSections TS ON TS.teacherUserId = SD.currentUserId
        JOIN Sections S ON S.id == TS.sectionId 
        JOIN Courses C ON C.id = S.courseId
        LEFT JOIN Careers CA ON CA.id = C.careerId
    """
    )
    fun listTeacherCoursesStream(): LiveData<List<SimpleCourseSectionQueryModel>>

    @Query(
        """
        SELECT DISTINCT TS.sectionId FROM TeacherSections TS
        INNER JOIN SystemData SD ON SD.currentUserId = TS.teacherUserId
    """
    )
    suspend fun listAcademicCharge(): List<String>
}