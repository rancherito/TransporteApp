package com.unamad.aulago.ui.components.classRoom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.Roles
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.models.query.ScheduleCourseQueryModel
import com.unamad.aulago.ui.components.HorizontalTextIcon
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R


@Composable
fun ClassRoomBox(
    building: List<ScheduleCourseQueryModel>,
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {

    val role = viewModelStorage.generalRepository.userSystemDataStream.observeAsState().value?.role == Roles.Teacher
    val classrooms = building
        .sortedBy { it.classroomName }
        .groupBy { it.classroomName }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth(),
    ) {

        Text(
            text = building.first().buildingName!!,
            color = myColors().body,
            fontWeight = FontWeight.Medium
        )

        Icon(
            painter = painterResource(id = R.drawable.building),
            contentDescription = null,
            modifier = Modifier
                .size(16.dp),
            tint = myColors().body
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        classrooms.forEach { classroom ->
            val classroomInfo = classroom.value.first()

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .card()
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = classroomInfo.typeName!!,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (!classroomInfo.typeName.lowercase().contains("virtual")) {
                        Text(
                            text = classroomInfo.classroomName!!.uppercase() + if (classroomInfo.floor == 0) "" else " PISO ${classroomInfo.floor}",
                            fontSize = 14.sp,
                            color = myColors().body
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    classroom.value.map {
                        CourseColor(
                            courseName = it.courseName,
                            colorNumber = it.colorNumber,
                            courseCode = it.courseCode,
                            sectionCode = it.sectionCode,
                            careerCode = it.careerCode ?:"",
                        )
                    }.distinct().forEach { course ->
                        val careerCode = if (role) "[${course.careerCode}] " else ""
                        HorizontalTextIcon(
                            title = careerCode + course.courseName,
                            description = "${course.courseCode}-${course.sectionCode}",
                            icon = painterResource(id = R.drawable.book),
                            iconColor = Color(course.colorNumber)
                        )
                    }
                }
            }
        }
    }
}

data class CourseColor(
    val courseName: String,
    val colorNumber: Long,
    val courseCode: String,
    val sectionCode: String,
    val careerCode: String
)