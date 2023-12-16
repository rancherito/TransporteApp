package com.unamad.aulago.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.unamad.aulago.Roles
import com.unamad.aulago.SystemDatabase
import com.unamad.aulago.SystemKeys
import com.unamad.aulago.Utils.Companion.toJSON
import com.unamad.aulago.api.IApiGeneral
import com.unamad.aulago.classes.Validation
import com.unamad.aulago.genericReplacement
import com.unamad.aulago.models.apiModels.StudentProfileApiModel
import com.unamad.aulago.models.apiModels.SyllabusScheduleApiModel
import com.unamad.aulago.models.apiModels.UnitsScheduleApiModel
import com.unamad.aulago.models.apiModels.UserApiModel
import com.unamad.aulago.models.apiModels.WhatsappLinkApiModel
import com.unamad.aulago.models.database.BuildingModel
import com.unamad.aulago.models.database.CareerModel
import com.unamad.aulago.models.database.ClassScheduleModel
import com.unamad.aulago.models.database.ClassroomModel
import com.unamad.aulago.models.database.CourseModel
import com.unamad.aulago.models.database.PaymentModel
import com.unamad.aulago.models.database.RoleUserModel
import com.unamad.aulago.models.database.SectionModel
import com.unamad.aulago.models.database.StudentSectionModel
import com.unamad.aulago.models.database.SyllabusScheduleModel
import com.unamad.aulago.models.database.SystemDataModel
import com.unamad.aulago.models.database.SystemModel
import com.unamad.aulago.models.database.TeacherSectionModel
import com.unamad.aulago.models.database.TermModel
import com.unamad.aulago.models.database.UnitsScheduleModel
import com.unamad.aulago.models.database.UserModel
import com.unamad.aulago.models.query.SessionQueryModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject


open class GeneralRepository @Inject constructor(
    val database: SystemDatabase,
    val api: IApiGeneral,
    private val scope: CoroutineScope,
    @ApplicationContext val context: Context
) {
    val syllabusScheduleStream = database.syllabusScheduleDao().getStream()

    val studentCoursesScheduleStream = database.generalDao().studentCoursesScheduleStream()

    val userSystemDataStream = database.generalDao().userSystemDataStream()

    val classDatesSectionStream = database.generalDao().classDatesSectionStream()

    val paymentsUserStream = database.paymentDao().listStream()

    val activeTermStream = database.termDao().activeTermStream()

    val unitsScheduleStream = database.unitsScheduleDao().listStream()

    val sysStudentInfoStream = database.systemDao().getStream(SystemKeys.STUDENT_INFO.name).map {
        if (it == null) return@map null
        Gson().fromJson(it, StudentProfileApiModel::class.java)
    }


    suspend fun getSystemData(): SystemDataModel {
        val currentUserSystem = database.generalDao().getSystemData()
        if (currentUserSystem == null) {
            val sys = SystemDataModel()
            insertOrUpdateSystemData(sys, "Insert if first call")
            return sys
        }
        return currentUserSystem
    }

    private suspend fun insertSystemUser(user: UserModel, token: String, tokenVerify: Boolean) {
        val checkUser = database.generalDao().getUser(user.id)
        if (checkUser != null) {
            val currentUserSystem = getSystemData()
            currentUserSystem.currentUserId = checkUser.id
            currentUserSystem.token = token
            currentUserSystem.tokenVerify = tokenVerify
            insertOrUpdateSystemData(currentUserSystem, message = "Insert system user")
        }
    }

    suspend fun insertOrUpdateSystemData(systemDataModel: SystemDataModel, message: String) {

        Log.i("system insert", message + " || " + systemDataModel.assistanceSectionId)
        database.generalDao().insertSystemData(systemDataModel)
        database.generalDao().updateSystemData(systemDataModel)

    }

    fun closeSession() {
        database.generalDao().closeSession()
        database.systemDao().deleteAll()
    }

    fun replaceUser(user: List<UserModel>) {
        genericReplacement(
            list = user,
            insertFun = database.generalDao()::insertUser,
            updateFun = database.generalDao()::updateUser
        )
    }

    private fun insertOrUpdateCredentials(roleUserModel: RoleUserModel) {
        //Verificamos que no exista duplicidad de usuarios para poder insertar caso contrario actualizamos datos
        val checkUser = database.generalDao().getUser(roleUserModel.userId)

        if (checkUser != null) {
            val credential = database.generalDao().getCredentialUser(roleUserModel.userId)
            if (credential != null) roleUserModel.id = credential.id
            database.generalDao().insertCredentials(roleUserModel)
        }
    }

    private fun insertOrUpdateTerm(list: List<TermModel>) {
        genericReplacement(
            list = list,
            updateFun = database.termDao()::update,
            insertFun = database.termDao()::insert
        )

    }

    fun replaceStudentSection(
        studentSectionModel: List<StudentSectionModel>,
        notDelete: Boolean = false
    ) {
        if (notDelete) {
            genericReplacement(
                list = studentSectionModel,
                insertFun = database.studentDao()::insertStudentSection,
                updateFun = database.studentDao()::updateStudentSection
            )
        } else
            genericReplacement(
                list = studentSectionModel,
                oldList = database.studentDao().listStudentSection(),
                deleteFun = database.studentDao()::deleteStudentSection,
                insertFun = database.studentDao()::insertStudentSection,
                updateFun = database.studentDao()::updateStudentSection
            )
    }

    fun listCurrentStudentSection(): List<StudentSectionModel> {
        return database.studentDao().listStudentSection()
    }

    fun replaceCourse(courseModel: List<CourseModel>) {
        genericReplacement(
            list = courseModel,
            oldList = database.courseDao().list(),
            deleteFun = database.courseDao()::delete,
            updateFun = database.courseDao()::update,
            insertFun = database.courseDao()::insert
        )
    }

    fun replaceSection(section: List<SectionModel>) {
        genericReplacement(
            list = section,
            oldList = database.sectionDao().list(),
            insertFun = database.sectionDao()::insert,
            updateFun = database.sectionDao()::update,
            deleteFun = database.sectionDao()::delete
        )
    }

    private fun insertOrUpdateBuilding(buildingModel: List<BuildingModel>) {
        genericReplacement(
            list = buildingModel,
            updateFun = database.generalDao()::updateBuilding,
            insertFun = database.generalDao()::insertBuilding
        )
    }

    private fun insertOrUpdateClassroom(classroomModel: List<ClassroomModel>) {
        genericReplacement(
            list = classroomModel,
            updateFun = database.generalDao()::updateClassroom,
            insertFun = database.generalDao()::insertClassroom
        )
    }

    private fun replaceSchedule(classScheduleModel: List<ClassScheduleModel>) {

        genericReplacement(
            list = classScheduleModel,
            updateFun = database.generalDao()::updateSchedule,
            insertFun = database.generalDao()::insertSchedule,
            deleteFun = database.generalDao()::deleteSchedule,
            oldList = database.generalDao().listSchedule()
        )
    }

    suspend fun getSessionData(): SessionQueryModel? {
        return database.generalDao().getUserSystemData()
    }


    suspend fun loadStudentSection(
        session: SessionQueryModel,
        sections: List<String>
    ): Validation {
        try {
            val localDate = LocalDateTime.now()
            val studentSections = mutableListOf<StudentSectionModel>()
            val users = mutableListOf<UserModel>()
            sections.forEach { section ->
                val response = api.getStudentSection(section, token = session.token)
                if (!response.isSuccess)
                    return Validation(false, "Estudiantes: " + response.message)

                val data = response.data ?: listOf()
                data.forEach { student ->
                    studentSections.add(
                        StudentSectionModel(
                            id = student.studentSection,
                            sectionId = section,
                            studentUserId = student.userId
                        )
                    )
                    users.add(
                        UserModel(
                            id = student.userId,
                            name = student.name,
                            paternalSurname = student.paternalSurname,
                            maternalSurname = student.maternalSurname,
                            sex = student.sex,
                            email = student.email,
                            phoneNumber = student.phoneNumber?.replace(" ", ""),
                            modifyAt = localDate.toString(),
                            userName = student.userName
                        )
                    )
                }
            }
            replaceUser(users.distinct())
            replaceStudentSection(studentSections.distinct())

            val system = getSystemData()
            system.companionsLastDataCached = localDate.toString()
            insertOrUpdateSystemData(system, "companions last data cached")
            return Validation(true)
        } catch (e: Exception) {
            return Validation(false, "Estudiantes: " + e.message)
        }
    }


    fun replacePayments(payments: List<PaymentModel>) {
        /* val listDelete = database.generalDao().listPaymentUser().filter { old ->
             payments.find { current -> old.id == current.id } == null
         }
         database.generalDao().deletePayments(listDelete)
         database.generalDao().insertPayments(payments)
         database.generalDao().updatePayments(payments)*/

        Log.i("PAYMENT", payments.size.toString())
        genericReplacement(
            list = payments,
            oldList = database.paymentDao().list(),
            deleteFun = database.paymentDao()::delete,
            updateFun = database.paymentDao()::update,
            insertFun = database.paymentDao()::insert
        )
    }

    suspend fun loadSchedule(
        session: SessionQueryModel
    ): Validation {
        try {
            val localDate = LocalDateTime.now()
            val response = if (session.role == Roles.Student) api.getStudentSchedule(
                session.userId,
                session.termId,
                token = session.token
            ) else api.getTeacherSchedule(
                session.userId,
                session.termId,
                token = session.token
            )

            if (!response.isSuccess)
                return Validation(false, "Hoario: " + response.message)

            val data = response.data ?: listOf()

            val buildings = mutableListOf<BuildingModel>()
            val classrooms = mutableListOf<ClassroomModel>()
            val scheduleUser = mutableListOf<ClassScheduleModel>()


            data.forEach { schedule ->
                buildings.add(
                    BuildingModel(
                        id = schedule.buildingId,
                        name = schedule.buildingName,
                        floors = schedule.buildingFloors?.toIntOrNull() ?: 0
                    )
                )
                classrooms.add(
                    ClassroomModel(
                        id = schedule.classroomId,
                        name = schedule.classroomCode,
                        buildingId = schedule.buildingId,
                        typeName = schedule.classroomTypeName,
                        floor = schedule.floor?.toIntOrNull() ?: 0
                    )
                )

                scheduleUser.add(
                    ClassScheduleModel(
                        startTime = utcTimeToLocal(LocalTime.parse(schedule.startTime)),
                        endTime = utcTimeToLocal(LocalTime.parse(schedule.endTime)),
                        sectionId = schedule.sectionId,
                        classroomId = schedule.classroomId,
                        weekDay = schedule.weekDay,
                        id = schedule.classScheduleId
                    )
                )
            }
            insertOrUpdateBuilding(buildings.distinct())
            insertOrUpdateClassroom(classrooms.distinct())
            replaceSchedule(scheduleUser.distinct())

            val system = getSystemData()
            system.scheduleLastDataCached = localDate.toString()
            insertOrUpdateSystemData(system, "Schedule last data cached")
            return Validation(true)

        } catch (e: Exception) {
            return Validation(false, "Hoario: " + e.message)
        }
    }

    private fun utcTimeToLocal(time: LocalTime): Int {
        //TODO: Fix carrier week value
        val gtm = TimeZone.getDefault().rawOffset / (1000 * 60 * 60)
        val hour = (time.hour + gtm) % 24
        return hour + if (hour < 0) 24 else 0
    }


    suspend fun loadTerm() {
        val localDate = LocalDateTime.now()
        val session = getSystemData()
        val response = api.getTerms()

        if (!response.isSuccess) throw Exception("Semestres: " + response.message)

        val data = response.data ?: listOf()
        insertOrUpdateTerm(data.map { item ->
            TermModel(
                id = item.id,
                name = item.name,
                createdAt = item.createdAt,
                isSummer = item.isSummer,
                number = item.number,
                year = item.year,
                status = item.status
            )
        })
        session.termLastDataCached = localDate.toString()
        insertOrUpdateSystemData(session, "term Last Data Cached repo")

    }

    suspend fun replaceTeacherSection(teacherSectionModel: List<TeacherSectionModel>) {
        genericReplacement(
            list = teacherSectionModel,
            oldList = database.teacherDao().listTeacherSection(),
            deleteFun = database.teacherDao()::deleteTeacherSection,
            updateFun = database.teacherDao()::updateTeacherSection,
            insertFun = database.teacherDao()::insertTeacherSection
        )
    }

    fun replaceCareer(careers: List<CareerModel>) {
        genericReplacement(
            list = careers,
            updateFun = database.careerDao()::update,
            insertFun = database.careerDao()::insert

        )
    }

    suspend fun validateCredentials(userApiModel: UserApiModel) {
        val user = UserModel(
            id = userApiModel.id,
            name = userApiModel.name,
            paternalSurname = userApiModel.paternalSurname,
            maternalSurname = userApiModel.maternalSurname,
            sex = userApiModel.sex,
            email = userApiModel.email,
            phoneNumber = userApiModel.phoneNumber?.replace(" ", ""),
            modifyAt = LocalDateTime.now().toString()

        )
        replaceUser(listOf(user))

        insertOrUpdateCredentials(
            RoleUserModel(
                userId = userApiModel.id,
                role = userApiModel.role
            )
        )
        insertSystemUser(user, "Bearer " + userApiModel.token, tokenVerify = true)
    }

    private fun insertUnitsSchedule(listUnitsSchedule: MutableList<UnitsScheduleModel>) {
        database.unitsScheduleDao().deleteAll()
        database.unitsScheduleDao().insert(listUnitsSchedule)
    }

    fun loadGeneralSchedule() {

        scope.launch {
            try {
                processGeneralSchedule()
            } catch (e: Exception) {
                Log.i("GENERAL SCHEDULE", e.message.toString())
            }
        }

    }


    private fun processUnitsSchedule(list: List<UnitsScheduleApiModel>) {
        val listUnitsSchedule = mutableListOf<UnitsScheduleModel>()
        list.forEach { unitsScheduleApiModel ->
            //print json
            val data = unitsScheduleApiModel.data
            Log.i("UNITS SCHEDULE", unitsScheduleApiModel.toJSON())
            listUnitsSchedule.add(
                UnitsScheduleModel(
                    UUID.randomUUID().toString(),
                    unitsScheduleApiModel.componentId,
                    unitsScheduleApiModel.component,
                    unitsScheduleApiModel.numberUnit,
                    data?.startDate,
                    data?.endDate
                )
            )
        }
        insertUnitsSchedule(listUnitsSchedule)
    }

    private suspend fun processSyllabus(syllabus: SyllabusScheduleApiModel?) {
        val get = database.syllabusScheduleDao().get()
        if (get == null) {
            if (syllabus != null) {
                val syllabusScheduleModel = SyllabusScheduleModel(
                    endDate = syllabus.endDate,
                    startDate = syllabus.startDate,
                    typeDescription = syllabus.typeDescription
                )
                database.syllabusScheduleDao().insert(syllabusScheduleModel)
                return
            }
        } else {
            if (syllabus == null) {
                database.syllabusScheduleDao().delete()
                return
            }
        }
    }

    private suspend fun processGeneralSchedule() {
        val session =
            getSessionData() ?: throw Exception("No se ha iniciado sesión")

        val token = session.token
        val syllabusScheduleApiModel = api.geGeneralSchedule(token)

        val syllabus = syllabusScheduleApiModel.syllabus
        val units = syllabusScheduleApiModel.units

        processUnitsSchedule(units)
        processSyllabus(syllabus)
    }

    suspend fun saveWhatsappLink(sectionId: String, link: String, session: SessionQueryModel) {

        val token = session.token

        val whatsapp = WhatsappLinkApiModel(sectionId, link)

        val response = api.saveWhatsappLink(token, whatsapp)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
        }

    }

    fun loadStudentProfile() {
        scope.launch {
            try {
                val session = getSessionData() ?: return@launch
                val response = api.getStudentProfile(session.token)

                val type = SystemKeys.STUDENT_INFO.name
                val json = response.toJSON()
                database.systemDao().insert(SystemModel(type, json))

            } catch (e: Exception) {
                Log.i("STUDENT PROFILE", e.message.toString())
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


}