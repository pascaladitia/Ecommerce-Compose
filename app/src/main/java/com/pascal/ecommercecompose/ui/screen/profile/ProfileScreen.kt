package com.pascal.ecommercecompose.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.ui.component.button.ButtonComponent
import com.pascal.ecommercecompose.ui.theme.AppTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.X
import kotlinx.coroutines.launch
import org.bouncycastle.asn1.x500.style.RFC4519Style.title
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: ProfileViewModel = koinViewModel(),
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        ProfileContent(
            uiEvent = ProfileUIEvent(
                onVerified = {

                },
                onLogout = {
                    coroutine.launch {
                        viewModel.loadLogout(context)
                        onLogout()
                    }
                }
            )
        )
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    uiEvent: ProfileUIEvent
) {
    val context = LocalContext.current
    val pref = PreferencesLogin.getLoginResponse(context)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = pref?.photo_url?.toUri())
                    .error(R.drawable.no_profile)
                    .apply { crossfade(true) }
                    .build()
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(200.dp)
                .background(MaterialTheme.colorScheme.background, CircleShape)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            modifier = Modifier,
            text = pref?.name ?: "No Name",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier,
            text = pref?.email ?: "No Email",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            modifier = Modifier,
            text = "Status",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .clickable { uiEvent.onVerified() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "No Verified",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.width(32.dp))

                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }

        Spacer(Modifier.height(48.dp))

        ButtonComponent(text = "Logout") {
            uiEvent.onLogout()
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ProfilePreview() {
    AppTheme {
        ProfileContent(
            uiEvent = ProfileUIEvent()
        )
    }
}