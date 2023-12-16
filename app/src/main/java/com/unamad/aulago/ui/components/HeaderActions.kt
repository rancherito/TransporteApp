package com.unamad.aulago.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance

@Composable
fun HeaderActions(
    modifier: Modifier = Modifier,
    headerInformation: HeaderInformation? = null,
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    content: @Composable (() -> Unit)? = null
) {
    val headerInformationViewModel =
        headerInformation ?: HeaderInformation(title = "INICIO")
    viewModelStorage.isContrast.postValue(true)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = headerInformationViewModel.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Black

            )

            AnimatedVisibility(visible = headerInformationViewModel.subtitle != null) {
                Text(
                    text = headerInformationViewModel.subtitle!!,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    color = myColors().body,

                    )
            }
            AnimatedVisibility(visible = headerInformationViewModel.indicator != null) {
                Text(
                    text = headerInformationViewModel.indicator!!,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = myColors().gray200,
                    )
                )
            }
        }
        if (content != null) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHeader() {
    HeaderActions()
}
