package org.guardevour.developerdiary

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import org.guardevour.developerdiary.room.AppDatabase
import java.io.IOException

suspend fun getSqlFromProject(context: Context, projectId: Int): String = coroutineScope {
    val dao = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "DevDiaryDatabase"
    ).allowMainThreadQueries().build().Dao()
    var resultString = ""

    val tables = dao.getAllTables(projectId)

    for (table in tables){
        resultString += "CREATE TABLE ${table.name}( \n"
        val fields = dao.getAllFields(table.uid)
        val relations = dao.getAllRelations(
            fields.map {
                it.uid
            }.toIntArray()
        )
        for (field in fields){
            resultString += "${field.name} ${field.type}"
            resultString += if (field.length != null) "(${field.length})" else " "
            resultString += if (field.isPrimaryKey) " PRIMARY KEY " else " "
            resultString += field.additionalData + ",\n"
        }

        for (relation in relations){
            val tableAndField = relation.foreignFieldName.split(" ")
            resultString += "FOREIGN KEY (${relation.fieldName}) REFERENCES ${tableAndField[0]} (${tableAndField[1]})"
        }
        resultString += ")\n"
    }


    resultString
}


fun createFileAndSave(fileName: String, fileContent: String, context: Context) {
    val resolver = context.contentResolver

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
    }

    val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

    uri?.let { documentUri ->
        try {
            resolver.openOutputStream(documentUri)?.use { outputStream ->
                outputStream.write(fileContent.toByteArray())
                Toast.makeText(context, "File saved successfully in Documents", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving file", Toast.LENGTH_SHORT).show()
        }
    }
}