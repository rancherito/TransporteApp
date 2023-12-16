package com.unamad.aulago.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.unamad.aulago.toHumanDate
import com.unamad.aulago.ui.theme.SonoMono
import java.time.LocalDateTime

@Composable
fun FromToDate(
    dateStart: LocalDateTime?,
    dateEnd: LocalDateTime?,
    modifier: Modifier = Modifier
) {

    val onceDateIsNull = dateStart == null || dateEnd == null

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (!onceDateIsNull) {
                Text(
                    text = dateStart?.toHumanDate()?.lowercase() ?: "-",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "al",
                    modifier = Modifier.width(22.dp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateEnd?.toHumanDate()?.lowercase() ?: "-",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,

                    )
                }
            } else {
                Text(
                    text = "Sin definir",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                )
            }
        }

    }
}