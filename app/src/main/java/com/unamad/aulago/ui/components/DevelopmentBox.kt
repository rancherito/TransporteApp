package com.unamad.aulago.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.R

@Composable
fun DevelopmentBox(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.console),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                myColors().body
            ),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "DEVELOPMENT MODE",
            fontFamily = FontFamily.Monospace,
            color = myColors().body
        )
    }
}