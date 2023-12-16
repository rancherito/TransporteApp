package com.unamad.aulago.ui.views.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unamad.aulago.NavigationApp
import com.unamad.aulago.Utils
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.models.query.StudentSectionQueryModel
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.ui.components.IconButtonSys
import com.unamad.aulago.ui.components.UserContactBox
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.ui.layouts.rememberBoolean
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R

@Composable
fun StudentSectionsView(
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    sectionInfo: List<StudentSectionQueryModel> = viewModelStorage.studentRepository.listStudentSectionInfoStream.observeAsState(
        listOf()
    ).value,
    navController: NavHostController = navControllerInstance()
) {
    val sections = sectionInfo.sortedBy { it.courseName }
        .groupBy { it.sectionId }

    val context = LocalContext.current
    val currentSectionId = remember {
        mutableStateOf<String?>(null)
    }
    val delegate = viewModelStorage.teacherRepository.getRepresentativeStudentFullNameStream(currentSectionId.value).observeAsState().value
    val openModal = rememberBoolean()
    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Cursos",
            subtitle = "Cursos matriculados y compañeros"
        ),
        contentBottom = {
            //si no hay delegado mostrar texto si delegado
            if (delegate == null){
                Text(text = "EL curso no tiene delegado asignado")
            }
            else{
                Text(text = "Delegado de curso", fontWeight = FontWeight.Bold, color = myColors().primary)
                Spacer(modifier = Modifier.padding(8.dp))
                UserContactBox(user = delegate)
            }

        },
        stateContentBottom = openModal,

    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {


            items(sections.toList()) {
                val course = it.second.first()
                val courseColor = Color(course.colorNumber)
                Row(
                    modifier = Modifier
                        .card()
                        .clickable {
                            currentSectionId.value = course.sectionId
                            openModal.value = true
                        }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(

                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = course.courseName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Text(
                            text = "Tienes ${it.second.size} compañeros",
                            fontSize = 12.sp,
                            color = myColors().primary,
                        )
                    }
                    if (course.representativeStudentUserId != null) {
                        IconButtonSys(
                            painter = painterResource(id = R.drawable.person),
                            onClick = {
                                currentSectionId.value = course.sectionId
                                openModal.value = true
                            },
                            color = courseColor
                        )
                    }
                    if (course.externalGroupLink != null) {
                        IconButtonSys(
                            painter = painterResource(id = R.drawable.whatsapp),
                            onClick = {
                                Utils.openLink(context, course.externalGroupLink)
                            },
                            color = courseColor
                        )
                    }
                    IconButtonSys(
                        painter = painterResource(id = R.drawable.comunity),
                        onClick = {
                            navController.navigate("${NavigationApp.Student.COMPANIONS}/${course.sectionId}")

                        },
                        color = courseColor
                    )
                }
            }

        }

    }

}

