package org.guardevour.developerdiary.screens.modulescreens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import org.guardevour.developerdiary.room.getDatabase
import kotlin.math.roundToInt

@Composable
fun DataBaseVisualisationScreen(
    projectId: Int
) {
    val dao =  getDatabase(LocalContext.current).dao()
    val tables = dao.getAllTables(projectId)

    val relations = dao.getAllRelations(
        dao.getAllFields(
            tables.map {
                it.uid
            }
        ).map {
            it.uid
        }.toIntArray()
    )

    var index = 1

    val multiplier = (LocalConfiguration.current.screenHeightDp / (tables.size + 2)).toFloat()
    val points = tables.associateWith { _ ->
        index++
        (mutableFloatStateOf(index * multiplier) to mutableFloatStateOf(index * multiplier))
    }
    val tableWithFields = tables.associateWith {table->
        dao.getAllFields(
            table.uid
        ).map {
            it.uid
        }
    }

    tables.forEach {table ->
        val isExpanded = remember {
            mutableStateOf(false)
        }
        Box(modifier = Modifier.fillMaxSize()) {
            table.DrawVisual(
                Modifier
                    .offset {
                        IntOffset(
                            points[table]!!.first.floatValue.roundToInt(),
                            points[table]!!.second.floatValue.roundToInt()
                        )
                    }
                    .clickable {
                        isExpanded.value = !isExpanded.value
                    }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            points[table]!!.first.floatValue += dragAmount.x
                            points[table]!!.second.floatValue += dragAmount.y
                        }
                    }
                ,
                isExpanded
            )

        }
    }

    Canvas(modifier = Modifier.fillMaxSize()){
        relations.forEach {relation ->

            val tableFK = tableWithFields.filter {(_, value)->
                value.contains(relation.fieldID)
            }.keys
            val tableREF = tableWithFields.filter {(_, value)->
                value.contains(relation.foreignFieldID)
            }.keys
            if (tableFK.isNotEmpty() && tableREF.isNotEmpty()){
                drawLine(
                    Color.Black,
                    Offset(
                        points[tableREF.toList()[0]]!!.first.floatValue + if
                                (points[tableREF.toList()[0]]!!.first.floatValue >
                            points[tableFK.toList()[0]]!!.first.floatValue
                            ) 30f else 250f,
                        points[tableREF.toList()[0]]!!.second.floatValue + if
                                (points[tableREF.toList()[0]]!!.second.floatValue >
                            points[tableFK.toList()[0]]!!.second.floatValue
                        ) 30f else 260f
                    ),
                    Offset(
                        points[tableFK.toList()[0]]!!.first.floatValue + if
                                (points[tableREF.toList()[0]]!!.first.floatValue <
                            points[tableFK.toList()[0]]!!.first.floatValue
                        ) 30f else 250f,
                        points[tableFK.toList()[0]]!!.second.floatValue + if
                                (points[tableREF.toList()[0]]!!.second.floatValue <
                            points[tableFK.toList()[0]]!!.second.floatValue
                        ) 30f else 250f
                    ),
                    strokeWidth = 3f
                )
                drawPoints(
                    points = listOf(
                        Offset(
                            points[tableFK.toList()[0]]!!.first.floatValue + if
                                    (points[tableREF.toList()[0]]!!.first.floatValue <
                                points[tableFK.toList()[0]]!!.first.floatValue
                            ) 30f else 250f,
                            points[tableFK.toList()[0]]!!.second.floatValue + if
                                    (points[tableREF.toList()[0]]!!.second.floatValue <
                                points[tableFK.toList()[0]]!!.second.floatValue
                            ) 30f else 250f
                        )
                    ),
                    pointMode = PointMode.Points,
                    Color.Black,
                    strokeWidth = 20f
                )
            }
        }
    }
    
}
