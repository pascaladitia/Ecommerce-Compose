package com.pascal.ecommercecompose.ui.component.screenUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CardComponent(
    modifier: Modifier = Modifier,
    radius: Dp = 16.dp,
    padding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radius))
            .border(1.dp, LightGray, RoundedCornerShape(radius))
            .background(Color.White)
            .padding(padding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            content()
        }
    }
}