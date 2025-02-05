package com.pascal.ecommercecompose.ui.component.screenUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.ecommercecompose.ui.theme.AppTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft

@Composable
fun IconComponent(
    modifier: Modifier = Modifier,
    icon: ImageVector = FeatherIcons.ArrowLeft,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(24.dp, CircleShape)
            .clip(CircleShape)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.background, CircleShape)
    ) {
        Icon(
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp),
            imageVector = icon,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IconPreview() {
    AppTheme {
        IconComponent() {}
    }
}