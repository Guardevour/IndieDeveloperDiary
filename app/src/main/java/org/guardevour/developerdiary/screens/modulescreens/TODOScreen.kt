package org.guardevour.developerdiary.screens.modulescreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.guardevour.developerdiary.dialogs.DeleteDialog
import org.guardevour.developerdiary.dialogs.NewTaskDialog
import org.guardevour.developerdiary.room.getDatabase

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TODOScreen(
    projectId: Int
) {
    val dao = getDatabase(LocalContext.current).Dao()
    val tasks = dao.getAllTasks(projectId)
    val uncompletedTasks = tasks.filter {
        !it.isCompleted
    }.toMutableStateList()

    val completedTasks = tasks.filter {
        it.isCompleted //Добавил разделение на завершенные/незавершенные
    }.toMutableStateList()

    val isNewTaskDialogOpen = remember {
        mutableStateOf(false)
    }
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ){
            item{
                Text(text = "Tasks")
            }
            items(uncompletedTasks.size){
                val isExpanded = remember {
                    mutableStateOf(false)
                }
                val isDeleteDialogOpen = remember {
                    mutableStateOf(false)
                }
                uncompletedTasks[it].DrawTask(modifier = Modifier.combinedClickable(
                    MutableInteractionSource(),
                    indication = null,
                    onClick = {isExpanded.value = !isExpanded.value},
                    onLongClick = {
                        isDeleteDialogOpen.value = true
                    }
                ),
                    isExpanded
                )
                if (isDeleteDialogOpen.value){
                    DeleteDialog(value = isDeleteDialogOpen, entity = uncompletedTasks[it], name = "task")
                }
            }

            item{
                Text(text = "Completed Tasks")
            }
            items(completedTasks.size){
                val isExpanded = remember {
                    mutableStateOf(false)
                }
                val isDeleteDialogOpen = remember {
                    mutableStateOf(false)
                }
                completedTasks[it].DrawTask(modifier = Modifier.combinedClickable(
                    MutableInteractionSource(),
                    indication = null,
                    onClick = {isExpanded.value = !isExpanded.value},
                    onLongClick = {
                        isDeleteDialogOpen.value = true
                    }
                ),
                    isExpanded
                )
                if (isDeleteDialogOpen.value){
                    DeleteDialog(value = isDeleteDialogOpen, entity = completedTasks[it], name = "task")
                }
            }
        }

        Button(onClick = { isNewTaskDialogOpen.value = true }) {
            Text(text = "New Task")
        }

        if (isNewTaskDialogOpen.value){
            NewTaskDialog(value = isNewTaskDialogOpen, tasks = uncompletedTasks, projectId = projectId)
        }
    }


}