package com.unamad.aulago.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unamad.aulago.models.database.*
import com.unamad.aulago.models.query.*

@Dao
interface GeneralDao {

    //REGION USER QUERIES
    @Query("SELECT * FROM Users U WHERE U.id = :userId")
    fun getUser(userId: String): UserModel?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: List<UserModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateUser(user: List<UserModel>)
    //ENDREGION

    //REGION SYSTEM QUERIES
    @Query(
        """ 
        SELECT 
            (u.paternalSurname || ' ' || u.maternalSurname || ', ' || U.name ) fullName, 
            U.name,
            U.sex,
            s.token,
            (s.tokenVerify IS NOT NULL) tokenVerify, 
            c.role, c.userId, 
            (SELECT tt.name FROM terms tt WHERE tt.status = 1 LIMIT 1) termName
        FROM SystemData s 
        INNER JOIN Users u ON u.id = s.currentUserId 
        INNER JOIN credentials c ON c.userId = u.id
        LIMIT 1"""
    )
    fun userSystemDataStream(): LiveData<CurrentSessionQueryModel?>

    @Query(
        """ 
        SELECT (U.paternalSurname || ' ' || U.maternalSurname || ', ' || U.name) fullName, s.token, c.role, c.userId, T.id termId 
        FROM SystemData s 
        INNER JOIN Users u ON u.id = s.currentUserId
        INNER JOIN credentials c ON c.userId = u.id
        CROSS JOIN TERMS T
        WHERE T.status = 1
        LIMIT 1"""
    )
    suspend fun getUserSystemData(): SessionQueryModel?

    @Query("SELECT * FROM SystemData S LIMIT 1")
    fun getSystemData(): SystemDataModel?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSystemData(systemData: SystemDataModel)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateSystemData(systemData: SystemDataModel)

    @Query("DELETE FROM SystemData")
    fun closeSession()
    //ENDREGION

    //REGION CREDENTIALS
    @Query("SELECT * FROM  credentials WHERE userId = :userId")
    fun getCredentialUser(userId: String): RoleUserModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCredentials(credential: RoleUserModel)

    //ENDREGION


    //ENDREGION

    //REGION BUILDING
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBuilding(buildingModel: List<BuildingModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateBuilding(buildingModel: List<BuildingModel>)
    //ENDREGION

    //REGION CLASSROOM
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertClassroom(classroomModel: List<ClassroomModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateClassroom(classroomModel: List<ClassroomModel>)
    //ENDREGION

    //REGION CLASSROOM
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSchedule(classScheduleModel: List<ClassScheduleModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateSchedule(classScheduleModel: List<ClassScheduleModel>)

    @Query("SELECT * FROM ClassSchedule")
    fun listSchedule(): List<ClassScheduleModel>

    @Delete
    fun deleteSchedule(list: List<ClassScheduleModel>)

    @Query(
        """
        SELECT 
            C.name courseName, SE.code sectionCode, S.startTime, S.endTime, s.weekDay, B.name buildingName, 
            CR.name classroomName, CR.typeName, CR.floor, SE.colorNumber, C.code courseCode, R.role, SS.sectionId, CA.code careerCode, CA.name careerName
        FROM StudentSections SS
        INNER JOIN  SystemData SD ON SD.currentUserId = SS.studentUserId
        INNER JOIN Sections SE ON SE.id = SS.sectionId        
        INNER JOIN Courses C ON C.id = SE.courseId
        INNER JOIN Credentials R ON R.userId = SD.currentUserId
        LEFT JOIN Careers CA ON CA.id = C.careerId
        LEFT JOIN ClassSchedule S ON S.sectionId = SE.id
        LEFT JOIN Classrooms CR ON CR.id = S.classroomId
        LEFT JOIN Buildings B ON B.id = CR.buildingId
        """
    )
    fun studentCoursesScheduleStream(): LiveData<List<ScheduleCourseQueryModel>>

    @Query(
        """
        SELECT 
            C.name courseName, SE.code sectionCode, S.startTime, S.endTime, s.weekDay, B.name buildingName, 
            CR.name classroomName, CR.typeName, CR.floor, SE.colorNumber, C.code courseCode, R.role, TS.sectionId, CA.code careerCode, CA.name careerName
        FROM TeacherSections TS
        INNER JOIN  SystemData SD ON SD.currentUserId = TS.teacherUserId
        INNER JOIN Sections SE ON SE.id = TS.sectionId        
        INNER JOIN Courses C ON C.id = SE.courseId
        INNER JOIN Credentials R ON R.userId = SD.currentUserId
        LEFT JOIN Careers CA ON CA.id = C.careerId
        LEFT JOIN ClassSchedule S ON S.sectionId = SE.id
        LEFT JOIN Classrooms CR ON CR.id = S.classroomId
        LEFT JOIN Buildings B ON B.id = CR.buildingId
        """
    )
    fun listTeacherCoursesSchedule(): LiveData<List<ScheduleCourseQueryModel>>
    //ENDREGION

    //REGION TEACHER CLASS
    @Query("SELECT * FROM TeacherClasses")
    suspend fun listTeacherClass(): List<TeacherClassModel>

    @Delete
    fun deleteTeacherClass(listForDeleted: List<TeacherClassModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTeacherClass(teacherClasses: List<TeacherClassModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateTeacherClass(teacherClasses: List<TeacherClassModel>)

    @Query(
        """
        SELECT TCS.* FROM TeacherClasses TCS
        INNER JOIN ClassSchedule CS ON CS.id = TCS.classScheduleId
        INNER JOIN SystemData SD ON SD.currentUserId = TCS.teacherUserId AND SD.assistanceSectionId = CS.sectionId
        ORDER BY TCS.startTime
        """
    )
    fun classDatesSectionStream(): LiveData<List<TeacherClassModel>>
    //ENDREGION

    @Query(
        """
        DELETE FROM TeacherClassStudent WHERE teacherClassId = :classId
        """
    )
    suspend fun deleteTeacherStudentClass(classId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTeacherStudentClass(list: List<TeacherClassStudentModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTeacherStudentClass(list: List<TeacherClassStudentModel>)

    @Delete
    suspend fun deleteTeacherStudentClass(list: List<TeacherClassStudentModel>)

}
