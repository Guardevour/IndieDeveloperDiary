package org.guardevour.developerdiary.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.guardevour.developerdiary.room.entities.Project
import org.guardevour.developerdiary.room.entities.Table
import org.guardevour.developerdiary.room.getDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTableDialog(
    value: MutableState<Boolean>,
    projectId: Int,
    tableList: SnapshotStateList<Table>
){
    AlertDialog(onDismissRequest = { value.value = false }) {
       Dialog{
            val name = remember {
                mutableStateOf("")
            }

           Column(
               verticalArrangement = Arrangement.SpaceBetween,
               modifier = Modifier
                   .padding(30.dp)
                   .fillMaxHeight()
           ) {
               Text(text = "[New Table]", fontSize = 19.sp, textAlign = TextAlign.Center, modifier = Modifier
                   .fillMaxWidth()
                   .offset(y = 10.dp))

               TextField(value = name.value, onValueChange = {
                   name.value = it
               },
                   placeholder = { Text(text = "Enter new table name") },
                   label = {
                       Text(text = "Name")
                   }
               )
               val dao = getDatabase(LocalContext.current).Dao()
               Button(onClick = {
                       if (name.value != ""){
                           val table = Table(
                               dao.getLastTable() + 1,
                               name.value.trim(),
                               projectId
                           )
                           dao.addTable(
                               table
                           )
                           tableList.add(table)
                           value.value = false
                    }

               }, modifier = Modifier.fillMaxWidth()) {
                   Text(text = "Add Table")
               }
           }
       }
    }
}