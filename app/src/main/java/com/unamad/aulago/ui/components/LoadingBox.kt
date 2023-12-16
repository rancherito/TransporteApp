package com.unamad.aulago.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LoadingBox(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    val opacities = remember { mutableStateOf(listOf(0.25f, 0.5f, 0.75f, 1f)) }

    val size = 30.dp
    LaunchedEffect(Unit) {
        while (true) {

            opacities.value = opacities.value.map { opacity ->
                if (opacity == .25f) 1f else opacity - 0.25f
            }
            delay(100) // Delay between each animation cycle
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
    ) {
        opacities.value.forEachIndexed { index, opacity ->
            val modifier = when (index) {
                0 -> Modifier
                    .align(Alignment.TopStart)
                    .clip(RoundedCornerShape(4.dp, 0.dp, 0.dp, 0.dp))

                1 -> Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(0.dp, 4.dp, 0.dp, 0.dp))

                2 -> Modifier
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(0.dp, 0.dp, 4.dp, 0.dp))

                else -> Modifier
                    .align(Alignment.BottomStart)
                    .clip(RoundedCornerShape(0.dp, 0.dp, 0.dp, 4.dp))
            }

            Box(
                modifier = modifier
                    .alpha(opacity)
                    .size((size - (2).dp) / 2)
                    .background(color)
            )
        }
    }
}