package com.unamad.aulago.ui.views.teacher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unamad.aulago.NavigationApp
import com.unamad.aulago.R
import com.unamad.aulago.navControllerInstance

@Composable
fun NewConductor(
    navController: NavController = navControllerInstance()
){

    val name = remember {
        mutableStateOf("")
    }
    Column {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.navigate(NavigationApp.Administrator.DASHBOARD)
            }) {
                Icon(painter = painterResource(id = R.drawable.arrow_narrow_left), contentDescription = null, modifier = Modifier.size(24.dp))
            }
            Text(text = "Registrar Conductor".uppercase(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value = it},
                    label = {Text(text = "Nombre")}
                )
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value = it},
                    label = {Text(text = "Apellido Paterno")}
                )
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value = it},
                    label = {Text(text = "Apellido Materno")}
                )
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value = it},
                    label = {Text(text = "Genero")}
                )
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value = it},
                    label = {Text(text = "Email")}
                )
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value = it},
                    label = {Text(text = "Telefono")}
                )
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value = it},
                    label = {Text(text = "Fecha de Nacimiento")}
                )
            }
        }
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value = it},
                    label = {Text(text = "Tipo de Auto")}
                )
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value = it},
                    label = {Text(text = "Nro Asientos")}
                )
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {name.value = it},
                    label = {Text(text = "Placas")}
                )
            }
        }
    }

}