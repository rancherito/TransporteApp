package com.unamad.aulago

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.unamad.aulago.dao.CareerDao
import com.unamad.aulago.dao.CourseDao
import com.unamad.aulago.dao.GeneralDao
import com.unamad.aulago.dao.PaymentDao
import com.unamad.aulago.dao.SectionDao
import com.unamad.aulago.dao.SyllabusScheduleDao
import com.unamad.aulago.dao.TermDao
import com.unamad.aulago.dao.UnitsScheduleDao
import com.unamad.aulago.dao.studentDao.HomeworkStudentDao
import com.unamad.aulago.dao.StudentAssistanceDao
import com.unamad.aulago.dao.SystemDao
import com.unamad.aulago.dao.studentDao.StudentAbsenceJustificationDao
import com.unamad.aulago.dao.studentDao.StudentDao
import com.unamad.aulago.dao.studentDao.StudentHomeworkDao
import com.unamad.aulago.dao.teacherDao.SchedulePublishGradesDao
import com.unamad.aulago.dao.teacherDao.TeacherDao
import com.unamad.aulago.models.database.StudentAssistanceModel
import com.unamad.aulago.models.database.BuildingModel
import com.unamad.aulago.models.database.CareerModel
import com.unamad.aulago.models.database.ClassScheduleModel
import com.unamad.aulago.models.database.ClassroomModel
import com.unamad.aulago.models.database.CourseModel
import com.unamad.aulago.models.database.StudentHomeworkModel
import com.unamad.aulago.models.database.HomeworkStudentModel
import com.unamad.aulago.models.database.PaymentModel
import com.unamad.aulago.models.database.RoleUserModel
import com.unamad.aulago.models.database.SchedulePublishGradesModel
import com.unamad.aulago.models.database.SectionModel
import com.unamad.aulago.models.database.StudentAbsenceJustificationModel
import com.unamad.aulago.models.database.StudentSectionModel
import com.unamad.aulago.models.database.SyllabusScheduleModel
import com.unamad.aulago.models.database.SystemDataModel
import com.unamad.aulago.models.database.SystemModel
import com.unamad.aulago.models.database.TeacherClassModel
import com.unamad.aulago.models.database.TeacherClassStudentModel
import com.unamad.aulago.models.database.TeacherSectionModel
import com.unamad.aulago.models.database.TermModel
import com.unamad.aulago.models.database.UnitsScheduleModel
import com.unamad.aulago.models.database.UserModel

@Database(
    entities = [
        UserModel::class,
        SystemDataModel::class,
        RoleUserModel::class,
        TermModel::class,
        StudentSectionModel::class,
        CourseModel::class,
        SectionModel::class,
        TeacherSectionModel::class,
        BuildingModel::class,
        ClassroomModel::class,
        ClassScheduleModel::class,
        TeacherClassModel::class,
        TeacherClassStudentModel::class,
        CareerModel::class,
        HomeworkStudentModel::class,
        PaymentModel::class,
        UnitsScheduleModel::class,
        SyllabusScheduleModel::class,
        SchedulePublishGradesModel::class,
        StudentAssistanceModel::class,
        SystemModel::class,
        StudentAbsenceJustificationModel::class,
        StudentHomeworkModel::class
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)
abstract class SystemDatabase : RoomDatabase() {
    abstract fun generalDao(): GeneralDao
    abstract fun studentDao(): StudentDao
    abstract fun careerDao(): CareerDao
    abstract fun teacherDao(): TeacherDao
    abstract fun courseDao(): CourseDao
    abstract fun sectionDao(): SectionDao
    abstract fun termDao(): TermDao
    abstract fun paymentDao(): PaymentDao
    abstract fun unitsScheduleDao(): UnitsScheduleDao
    abstract fun syllabusScheduleDao(): SyllabusScheduleDao
    abstract fun schedulePublishGradesDao(): SchedulePublishGradesDao
    abstract fun studentAssistanceDao(): StudentAssistanceDao
    abstract  fun systemDao(): SystemDao
    abstract  fun studentAbsenceJustification(): StudentAbsenceJustificationDao
    abstract fun studentHomeworkDao(): StudentHomeworkDao

}
