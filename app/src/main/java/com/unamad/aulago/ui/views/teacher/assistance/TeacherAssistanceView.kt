package com.unamad.aulago.ui.views.teacher.assistance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.models.database.TeacherClassModel
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.viewModelInstance
import java.time.LocalDateTime


@Composable
fun TeacherAssistanceView(
    sectionId: String,
    classId: String? = null,
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    navController: NavHostController = navControllerInstance(),
    isSaving: Boolean = viewModelStorage.teacherRepository.isSavingAssistance.observeAsState(initial = false).value
) {
    val now = LocalDateTime.now()
    val currentSessionClasses = remember {
        mutableStateOf<TeacherClassModel?>(null)
    }

    val sectionInfo =
        viewModelStorage.teacherRepository.listTeacherCoursesStream.observeAsState(listOf()).value.find {
            it.sectionId == sectionId
        }

    val listClassDatesSection =
        viewModelStorage.listClassDatesSection.observeAsState(listOf()).value

    val parameterTeacherClass = listClassDatesSection.firstOrNull { it.id == classId }
    val availableSessionClasses = listClassDatesSection.filter {
        LocalDateTime.parse(it.startTime) >= now
    }
    val lastSessionClasses = listClassDatesSection.filter {
        LocalDateTime.parse(it.startTime) <= now
    }
    currentSessionClasses.value = availableSessionClasses.find {
        now >= LocalDateTime.parse(it.startTime) && now < LocalDateTime.parse(it.endTime)
    }
    val nextSessionClass =
        availableSessionClasses.getOrNull(if (currentSessionClasses.value == null) 0 else 1)

    val lastClass =
        lastSessionClasses.getOrNull(if (currentSessionClasses.value == null) lastSessionClasses.lastIndex else lastSessionClasses.lastIndex - 1)

    val head = if (sectionInfo != null) {
        HeaderInformation(
            title = "Asistencia",
            subtitle = "${sectionInfo.sectionCode} ${sectionInfo.courseName}",
            indicator = sectionInfo.careerName,
            color = Color(sectionInfo.colorNumber)
        )
    } else null

    val sessionClassesFromUserSelection = remember {
        mutableStateOf<TeacherClassModel?>(null)
    }
    //UI DESIGN

    CommonLayout(
        headerInformation = head,
        loadingState = isSaving
    ) {
        val sessionClasses =
            parameterTeacherClass ?: (currentSessionClasses.value
                ?: sessionClassesFromUserSelection.value)

        if (sessionClasses == null || sectionInfo == null) {
            EmptyAssistanceBox(
                nextSessionClass,
                lastClass,
                navController,
                color = if (sectionInfo == null) null else Color(sectionInfo.colorNumber),
                sectionId = sectionId
            ) {
                sessionClassesFromUserSelection.value = it
            }

        } else {
            viewModelStorage.teacherRepository.loadListTeacherStudentAssistance(
                classId = sessionClasses.id,
                sectionId = sectionId
            )

            TakeAssistanceBox(
                teacherClassModel = sessionClasses
            )
        }


    }
}

