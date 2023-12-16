package com.unamad.aulago.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.ui.theme.WrapperTheme
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R

@Composable
fun SpLashView(
    modifier: Modifier = Modifier,
    viewModelStorage: ViewModelStorage =  viewModelInstance(),
    internet: Boolean = viewModelStorage.isOnline.observeAsState(true).value
) {

    WrapperTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.primary
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logoaulagofull),
                contentDescription = null, Modifier.size(80.dp),
                tint = myColors().textDark
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 26.sp,
                color = myColors().textDark,
                fontWeight = FontWeight.Bold
            )
            /*Spacer(modifier = Modifier.padding(8.dp))
            if (!internet){
                Text(
                    text = "No hay conexión a internet",
                    fontSize = 12.sp,
                    color = myColors().textDark,
                    fontWeight = FontWeight.Bold
                )
            }*/

        }
    }
}

