package org.guardevour.developerdiary.room.entities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.guardevour.developerdiary.DataBase

@Entity
data class Project(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "ploject_name") val name: String,
    @ColumnInfo(name = "selected_db") val selectedDatabase: DataBase = DataBase.None,
    @ColumnInfo(name = "is_enabled_DB") val isEnabledDB: Boolean = true,
    @ColumnInfo(name = "is_enabled_TODO") val isEnabledTODO: Boolean = true,
    @ColumnInfo(name = "is_enabled_TS") val isEnabledTS: Boolean = true,
    @ColumnInfo(name = "is_enabled_Notes") val isEnabledNotes: Boolean = true,
    @ColumnInfo(name = "is_enabled_DA") val isEnabledDomainAnalysis: Boolean = true,
): DrawableEntity {
    @Composable
    override fun Draw(
        modifier: Modifier
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .then(modifier)
                .padding(5.dp)
                .height(100.dp)
                .width(100.dp).background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))

        ) {
            Text(text = name)
        }
    }
}