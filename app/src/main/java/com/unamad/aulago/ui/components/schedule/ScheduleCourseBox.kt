package com.unamad.aulago.ui.components.schedule

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.card
import com.unamad.aulago.models.query.ScheduleCourseQueryModel
import com.unamad.aulago.toDarkness
import com.unamad.aulago.toHighDarkness
import com.unamad.aulago.toHighLightness
import com.unamad.aulago.toLightness

@Composable
fun ScheduleCourseBox(
    course: ScheduleCourseQueryModel,
    out: (courseOut: ScheduleCourseQueryModel) -> Unit
) {
    val courseColor = Color(course.colorNumber)
    val titleColor = if(isSystemInDarkTheme()) courseColor.toHighLightness() else courseColor.toHighDarkness()
    val bodyColor = if(isSystemInDarkTheme()) courseColor.toLightness() else  courseColor.toDarkness()

    val isVirtual = course.typeName?.lowercase()?.contains("virtual") ?: false
    val classRoomName = if (isVirtual) course.typeName ?: "" else course.classroomName ?: ""
    val modifier = if (isSystemInDarkTheme()) {
        Modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = courseColor, shape = RoundedCornerShape(16.dp))
    } else {
        Modifier.card(courseColor.copy(alpha = 0.2f))
    }
    Column(
        modifier = modifier
            .clickable {
                out(course.copy())
            }
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text(
            text = classRoomName,
            fontSize = 12.sp,
            lineHeight = 13.sp,
            color = titleColor,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "[${course.careerCode}] ${course.courseName}",
            color = bodyColor,
            fontSize = 12.sp,
            lineHeight = 13.sp,
            overflow = TextOverflow.Ellipsis
        )


    }
}