package com.unamad.aulago.ui.views.conductor

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.viewModelInstance


@Composable
fun ConductorDashboardView(
    viewModelStorage: ViewModelStorage = viewModelInstance(),
) {

    Card {
       Column {
           Text(text = "Ventana De inicio uSUARIO CONDUCTOR")
           Button(onClick = {
               viewModelStorage.generalRepository.closeSessionAsync()
           }) {
               Text(text = "Cerrar Sesión")
           }
       }
    }

}