package org.guardevour.developerdiary.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.guardevour.developerdiary.dialogs.DeleteDialog
import org.guardevour.developerdiary.dialogs.NewProjectDialog
import org.guardevour.developerdiary.room.getDatabase

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navHostController: NavHostController
){
    val db = getDatabase(LocalContext.current)

    val projects = db.dao().getAllProjects().toMutableStateList()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Special Thanks to GRizzi91 for a great library for reading PDF - bouquet",
            fontSize = 8.sp
        )
        Text(text = "Welcome!")

            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier.height(400.dp)){
                items(if (projects.isNotEmpty()) projects.size else 0){index->
                    if (index+1>0){
                        val isOpenedDeleteDialog = remember { mutableStateOf(false) }
                        projects[index].Draw(
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        navHostController.navigate("project/${projects[index].uid}")
                                    },
                                    onLongClick = {
                                        isOpenedDeleteDialog.value = true
                                    }
                                )
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(11.dp))
                        )
                        if (isOpenedDeleteDialog.value){
                            DeleteDialog(value = isOpenedDeleteDialog, projects[index], projects[index].name){
                                projects.remove(projects[index])
                            }
                        }
                    }
                }
                item {
                    val isOpenedAddDialog = remember { mutableStateOf(false) }
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "", modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .padding(5.dp)
                        .alpha(0.75f)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
                        .clickable {
                            isOpenedAddDialog.value = true
                        }
                    )
                    if (isOpenedAddDialog.value){
                        NewProjectDialog(value = isOpenedAddDialog, navHostController)
                }
            }
        }
    }

}
