package com.unamad.aulago.ui.views.teacher

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.viewModelInstance


@Composable
fun AdminDashboardView(
    viewModelStorage: ViewModelStorage = viewModelInstance(),
) {
    Card {
        Column {
            Text(text = "Ventana De inicio Adminis")
            Button(onClick = {
                viewModelStorage.generalRepository.closeSessionAsync()
            }) {
                Text(text = "Cerrar Sesión")
            }
        }
    }
}
