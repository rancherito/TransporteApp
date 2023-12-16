package com.unamad.aulago.ui.views

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unamad.aulago.NavigationApp
import com.unamad.aulago.Roles
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.getParameter
import com.unamad.aulago.getPath
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.ui.views.general.SupportView
import com.unamad.aulago.ui.views.student.AbsencesJustificationView
import com.unamad.aulago.ui.views.student.RepresentativeManagement
import com.unamad.aulago.ui.views.student.StudentActivitiesView
import com.unamad.aulago.ui.views.student.StudentAssistanceView
import com.unamad.aulago.ui.views.student.StudentClassRoomUi
import com.unamad.aulago.ui.views.student.StudentSectionsView
import com.unamad.aulago.ui.views.student.StudentDashboardView
import com.unamad.aulago.ui.views.student.StudentDebtsView
import com.unamad.aulago.ui.views.student.StudentSchedule
import com.unamad.aulago.ui.views.student.StudentSectionCompanionView
import com.unamad.aulago.ui.views.student.StudentTeachersUi
import com.unamad.aulago.ui.views.teacher.TeacherCalendarAssistanceUi
import com.unamad.aulago.ui.views.teacher.TeacherClassRoomUi
import com.unamad.aulago.ui.views.teacher.TeacherDashboardView
import com.unamad.aulago.ui.views.teacher.TeacherGeneralSchedulesView
import com.unamad.aulago.ui.views.teacher.TeacherScheduleView
import com.unamad.aulago.ui.views.teacher.TeacherSectionView
import com.unamad.aulago.ui.views.teacher.TeacherStudentsSection
import com.unamad.aulago.ui.views.teacher.academiccharge.TeacherAcademicCharge
import com.unamad.aulago.ui.views.teacher.assistance.TeacherAssistanceView
import com.unamad.aulago.viewModelInstance


@Composable
fun RouteView(
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {
    viewModelStorage.loadInitialData(context = LocalContext.current)

    val loginStatus = viewModelStorage.generalRepository.userSystemDataStream.observeAsState()
    val isOnline = viewModelStorage.isOnline.observeAsState(false).value

    if (loginStatus.value != null && isOnline) {
        viewModelStorage.loadDependencyDataAsync(context = LocalContext.current)

        if (!loginStatus.value!!.tokenVerify) viewModelStorage.closeSession()
    }
    val navController = navControllerInstance()

    Crossfade(targetState = loginStatus.value?.role, label = "changeLayoutViewAnimation") { role ->

        when (role) {
            Roles.Student -> {

                NavHost(
                    navController = navController,
                    startDestination = NavigationApp.Student.DASHBOARD
                ) {
                    composable(route = NavigationApp.Student.DASHBOARD) {
                        StudentDashboardView()
                    }

                    composable(route = NavigationApp.Student.JUSTIFICATIONS_ABSENCES) {
                        AbsencesJustificationView()
                    }

                    composable(route = NavigationApp.Student.DASHBOARD) {
                        StudentDashboardView()
                    }
                    composable(route = NavigationApp.Student.DEBTS) {
                        StudentDebtsView()
                    }
                    composable(route = NavigationApp.Student.ACTIVITIES) {
                        StudentActivitiesView()
                    }
                    composable(route = NavigationApp.Student.SCHEDULE) {
                        StudentSchedule()
                    }
                    composable(route = NavigationApp.Student.TEACHERS) {
                        StudentTeachersUi()
                    }
                    composable(route = NavigationApp.Student.COMPANIONS) {
                        StudentSectionsView()
                    }
                    composable(route = NavigationApp.Student.ASSISTANCE) {
                        StudentAssistanceView()
                    }
                    composable(route = "${NavigationApp.Student.COMPANIONS}/{sectionId}") {
                        val sectionId = it.getPath("sectionId")
                        StudentSectionCompanionView(sectionId)
                    }
                    composable(route = NavigationApp.Student.CLASSROOM) {
                        StudentClassRoomUi()
                    }
                    composable(route = NavigationApp.General.SUPPORT) {
                        SupportView()
                    }
                    composable(route = NavigationApp.Student.REPRESENTATIVE_MANAGEMENT) {
                        RepresentativeManagement()
                    }
                }

            }

            Roles.Teacher -> {

                NavHost(
                    navController = navController,
                    startDestination = NavigationApp.Teacher.DASHBOARD
                ) {
                    composable(route = NavigationApp.Teacher.DASHBOARD) {
                        TeacherDashboardView()
                    }
                    composable(route = NavigationApp.Teacher.GENERAL_SCHEDULE) {
                        TeacherGeneralSchedulesView()
                    }
                    composable(
                        route = NavigationApp.Teacher.CLASS_SCHEDULE
                    ) {
                        TeacherScheduleView()
                    }
                    composable(
                        route = NavigationApp.Teacher.SECTION_STUDENTS + "/{sectionId}"
                    ) {
                        val sectionId = it.getPath("sectionId")
                        viewModelStorage.loadTeacherStudent(sectionId)

                        TeacherStudentsSection(sectionId)
                    }

                    composable(route = "${NavigationApp.Teacher.SECTION}/{sectionId}") {
                        val sectionId = it.getPath("sectionId")

                        TeacherSectionView(sectionId)
                    }
                    composable(
                        route = NavigationApp.Teacher.CLASSROOM
                    ) {
                        TeacherClassRoomUi()
                    }
                    composable(
                        route = "${NavigationApp.Teacher.CALENDAR_ASSISTANCE}/{sectionId}"
                    ) {
                        val sectionId = it.getPath("sectionId")
                        viewModelStorage.loadTeacherStudent(sectionId)
                        TeacherCalendarAssistanceUi(sectionId)
                    }
                    composable(
                        route = "${NavigationApp.Teacher.ASSISTANCE}/{sectionId}?classId={classId}"
                    ) {
                        val sectionId = it.getPath("sectionId")
                        val classId = it.getParameter("classId")

                        viewModelStorage.loadTeacherStudent(sectionId)
                        TeacherAssistanceView(
                            sectionId = sectionId,
                            classId = classId
                        )
                    }
                    composable(
                        route = NavigationApp.Teacher.ACADEMIC_CHARGE
                    ) {
                        TeacherAcademicCharge()
                    }
                    composable(route = NavigationApp.General.SUPPORT) {
                        SupportView()
                    }
                }

            }

            else -> {
                AccessView()
            }

        }
    }

}

