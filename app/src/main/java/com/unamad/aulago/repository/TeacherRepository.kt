package com.unamad.aulago.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.MutableLiveData
import com.unamad.aulago.MaterialColors
import com.unamad.aulago.SystemDatabase
import com.unamad.aulago.Utils
import com.unamad.aulago.api.IApiGeneral
import com.unamad.aulago.classes.Validation
import com.unamad.aulago.classes.ValidationData
import com.unamad.aulago.emums.IsLoadData
import com.unamad.aulago.genericReplacement
import com.unamad.aulago.genericReplacement2
import com.unamad.aulago.models.apiModels.AssistanceStudentApiModel
import com.unamad.aulago.models.apiModels.SaveAssistanceApiModel
import com.unamad.aulago.models.database.*
import com.unamad.aulago.models.query.SessionQueryModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class TeacherRepository @Inject constructor(
    private val database: SystemDatabase,
    private val api: IApiGeneral,
    private val generalRepository: GeneralRepository,
    private val scope: CoroutineScope,
    @ApplicationContext private val context: Context
) {
    val listAcademicChargesStream = database.teacherDao().listAcademicChargeStream()
    val listTeacherCoursesStream = database.teacherDao().listTeacherCoursesStream()
    val listTeacherScheduleStream = database.generalDao().listTeacherCoursesSchedule()
    val isSavingAssistance = MutableLiveData(false)

    fun listTeacherSchedulePublishGradesStream(sectionId: String) =
        database.schedulePublishGradesDao().listStream(sectionId)

    fun listTeacherStudentsStream(sectionId: String) =
        database.teacherDao().listTeacherStudentsStream(sectionId)

    fun getTeacherSectionStream(sectionId: String) = database.teacherDao().geStream(sectionId)
    fun getRepresentativeStudentFullNameStream(sectionId: String?) =
        database.sectionDao().getRepresentativeStudentStream(sectionId)

    val assistanceStudentIsLoadData = MutableLiveData(IsLoadData.Initializing)

    val assistanceData = MutableLiveData(listOf<AssistanceStudentApiModel>())

    fun saveTeacherAssistanceList() {
        scope.launch {
            isSavingAssistance.postValue(true)
            try {
                val session = generalRepository.getSessionData()
                    ?: throw Exception("No se ha iniciado sesión")


                val saveList = assistanceData.value?.map {
                    SaveAssistanceApiModel(
                        isAbsent = it.isAbsent ?: false,
                        studentUserId = it.studentUserId
                    )
                } ?: emptyList()

               val res = api.saveTeacherAssistanceClassNew(
                    data = saveList,
                    classId = assistanceData.value?.firstOrNull()?.classId ?: Utils.EmptyUUId,
                    token = session.token
                )

                scope.launch(Dispatchers.Main) {
                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            isSavingAssistance.postValue(false)
        }
    }

    fun loadListTeacherStudentAssistance(
        classId: String,
        sectionId: String
    ) {

        scope.launch {
            val isCorrectLoad = loadAssistanceClass(classId = classId, sectionId = sectionId)

            if (isSavingAssistance.value == true)  return@launch

            Log.i("LOAD ASSISTANCE", "tamos cargando we ${isSavingAssistance.value}")

            if (!isCorrectLoad.isValid) {
                Toast.makeText(context, isCorrectLoad.message ?: "", Toast.LENGTH_LONG).show()
                return@launch
            }
            //assistanceStudentList.postValue((isCorrectLoad.data ?: listOf()).toMutableStateList())
            assistanceData.postValue(isCorrectLoad.data?.toList())
        }
    }

    private suspend fun loadAssistanceClass(
        classId: String,
        sectionId: String
    ): ValidationData<List<AssistanceStudentApiModel>> {
        val session = generalRepository.getSessionData() ?: return ValidationData(
            false,
            "No se ha iniciado sesión"
        )
        try {
            assistanceStudentIsLoadData.postValue(IsLoadData.Loading)
            val response =
                api.getTeacherAssistanceClass(
                    classId = classId,
                    token = session.token,
                    sectionId = sectionId
                )
            if (!response.isSuccess) return ValidationData(false, response.message)
            val data = response.data ?: listOf()

            assistanceStudentIsLoadData.postValue(IsLoadData.Finish)
            return ValidationData(true, data = data.sortedBy { it.fullName })

        } catch (e: Exception) {
            assistanceStudentIsLoadData.postValue(IsLoadData.Finish)
            return ValidationData(false, e.message ?: "")
        }

    }

    private suspend fun listAcademicCharge(): List<String> {
        return database.teacherDao().listAcademicCharge()
    }

    private suspend fun replaceTeacherClass(teacherClasses: MutableList<TeacherClassModel>) {
        genericReplacement(
            list = teacherClasses,
            oldList = database.generalDao().listTeacherClass(),
            deleteFun = database.generalDao()::deleteTeacherClass,
            insertFun = database.generalDao()::insertTeacherClass,
            updateFun = database.generalDao()::updateTeacherClass
        )
    }

    private suspend fun loadTeacherClasses(
        session: SessionQueryModel,
        sections: List<String>
    ): Validation {
        try {
            val teacherClasses = mutableListOf<TeacherClassModel>()
            sections.forEach { section ->
                val response = api.getTeacherClasses(
                    userId = session.userId,
                    sectionId = section,
                    token = session.token
                )
                if (!response.isSuccess) {
                    return Validation(false, response.message)
                }
                val data = response.data ?: listOf()

                data.forEach { classes ->
                    teacherClasses.add(
                        TeacherClassModel(
                            id = classes.classId,
                            classScheduleId = classes.classScheduleId,
                            teacherUserId = session.userId,
                            endTime = Utils.forceUTC(classes.endTime),
                            startTime = Utils.forceUTC(classes.startTime),
                            isDictated = classes.isDictated
                        )
                    )
                }
            }
            replaceTeacherClass(teacherClasses)
            return Validation(true)
        } catch (e: Exception) {
            return Validation(false, e.message ?: "")
        }
    }

    suspend fun loadTeacherClasses(session: SessionQueryModel): Validation {
        val sections = listAcademicCharge()
        return loadTeacherClasses(session = session, sections = sections)
    }


    private suspend fun loadAcademicCharge(session: SessionQueryModel) {
        val localDate = LocalDateTime.now()

        val response = api.getTeacherAcademicCharge(
            userId = session.userId,
            termId = session.termId,
            token = session.token
        )
        if (!response.isSuccess) throw Exception("Carga Académica: " + response.message)

        val data = response.data ?: listOf()
        val courses = mutableListOf<CourseModel>()
        val sections = mutableListOf<SectionModel>()
        val users = mutableListOf<UserModel>()
        val teacherSection = mutableListOf<TeacherSectionModel>()
        val careers = mutableListOf<CareerModel>()


        data.forEachIndexed { index, charge ->
            courses.add(
                CourseModel(
                    id = charge.courseId,
                    name = charge.courseName,
                    academicYear = charge.academicYearCourse,
                    code = charge.courseCode,
                    modifyAt = localDate.toString(),
                    isElective = charge.isElective,
                    colorNumber = MaterialColors[index].toArgb().toLong(),
                    careerId = charge.careerId
                )
            )
            careers.add(
                CareerModel(
                    id = charge.careerId,
                    code = charge.careerCode,
                    name = charge.careerName
                )
            )
            users.add(
                UserModel(
                    id = charge.teacherId,
                    modifyAt = localDate.toString(),
                    email = charge.email,
                    maternalSurname = charge.maternalSurname,
                    paternalSurname = charge.paternalSurname,
                    phoneNumber = charge.phoneNumber?.replace(" ", ""),
                    name = charge.name,
                    sex = charge.sex
                )
            )
            sections.add(
                SectionModel(
                    id = charge.sectionId,
                    code = charge.sectionCode,
                    courseId = charge.courseId,
                    isDirectedCourse = charge.isDirectedCourse,
                    modifyAt = localDate.toString(),
                    termId = session.termId,
                    vacancies = charge.vacancies,
                    colorNumber = MaterialColors[index].toArgb().toLong(),
                    representativeStudentUserId = charge.representativeStudentUserId,
                    externalGroupLink = charge.externalGroupLink
                )
            )
            teacherSection.add(
                TeacherSectionModel(
                    id = charge.teacherSectionId,
                    isPrincipal = charge.isPrincipal,
                    sectionId = charge.sectionId,
                    teacherUserId = charge.teacherId
                )
            )
        }
        generalRepository.replaceCareer(careers.distinct())
        generalRepository.replaceCourse(courses.distinct())

        generalRepository.replaceSection(sections.distinct())
        generalRepository.replaceUser(users.distinct())
        generalRepository.replaceTeacherSection(teacherSection.distinct())
    }


    private suspend fun loadTeacherStudents(session: SessionQueryModel): Validation {
        val sections = listAcademicCharge()
        return generalRepository.loadStudentSection(session = session, sections = sections)
    }

    suspend fun loadTeacherDependency(session: SessionQueryModel): Validation {
        try {
            loadAcademicCharge(session = session)

            val loadSchedule = generalRepository.loadSchedule(session = session)
            if (!loadSchedule.isValid) return loadSchedule

            val loadTeacherStudents = loadTeacherStudents(session)
            if (!loadTeacherStudents.isValid) return loadTeacherStudents

            return loadTeacherClasses(session)
        } catch (e: Exception) {
            return Validation(false, e.message ?: "")
        }
    }

    suspend fun loadTeacherStudent(sectionId: String) {

        val system = generalRepository.getSystemData()
        system.assistanceSectionId = sectionId
        generalRepository.insertOrUpdateSystemData(system, "SETTER SECTION SELECTED")
    }

    fun loadAttemptsCourseGradesPublication() {
        scope.launch {
            try {
                val list = mutableListOf<SchedulePublishGradesModel>()
                val session =
                    database.generalDao().getUserSystemData()
                        ?: throw Exception("No se ha iniciado sesión")
                val data = api.scheduleGradesPublication(session.token)
                data.forEach { schedule ->

                    schedule.attempts.forEach { dates ->

                        val item = SchedulePublishGradesModel(
                            id = Utils.NewUUId(),
                            careerCode = schedule.careerCode,
                            courseCode = schedule.courseCode,
                            courseName = schedule.courseName,
                            sectionCode = schedule.sectionCode,
                            sectionId = schedule.sectionId,
                            endDate = dates.endDate,
                            numberOfUnit = dates.numberOfUnit,
                            startDate = dates.startDate
                        )
                        list.add(item)
                    }
                }

                genericReplacement2(
                    parameters = arrayOf(
                        SchedulePublishGradesModel::sectionId.name,
                        SchedulePublishGradesModel::numberOfUnit.name
                    ),
                    insertList = list,
                    oldList = database.schedulePublishGradesDao().list(),
                    deleteFun = database.schedulePublishGradesDao()::delete,
                    insertFun = database.schedulePublishGradesDao()::insert,
                    updateFun = database.schedulePublishGradesDao()::update,

                    )


            } catch (e: Exception) {
                Log.e("ERROR", e.message ?: "")
            }
        }
    }

    fun saveRepresentativeStudentAsync(sectionId: String, studentUserId: String) {
        scope.launch {
            try {
                Log.i("SAVE REPRESENTATIVE", "SAVE REPRESENTATIVE")
                val session =
                    database.generalDao().getUserSystemData()
                        ?: throw Exception("No se ha iniciado sesión")
                val message = api.saveRepresentativeStudent(
                    sectionId = sectionId,
                    studentUserId = studentUserId,
                    token = session.token
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                loadAcademicCharge(session = session)
            } catch (e: Exception) {
                val message = e.message.toString()
                Log.i("ERROR REPRESENTATIVE", message)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun saveWhatsappLinkAsync(sectionId: String, link: String) {
        scope.launch {
            try {
                val session =
                    database.generalDao().getUserSystemData()
                        ?: throw Exception("No se ha iniciado sesión")


                generalRepository.saveWhatsappLink(
                    sectionId = sectionId,
                    link = link,
                    session = session
                )
                loadAcademicCharge(session = session)
            } catch (e: Exception) {
                val message = e.message.toString()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
