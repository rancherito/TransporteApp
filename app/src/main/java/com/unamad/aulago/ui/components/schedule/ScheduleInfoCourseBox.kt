package com.unamad.aulago.ui.components.schedule

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.Roles
import com.unamad.aulago.Utils
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.models.query.ScheduleCourseQueryModel
import com.unamad.aulago.models.query.TeacherSectionQueryModel
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")

@Composable
fun ScheduleInfoCourseBox(
    course: ScheduleCourseQueryModel?,
    courseDays: List<CourseDay>,
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {

    val coroutine = rememberCoroutineScope()
    val listTeachers = remember {
        mutableStateOf<MutableList<TeacherSectionQueryModel>>(mutableListOf())
    }

    if (course != null) {
        val isVirtual = course.typeName?.lowercase()?.contains("virtual") ?: false
        val classRoomName = if (isVirtual) course.typeName ?: "" else course.classroomName ?: ""

        if (course.role == Roles.Student) {
            coroutine.launch {
                listTeachers.value = withContext(Dispatchers.IO) {
                    viewModelStorage.studentRepository.listTeacherSectionFilter(
                        course.sectionId
                    )
                        .toMutableList()

                }
            }
        }
        AnimatedContent(targetState = course, label = "") { courseAnimate ->
            val colorPanel = Color(courseAnimate.colorNumber)

            Row(
                Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {

                    },
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        modifier = Modifier
                            .background(
                                color = colorPanel,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp, 2.dp),
                        text = courseAnimate.sectionCode,
                        color = myColors().card,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = courseAnimate.courseCode,
                        color = myColors().body,
                        fontSize = 10.sp
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    if (courseAnimate.buildingName != null)
                        Text(
                            text = courseAnimate.buildingName,
                            color = colorPanel,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                    if (courseAnimate.classroomName != null)
                        Text(
                            text = "SALÓN: " + if (isVirtual) classRoomName else "${courseAnimate.classroomName} ${if (courseAnimate.floor != null && courseAnimate.floor != 0) "| PISO ${courseAnimate.floor}" else ""}",
                            color = myColors().body,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                    Text(
                        text = courseAnimate.courseName,
                        color = myColors().body,
                        fontSize = 12.sp,
                        lineHeight = 14.sp
                    )
                    if (courseAnimate.role == Roles.Student) {


                        Spacer(modifier = Modifier.padding(4.dp))
                        if (listTeachers.value.size == 0) {
                            Text(
                                text = "SIN DOCENTE",
                                color = myColors().body,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "DOCENTES:",
                                color = colorPanel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            listTeachers.value.forEach {
                                Text(
                                    text = "- ${it.fullName}",
                                    color = myColors().body,
                                    fontSize = 12.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }


                    }
                    if (courseDays.isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            courseDays.forEach {
                                OutlinedButton(
                                    onClick = { /*TODO*/ },
                                    contentPadding = PaddingValues(2.dp),
                                    modifier = Modifier.height(32.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = myColors().body),
                                    border = BorderStroke(1.dp, myColors().body)
                                ) {
                                    Text(
                                        text = Utils.days[it.weekDay].substring(0, 2),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    //Text(text = modeInfoCourse.value!!.courseName)

                }
            }
        }
    }
}