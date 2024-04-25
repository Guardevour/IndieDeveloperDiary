package org.guardevour.developerdiary.screens.modulescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.guardevour.developerdiary.room.entities.Note
import org.guardevour.developerdiary.room.getDatabase

@Composable
fun NotesScreen(
    projectId: Int
) {
    val context = LocalContext.current
    val dao = getDatabase(context).dao()
    val notes = dao.getAllNotes(projectId).toMutableStateList()
    val scope = rememberCoroutineScope()

    LazyColumn(){
        item{
            Text(text = "Notes")
        }
        items(notes.size){index->
            IconButton(onClick = {
                scope.launch {
                    dao.delete(notes[index])
                    notes.remove(notes[index])
                }
            }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "")
            }
            notes[index].Draw(modifier = Modifier)
        }
        item {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "", modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .alpha(0.75f)
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                .border(5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                .clickable {
                    Note(
                        dao.getLastNote() + 1,
                        "",
                        "",
                        projectId
                    ).let { note ->
                        dao.insert(note)
                        notes.add(note)
                    }
                }
            )
        }
    }

}