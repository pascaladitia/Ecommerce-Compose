package com.pascal.ecommercecompose.ui.component.screenUtils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.domain.model.user.User

@Composable
fun TopAppBarHeader(
    modifier: Modifier = Modifier,
    user: User? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Card(
            modifier = Modifier
                .width(50.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(12.dp),
            colors =  CardDefaults.cardColors(Color.White)
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = ""
                )
            }
        }

        Card(
            modifier = Modifier
                .size(50.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(12.dp),
            colors =  CardDefaults.cardColors(Color.White)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = user?.photo_url?.toUri())
                        .error(R.drawable.no_profile)
                        .apply { crossfade(true) }
                        .build()
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
            )
        }

    }
}