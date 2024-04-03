package org.guardevour.developerdiary.components

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckBox(
    value: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    iconUnselected: ImageVector = Icons.Rounded.Check,
    iconSelected: ImageVector = Icons.Rounded.Check,
    text: String
){
    val offset by animateIntOffsetAsState(
        targetValue = if (value.value) {
            IntOffset(70, 0)
        } else {
            IntOffset(10, 0)
        },
        label = "offset"
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .composed {
                    modifier
                }
                .toggleable(
                    value.value,
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    value.value = !value.value
                }
                .width(50.dp)
                .offset(10.dp)
                .background(
                    if (!value.value)
                        MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.tertiary, RoundedCornerShape(30)
                )
        ) {
            Icon(
                imageVector = if (value.value) iconSelected else iconUnselected,
                contentDescription = "",
                modifier = Modifier
                    .offset { offset }
                    .padding(3.dp)

            )
        }
        Text(text = text, fontSize = 13.sp, modifier = Modifier.offset((-10).dp))
    }


}