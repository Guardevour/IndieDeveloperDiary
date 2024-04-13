package org.guardevour.developerdiary.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.guardevour.developerdiary.room.entities.DrawableEntity
import org.guardevour.developerdiary.room.entities.Field
import org.guardevour.developerdiary.room.entities.Project
import org.guardevour.developerdiary.room.entities.Relation
import org.guardevour.developerdiary.room.entities.Table
import org.guardevour.developerdiary.room.entities.Tag
import org.guardevour.developerdiary.room.entities.Task
import org.guardevour.developerdiary.room.getDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialog(
    value: MutableState<Boolean>,
    entity: DrawableEntity,
    name: String,
    additionalOnClick: () -> Unit = { }
){
    AlertDialog(onDismissRequest = { value.value = false }) {
       Dialog {
            Text(text = "[Delete]", fontSize = 19.sp, textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxWidth()
                .offset(y = 10.dp))
                Text(
                    text = "Are you sure want to delete \n${name}?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(y = (-20).dp)
                )

                entity.Draw(modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .offset(y = (-20).dp))

                val context = LocalContext.current

                Row(
                    modifier = Modifier.offset(y = (-20).dp)
                ) {
                    Button(onClick = {
                        if (entity is Field) {
                            getDatabase(context).Dao().delete(entity)
                            additionalOnClick()
                        }
                        if (entity is Project) {
                            getDatabase(context).Dao().delete(entity)
                            additionalOnClick()
                        }
                        when (entity){
                            is Tag ->{
                                getDatabase(context).Dao().delete(entity)
                            }
                            is Table -> {
                                getDatabase(context).Dao().delete(entity)
                            }
                            is Field -> {
                                getDatabase(context).Dao().delete(entity)
                            }
                            is Project -> {
                                getDatabase(context).Dao().delete(entity)
                            }
                            is Relation ->{
                                getDatabase(context).Dao().delete(entity)
                            }
                            is Task ->{
                                getDatabase(context).Dao().delete(entity)
                            }
                        }
                        additionalOnClick()
                        value.value = false
                    }) {
                        Text(text = "Delete")
                    }
                    Button(onClick = { value.value = false }) {
                        Text(text = "Cancel")
                    }
                }
        }
    }
}