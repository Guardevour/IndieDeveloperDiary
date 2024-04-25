package org.guardevour.developerdiary.screens.modulescreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.guardevour.developerdiary.dialogs.NewTaskDialog
import org.guardevour.developerdiary.room.getDatabase

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TODOScreen(
    projectId: Int
) {
    val dao = getDatabase(LocalContext.current).dao()
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
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(5.dp)
        ){
            item{
                Text(text = "Tasks")
            }
            items(uncompletedTasks.size){
                val isExpanded = remember {
                    mutableStateOf(false)
                }
                var isDeleted by remember{ mutableStateOf(true)}
                AnimatedVisibility(visible = isDeleted) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(10.dp)
                        )

                    ) {
                        IconButton(onClick = {
                            uncompletedTasks[it].isCompleted = true
                            dao.complete(uncompletedTasks[it])
                            completedTasks.add(uncompletedTasks[it])
                            uncompletedTasks.remove(uncompletedTasks[it])
                        }) {
                            Icon(imageVector = Icons.Filled.Check, contentDescription = "")
                        }
                        uncompletedTasks[it].DrawTask(modifier = Modifier.clickable(
                            MutableInteractionSource(),
                            indication = null,
                            onClick = { isExpanded.value = !isExpanded.value }
                        ),
                            isExpanded
                        )
                        IconButton(onClick = {
                            isDeleted = false
                            dao.delete(uncompletedTasks[it])
                            uncompletedTasks.remove(uncompletedTasks[it])
                        }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "")
                        }
                    }
                }
            }

            item{
                Text(text = "Completed Tasks")
            }
            items(completedTasks.size){
                val isExpanded = remember {
                    mutableStateOf(false)
                }
                var isDeleted by remember{ mutableStateOf(false)}
                AnimatedVisibility(visible = !isDeleted) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(10.dp)
                            )
                            .alpha(0.6f)
                    ) {
                        Icon(imageVector = Icons.Filled.ThumbUp, contentDescription = "")
                        completedTasks[it].DrawTask(modifier = Modifier.clickable(
                            MutableInteractionSource(),
                            indication = null,
                            onClick = {isExpanded.value = !isExpanded.value}
                        ),
                            isExpanded
                        )
                        IconButton(onClick = {
                            isDeleted = true
                            dao.delete(completedTasks[it])
                            completedTasks.remove(completedTasks[it])
                        }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "")
                        }
                    }
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