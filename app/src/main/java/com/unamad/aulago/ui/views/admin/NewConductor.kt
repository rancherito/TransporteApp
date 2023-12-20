package com.unamad.aulago.ui.views.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unamad.aulago.NavigationApp
import com.unamad.aulago.R
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.viewModelInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewConductor(
    navController: NavController = navControllerInstance(),
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {

    val nombres = remember {
        mutableStateOf("")
    }
    val apellidoPaterno = remember {
        mutableStateOf("")
    }
    val apellidoMaterno = remember {
        mutableStateOf("")
    }
    val genero = remember {
        mutableStateOf("")
    }
    val email = remember {
        mutableStateOf("")
    }
    val telefono = remember {
        mutableStateOf("")
    }
    val fechaNacimiento = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }


    val tipoAuto = remember {
        mutableStateOf("Auto")
    }

    val openTipoAuto = remember {
        mutableStateOf(false)
    }


    val nroAsientos = remember {
        mutableStateOf(0)
    }
    val placa = remember {
        mutableStateOf("")
    }
    val modelo = remember {
        mutableStateOf("")
    }
    val marca = remember {
        mutableStateOf("")
    }
    val usuario = remember {
        mutableStateOf("")
    }


    //TODO: Agregar los demas campos
    LazyColumn {
        item {
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
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_narrow_left),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Registrar Conductor".uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
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
                        value = nombres.value,
                        onValueChange = { nombres.value = it },
                        label = { Text(text = "Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = apellidoPaterno.value,
                        onValueChange = { apellidoPaterno.value = it },
                        label = { Text(text = "Apellido Paterno") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = apellidoMaterno.value,
                        onValueChange = { apellidoMaterno.value = it },
                        label = { Text(text = "Apellido Materno") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = genero.value,
                        onValueChange = { genero.value = it },
                        label = { Text(text = "Genero") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text(text = "Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = telefono.value,
                        onValueChange = { telefono.value = it },
                        label = { Text(text = "Telefono") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = fechaNacimiento.value,
                        onValueChange = { fechaNacimiento.value = it },
                        label = { Text(text = "Fecha de Nacimiento") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    //password
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text(text = "Contraseña") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    //usuario
                    OutlinedTextField(
                        value = usuario.value,
                        onValueChange = { usuario.value = it },
                        label = { Text(text = "Usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        item {
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
                    Box {

                        OutlinedCard(
                            onClick = { openTipoAuto.value = true }
                        ) {
                            Text(
                                text = "Tipo de Auto: ${tipoAuto.value}",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                            )
                        }
                        DropdownMenu(
                            expanded = openTipoAuto.value,
                            onDismissRequest = { openTipoAuto.value = false }) {
                            //rellena 3 items

                            Text(
                                text = "Auto",
                                modifier = Modifier.clickable {
                                    tipoAuto.value = "Auto"; openTipoAuto.value = false
                                })
                            Text(
                                text = "Motocicleta",
                                modifier = Modifier.clickable {
                                    tipoAuto.value = "Camioneta"; openTipoAuto.value = false
                                })
                            Text(
                                text = "Mototaxi",
                                modifier = Modifier.clickable {
                                    tipoAuto.value = "Mototaxi"; openTipoAuto.value = false
                                })
                            Text(
                                text = "Combi",
                                modifier = Modifier.clickable {
                                    tipoAuto.value = "Combi"; openTipoAuto.value = false
                                })


                        }
                    }


                    OutlinedTextField(
                        value = nroAsientos.value.toString(),
                        onValueChange = { nroAsientos.value = it.toInt() },
                        label = { Text(text = "Nro Asientos") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = placa.value,
                        onValueChange = { placa.value = it },
                        label = { Text(text = "Placa") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = modelo.value,
                        onValueChange = { modelo.value = it },
                        label = { Text(text = "Modelo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = marca.value,
                        onValueChange = { marca.value = it },
                        label = { Text(text = "Marca") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        item {
            //boton guardar
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Button(onClick = {
                    viewModelStorage.adminRepository.registrarConductor(
                        nombre = nombres.value,
                        apellidoPaterno = apellidoPaterno.value,
                        apellidoMaterno = apellidoMaterno.value,
                        telefono = telefono.value,
                        genero = genero.value,
                        fechaNacimiento = fechaNacimiento.value,
                        email = email.value,
                        password = password.value,
                        placa = placa.value,
                        modelo = modelo.value,
                        marca = marca.value,
                        tipo = tipoAuto.value,
                        nroAsientos = nroAsientos.value,
                        usuario = usuario.value
                    )
                }) {
                    Text(text = "Guardar")
                }
            }
        }
    }

}