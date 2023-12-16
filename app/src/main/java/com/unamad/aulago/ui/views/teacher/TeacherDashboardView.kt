package com.unamad.aulago.ui.views.teacher

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unamad.aulago.NavigationApp
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.models.query.CurrentSessionQueryModel
import com.unamad.aulago.models.query.SimpleCourseSectionQueryModel
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.ui.components.EmptyCard
import com.unamad.aulago.ui.components.SmallDashBoardBox
import com.unamad.aulago.ui.components.IconSquare
import com.unamad.aulago.ui.layouts.DashboardLayout
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R


@Composable
fun TeacherDashboardView(
    viewModelStorage: ViewModelStorage = viewModelInstance(),
) {
    val courses =
        viewModelStorage.teacherRepository.listTeacherCoursesStream.observeAsState(listOf()).value
    val user = viewModelStorage.generalRepository.userSystemDataStream.observeAsState().value
    viewModelStorage.teacherRepository.loadAttemptsCourseGradesPublication()
    _TeacherDashboardView(courses = courses, user = user)
}

@Composable
fun _TeacherDashboardView(

    courses: List<SimpleCourseSectionQueryModel>,
    user: CurrentSessionQueryModel?
) {
    DashboardLayout(
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (user != null) {


                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (courses.isNotEmpty()) {


                        Dashboard(courses)

                    } else {
                        EmptyCard(title = "Sin carga académica para el periodo ${user.termName}")
                    }
                }

            }
        }
    }
}

@SuppressLint("InvalidColorHexValue")
@Composable
private fun Dashboard(
    courses: List<SimpleCourseSectionQueryModel>,
) {
    val widthBoxes = 100.dp
    Column {
        LazyVerticalGrid(
            contentPadding = PaddingValues(16.dp),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item(
                span = {
                    GridItemSpan(2)
                }
            ){
                Text(
                    text = "ACCESOS PRINCIPALES",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            item(
                span = {
                    GridItemSpan(2)
                }
            ) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(1),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {

                    item {
                        SmallDashBoardBox(
                            text = "Horario",
                            iconSource = R.drawable.schedule,
                            link = NavigationApp.Teacher.CLASS_SCHEDULE,
                            width = widthBoxes
                        )
                    }

                    item {
                        SmallDashBoardBox(
                            text = "Fechas",
                            iconSource = R.drawable.calendar_check,
                            link = NavigationApp.Teacher.GENERAL_SCHEDULE,
                            width = widthBoxes
                        )
                    }
                    item {
                        SmallDashBoardBox(
                            text = "Carga",
                            iconSource = R.drawable.book,
                            link = NavigationApp.Teacher.ACADEMIC_CHARGE,
                            width = widthBoxes
                        )
                    }
                    item {
                        SmallDashBoardBox(
                            text = "Aulas",
                            iconSource = R.drawable.building,
                            link = NavigationApp.Teacher.CLASSROOM,
                            width = widthBoxes
                        )
                    }
                    item {
                        SmallDashBoardBox(
                            text = "Soporte",
                            iconSource = R.drawable.support,
                            link = NavigationApp.General.SUPPORT,
                            width = widthBoxes
                        )
                    }
                }
            }
            item(
                span = {
                    GridItemSpan(2)
                }
            ){
                Text(
                    text = "ACCESO A CURSOS",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            items(
                courses,
                span = {
                    GridItemSpan(1)
                }) {
                CourseBox(
                    course = it
                )
            }
            item(
                span = {
                    GridItemSpan(2)
                }
            ) {
                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    }
}


@Preview
@Composable
fun TestCourseBox() {
    val course = SimpleCourseSectionQueryModel(
        courseName = "[ADC] Curso de prueba con el nombre mas largo que se les pueda ocurrir",
        careerName = "Ingeniería de Sistemas e informática",
        sectionCode = "B",
        colorNumber = MaterialTheme.colorScheme.primary.toArgb().toLong(),
        sectionId = "A",
        courseCode = "ISI",
    )
    CourseBox(
        course = course
    )
}

@Composable
fun CourseBox(
    course: SimpleCourseSectionQueryModel,
    navController: NavHostController = navControllerInstance(),
) {
    val courseColor = Color(course.colorNumber)


    Column(
        modifier = Modifier
            .card(courseColor)
            .clickable {
                navController.navigate("${NavigationApp.Teacher.SECTION}/${course.sectionId}")
            }
            .height(160.dp)
            .widthIn(max = 400.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            Row(

                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconSquare(painter = painterResource(id = R.drawable.duotone_layers), color = Color.White)
                Column(
                ) {
                    Text(
                        text = course.courseCode,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    Text(
                        text = "GRUPO ${course.sectionCode}",
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = course.courseName,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = course.careerName ?: "",
                fontSize = 10.sp,
                lineHeight = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.White.copy(.8f)
            )
        }
        /*Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
    ) {

        AssistChip(
            onClick = {
                navController?.navigate("${NavigationApp.Teacher.ASSISTANCE}/${course.sectionId}")
            },
            label = {
                Text(text = "Asist.", fontSize = 10.sp)
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.list_check),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            colors = AssistChipDefaults.assistChipColors(
                labelColor = courseColor,
                trailingIconContentColor = courseColor,

            ),
            border = AssistChipDefaults.assistChipBorder(
                borderColor = courseColor,
            ),

        )

    }*/
    }


}

private class RippleCustomTheme(
    val color: Color = Color.Black,
) : RippleTheme {

    //Your custom implementation...
    @Composable
    override fun defaultColor() =
        RippleTheme.defaultRippleColor(
            contentColor = color,
            lightTheme = true
        )

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleTheme.defaultRippleAlpha(
            Color.Black,
            lightTheme = true
        )
}