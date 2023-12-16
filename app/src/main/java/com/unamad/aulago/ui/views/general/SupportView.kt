package com.unamad.aulago.ui.views.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.unamad.aulago.MaterialColors
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.emums.TextIconType
import com.unamad.aulago.ui.components.TextIcon
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.R
import com.unamad.aulago.getAppVersion

@Composable
fun SupportView() {

    val context = LocalContext.current
    CommonLayout(
        headerInformation = HeaderInformation(
            title = "SOPORTE",
            subtitle = "Ayuda y sugerencias"
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "¡Hola! Queremos expresar nuestro sincero agradecimiento por elegir utilizar nuestra aplicación en fase de desarrollo, AulaGO. Tu apoyo y feedback son clave para mejorar AulaGO. ¡Gracias por ser parte de nuestro viaje!",
                color = MaterialTheme.colorScheme.surface
            )
            Text(
                text = "Si tuviese alguna sugerencia o problema puede reportarnos el error a travéz de comentarios y capturas de pantalla.",
                color = MaterialTheme.colorScheme.surface
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Contacto  del desarrollador:", color = MaterialTheme.colorScheme.surface)

            TextIcon(
                text = "+51941643518",
                painter = painterResource(id = R.drawable.phone),
                isCopyable = true,
                textIconType = TextIconType.PhoneNumber
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Version ${getAppVersion(context)}", color = MaterialTheme.colorScheme.surface)
        }
    }
}