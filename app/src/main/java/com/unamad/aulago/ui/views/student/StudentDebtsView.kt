package com.unamad.aulago.ui.views.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unamad.aulago.Utils
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.ui.components.EmptyCard
import com.unamad.aulago.ui.components.payment.PaymentCard
import com.unamad.aulago.ui.components.payment.PaymentTotalCard
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.viewModelInstance
import java.time.LocalDateTime

@Composable
fun StudentDebtsView(
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {

    val debts = viewModelStorage.generalRepository.paymentsUserStream.observeAsState(listOf()).value
    //viewModelStorage.studentRepository.LoadDebts()

    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Deudas",
            subtitle = "Listado de deudas pendientes",
            indicator = "* Pago necesario para matrículas y graduación"
        )
    ) {
        if (debts.isEmpty()) {
            EmptyCard(title = "No hay deudas pendientes")
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(debts) {
                        val date = LocalDateTime.parse(Utils.forceUTC(it.issueDate))

                        PaymentCard(
                            mount = it.total,
                            concept = it.conceptCode ?: "",
                            description = it.description,
                            date = date
                        )
                    }
                }
                PaymentTotalCard(
                    mount = debts.map { it.total }.reduce { acc, d -> d + acc },
                    modifier = Modifier.padding(16.dp, 0.dp)
                )

            }
        }
    }
}