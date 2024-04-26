package org.guardevour.developerdiary.room.entities

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.guardevour.developerdiary.DataBaseFormats
import org.guardevour.developerdiary.dialogs.DeleteDialog
import org.guardevour.developerdiary.dialogs.NewRelationDialog
import org.guardevour.developerdiary.room.getDatabase

@Entity(
    foreignKeys = [ForeignKey(
        entity = Project::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("project_name"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Table(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "table_name", index = true) val name: String,
    @ColumnInfo(name = "project_name") val prName: Int
): DrawableEntity {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
     fun DrawFull() {
        val context = LocalContext.current
        val fields = getDatabase(context).dao().getAllFields(uid).toMutableStateList()
        val relations = getDatabase(context).dao().getAllRelations(
            fields.map{
                it.uid
            }.toIntArray()
        ).toMutableStateList()
        val isNewRelationDialogOpen = remember {
            mutableStateOf(false)
        }
        Column{
            Text(text = name)
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                    .height(300.dp)
            ) {
                items(fields.size){index->

                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                    ){
                        IconButton(onClick = {
                            getDatabase(context).dao().delete(fields[index])
                            fields.remove(fields[index])
                        }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "")
                        }
                        fields[index].Draw(modifier = Modifier
                        )
                    }

                }
                item {
                    val dao = getDatabase(LocalContext.current).dao()
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "", modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .alpha(0.75f)
                        .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                        .border(5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .clickable {
                            try {
                                val project = dao.getProjectByID(prName)
                                val field =
                                    DataBaseFormats.DataBaseDataTypes[project.selectedDatabase]
                                        ?.get(0)
                                        ?.let {
                                            Field(
                                                uid = dao.getLastField() + 1,
                                                name = "",
                                                length = if (it.isEnabledLength) 255 else null,
                                                type = it.name,
                                                tbName = uid,
                                                isPrimaryKey = false,
                                                additionalData = "not null"
                                            )

                                        }
                                field?.let {
                                    dao.addField(field)
                                    fields.add(field)
                                }
                            } catch (ex: Exception) {
                                Toast
                                    .makeText(
                                        context,
                                        "You should select DBMS to edit tables",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }


                        }
                    )
                }
            }

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(relations.size){index->
                    val isDeleteDialogOpen = remember {
                        mutableStateOf(false)
                    }

                    Text(text = relations[index].toString(), modifier = Modifier.combinedClickable(
                        MutableInteractionSource(),
                        indication = null,
                        onClick = {},
                        onLongClick = {
                            isDeleteDialogOpen.value = true
                        }
                    ))

                    if (isDeleteDialogOpen.value)
                        DeleteDialog(value = isDeleteDialogOpen, entity = relations[index], name = "relation", additionalOnClick = {
                            relations.remove(relations[index])
                        })
                }
                item {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "", modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .alpha(0.75f)
                        .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                        .border(5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .clickable(
                            MutableInteractionSource(),
                            indication = null,
                            onClick = { isNewRelationDialogOpen.value = true }
                        )
                    )
                    if (isNewRelationDialogOpen.value){
                        NewRelationDialog(value = isNewRelationDialogOpen, this@Table, relations)
                    }
                }
            }
        }


    }

    @Composable
    override fun Draw(modifier: Modifier){

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(100.dp)
                .then(modifier)
                .width(100.dp)
                .padding(5.dp)
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                .border(5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
        ) {
            Text(text = name, textAlign = TextAlign.Center, fontSize = 12.sp)
        }
    }

    @Composable
    fun DrawVisual(modifier: Modifier = Modifier, isExpanded: MutableState<Boolean>){
        val fields = getDatabase(LocalContext.current).dao().getAllFields(uid)
        val relations = getDatabase(LocalContext.current).dao().getAllRelations(fields.map {
            it.uid
            }.toIntArray()
        )
        Column(
            verticalArrangement = if (isExpanded.value) Arrangement.Top else Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(if (isExpanded.value) 150.dp else 100.dp)
                .then(modifier)
                .width(120.dp)
                .padding(5.dp)
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                .border(5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
        ) {
            Text(text = name, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(20.dp))
            AnimatedVisibility(visible = isExpanded.value) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(3.dp)
                ) {
                    items(fields.size) {index->
                        Text(text = fields[index].name, fontSize = 11.sp)
                    }
                    item {
                        Text(text = "Relations:", fontSize = 11.sp)
                    }
                    items(relations.size) {index->
                       Text(text = "[${relations[index].shortToString()}]", fontSize =9.sp, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

    }
}