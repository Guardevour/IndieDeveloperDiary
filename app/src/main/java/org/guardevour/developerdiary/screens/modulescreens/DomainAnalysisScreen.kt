package org.guardevour.developerdiary.screens.modulescreens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DomainAnalysisScreen(
    projectId: Int
) {
    Text(text = projectId.toString())
}