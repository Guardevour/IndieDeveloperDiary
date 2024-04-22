package org.guardevour.developerdiary.screens.modulescreens

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import kotlinx.coroutines.launch
import org.guardevour.developerdiary.room.getDatabase
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import kotlin.io.path.Path

@Composable
fun TSScreen(
    projectId: Int
) {
    val context = LocalContext.current
    val fileName = "$projectId - Technical Specification.pdf"

    var isSelectedNewPdf by remember {
        mutableStateOf(false)
    }

    val pdfUri = remember {
        mutableStateOf(
            File(
                context.filesDir.path + "/$projectId - Technical Specification.pdf"
            ).toUri()
        )
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri ->
            isSelectedNewPdf = true
            pdfUri.value = selectedUri

        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Technical Specification")
        key(pdfUri.value){
                val pdfState = rememberVerticalPdfReaderState(
                    resource = ResourceType.Local(pdfUri.value),
                    isZoomEnable = true
                )
                VerticalPDFReader(
                    state = pdfState,
                    modifier = Modifier
                        .fillMaxSize(0.93f)
                        .background(color = MaterialTheme.colorScheme.background)
                )
                if (isSelectedNewPdf) {
                    pdfState.file?.let { file ->
                        Path(file.path).let { path ->
                            context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                                it.write(Files.readAllBytes(path))
                                Toast.makeText(context, "TS saved", Toast.LENGTH_SHORT).show()
                                isSelectedNewPdf = false
                            }
                        }
                    }

                }

        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary)
        ){
            Button(onClick = {
                launcher.launch("application/pdf")
            }) {
                Text("Select PDF")
            }
        }
    }

}
