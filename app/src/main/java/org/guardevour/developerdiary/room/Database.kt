package org.guardevour.developerdiary.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.guardevour.developerdiary.room.entities.Domain
import org.guardevour.developerdiary.room.entities.Field
import org.guardevour.developerdiary.room.entities.Note
import org.guardevour.developerdiary.room.entities.Project
import org.guardevour.developerdiary.room.entities.Relation
import org.guardevour.developerdiary.room.entities.Requirement
import org.guardevour.developerdiary.room.entities.Table
import org.guardevour.developerdiary.room.entities.Tag
import org.guardevour.developerdiary.room.entities.Task

@Database(
    entities = [Project::class,
                    Table::class,
                    Tag::class,
                    Field::class,
                    Relation::class,
                    Task::class,
                    Domain::class,
                    Requirement::class,
                    Note::class,
                     ], version = 3,
    autoMigrations = [
        AutoMigration (from = 2, to = 3)
    ],
    exportSchema = true

)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): RoomDao
}

fun getDatabase(context: Context): AppDatabase = Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    "DevDiaryDatabase"
).allowMainThreadQueries().build()