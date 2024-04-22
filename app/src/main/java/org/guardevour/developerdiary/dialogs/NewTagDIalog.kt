package org.guardevour.developerdiary.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.guardevour.developerdiary.room.entities.Tag
import org.guardevour.developerdiary.room.getDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTagDialog(
    value: MutableState<Boolean>,
    projectId: Int,
    tagList: SnapshotStateList<Tag>
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
                Text(text = "[New Tag]", fontSize = 19.sp, textAlign = TextAlign.Center, modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 10.dp))

                TextField(value = name.value, onValueChange = {
                    name.value = it
                },
                    placeholder = { Text(text = "Enter new tag name") },
                    label = {
                        Text(text = "Name")
                    }
                )
                val dao = getDatabase(LocalContext.current).dao()
                Button(onClick = {
                    if (name.value != ""){
                        val tag = Tag(
                            name.value,
                            projectId
                        )
                        dao.addTag(tag)
                        tagList.add(tag)
                        value.value = false
                    }

                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Add Tag")
                }
            }
        }
    }
}