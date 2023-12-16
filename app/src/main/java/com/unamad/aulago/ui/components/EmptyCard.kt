package com.unamad.aulago.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unamad.aulago.R

@Composable
fun EmptyCard(
    modifier: Modifier = Modifier,
    title: String = "SIN DATOS PARA MOSTRAR",
    painter: Painter = painterResource(id = R.drawable.duotone_elements),
    content: @Composable (BoxScope.() -> Unit)? = null
) {

   // val grayscaleImageBitmap = bitmapFilter(id = R.drawable.reading, color = MaterialTheme.colorScheme.primary)

    Column(
        modifier = modifier
            .padding(18.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.height(124.dp),
            colorFilter =  ColorFilter.tint(color =  MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = title, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.surface)
        if (content != null) {
            Box {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewEmptyCard(){
    EmptyCard()
}
