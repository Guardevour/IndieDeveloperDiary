package org.guardevour.developerdiary.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.style.TextAlign
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.guardevour.developerdiary.PassWordManager

@Composable
fun RegKeyboard(
    textInput: MutableState<String>,
    passwordManager: PassWordManager,
    value: MutableState<Boolean>,
) {
    Text(text = "Enter password for this app", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 100.dp)){
        items(9){index->
            Button(onClick = {
                if (textInput.value.length < 4)
                    textInput.value = "${textInput.value}${index+1}"
                if (textInput.value.length == 4){
                    passwordManager.setPassword(textInput.value)
                    value.value = true
                }

            },
                modifier = Modifier
                    .padding(10.dp)
                    .height(65.dp)
            ) {
                Text(text = "${index+1}", fontSize = 18.sp)
            }
        }
        item {
            Button(onClick = {
                if (textInput.value.isNotEmpty()){
                    textInput.value = ""
                }
            },
                modifier = Modifier
                    .padding(10.dp)
                    .height(65.dp)) {
                Icon(Icons.Filled.Close, contentDescription = "")
            }
        }
        item {
            Button(onClick = {
                if (textInput.value.length < 4)
                    textInput.value = "${textInput.value}0"
                if (textInput.value.length == 4){
                    passwordManager.setPassword(textInput.value)
                    value.value = true
                }
            },
                modifier = Modifier
                    .padding(10.dp)
                    .height(65.dp)) {
                Text(text = "0", fontSize = 18.sp)
            }
        }
        item {
            Button(onClick = {
                if (textInput.value.isNotEmpty()){
                    textInput.value = textInput.value.dropLast(1)
                }
            },
                modifier = Modifier
                    .padding(10.dp)
                    .height(65.dp)) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "")
            }
        }
    }
}