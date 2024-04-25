package org.guardevour.developerdiary.room.entities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.coroutines.launch
import org.guardevour.developerdiary.room.getDatabase

@Entity(
    foreignKeys = [ForeignKey(
        entity = Project::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("project_name"),
        onDelete = ForeignKey.CASCADE
    )]
)
class Note(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "header") var header: String,
    @ColumnInfo(name = "content") var content: String,
    @ColumnInfo(name = "project_name") val prId: Int,
) : DrawableEntity {
    @Composable
    override fun Draw(modifier: Modifier) {
        var header by remember {
            mutableStateOf(header)
        }
        var content by remember {
            mutableStateOf(content)
        }
        val scope = rememberCoroutineScope()
        val dao = getDatabase(LocalContext.current).dao()
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)
                )
                .padding(5.dp)
        ) {
            Row {
                 IconButton(onClick = {
                    scope.launch {
                        this@Note.header = header
                        this@Note.content = content
                        dao.save(this@Note)
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "")
                }
                TextField(value = header, onValueChange = {newHeader-> header = newHeader})
            }
            TextField(value = content, onValueChange = {newContent-> content = newContent})
        }
    }
}