package com.unamad.aulago.ui.views.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.viewModelInstance


@Composable
fun AdminDashboardView(
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    navController: NavController = navControllerInstance()

) {
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
            Text(text = "Ventana De inicio Adminis", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Spacer(modifier = Modifier.padding(8.dp))

            Button(onClick = {
               //viewModelStorage.generalRepository.closeSessionAsync()
                navController.navigate("newConductor")

            }) {
                Text(text = "Registrar Conductor")
            }

            Button(onClick = {
                viewModelStorage.generalRepository.closeSessionAsync()
            }) {
                Text(text = "Cerrar Sesión")
            }
        }
    }
}


