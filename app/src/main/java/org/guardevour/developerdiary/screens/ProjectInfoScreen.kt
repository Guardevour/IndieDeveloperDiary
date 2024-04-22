package org.guardevour.developerdiary.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.guardevour.developerdiary.DataBase
import org.guardevour.developerdiary.components.CheckBox
import org.guardevour.developerdiary.components.DataBaseComboBox
import org.guardevour.developerdiary.dialogs.DeleteDialog
import org.guardevour.developerdiary.dialogs.NewTagDialog
import org.guardevour.developerdiary.room.entities.Project
import org.guardevour.developerdiary.room.getDatabase

@Composable
fun ProjectInfoScreen(
    projectId: Int,
    navHostController: NavHostController
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val dao = getDatabase(LocalContext.current).dao()
        val project = dao.getProjectByID(projectId)
        val isExpandedDBMS = remember {
            mutableStateOf(false)
        }
        val selectedValue = remember {
            mutableStateOf(project.selectedDatabase.name)
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Project:${project.name}")
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.tertiary,
                    RoundedCornerShape(10.dp)
                )
                .height(300.dp)
                .width(250.dp)
                .alpha(0.7f)
        ){
            val checkBoxValues = remember {
                mutableStateListOf(
                    mutableStateOf(project.isEnabledDB) to "DB Design module",
                    mutableStateOf(project.isEnabledTODO) to "TODO module",
                    mutableStateOf(project.isEnabledTS) to "Technical Specification module",
                    mutableStateOf(project.isEnabledNotes) to "Notes module",
                    mutableStateOf(project.isEnabledDomainAnalysis) to "Domain Analysis module"
                )
            }

            LazyColumn{
                items(checkBoxValues.size){index->
                    CheckBox(value = checkBoxValues[index].first, text = checkBoxValues[index].second)
                }
            }

            AnimatedVisibility(visible = checkBoxValues[0].first.value) {
                DataBaseComboBox(isExpanded = isExpandedDBMS, selectedValue = selectedValue)
            }

            val scope = rememberCoroutineScope()
            Button(modifier = Modifier.offset(y = (-10).dp),onClick = {
                scope.launch {
                    dao.save(
                        Project(
                            uid = project.uid,
                            name = project.name,
                            DataBase.valueOf(selectedValue.value),
                            checkBoxValues[0].first.value,
                            checkBoxValues[1].first.value,
                            checkBoxValues[2].first.value,
                            checkBoxValues[3].first.value,
                            checkBoxValues[4].first.value
                        )
                    )
                }
            }

            ) {
                Text(text = "Save new config")
            }
        }



            val tags = dao.getAllTags(projectId).toMutableStateList()

        Text(text = "Your project's tags")
            LazyColumn(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.tertiary,
                        RoundedCornerShape(10.dp)
                    )
                    .height(300.dp)
                    .width(250.dp)
                    .alpha(0.7f)
            ){
                items(tags.size){index ->
                    val isDeleteDialogOpen = remember {
                        mutableStateOf(false)
                    }
                    Text(text = tags[index].name, modifier = Modifier
                        .fillParentMaxWidth()
                        .clickable {
                            isDeleteDialogOpen.value = true
                        }, textAlign = TextAlign.Center)
                    if (isDeleteDialogOpen.value){
                        DeleteDialog(value = isDeleteDialogOpen, entity = tags[index], name = "tag",additionalOnClick = {
                            tags.remove(tags[index])
                        })
                    }
                }
                item {
                    val isNewTagDialogOpen = remember {
                        mutableStateOf(false)
                    }
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "", modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .alpha(0.75f)
                        .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                        .border(5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .clickable {
                            isNewTagDialogOpen.value = true
                        }
                    )

                    if (isNewTagDialogOpen.value){
                        NewTagDialog(value = isNewTagDialogOpen, projectId = projectId, tagList = tags)
                    }
                }
            }



    }
}