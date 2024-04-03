package org.guardevour.developerdiary.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import org.guardevour.developerdiary.DataBase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataBaseComboBox(isExpanded: MutableState<Boolean>, selectedValue: MutableState<String>) {
    ExposedDropdownMenuBox(
        expanded = isExpanded.value,
        onExpandedChange = {
            isExpanded.value = !isExpanded.value
        }
    ){
        TextField(
        value = selectedValue.value,
        onValueChange = {},
        readOnly = true,
        label = { Text(text = "Choose DB for your project") },
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded.value) },
        modifier = Modifier.menuAnchor()
    )
        ExposedDropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false }
        ) {
            DataBase.entries.forEach{ db->
                DropdownMenuItem(text = { Text(text = db.name) }, onClick = {
                    selectedValue.value = db.name
                    isExpanded.value = false
                })
            }
        }
    }
}