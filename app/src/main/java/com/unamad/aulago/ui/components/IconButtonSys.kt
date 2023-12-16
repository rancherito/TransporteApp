package com.unamad.aulago.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unamad.aulago.R

@Composable
fun IconButtonSys(
    onClick: () -> Unit,
    painter: Painter,
    color: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(36.dp)
            .padding(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.08f),
            contentColor = color
        ),
        contentPadding = PaddingValues(0.dp),
        enabled = enabled,
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Preview
@Composable
fun ttt() {
    IconButtonSys(onClick = {}, painter = painterResource(id = R.drawable.list_check))
}