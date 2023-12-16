package com.unamad.aulago.ui.components.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import com.unamad.aulago.ui.theme.myColors

@Composable
fun PaymentTotalCard(mount: Double, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "MONTO TOTAL", style = TextStyle(
                fontSize = 12.sp,
                color = myColors().bodyAlt
            ),
            modifier = Modifier.offset(x = 16.dp)
        )
        Text(text = "S/ $mount", fontFamily = FontFamily.Monospace, modifier = Modifier.card().padding(16.dp))
    }
}

@Preview
@Composable
fun PreviewPaymentTotal() {
    PaymentTotalCard(
        mount = 20.0,
    )
}