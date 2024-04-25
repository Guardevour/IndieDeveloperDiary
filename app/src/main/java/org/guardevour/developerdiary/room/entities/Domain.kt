package org.guardevour.developerdiary.room.entities

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
class Domain(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "project_name") val prId: Int
) {
    @Composable
    fun Draw() {
        val context = LocalContext.current
        val dao = getDatabase(context).dao()
        val requirements = dao.getAllRequirements(uid).toMutableStateList()
        val scope = rememberCoroutineScope()
        var name by remember {
            mutableStateOf(name)
        }


        Column{
            Row{
                IconButton(onClick = {
                    scope.launch {
                        this@Domain.name = name
                        dao.save(this@Domain)
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "")
                }
                TextField(
                    value = name,
                    onValueChange = {newText -> name = newText},
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                )

            }
            requirements.forEach{ requirement->
               Row(modifier = Modifier.offset(x = 20.dp)) {
                   IconButton(onClick = {
                       scope.launch {
                           dao.delete(requirement)
                           requirements.remove(requirement)
                       }
                   }) {
                       Icon(imageVector = Icons.Filled.Close, contentDescription = "")
                   }
                   requirement.Draw(modifier = Modifier)

               }
            }

                Icon(imageVector = Icons.Filled.Add, contentDescription = "", modifier = Modifier
                    .width(200.dp)
                    .padding(5.dp)
                    .alpha(0.75f)
                    .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                    .border(5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                    .clickable {
                        Requirement(
                            dao.getLastRequirement() + 1,
                            "",
                            uid
                        ).let { requirement ->
                            dao.insert(requirement)
                            requirements.add(requirement)
                        }
                    }
                )

        }
    }
}