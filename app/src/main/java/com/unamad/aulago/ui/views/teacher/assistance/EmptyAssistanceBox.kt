package com.unamad.aulago.ui.views.teacher.assistance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unamad.aulago.NavigationApp
import com.unamad.aulago.models.database.TeacherClassModel
import com.unamad.aulago.toHumanDate
import com.unamad.aulago.toHumanTime
import com.unamad.aulago.ui.components.FloatingIconButton
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.R
import java.time.LocalDateTime

@Composable
fun EmptyAssistanceBox(
    nextSessionClass: TeacherClassModel?,
    lastClass: TeacherClassModel?,
    navController: NavController,
    sectionId: String,
    color: Color? = MaterialTheme.colorScheme.primary,
    onChange: (teacherClassModel: TeacherClassModel) -> Unit
) {
    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .offset(y = 50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.duotone_clipboard),
                contentDescription = null,
                Modifier.size(100.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(color!!)
            )

            Spacer(modifier = Modifier.height(16.dp))
            if (nextSessionClass != null) {
                Text(
                    text = "SU CLASE INICIA ${
                        LocalDateTime.parse(nextSessionClass.startTime).toHumanDate(short = true)
                    }",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = myColors().body
                )
                Text(
                    text = LocalDateTime.parse(nextSessionClass.startTime).toHumanDate(),
                    textAlign = TextAlign.Center,
                    color = myColors().body
                )
                Text(
                    text = LocalDateTime.parse(nextSessionClass.startTime).toHumanTime(),
                    textAlign = TextAlign.Center,
                    color = myColors().body
                )
            }
            if (lastClass != null) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "CLASE ANTERIOR", fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.surface)

                Button(
                    onClick = {
                        onChange(lastClass.copy())
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = color)
                ) {

                    Text(
                        text = LocalDateTime.parse(lastClass.startTime).toHumanDate(),
                        fontSize = 12.sp
                    )

                }
            }


        }

        FloatingIconButton(
            icon = painterResource(id = R.drawable.calendar_month),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((-16).dp, (-16).dp)
        ) {
            navController.navigate("${NavigationApp.Teacher.CALENDAR_ASSISTANCE}/${sectionId}")
        }

    }
}