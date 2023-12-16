package com.unamad.aulago.ui.views.student

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.Utils
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.models.query.HomeworkStudentQueryModel
import com.unamad.aulago.ui.components.EmptyCard
import com.unamad.aulago.ui.components.HorizontalTextIcon
import com.unamad.aulago.ui.components.HtmlText
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.ui.layouts.rememberBoolean
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R
import com.unamad.aulago.models.query.StudentHomeworkQueryModel
import java.time.LocalDateTime

@Composable
fun StudentActivitiesView(
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {
    val homeWorkList =
        viewModelStorage.studentRepository.listHomeworkStudentStream.observeAsState(emptyList())
    val courseDetails = remember {
        mutableStateOf<StudentHomeworkQueryModel?>(null)
    }
    val expandDetails = remember {
        mutableStateOf(false)
    }
    val rotate: Float by animateFloatAsState(if (expandDetails.value) 0f else 180f, label = "")

    val layoutState = rememberBoolean()
    val filterList =
        homeWorkList.value.filter { LocalDateTime.now() < LocalDateTime.parse(it.dateEnd) }

    val filterListOld =
        homeWorkList.value.filter { LocalDateTime.now() >= LocalDateTime.parse(it.dateEnd) }

    val group = filterListOld.groupBy { it.sectionId }
    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Actividades",
            subtitle = "Listado de tareas"
        ),
        paddingContentBottom = PaddingValues(0.dp),
        contentBottom = {
            val course = courseDetails.value
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                if (course != null) {
                    Column(
                        Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = course.courseName,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontSize = 11.sp,
                            color = Color(course.colorNumber),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = course.homeworkTitle,
                            fontSize = 10.sp,
                            modifier = Modifier.fillMaxWidth(),
                            color = myColors().body
                        )
                    }

                    HtmlText(
                        html = course.homeworkDescription,
                        modifier = Modifier
                            .padding(16.dp, 0.dp)
                            .weight(1f)
                    )


                }
            }
        },
        stateContentBottom = layoutState
    ) {
        if (homeWorkList.value.isEmpty()) {
            EmptyCard (
                title = "No hay tareas registradas aún",
                painter = painterResource(id = R.drawable.duotone_backpack)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (filterList.isNotEmpty()) {

                    val pending = filterList.filter { it.usedAttempts == 0 }
                    val completes = filterList.filter { it.usedAttempts > 0 }

                    if (pending.isNotEmpty()) {
                        item {
                            Text(text = "TAREAS PENDIENTES", color = myColors().body)
                        }
                        items(pending) {

                            Course(homeworkItem = it) {
                                courseDetails.value = it
                                layoutState.value = true
                            }
                        }
                    }
                    if (completes.isNotEmpty()) {
                        item {
                            Text(text = "TAREAS COMPLETADAS", color = myColors().body)
                        }
                        items(completes) {

                            Course(homeworkItem = it) {
                                courseDetails.value = it
                                layoutState.value = true
                            }
                        }
                    }
                }
                if (filterListOld.isNotEmpty()) {
                    item {
                        Column {
                            Spacer(modifier = Modifier.padding(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "TAREAS FINALIZADAS", color = myColors().body)
                                    Text(
                                        text = "* Mayor información en su aula virtual",
                                        color = myColors().gray200,
                                        fontSize = 12.sp
                                    )
                                }
                                IconButton(
                                    modifier = Modifier.rotate(rotate),
                                    onClick = { expandDetails.value = !expandDetails.value }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.arrow_up),
                                        contentDescription = null,
                                        tint = myColors().body
                                    )
                                }
                            }

                        }
                    }
                }

                if (expandDetails.value) {
                    items(filterListOld) {
                        Column(
                            modifier = Modifier
                                .card()
                                .clickable {
                                    courseDetails.value = it
                                    layoutState.value = true
                                }
                                .padding(16.dp)
                        ) {
                            HorizontalTextIcon(
                                title = it.courseName,
                                description = it.homeworkTitle,
                                iconColor = Color(it.colorNumber),
                                icon = painterResource(id = R.drawable.time)
                            )
                        }
                    }
                } else {
                    items(group.toList()) {
                        val c = it.second.first()
                        HorizontalTextIcon(
                            title = c.courseName,
                            description = "Total tareas: ${it.second.size}",
                            iconColor = Color(c.colorNumber),
                            icon = painterResource(id = R.drawable.time)
                        )
                    }
                }
            }
        }


    }
}

@Composable
fun Course(
    homeworkItem: StudentHomeworkQueryModel,
    onClick: () -> Unit,
) {
    HorizontalTextIcon(
        title = homeworkItem.courseName,
        description = homeworkItem.homeworkTitle,
        iconColor = Color(homeworkItem.colorNumber),
        icon = painterResource(id = R.drawable.book),
        isACard = true,
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Bonito(
                title = "termina en",
                subtitle = Utils.calculateLeftDate(
                    LocalDateTime.parse(homeworkItem.dateEnd)
                ),
                color = Color(homeworkItem.colorNumber)
            )
            Bonito(
                title = "intentos",
                subtitle = "${homeworkItem.attempts - homeworkItem.usedAttempts} de ${homeworkItem.attempts}",
                color = Color(homeworkItem.colorNumber)
            )
            Box(modifier = Modifier.weight(1f))
            Bonito(
                title = "tipo",
                subtitle = "TAREA",
                color = Color(homeworkItem.colorNumber)
            )
        }
    }
}

@Composable
fun Bonito(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    color: Color = myColors().gray200
) {
    val style = TextStyle(
        textAlign = TextAlign.Center,
        fontSize = 11.sp,
        color = color
    )
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(8.dp)
            )
            //.background(color = myColors().gray, shape = RoundedCornerShape(4.dp))
            .padding(12.dp, 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = style
        )
        Text(
            text = subtitle,
            style = style
        )
    }
}
