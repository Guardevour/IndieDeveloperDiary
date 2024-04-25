package org.guardevour.developerdiary.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.guardevour.developerdiary.room.entities.Task
import org.guardevour.developerdiary.room.getDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskDialog(value: MutableState<Boolean>,
                 tasks: SnapshotStateList<Task>,
                  projectId: Int
) {
    val dao = getDatabase(LocalContext.current).dao()
    val scope = rememberCoroutineScope()
    val tags = dao.getAllTags(projectId)
    val difficulties = listOf(
        "Easy",
        "Medium",
        "Hard",
        "No sleep night"
    )

    var isDifficultiesExpanded by remember {
        mutableStateOf(false)
    }

    var isTagsExpanded by remember {
        mutableStateOf(false)
    }
    AlertDialog(onDismissRequest = { value.value = false }) {
        Dialog(height = 500.dp) {
            val properties = remember{
                mutableStateListOf(
                    mutableStateOf(""),
                    mutableStateOf(""),
                    mutableStateOf(""),
                    mutableStateOf(if (tags.isEmpty()) "" else tags[0].name)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .height(350.dp)
            ) {
                TextField(value = properties[0].value, onValueChange = { properties[0].value = it}, label = { Text(
                    text = "Name"
                )})

                TextField(value = properties[1].value, onValueChange = { properties[1].value = it},label = { Text(
                    text = "Description"
                )})

                ExposedDropdownMenuBox(expanded = isDifficultiesExpanded, onExpandedChange = { isDifficultiesExpanded = !isDifficultiesExpanded}){
                      TextField(
                            value = properties[2].value,
                            onValueChange = {properties[2].value = it},
                            readOnly = true,
                            label = { Text(text = "Difficulty of Task") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDifficultiesExpanded) },
                            modifier = Modifier.menuAnchor()

                        )

                        ExposedDropdownMenu(
                            expanded =isDifficultiesExpanded,
                            onDismissRequest = { isDifficultiesExpanded= false }
                        ) {
                            difficulties.forEach { difficulty ->
                                DropdownMenuItem(text = { Text(text = difficulty)}, onClick = {
                                    properties[2].value = difficulty
                                    isDifficultiesExpanded = false
                                })
                            }


                        }

                }

                ExposedDropdownMenuBox(expanded = isTagsExpanded, onExpandedChange = { isTagsExpanded = !isTagsExpanded }){
                     TextField(
                            value = properties[3].value,
                            onValueChange = {properties[3].value = it},
                            readOnly = true,
                            label = { Text(text = "Tag of Task") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTagsExpanded) },
                            modifier = Modifier.menuAnchor()

                        )

                        ExposedDropdownMenu(
                            expanded = isTagsExpanded,
                            onDismissRequest = { isTagsExpanded= false }
                        ) {
                            if (tags.isNotEmpty()){
                                tags.forEach { tag ->
                                    DropdownMenuItem(text = { Text(text = tag.name)}, onClick = {
                                        properties[3].value = tag.name
                                        isTagsExpanded = false
                                    })
                                }
                            }

                        }

                }


                Button(onClick = {
                    scope.launch {
                        Task(
                            uid = dao.getLastTask() + 1,
                            name = properties[0].value ,
                            description = properties[1].value,
                            difficulty = properties[2].value ,
                            tag = properties[3].value,
                            prId = projectId,
                            isCompleted = false
                        ).let {task ->
                            dao.addTask(task)
                            tasks.add(task)
                        }
                        value.value = false
                    }
                }) {
                    Text(text = "Create Task")
                }

            }

        }
    }
}