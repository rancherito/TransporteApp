package com.unamad.aulago.ui.views.teacher

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.classes.UserContact
import com.unamad.aulago.models.query.SimpleCourseSectionQueryModel
import com.unamad.aulago.models.query.TeacherStudentQueryModel
import com.unamad.aulago.ui.components.TextIcon
import com.unamad.aulago.ui.components.UserContactBox
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.ui.layouts.rememberBoolean
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R

@Composable
fun TeacherStudentsSection(
    sectionId: String,
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    sectionInfo: SimpleCourseSectionQueryModel? =
        viewModelStorage.teacherRepository.listTeacherCoursesStream.observeAsState().value?.find { it.sectionId == sectionId },
    listStudent: List<TeacherStudentQueryModel> = viewModelStorage.teacherRepository.listTeacherStudentsStream(sectionId).observeAsState(
        emptyList()
    ).value
) {

    val state = rememberBoolean()
    val currentStudent = remember {
        mutableStateOf<TeacherStudentQueryModel?>(null)
    }
    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Alumnos",
            subtitle = sectionInfo?.courseName ?: "Cargando...",
            indicator = "${sectionInfo?.careerName}",
            color = if (sectionInfo?.colorNumber != null) Color(sectionInfo.colorNumber) else null
        ),
        contentBottom = {
            if (currentStudent.value != null){

                val userContact = UserContact(
                    fullName = currentStudent.value!!.fullName,
                    phoneNumber = currentStudent.value!!.phoneNumber,
                    email = currentStudent.value!!.email

                )
                UserContactBox(user = userContact)
            }
        },
        stateContentBottom = state
    ) {
        if (sectionInfo != null) {
            Column(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = "${listStudent.size} alumnos matriculados",
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    color = Color(sectionInfo.colorNumber),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(listStudent) { student ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .card()
                                .clickable {
                                    state.value = !state.value
                                    currentStudent.value = student
                                }
                                .padding(16.dp, 8.dp)
                                .fillMaxWidth()
                        ) {
                            TextIcon(
                                text = student.fullName,
                                painter = painterResource(id = R.drawable.person)
                            )
                        }
                    }
                }
            }
        }
    }


}

