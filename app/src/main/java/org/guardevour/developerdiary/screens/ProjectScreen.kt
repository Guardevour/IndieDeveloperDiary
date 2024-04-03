package org.guardevour.developerdiary.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guardevour.developerdiary.R
import org.guardevour.developerdiary.components.SegmentedButton
import org.guardevour.developerdiary.room.entities.Project
import org.guardevour.developerdiary.room.getDatabase
import org.guardevour.developerdiary.screens.modulescreens.DataBaseScreen
import org.guardevour.developerdiary.screens.modulescreens.DomainAnalysisScreen
import org.guardevour.developerdiary.screens.modulescreens.NotesScreen
import org.guardevour.developerdiary.screens.modulescreens.TODOScreen
import org.guardevour.developerdiary.screens.modulescreens.TSScreen

@Composable
fun ProjectScreen(
    navHostController: NavHostController,
    projectId: Int
){
    val currentPage = remember{
        mutableStateOf(Module.None)
    }
    val modules = getDatabase(LocalContext.current).Dao().getProjectByID(projectId).initProjectModules(currentPage)
    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = when(currentPage.value){
                        Module.DataBase -> Color(0xA6A51223)
                        Module.TODO -> Color(0xA6034E10)
                        Module.DomainAnalysis -> Color(0xA64C8FD6)
                        Module.Notes -> Color(0xA6757412)
                        Module.TS -> Color(0xA61C4EB3)
                        else -> Color(0xA6343C4E)
                    })) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                    navHostController.navigate("projectInfo/$projectId")
                }
                        .offset(x = (-10).dp)
                )
            }
        },
        bottomBar = {
            SegmentedButton(
                *modules.map { it }.toTypedArray()
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(currentPage.value == Module.DataBase){
                DataBaseScreen(projectId = projectId)
            }
            AnimatedVisibility(currentPage.value == Module.TODO){
                TODOScreen(projectId = projectId)
            }
            AnimatedVisibility(currentPage.value == Module.TS){
                TSScreen(projectId = projectId)
            }
            AnimatedVisibility(currentPage.value == Module.Notes){
                NotesScreen(projectId = projectId)
            }
            AnimatedVisibility(currentPage.value == Module.DomainAnalysis){
                DomainAnalysisScreen(projectId = projectId)
            }
        }
    }
}

@Composable
fun Project.initProjectModules(currentPage: MutableState<Module>): List<Pair<ImageVector, () -> Unit>>{
    val resultModules = ArrayList<Pair<ImageVector, () -> Unit>>()

    if (isEnabledDB) resultModules.add(ImageVector.vectorResource(R.drawable.database) to {
        currentPage.value = Module.DataBase
    } )
    if (isEnabledTODO) resultModules.add(ImageVector.vectorResource(R.drawable.todo)to {
        currentPage.value = Module.TODO
    } )
    if (isEnabledTS) resultModules.add(ImageVector.vectorResource(R.drawable.tech_spec) to {
        currentPage.value = Module.TS
    } )
    if (isEnabledNotes) resultModules.add(ImageVector.vectorResource(R.drawable.notes) to {
        currentPage.value = Module.Notes
    } )
    if (isEnabledDomainAnalysis) resultModules.add(ImageVector.vectorResource(R.drawable.analysis) to {
        currentPage.value = Module.DomainAnalysis
    } )

    return resultModules
}

enum class Module{
    None, DataBase, TODO, TS, Notes, DomainAnalysis
}