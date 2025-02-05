package com.pascal.ecommercecompose.ui.component.screenUtils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.ecommercecompose.ui.theme.AppTheme

@Composable
fun ProgressComponent(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    target: Float = 100f,
    onFull: (Boolean) -> Unit
) {
    val progressFactor by remember(progress) {
        mutableFloatStateOf(progress / target)
    }

    val progressBarColor: Color

    if (progressFactor >= 1.0f) {
        progressBarColor = Red
        onFull(true)
    } else {
        progressBarColor = MaterialTheme.colorScheme.primary
        onFull(false)
    }

    LinearProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(38.dp))
            .height(8.dp),
        color = progressBarColor,
        trackColor = Gray,
        progress = progressFactor
    )
}

@Preview
@Composable
fun ProgressPreview() {
    AppTheme {
        ProgressComponent(progress = 50f) {}
    }
}