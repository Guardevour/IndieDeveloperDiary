package org.guardevour.developerdiary.room.entities

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Project::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("project_name"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Tag(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "project_name") val prName: Int
): DrawableEntity {
    @Composable
    override fun Draw(modifier: Modifier) {
        Text(text = name)
    }
}