package com.unamad.aulago.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unamad.aulago.card
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.ui.theme.myColors

@Composable
fun DashBoardBox(
    text: String,
    iconSource: Int,
    modifier: Modifier = Modifier,
    link: String? = null,
    counter: Int = 0,
    navController: NavHostController = navControllerInstance(),
    isSmall: Boolean = false,
    width: Dp = 500.dp
) {
    val enabled = link != null
    val height = if (isSmall) 80.dp else 120.dp
    val sizeIcon = if (isSmall) 24.dp else 40.dp
    val titleSize = if (isSmall) 12.sp else 14.sp
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .card()
                .clickable { if (enabled) navController!!.navigate(link!!) }
                .height(height)
                .widthIn(80.dp, width)
                .fillMaxWidth(),

            ) {
            Icon(
                painter = painterResource(id = iconSource),
                contentDescription = null,
                modifier = Modifier.size(sizeIcon),
                tint = if (enabled) MaterialTheme.colorScheme.primary else myColors().card
            )

            Text(
                text = text,
                fontSize = titleSize,
                modifier = Modifier.offset(y = 8.dp).fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (counter > 0) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(alignment = Alignment.TopEnd)
                    .offset(4.dp, (-4).dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(100f)
                    ),

                contentAlignment = Alignment.Center
            ) {
                Text(text = counter.toString(), fontSize = 10.sp, color = myColors().textDark)
            }
        }
    }
}

@Composable
fun SmallDashBoardBox(
    text: String,
    iconSource: Int,
    modifier: Modifier = Modifier,
    link: String? = null,
    counter: Int = 0,
    width: Dp = 500.dp
) {
    DashBoardBox(
        text = text,
        iconSource = iconSource,
        modifier = modifier,
        link = link,
        counter = counter,
        isSmall = true,
        width = width
    )
}