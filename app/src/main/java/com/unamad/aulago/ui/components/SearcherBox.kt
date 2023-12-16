package com.unamad.aulago.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.unamad.aulago.Utils.Companion.toJSON
import com.unamad.aulago.card
import com.unamad.aulago.removeAccents
import com.unamad.aulago.ui.layouts.rememberBoolean
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.R

@Composable
fun <T> SearcherBox(
    list: List<T>,
    placeholder: String = "Buscar...",
    showState: MutableState<Boolean> = rememberBoolean(),
    onSelected: ((T) -> Unit)? = null,
    item: (@Composable (T) -> Unit)? = null,
) {
    val search: MutableState<String> = remember {
        mutableStateOf("")
    }
    val notFocus = myColors().gray100
    val focus = MaterialTheme.colorScheme.primary
    val outlineColor = remember { mutableStateOf(notFocus) }

    val filter =
        list.filter {
            search.value == "" || it.toJSON().removeAccents()
                .contains(search.value.removeAccents(), ignoreCase = true)
        }
    if (showState.value) {
        Dialog(onDismissRequest = {
            showState.value = false
            search.value = ""
        }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .card(shadow = true)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                1.dp,
                                color = outlineColor.value,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = androidx.compose.ui.Alignment.CenterStart
                    ) {


                        if (search.value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color.LightGray,
                                modifier = Modifier.padding(16.dp, 0.dp)
                            )
                        }
                        BasicTextField(
                            value = search.value,
                            onValueChange = {
                                search.value = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 8.dp)
                                .onFocusChanged {
                                    outlineColor.value = if (it.isFocused) focus else notFocus
                                },
                            singleLine = true
                        )
                    }
                    if (search.value.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(shape = CircleShape)
                                .clickable {
                                    search.value = ""
                                }
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),

                    )
                {
                    items(filter) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .card(Color.Transparent)
                                .clickable {
                                    if (onSelected != null) {
                                        onSelected(it)
                                    }
                                    showState.value = false
                                    search.value = ""

                                }
                                .padding(16.dp, 8.dp)
                        ) {
                            if (item != null) {
                                item(it)
                            }
                        }
                    }
                    if (filter.isEmpty()) {
                        item {
                            Text(
                                text = "No se encontraron resultados",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .card()
                                    .padding(16.dp)
                                , color = MaterialTheme.colorScheme.surface
                            )

                        }
                    }

                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(myColors().gray100)
                )
                Text(
                    text = "Cancelar",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showState.value = false
                            search.value = ""
                        }
                        .padding(16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.surface
                )


            }
        }
    }

}

