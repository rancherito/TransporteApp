package com.unamad.aulago.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.R


//TEST
@Preview
@Composable
fun TestSquareIcon() {
    Row(
        modifier = Modifier
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        IconSquare(
            text = "Test",
            color = Color.Red
        )
        IconSquare(
            painter = painterResource(id = R.drawable.book),
            color = Color.Green
        )
    }
}


@Composable
fun IconSquare(
    painter: Painter,
    color: Color, modifier:
    Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Icon(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .background(
                color = color.copy(alpha = 0.2f),
                shape = shape
            )
            .padding(8.dp)
            .size(16.dp),
        tint = color
    )
}

@Composable
fun IconSquare(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Box(
         modifier = modifier
             .background(
                 color = color.copy(alpha = 0.2f),
                 shape = shape
             )
             .size(32.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
     ){
        Text(
            text = text,
            color = color,
            fontSize = 12.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}