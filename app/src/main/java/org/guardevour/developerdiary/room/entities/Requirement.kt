package org.guardevour.developerdiary.room.entities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.coroutines.launch
import org.guardevour.developerdiary.room.getDatabase


@Entity(
    foreignKeys = [ForeignKey(
        entity = Domain::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("domain_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
class Requirement(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "requirement_info") var reqInfo: String,
    @ColumnInfo(name = "domain_id") val domainId: Int,
) : DrawableEntity {
    @Composable
    override fun Draw(modifier: Modifier) {
        var reqInfo by remember {
            mutableStateOf(reqInfo)
        }
        val scope = rememberCoroutineScope()
        val dao = getDatabase(LocalContext.current).dao()
        IconButton(onClick = {
            scope.launch {
                this@Requirement.reqInfo = reqInfo
                dao.save(this@Requirement)
            }
        }) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "")
        }
        TextField(
            value = reqInfo,
            onValueChange = {newText -> reqInfo = newText},
            label = { Text(text = "Requirement") }
        )
    }
}