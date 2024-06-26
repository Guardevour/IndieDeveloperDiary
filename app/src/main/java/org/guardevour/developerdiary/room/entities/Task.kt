package org.guardevour.developerdiary.room.entities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [ForeignKey(
        entity = Project::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("project_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
class Task(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "difficulty") val difficulty: String,
    @ColumnInfo(name = "project_id") val prId: Int,
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean
): DrawableEntity {

    @Composable
    fun DrawTask(modifier: Modifier, isExpanded: MutableState<Boolean>) {

       Column(
           verticalArrangement = Arrangement.Top,
           horizontalAlignment = Alignment.CenterHorizontally,
           modifier = Modifier
               .height(if (isExpanded.value) 230.dp else 80.dp)
               .padding(5.dp)
               .taskWidth()
               .then(modifier)

       ) {
           Column(
               verticalArrangement =  Arrangement.Center,
               modifier = Modifier
                   .taskWidth()
                   .padding(5.dp)
                   .height(80.dp)
                   .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(10.dp))
           ) {
               Text(text = name)
               Text(text = "$tag : $difficulty")
           }

           AnimatedVisibility(visible = isExpanded.value) {
              Box(
                  modifier = Modifier.taskWidth()
                      .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(10.dp))
              ){
                  Text(text = description, textAlign = TextAlign.Justify, fontSize = 10.sp, modifier = Modifier
                      .padding(10.dp)
                  )
              }
           }
       }


    }

    @Composable
    override fun Draw(modifier: Modifier) {
        Text(name)
    }
}

fun Modifier.taskWidth(): Modifier{
    return this then Modifier.fillMaxWidth(0.85f)
}