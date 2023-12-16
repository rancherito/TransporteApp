package com.unamad.aulago.ui.views.teacher.academiccharge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.ui.components.HorizontalTextIcon
import com.unamad.aulago.ui.components.TextIcon
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R

@Composable
fun TeacherAcademicCharge(viewModelStorage: ViewModelStorage = viewModelInstance()) {
    val courseTeachers = viewModelStorage.teacherRepository.listAcademicChargesStream.observeAsState(listOf()).value
    val courses = courseTeachers.sortedByDescending { it.isMe }
        .groupBy { it.sectionId }

    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Carga académica",
            subtitle = "${courses.size} asignaturas registradas en su carga"
        )
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            courses.forEach { course ->
                item {
                    Column(
                        modifier = Modifier
                            .card()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val courseFirst = course.value.first()
                        HorizontalTextIcon(
                            title = courseFirst.courseName,
                            description = courseFirst.courseSectionCode,
                            icon = painterResource(id = R.drawable.book),
                            iconColor = Color(courseFirst.colorNumber)
                        )
                        Column(modifier = Modifier.fillMaxWidth()) {
                            TextIcon(
                                text = "${courseFirst.careerName}",
                                painter = painterResource(id = R.drawable.building)
                            )
                            TextIcon(
                                text = "${courseFirst.enrolledStudents} matriculados",
                                painter = painterResource(id = R.drawable.comunity)
                            )
                        }
                        course.value.forEach { teacher ->
                            TeacherBox(teacher)
                        }
                    }

                }
            }

        }
    }
}

