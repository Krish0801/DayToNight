package com.example.daytonight.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(searchState: MutableState<TextFieldValue>, searchFn: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(3.dp, Color.Black, shape = MaterialTheme.shapes.large)
    ) {
        TextField(
            value = searchState.value,
            onValueChange = { value ->
                searchState.value = value
            },
            placeholder = { Text("Enter City Name") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color(0xFFFF7300), fontSize = 18.sp),
            leadingIcon = {
                if (searchState.value.text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            searchState.value = TextFieldValue("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color.Black
                        )
                    }
                }
            },
            trailingIcon = {
                if (searchState.value.text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            searchFn(searchState.value.text)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}



