package com.unamad.aulago.ui.components.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.card
import com.unamad.aulago.toHumanDate
import com.unamad.aulago.ui.theme.myColors
import java.time.LocalDateTime

@Composable
fun PaymentCard(mount: Double, concept: String, description: String, date: LocalDateTime) {
    //val paymentText = Modifier.
    val textStyle = TextStyle(
        fontSize = 12.sp,
        color = myColors().bodyAlt
    )
    Row(
        modifier = Modifier
            .card()
            .padding(16.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = description.uppercase(), fontSize = 11.sp, lineHeight = 13.sp)
            Text(text = "concepto: $concept  fecha: ${date.toHumanDate().lowercase()}", style = textStyle, color = MaterialTheme.colorScheme.primary)

        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "monto", style = textStyle)
            Text(text = "S/ $mount", fontFamily = FontFamily.Monospace)
        }

    }
}

@Preview
@Composable
fun PreviewPaymentCard() {
    PaymentCard(
        mount = 20.0,
        concept = "0111",
        description = "POR PAGOS DE MATRICULA",
        date = LocalDateTime.now()
    )
}