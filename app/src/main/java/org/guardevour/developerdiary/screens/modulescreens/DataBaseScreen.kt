package org.guardevour.developerdiary.screens.modulescreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.guardevour.developerdiary.R
import org.guardevour.developerdiary.components.CheckBox
import org.guardevour.developerdiary.createFileAndSave
import org.guardevour.developerdiary.dialogs.DeleteDialog
import org.guardevour.developerdiary.dialogs.NewTableDialog
import org.guardevour.developerdiary.getSqlFromProject
import org.guardevour.developerdiary.room.getDatabase
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DataBaseScreen(
    projectId: Int
) {
    val isOpenedTable = remember {
        mutableStateOf(true)
    }
    val currentTable = remember {
        mutableIntStateOf(-1)
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dao by remember{ mutableStateOf(getDatabase(context = context).dao()) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        CheckBox(
            value = isOpenedTable, text = "", iconSelected = ImageVector.vectorResource(R.drawable.db_project),
            iconUnselected = ImageVector.vectorResource(R.drawable.db_visual)
        )

        Button(
            onClick = { scope.launch {
            createFileAndSave("${dao.getProjectByID(projectId).name}-${LocalDateTime.now()}.txt", getSqlFromProject(context = context, projectId), context = context)
        } }) {
            Text(text = "Export DB")
        }
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        val tables = dao.getAllTables(projectId).toMutableStateList()
        val isNewTableDialogOpen = remember { mutableStateOf(false) }
        AnimatedVisibility(visible = currentTable.intValue != -1 && isOpenedTable.value) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .height(550.dp)
                    .padding(10.dp)
            ) {
                key(currentTable.intValue){
                    tables[currentTable.intValue].Draw(modifier = Modifier)
                }

            }

        }
        AnimatedVisibility(!isOpenedTable.value) {
            DataBaseVisualisationScreen(projectId = projectId)
        }
        LazyRow(

        ){
            items(tables.size){index->
                val isOpenedDeleteDialog = remember { mutableStateOf(false) }
                tables[index].DrawMin(modifier = Modifier.combinedClickable(
                    onClick = {
                        currentTable.intValue = index
                    },
                    onLongClick = {
                        isOpenedDeleteDialog.value = true
                    }
                )
                )
                if (isOpenedDeleteDialog.value){
                    DeleteDialog(value = isOpenedDeleteDialog, tables[index], tables[index].name){
                        tables.remove(tables[index])
                    }
                }
            }
            item {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "", modifier = Modifier
                        .height(90.dp)
                        .width(90.dp)
                        .padding(5.dp)
                        .alpha(0.75f)
                        .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                        .border(5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .clickable {
                            isNewTableDialogOpen.value = true
                        }
                    )
               if (isNewTableDialogOpen.value) NewTableDialog(value = isNewTableDialogOpen, projectId, tables)

            }
        }
    }
}