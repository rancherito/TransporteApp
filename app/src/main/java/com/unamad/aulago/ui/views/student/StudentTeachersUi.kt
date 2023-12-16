package com.unamad.aulago.ui.views.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.unamad.aulago.Utils
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.emums.TextIconType
import com.unamad.aulago.models.query.CourseStudentTeachersQueryModel
import com.unamad.aulago.ui.components.EmptyCard
import com.unamad.aulago.ui.components.HorizontalTextIcon
import com.unamad.aulago.ui.components.TextIcon
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R

@Composable
fun StudentTeachersUi(viewModelStorage: ViewModelStorage = viewModelInstance()) {
    val sections = viewModelStorage.studentRepository.listStudentTeacherStream.observeAsState(listOf()).value


    val courses = sections.groupBy { it.sectionId }


    CommonLayout(headerInformation = HeaderInformation(title = "Docentes", subtitle = "EMail y contactos")) {
        if ((sections.size) > 0) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                courses.forEach { list ->

                    items(list.value) { teacher ->
                        BoxTeacherInfo(teacher)
                    }
                }
            }
        } else {
            EmptyCard()
        }
    }


}

@Composable
fun BoxTeacherInfo(course: CourseStudentTeachersQueryModel) {

    val prefixNumber = Utils.PREFIX_PERUVIAN
    Column(
        modifier = Modifier
            .card(color = myColors().card)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HorizontalTextIcon(
            title = course.courseName,
            description = course.curseSectionCode,
            icon = painterResource(id = R.drawable.book),
            iconColor = Color(course.colorNumber)
        )
        if (course.teacherFullName == null) {
            Text(text = "DOCENTE NO DISPONIBLE", color = Color.Gray)
        } else {

            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TextIcon(
                    text = course.teacherFullName,
                    painter = painterResource(id = R.drawable.person),
                    isCopyable = true
                )
                TextIcon(
                    text = if (course.phoneNumber == null) "Sin Registro" else "${prefixNumber}${course.phoneNumber}",
                    painter = painterResource(id = R.drawable.phone),
                    isCopyable = true,
                    textIconType = TextIconType.PhoneNumber
                )
                TextIcon(
                    text = course.email ?: "Sin Registro",
                    painter = painterResource(id = R.drawable.email),
                    isCopyable = true
                )
            }
        }

    }
}