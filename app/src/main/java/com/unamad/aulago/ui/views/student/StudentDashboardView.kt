package com.unamad.aulago.ui.views.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unamad.aulago.NavigationApp
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.models.database.PaymentModel
import com.unamad.aulago.models.query.CurrentSessionQueryModel
import com.unamad.aulago.models.query.StatusRepresentativeStudentQuery
import com.unamad.aulago.models.query.StudentSectionQueryModel
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.ui.components.DashBoardBox
import com.unamad.aulago.ui.components.EmptyCard
import com.unamad.aulago.ui.layouts.DashboardLayout
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R
import java.time.LocalDateTime



@Composable
fun StudentDashboardView(
    navController: NavHostController = navControllerInstance(),
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    user: CurrentSessionQueryModel? = viewModelStorage.generalRepository.userSystemDataStream.observeAsState().value,
    enrollmentSection: List<StudentSectionQueryModel> =
        viewModelStorage.studentRepository.listStudentSectionInfoStream.observeAsState(listOf()).value,
) {

    viewModelStorage.generalRepository.loadStudentProfile()

    viewModelStorage.studentRepository.loadAssistanceStudentAsync()

    DashboardLayout {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (user != null) {
                val debts =
                    viewModelStorage.generalRepository.paymentsUserStream.observeAsState(listOf())
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (enrollmentSection.isNotEmpty()) ItemsDashboard()
                    else EmptyCard(
                        title = "Sin matrícula en el periodo ${user.termName}",
                        content = {
                            if (debts.value.isNotEmpty()) {
                                OutlinedButton(
                                    onClick = {
                                        navController.navigate(NavigationApp.Student.DEBTS)
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.money),
                                        contentDescription = null,
                                        Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    Text(text = "Tiene ${debts.value.size} pagos pendienstes")
                                }
                            }

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemsDashboard(
    navController: NavHostController = navControllerInstance(),
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    listStatusRepresentative: List<StatusRepresentativeStudentQuery> = viewModelStorage.studentRepository.listStatusRepresentativeStream.observeAsState(
        listOf()
    ).value,
    debts: List<PaymentModel> =
        viewModelStorage.generalRepository.paymentsUserStream.observeAsState(listOf()).value
) {

    LazyVerticalGrid(
        contentPadding = PaddingValues(16.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            DashBoardBox(
                text = "Mi Horario",
                iconSource = R.drawable.schedule,
                link = NavigationApp.Student.SCHEDULE,
                navController = navController
            )
        }

        item {
            val list =
                viewModelStorage.studentRepository.listHomeworkStudentStream.observeAsState(listOf())
            val filterList =
                list.value.filter { LocalDateTime.now() < LocalDateTime.parse(it.dateEnd) && it.usedAttempts == 0 }
            DashBoardBox(
                text = "Mis Tareas",
                iconSource = R.drawable.backpack,
                link = NavigationApp.Student.ACTIVITIES,
                navController = navController,
                counter = filterList.size
            )
        }
        item {
            DashBoardBox(
                text = "Mis Docentes",
                iconSource = R.drawable.teacher,
                link = NavigationApp.Student.TEACHERS,
                navController = navController
            )
        }
        item {
            DashBoardBox(
                text = "Cursos y Compañeros",
                iconSource = R.drawable.desk,
                link = NavigationApp.Student.COMPANIONS,
                navController = navController
            )
        }
        item {
            DashBoardBox(
                text = "Aulas de Clase",
                iconSource = R.drawable.building,
                link = NavigationApp.Student.CLASSROOM,
                navController = navController
            )
        }
        item {
            DashBoardBox(
                text = "Mi Asistencia",
                iconSource = R.drawable.doc_success,
                link = NavigationApp.Student.ASSISTANCE,
                navController = navController
            )
        }
        item {
            DashBoardBox(
                text = "Justificación de inasistencias",
                iconSource = R.drawable.doc_success,
                link = NavigationApp.Student.JUSTIFICATIONS_ABSENCES,
                navController = navController
            )
        }
        if (listStatusRepresentative.isNotEmpty()) {
            item {
                DashBoardBox(
                    text = "Funciones de delegado",
                    iconSource = R.drawable.path,
                    link = NavigationApp.Student.REPRESENTATIVE_MANAGEMENT,
                    navController = navController
                )
            }
        }
        if (debts.isNotEmpty()) {
            item {
                DashBoardBox(
                    text = "Deudas Pendientes",
                    iconSource = R.drawable.money,
                    link = NavigationApp.Student.DEBTS,
                    navController = navController,
                    counter = debts.size
                )
            }
        }

        item {
            DashBoardBox(
                text = "Soporte",
                iconSource = R.drawable.support,
                link = NavigationApp.General.SUPPORT,
                navController = navController
            )
        }/*
        item {
            DashBoardBox(
                text = "Notificaciones",
                IconSource = R.drawable.bell,
                Link = null,
                navController = navController
            )
        }*/
    }


}