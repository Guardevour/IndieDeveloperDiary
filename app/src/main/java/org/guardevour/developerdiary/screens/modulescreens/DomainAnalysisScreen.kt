package org.guardevour.developerdiary.screens.modulescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.guardevour.developerdiary.room.entities.Domain
import org.guardevour.developerdiary.room.getDatabase

@Composable
fun DomainAnalysisScreen(
    projectId: Int
) {
    val context = LocalContext.current
    val dao = getDatabase(context).dao()
    val domains = dao.getAllDomains(projectId).toMutableStateList()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column {
        Text(text = "Modules:", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .horizontalScroll(scrollState)){
            items(domains.size){index ->
                Row{

                    IconButton(onClick = {
                        scope.launch {
                            dao.delete(domains[index])
                            domains.remove(domains[index])
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "")
                    }
                    domains[index].Draw()
                }

            }
            item {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "", modifier = Modifier
                    .width(200.dp)
                    .padding(5.dp)
                    .alpha(0.75f)
                    .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                    .border(5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                    .clickable {
                        Domain(
                            dao.getLastDomain() + 1,
                            "",
                            projectId
                        ).let { domain ->
                            domains.add(domain)
                            dao.insert(domain)
                        }
                    }
                )
            }
        }
    }
}