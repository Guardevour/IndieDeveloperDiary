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
        entity = Field::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("field_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Relation(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "field_id") val fieldID: Int,
    @ColumnInfo(name = "field_name") val fieldName: String,
    @ColumnInfo(name = "foreign_field_id") val foreignFieldID: Int,
    @ColumnInfo(name = "foreign_field_name") val foreignFieldName: String,
): DrawableEntity {
    @Composable
    override fun Draw(modifier: Modifier) {
        Text(text = uid.toString())
    }

    override fun toString(): String {
        return "$uid FK=$fieldName REF=$foreignFieldName"
    }

    fun shortToString() = "FK=$fieldName REF=$foreignFieldName"


    infix fun isIn(collection: Collection<Relation>): Boolean {
        for (relation in collection){
            if (relation.fieldID == this.fieldID
                || relation.foreignFieldID == this.foreignFieldID){
                return true
            }
        }
        return false
    }
}