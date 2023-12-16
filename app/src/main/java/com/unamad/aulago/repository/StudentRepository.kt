package com.unamad.aulago.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.MutableLiveData
import com.unamad.aulago.MaterialColors
import com.unamad.aulago.SystemDatabase
import com.unamad.aulago.Utils.Companion.toJSON
import com.unamad.aulago.api.IApiGeneral
import com.unamad.aulago.classes.Validation
import com.unamad.aulago.genericReplacement
import com.unamad.aulago.genericReplacement2
import com.unamad.aulago.models.database.StudentAssistanceModel
import com.unamad.aulago.models.database.CareerModel
import com.unamad.aulago.models.database.CourseModel
import com.unamad.aulago.models.database.HomeworkStudentModel
import com.unamad.aulago.models.database.PaymentModel
import com.unamad.aulago.models.database.SectionModel
import com.unamad.aulago.models.database.StudentAbsenceJustificationModel
import com.unamad.aulago.models.database.StudentHomeworkModel
import com.unamad.aulago.models.database.StudentSectionModel
import com.unamad.aulago.models.database.TeacherSectionModel
import com.unamad.aulago.models.database.UserModel
import com.unamad.aulago.models.query.SessionQueryModel
import com.unamad.aulago.models.query.TeacherSectionQueryModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class StudentRepository @Inject constructor(
    private val database: SystemDatabase,
    private val api: IApiGeneral,
    private val generalRepository: GeneralRepository,
    private val scope: CoroutineScope,
    @ApplicationContext private val context: Context

) {
    val studentAssistanceStream = database.studentAssistanceDao().listStream()
    val listHomeworkStudentStream =
        database.studentHomeworkDao().listStream()
    val listStudentTeacherStream = database.studentDao().listStudentTeacherStream()
    val listStudentSectionInfoStream =
        database.studentDao().listStudentSectionInfoStream()

    val listStatusRepresentativeStream = database.sectionDao().listStatusRepresentativeStream()
    val isLoadingAssistance = MutableLiveData(false)
    val isLoadingAbsenceJustification = MutableLiveData(false)

    val listStudentAbsenceJustificationStream = database.studentAbsenceJustification().listStream()
    private suspend fun loadStudentCompanions(session: SessionQueryModel): Validation {
        val sections =
            generalRepository.listCurrentStudentSection().map { it.sectionId }.distinct()
        return generalRepository.loadStudentSection(session = session, sections = sections)
    }


    //CREATE TRANSACTIONS WITH DATABASE

    suspend fun loadDebtSync(session: SessionQueryModel) {

        val data = api.getDebt(token = session.token, userId = session.userId)
        if (!data.isSuccess) throw Exception(data.message)

        val debs = data.data ?: listOf()
        val paymentList = mutableListOf<PaymentModel>()
        debs.forEach {
            paymentList.add(
                PaymentModel(
                    id = it.id,
                    termName = it.termName,
                    description = it.description,
                    userId = session.userId,
                    status = it.status,
                    createdBy = it.createdBy,
                    isBankPayment = it.isBankPayment,
                    issueDate = it.issueDate,
                    paymentDate = it.paymentDate,
                    total = it.total,
                    conceptCode = it.conceptCode
                )
            )
        }
        Log.i("payment", paymentList.size.toString())
        generalRepository.replacePayments(paymentList)
    }

    //LOADING DATA FOR DATABASE

    suspend fun loadDependency(session: SessionQueryModel) {

        loadStudentCourseSectionSync(session)

        val loadSchedule = generalRepository.loadSchedule(session = session)
        if (!loadSchedule.isValid) throw Exception(loadSchedule.message)

        val dataCompanions = loadStudentCompanions(session)
        if (!dataCompanions.isValid) throw Exception(dataCompanions.message)

        loadHomeworkSync(session)
        loadDebtSync(session)

    }

    suspend fun loadHomeworkSync(session: SessionQueryModel): List<StudentHomeworkModel> {

        val testResponse = api.getStudentHomeworkV2(token = session.token)

        val list = testResponse.map {
            StudentHomeworkModel(
                id = it.id,
                contentId = it.contentId,
                sectionId = it.sectionId,
                homeworkTitle = it.homeworkTitle,
                homeworkDescription = it.homeworkDescription,
                show = it.show,
                dateBegin = it.dateBegin,
                dateEnd = it.dateEnd,
                studentUserId = session.userId,
                attempts = it.attempts,
                isGroup = it.isGroup,
                unitName = it.unitName,
                usedAttempts = it.usedAttempts,
                homeworkId = it.homeworkId,
                courseName = it.courseName,
                sectionCode = it.sectionCode
            )
        }


        val result = genericReplacement2(
            parameters = arrayOf(
                StudentHomeworkModel::id.name,
                StudentHomeworkModel::sectionId.name
            ),
            insertList = list,
            oldList = database.studentHomeworkDao().list(),
            updateFun = database.studentHomeworkDao()::update,
            insertFun = database.studentHomeworkDao()::insert,
            deleteFun = database.studentHomeworkDao()::delete
        )
        /*
                val response =
                    api.getStudentHomework(
                        userId = session.userId,
                        termId = session.termId,
                        token = session.token
                    )
                if (!response.isSuccess) throw Exception(response.message)

                val data = response.data ?: listOf()

                val save = data.map {
                    HomeworkStudentModel(
                        id = it.homeworkId,
                        contentId = it.contentId,
                        sectionId = it.sectionId,
                        homeworkTitle = it.homeworkTitle,
                        homeworkDescription = it.homeworkDescription,
                        show = it.show,
                        dateBegin = it.dateBegin,
                        dateEnd = it.dateEnd,
                        studentUserId = session.userId,
                        attempts = it.attempts,
                        isGroup = it.isGroup,
                        unitName = it.unitName,
                        usedAttempts = it.usedAttempts,

                    )
                }*/
        Log.i("homework", result.size.toString())

        return result

    }

    suspend fun listTeacherSectionFilter(sectionId: String): List<TeacherSectionQueryModel> {
        return database.teacherDao().listTeacherSectionFilter(sectionId)
    }


    private suspend fun loadAssistanceStudentSync(session: SessionQueryModel) {
        val response = api.getStudentAssistance(token = session.token)

        val studentAssistance = mutableListOf<StudentAssistanceModel>()
        response.forEach {
            Log.i("response", it.toJSON())
            studentAssistance.add(
                StudentAssistanceModel(
                    sectionId = it.sectionId,
                    course = it.course,
                    total = it.total,
                    maxAbsences = it.maxAbsences,
                    dictated = it.dictated,
                    assisted = it.assisted,
                    absences = it.absences,
                    termId = it.termId,
                    code = it.code,
                    userId = session.userId,
                    id = UUID.randomUUID().toString()
                )
            )
        }

        genericReplacement2(
            parameters = arrayOf(StudentAssistanceModel::sectionId.name),
            insertList = studentAssistance,
            oldList = database.studentAssistanceDao().list(),
            deleteFun = database.studentAssistanceDao()::delete,
            updateFun = database.studentAssistanceDao()::update,
            insertFun = database.studentAssistanceDao()::insert
        )
    }

    fun loadAssistanceStudentAsync() {
        scope.launch {
            isLoadingAssistance.postValue(true)
            try {
                val session = database.generalDao().getUserSystemData() ?: return@launch
                loadAssistanceStudentSync(session)
            } catch (e: Exception) {
                val message = e.message.toString()
                Log.i("error", message)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Asistencia $message", Toast.LENGTH_SHORT).show()
                }
            }
            isLoadingAssistance.postValue(false)
        }
    }


    private suspend fun loadStudentCourseSectionSync(session: SessionQueryModel) {
        val localDate = LocalDateTime.now()

        val response = api.getStudentCourseSection(session.termId, token = session.token)
        val users = mutableListOf<UserModel>()
        val sections = mutableListOf<SectionModel>()

        val studentSection = mutableListOf<StudentSectionModel>()
        val courses = mutableListOf<CourseModel>()
        val careers = mutableListOf<CareerModel>()
        val teacherSection = mutableListOf<TeacherSectionModel>()


        response.forEachIndexed { index, section ->
            Log.i("response", section.toJSON())

            courses.add(
                CourseModel(
                    id = section.courseId,
                    name = section.courseName,
                    code = section.courseCode,
                    modifyAt = localDate.toString(),
                    academicYear = section.academicYearCourse,
                    isElective = section.isElective,
                    colorNumber = MaterialColors[index].toArgb().toLong(),
                    careerId = section.careerId
                )
            )
            careers.add(
                CareerModel(
                    id = section.careerId,
                    code = section.careerCode,
                    name = section.careerName
                )
            )
            sections.add(
                SectionModel(
                    id = section.sectionId,
                    code = section.sectionCode,
                    courseId = section.courseId,
                    termId = session.termId,
                    modifyAt = localDate.toString(),
                    isDirectedCourse = section.isDirectedCourse,
                    vacancies = section.vacancies,
                    colorNumber = MaterialColors[index].toArgb().toLong(),
                    representativeStudentUserId = section.representativeStudentUserId,
                    externalGroupLink = section.externalGroupLink
                )
            )
            section.teachers.forEach { teacher ->
                users.add(
                    UserModel(
                        id = teacher.teacherId,
                        name = teacher.name,
                        paternalSurname = teacher.paternalSurname,
                        maternalSurname = teacher.maternalSurname,
                        sex = teacher.sex,
                        email = teacher.email,
                        phoneNumber = teacher.phoneNumber.replace(" ", ""),
                        modifyAt = localDate.toString()
                    )
                )
                teacherSection.add(
                    TeacherSectionModel(
                        id = teacher.teacherSectionId,
                        sectionId = section.sectionId,
                        teacherUserId = teacher.teacherId,
                        isPrincipal = teacher.isPrincipal
                    )
                )
            }
            studentSection.add(
                StudentSectionModel(
                    id = section.studentSectionId,
                    studentUserId = session.userId,
                    sectionId = section.sectionId
                )
            )
        }
        generalRepository.replaceCareer(careers.distinct())
        generalRepository.replaceCourse(courses.distinct())
        generalRepository.replaceUser(users.distinct())
        generalRepository.replaceSection(sections.distinct())

        generalRepository.replaceStudentSection(studentSection.distinct(), notDelete = true)
        generalRepository.replaceTeacherSection(teacherSection.distinct())

        val system = generalRepository.getSystemData()
        system.teachersLastDataCached = localDate.toString()
        generalRepository.insertOrUpdateSystemData(system, "teachers Last Data Cached")

    }

    fun saveWhatsappLinkAsync(sectionId: String, link: String) {
        scope.launch {
            try {
                val session = database.generalDao().getUserSystemData() ?: return@launch
                generalRepository.saveWhatsappLink(
                    sectionId = sectionId,
                    link = link,
                    session = session
                )
                //loadAcademicCharge(session = session)
                //TODO: CREAR UNA FUNCION QUE ACTUALICE LA INFORMACION DE LA SECCION CON EL LINK DE WHATSAPP

                loadStudentCourseSectionSync(session)
            } catch (e: Exception) {
                val message = e.message.toString()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun loadAbsencesJustificationsAsync() {
        scope.launch {
            try {
                val session = database.generalDao().getUserSystemData() ?: return@launch

                isLoadingAbsenceJustification.postValue(true)
                val list = api.listStudentAbsenceJustification(token = session.token)

                val data = list.map {
                    StudentAbsenceJustificationModel(
                        id = it.studentAbsenceJustificationsId,
                        sectionId = it.sectionId,
                        date = it.date,
                        status = it.status,
                        code = it.code,
                        classDate = it.classDate,
                        course = it.course,
                        description = it.description,
                        file = it.file,
                        name = it.name,
                        observation = it.observation,
                        termId = it.termId
                    )
                }


                genericReplacement2(
                    parameters = arrayOf(
                        StudentAbsenceJustificationModel::id.name
                    ),
                    insertList = data,
                    oldList = database.studentAbsenceJustification().list(),
                    deleteFun = database.studentAbsenceJustification()::delete,
                    updateFun = database.studentAbsenceJustification()::update,
                    insertFun = database.studentAbsenceJustification()::insert
                )
                Log.i("list asis", list.toJSON())
                isLoadingAbsenceJustification.postValue(false)
            } catch (e: Exception) {
                val message = e.message.toString()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Justificaciones $message", Toast.LENGTH_SHORT).show()
                }
                isLoadingAbsenceJustification.postValue(false)
            }
        }
    }

}