package com.unamad.aulago.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.R


@Composable
fun ChipSquare(label: String, icon: Painter? = null) {
    Row(
        modifier = Modifier
            .background(
                color = myColors().card,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp, 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = myColors().text,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            color = myColors().text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
//PREVIEW
@Preview
@Composable
fun PreviewChipSquare() {
    Column (
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        ChipSquare("GRUPO 1")
        ChipSquare("GRUPO 1", painterResource(id = R.drawable.book))
    }
}