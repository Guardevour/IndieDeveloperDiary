package org.guardevour.developerdiary.room.entities

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


interface DrawableEntity {
    @Composable
    fun Draw(modifier: Modifier)
}