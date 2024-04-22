package org.guardevour.developerdiary.room.entities

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.twotone.Build
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.coroutines.launch
import org.guardevour.developerdiary.DataBaseFormats
import org.guardevour.developerdiary.DataType
import org.guardevour.developerdiary.room.getDatabase

@Entity(
    foreignKeys = [ForeignKey(
        entity = Table::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("table_name"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Field(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "field_name") val name: String,
    @ColumnInfo(name = "data_type") val type: String,
    @ColumnInfo(name = "length") val length: Int?,
    @ColumnInfo(name = "is_primary_key") val isPrimaryKey: Boolean,
    @ColumnInfo(name = "additional_data") val additionalData: String,
    @ColumnInfo(name = "table_name") val tbName: Int,
): DrawableEntity {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Draw(modifier: Modifier) {
        val properties = remember {
            mutableStateListOf(
                mutableStateOf(name) to "Name",
                mutableStateOf(type) to "Type",
                mutableStateOf(length.toString()) to "Length",
                mutableStateOf(additionalData) to "Additional Data",
            )
        }
        var selectedType by remember {
            mutableStateOf(DataType(type, length != null))
        }
        val dao = getDatabase(LocalContext.current).dao()
        val key = remember {
            mutableStateOf(isPrimaryKey)
        }
        val context = LocalContext.current
        val project = dao.getProjectByID(dao.getTable(tbName).prName)
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier
                .composed {
                    modifier
                }
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(11.dp))
                .fillMaxWidth()
                .height(70.dp)
        ) {
            stickyHeader{
                val scope = rememberCoroutineScope()
                Icon(Icons.Filled.AddCircle,
                    contentDescription = "",
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .height(70.dp)
                        .clickable {
                            scope.launch {
                                dao.save(
                                    Field(
                                        uid,
                                        properties[0].first.value,
                                        properties[1].first.value,
                                       if (properties[2].first.value == "null") null else properties[2].first.value.toInt(),
                                        key.value,
                                        properties[3].first.value,
                                        tbName
                                    )
                                )
                                Toast
                                    .makeText(context, "Saving successfull", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                )
            }
            item {
                Icon(if (key.value) Icons.Filled.Build else Icons.TwoTone.Build,
                    contentDescription = "",
                    modifier = Modifier
                        .height(70.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .toggleable(value = key.value) {
                            key.value = it
                            Toast
                                .makeText(
                                    context,
                                    "Primary key parameter = ${key.value}",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        })
            }
            items(properties.size){index->
                when (properties[index].second) {
                    "Type" -> {
                        val isExpanded = remember {
                            mutableStateOf(false)
                        }
                        ExposedDropdownMenuBox(
                            expanded = isExpanded.value,
                            onExpandedChange = {
                                isExpanded.value = !isExpanded.value
                            }
                        ){
                            TextField(
                                value = properties[index].first.value,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(text = properties[index].second) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded.value) },
                                modifier = Modifier.menuAnchor().width(180.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = isExpanded.value,
                                onDismissRequest = { isExpanded.value = false }
                            ) {
                                DataBaseFormats.DataBaseDataTypes[project.selectedDatabase]?.forEach{ type->
                                    DropdownMenuItem(text = { Text(text = type.name) }, onClick = {
                                        properties[index].first.value = type.name
                                        selectedType = type
                                        if (!selectedType.isEnabledLength){
                                            properties[2].first.value = null.toString()
                                        }
                                        isExpanded.value = false
                                    })
                                }
                            }
                        }
                    }
                    "Length" -> {
                        AnimatedVisibility(visible = selectedType.isEnabledLength) {
                            TextField(value = properties[index].first.value, onValueChange = {
                                properties[index].first.value = it
                            },
                                modifier = Modifier.width(90.dp),
                                label = { Text(text = properties[index].second) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                                )
                        }

                    }
                    "Additional Data" ->{
                        val isExpanded = remember {
                            mutableStateOf(false)
                        }
                        ExposedDropdownMenuBox(
                            expanded = isExpanded.value,
                            onExpandedChange = {
                                isExpanded.value = !isExpanded.value
                            }
                        ){
                            TextField(
                                value = properties[index].first.value,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(text = properties[index].second) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded.value) },
                                modifier = Modifier.menuAnchor().width(180.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = isExpanded.value,
                                onDismissRequest = { isExpanded.value = false }
                            ) {
                                listOf("NULL", "NOT NULL").forEach{ string->
                                    DropdownMenuItem(text = { Text(text = string) }, onClick = {
                                        properties[index].first.value = string
                                        isExpanded.value = false
                                    })
                                }
                            }
                        }
                    }
                    else -> {
                        TextField(value = properties[index].first.value, onValueChange = {
                            properties[index].first.value = it
                        },
                            modifier = Modifier.width(180.dp),
                            label = { Text(text = properties[index].second) },
                            keyboardOptions = KeyboardOptions(),

                            )
                    }
                }
            }

        }
    }


}