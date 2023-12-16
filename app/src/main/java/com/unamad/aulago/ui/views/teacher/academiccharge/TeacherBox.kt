package com.unamad.aulago.ui.views.teacher.academiccharge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unamad.aulago.Utils
import com.unamad.aulago.card
import com.unamad.aulago.models.query.AcademicChargeQueryModel
import com.unamad.aulago.ui.components.TextIcon
import com.unamad.aulago.R


@Preview
@Composable
fun PreviewTeacherBox(){
    TeacherBox(
        teacher = AcademicChargeQueryModel(
            fullName = "Juan Perez",
            isMe = true,
            isPrincipal = true,
            phoneNumber = "123456789",
            sectionId = "1",
            colorNumber = 1,
            courseName = "Curso de prueba",
            courseSectionCode = "1234",
            careerName = "Ingenieria de prueba",
            enrolledStudents = 10,
            sex = 1,
            isDirectedCourse = false
        ),
        modifier = Modifier
            .fillMaxWidth().card()
            .padding(16.dp)
    )
}

@Composable
fun TeacherBox(teacher: AcademicChargeQueryModel, modifier: Modifier = Modifier) {
    val prefixNumber = Utils.PREFIX_PERUVIAN
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TextIcon(
            text = teacher.fullName + if (teacher.isMe) " (ud.)" else "",
            painter = painterResource(id = R.drawable.person)
        )
        if (teacher.isPrincipal)
            TextIcon(
                text = "DOCENTE PRINCIPAL",
                painter = painterResource(id = R.drawable.fire)
            )
        if (!teacher.isMe)
            TextIcon(
                text = if (teacher.phoneNumber == null) "sin número" else "${prefixNumber}${teacher.phoneNumber}",
                painter = painterResource(id = R.drawable.phone),
                isCopyable = true,
                fontStyle = if (teacher.phoneNumber == null) FontStyle.Italic else FontStyle.Normal
            )
    }
}