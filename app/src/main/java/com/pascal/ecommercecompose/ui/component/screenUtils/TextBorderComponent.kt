package com.pascal.ecommercecompose.ui.component.screenUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pascal.ecommercecompose.ui.theme.AppTheme

@Composable
fun TextBorderComponent(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onSecondary
) {
    Box(
        modifier = modifier
            .background(color.copy(0.1f), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .padding(vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                color = color
            ),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
@Composable
fun TextBorderComponentSmall(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onSecondary
) {
    Box(
        modifier = modifier
            .background(color.copy(0.1f), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                color = color,
                fontSize = 10.sp
            ),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
fun TextSelectedComponent(
    modifier: Modifier = Modifier,
    text: String,
    value: String = "0",
    isSelect: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .background(
                if (isSelect) MaterialTheme.colorScheme.primary else Color.White,
                RoundedCornerShape(8.dp)
            )
            .border(
                1.dp,
                if (isSelect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val fontWeight = if (isSelect) FontWeight.Bold else FontWeight.Normal
        val fontColor = if (isSelect) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface

        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = fontWeight,
                color = fontColor
            )
        )
        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Red)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = value,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = fontWeight,
                    color = Color.White,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Preview
@Composable
fun TextBorderPreview() {
    AppTheme {
        TextBorderComponent(text = "PTP")
    }
}

@Preview
@Composable
fun TextSelectedPreview() {
    AppTheme {
        TextSelectedComponent(text = "PTP", value = "14") {}
    }
}