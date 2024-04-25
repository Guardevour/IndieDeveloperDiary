package org.guardevour.developerdiary.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgramInfoScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "Name of the program - Indie Developer's Diary",
            modifier = Modifier.background(
            MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp)
        ).padding(5.dp)
        )
        Text(text = "Modules: \n" +
                "Database - manage your project DB schema, visualise it and export; \n" +
                "TODO - manage your project tasks; \n" +
                "Technical Specification - browse TS (in PDF) for your project; \n" +
                "Notes - manage notes; \n" +
                "Domain Analysis - manage more visually analysis of requirements;",
            textAlign = TextAlign.Justify, fontSize = 12.sp,
            modifier = Modifier.background(
                MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp)
            )
                .padding(5.dp)
                .fillMaxWidth(0.9f)
        )
        Text(
            text = "Special Thanks to GRizzi91 for a great library for reading PDF - bouquet",
            fontSize = 10.sp,
            modifier = Modifier.background(
                MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp)
            )
                .padding(5.dp)
        )
    }
}