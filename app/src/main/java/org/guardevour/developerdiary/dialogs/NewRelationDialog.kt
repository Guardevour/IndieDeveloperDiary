package org.guardevour.developerdiary.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.guardevour.developerdiary.room.entities.Relation
import org.guardevour.developerdiary.room.entities.Table
import org.guardevour.developerdiary.room.getDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRelationDialog(value: MutableState<Boolean>, table: Table, relations: SnapshotStateList<Relation>) {
        AlertDialog(onDismissRequest = { value.value = false }) {
            Dialog {
                val dao = getDatabase(LocalContext.current).dao()
                var expandedField by remember { mutableStateOf(false) }
                var expandedForeignField by remember { mutableStateOf(false) }
                val fields = dao.getAllFields(table.uid).toMutableStateList()
                var selectedField by remember { mutableStateOf("") }
                var selectedFieldId by remember { mutableIntStateOf(0)  }
                val foreignFields = dao.getAllForeignFields(dao.getAllTables(table.prName).map {
                    it.uid
                }.filter {
                    it != table.uid
                }).toMutableStateList()
                var selectedForeignField by remember { mutableStateOf("") }
                var selectedForeignFieldId by remember { mutableIntStateOf(0) }
                val context = LocalContext.current
                val tables = dao.getAllTables(table.prName).associateBy {
                    it.uid
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                        .height(350.dp)
                ){
                    Text(text = "[New Relation]", fontSize = 19.sp, textAlign = TextAlign.Center, modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 10.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedField,
                        onExpandedChange = {
                            expandedField = !expandedField
                        }
                    ){
                        TextField(
                            value = selectedField,
                            onValueChange = {},
                            label = { Text(text = "ForeignKey")},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedField) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedField,
                            onDismissRequest = { expandedField = false }
                        ) {
                            fields.forEach { field ->
                                DropdownMenuItem(text = { Text(text = field.name)}, onClick = {
                                    selectedField = field.name
                                    selectedFieldId = field.uid
                                    expandedField = false
                                })
                            }

                        }


                    }
                    ExposedDropdownMenuBox(
                        expanded = expandedForeignField,
                        onExpandedChange = {
                            expandedForeignField = !expandedForeignField
                        }
                    ){TextField(
                        value = selectedForeignField,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Reference")},
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedForeignField) },
                        modifier = Modifier.menuAnchor()
                    )
                        ExposedDropdownMenu(
                            expanded = expandedForeignField,
                            onDismissRequest = { expandedForeignField= false }
                        ) {
                            if (foreignFields.isNotEmpty()){
                                foreignFields.forEach { field ->
                                    DropdownMenuItem(text = { Text(text = "${tables[field.tbName]?.name} ${field.name}")}, onClick = {
                                        selectedForeignField = "${tables[field.tbName]?.name} ${field.name}"
                                        selectedForeignFieldId = field.uid
                                        expandedForeignField = false
                                    })
                                }
                            }

                        }
                    }

                    Button(onClick = {
                        if (selectedField.isNotBlank() && selectedForeignField.isNotBlank()){
                            val relation = Relation(
                                dao.getLastRelation() + 1,
                                selectedFieldId,
                                selectedField,
                                selectedForeignFieldId,
                                selectedForeignField
                            )
                            if (!(relation isIn relations)){
                                dao.addRelation(relation)
                                relations.add(relation)
                                value.value = false
                            }
                            else{
                                Toast.makeText(context, "This relation exist", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }) {
                        Text(text = "Create relation")
                    }

                }
                }

            }

        }
