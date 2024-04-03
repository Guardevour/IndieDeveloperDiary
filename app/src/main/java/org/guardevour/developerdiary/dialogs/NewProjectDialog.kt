package org.guardevour.developerdiary.dialogs

import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.guardevour.developerdiary.DataBase
import org.guardevour.developerdiary.components.CheckBox
import org.guardevour.developerdiary.components.DataBaseComboBox
import org.guardevour.developerdiary.room.entities.Project
import org.guardevour.developerdiary.room.entities.Tag
import org.guardevour.developerdiary.room.getDatabase
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectDialog(
    value: MutableState<Boolean>,
    navHostController: NavHostController
){
    AlertDialog(onDismissRequest = { value.value = false }) {
        Dialog(height = 530.dp) {
            var name by remember { mutableStateOf("") }

            val checkBoxValues = remember {
                mutableStateListOf(
                    mutableStateOf(true) to "DB Design module",
                    mutableStateOf(true) to "TODO module",
                    mutableStateOf(true) to "Technical Specification module",
                    mutableStateOf(true) to "Notes module",
                    mutableStateOf(true) to "Domain Analysis module"
                )
            }
            val isExpandedDBMS = remember {
                mutableStateOf(false)
            }
            val selectedValue = remember {
                mutableStateOf("None")
            }

            val tags = remember { mutableStateListOf<String>() }
            var newTag by remember { mutableStateOf("") }

            Text(text = "[New Project]", fontSize = 19.sp, textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxWidth()
                .offset(y = 10.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(value = name, onValueChange = {
                    name = it
                },
                    placeholder = { Text(text = "Enter new project name")},
                    label = {
                        Text(text = "Name")
                    }
                )
                LazyColumn{
                    items(checkBoxValues.size){index->
                        CheckBox(value = checkBoxValues[index].first, text = checkBoxValues[index].second)
                    }
                }
                AnimatedVisibility(visible = checkBoxValues[0].first.value) {
                    DataBaseComboBox(isExpanded = isExpandedDBMS, selectedValue = selectedValue)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
            AnimatedVisibility(visible = checkBoxValues[1].first.value) {
                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    TextField(value = newTag, onValueChange = {
                        newTag = it
                    },
                        placeholder = { Text(text = "Enter new tag for tasks in project")},
                        label = {
                            Text(text = "Tag")
                        }
                    )
                    Text(text = tags.joinToString(prefix = "<", postfix = ">", separator = ", "), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    Button(onClick = {
                        if (newTag.trim() != "") tags.add(newTag.trim())
                        newTag = ""
                    }) {
                        Text(text = "Add tag")
                    }
                }
            }

            val context = LocalContext.current
            
            Button(modifier = Modifier.offset(y = (-10).dp),onClick = {
                if (name != ""){
                    val newProject = Project(
                        getDatabase(context).Dao().getLastProject() + 1,
                        name,
                        DataBase.valueOf(selectedValue.value),
                        checkBoxValues[0].first.value,
                        checkBoxValues[1].first.value,
                        checkBoxValues[2].first.value,
                        checkBoxValues[3].first.value,
                        checkBoxValues[4].first.value
                    )
                    val dao = getDatabase(context).Dao()
                    dao.newProject(
                        newProject
                    )

                    tags.forEach {tag->
                        dao.addTag(
                            Tag(
                                tag,
                                newProject.uid
                            )
                        )
                    }
                    value.value = false
                    navHostController.navigate("project/${newProject.uid}")
                }
                else{
                    Toast.makeText(context, "Invalid project name", Toast.LENGTH_SHORT).show()
                }

            }
            ) {
                Text(text = "Create project")
            }
        }
    }
}


