package com.pascal.ecommercecompose.ui.component.screenUtils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.ui.theme.AppTheme

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Dialog(
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        ),
        onDismissRequest = {  },
    ) {
        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(R.drawable.loading1)
            .decoderFactory(GifDecoder.Factory())
            .crossfade(true)
            .build()

        Column(
            modifier = modifier
                .shadow(6.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
            )
            AsyncImage(
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp),
                model = imageRequest,
                contentDescription = "GIF"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    AppTheme {
        LoadingScreen()
    }
}