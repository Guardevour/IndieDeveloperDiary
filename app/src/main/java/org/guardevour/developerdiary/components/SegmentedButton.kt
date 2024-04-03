package org.guardevour.developerdiary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun SegmentedButton(
    vararg buttons: Pair<ImageVector, () -> Unit>,
    modifier: Modifier = Modifier
){
    val width = LocalConfiguration.current.screenWidthDp.dp
    LazyRow(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .width(width)
            .composed {
                modifier
            }
            .background(MaterialTheme.colorScheme.primary)
    ) {
        items(buttons.size){index->
            val border: RoundedCornerShape = when(index){
                0 -> RoundedCornerShape(10.dp, 0.dp, 0.dp, 10.dp,)
                buttons.lastIndex -> RoundedCornerShape(0.dp, 10.dp, 10.dp, 0.dp)
                else -> RoundedCornerShape(0.dp)
            }

            Icon(imageVector = buttons[index].first, contentDescription = "",
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.secondary, border)
                    .clip(border)
                    .height(40.dp)
                    .padding(3.dp)
                    .width(width / buttons.size - 6.dp)

                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ){
                        buttons[index].second()
                    }
            )
        }
    }
}