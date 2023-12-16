package com.unamad.aulago

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unamad.aulago.dao.GeneralDao
import com.unamad.aulago.dao.SystemDao
import com.unamad.aulago.dao.studentDao.StudentDao
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
        SystemModel::class
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)
abstract class SystemDatabase : RoomDatabase() {
    abstract fun generalDao(): GeneralDao
    abstract fun studentDao(): StudentDao
    abstract fun teacherDao(): TeacherDao
    abstract  fun systemDao(): SystemDao

}
