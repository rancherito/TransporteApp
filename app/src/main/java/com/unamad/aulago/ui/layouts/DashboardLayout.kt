package com.unamad.aulago.ui.layouts

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.R
import com.unamad.aulago.Roles
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.models.apiModels.StudentProfileApiModel
import com.unamad.aulago.ui.components.DecorationTitle
import com.unamad.aulago.ui.theme.QuickSandFont
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance

@SuppressLint("InternalInsetResource", "DiscouragedApi")
@Composable
fun DashboardLayout(
    modifier: Modifier = Modifier,
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    contentModifier: Modifier = Modifier,
    content: @Composable (BoxScope.() -> Unit)
) {
    val context = LocalContext.current
    viewModelStorage.isContrast.postValue(true)
    val systemData = viewModelStorage.generalRepository.userSystemDataStream.observeAsState().value
    val isLoadGlobalData = viewModelStorage.isLoadGlobalData.observeAsState(true).value
    val state = rememberBoolean(false)

    var modifierPresentation: Modifier = Modifier
    //si el rol es de estudiante, entonces hacer que modifierPresentation sea clickeable
    if (systemData?.role == Roles.Conductor) {
        modifierPresentation = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                state.value = true
            }
    }

    val fullName = systemData?.name?.split(" ")?.firstOrNull() ?: ""
    val welcomeGenre = if (systemData?.sex == 1) "Bienvenido" else "Bienvenida"
    val welcomeText = "$welcomeGenre al panel principal"
    BaseLayout(modifier = modifier) {


        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp))
            {
                DecorationTitle()
                Column(
                    modifier = modifierPresentation
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "HOLA, $fullName",
                        fontFamily = QuickSandFont,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,

                        )
                    Text(
                        text = welcomeText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 18.sp,
                            color = myColors().body
                        ),
                        modifier = Modifier
                    )
                }
            }
            Box(
                modifier = contentModifier
                    .fillMaxWidth()
                    .weight(1f),
                content = content
            )

        }

    }
}


@Composable
fun StudentProfileCard(
    infoStudent: StudentProfileApiModel?,
    callback: () -> Unit = {}
) {


    Box(
        modifier = Modifier
            .height(570.dp)
            .padding(horizontal =  16.dp)
            .card()
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, delta ->
                    if (delta < -40f) { // Puedes ajustar este valor según tus necesidades

                        callback()
                    }
                }
            }
    ) {

        Image(
            painter = painterResource(id = R.drawable.puerto_3),
            contentDescription = null,
            contentScale = androidx.compose.ui.layout.ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0x68131E24), Color(0xFF131E24)),
                        startY = 0f,
                        endY = 1000f
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_unamad),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Text(text = "Universidad Nacional Amazónica de Madre de Dios", color = Color.White)
            }
            Icon(
                painter = painterResource(id = R.drawable.person),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 16.dp)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0x40FFFFFF))
                    .padding(16.dp),
                tint = Color.White
            )
            //fullname
            Text(
                text = "${infoStudent?.maternalSurname} ${infoStudent?.paternalSurname}, ${infoStudent?.name}",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 26.sp
            )
            Text(
                text = "${infoStudent?.statusStudent}",
                fontSize = 20.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 18.sp
            )
            //username
            Text(
                text = "${infoStudent?.userName}",
                fontSize = 20.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Color(
                            0x1AFFFFFF
                        )
                    )
                    .padding(16.dp, 4.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${infoStudent?.careerName}",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 18.sp
            )
            //semestre de ingreso
            Text(
                text = "Semestre de ingreso: ${infoStudent?.admissionTermName}",
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 18.sp
            )
            //ultimo semestre cursado
            Text(
                text = "Última matricula: ${infoStudent?.lastEnrollment}",
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 18.sp
            )
            //mensaje que diga deslize hacia arriba para cerrar

        }
        Text(
            text = "Desliza hacia arriba para cerrar",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 18.sp,

            )
    }
}