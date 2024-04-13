package org.guardevour.developerdiary.room

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.guardevour.developerdiary.room.entities.*

@Database(entities = [Project::class,
                    Table::class,
                    Tag::class,
                    Field::class,
                    Relation::class,
                    Task::class
                     ], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun Dao(): RoomDao
}

fun getDatabase(context: Context): AppDatabase = Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    "DevDiaryDatabase"
).allowMainThreadQueries().build()