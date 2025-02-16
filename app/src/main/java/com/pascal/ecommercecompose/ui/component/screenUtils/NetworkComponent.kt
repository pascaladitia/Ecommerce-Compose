package com.pascal.ecommercecompose.ui.component.screenUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.ui.screen.home.HomeViewModel
import com.pascal.ecommercecompose.ui.theme.AppTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun NetworkComponent(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    isOnline: () -> Unit,
    isOffline: () -> Unit
) {
    val context = LocalContext.current
    val isOnline by viewModel.isOnline.collectAsState()
    var showSnackbar by remember { mutableIntStateOf(NETWORK_DISSMISS) }
    var isFirstLaunch by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.startCheckInternet(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopCheckInternet()
        }
    }

    LaunchedEffect(isOnline) {
        if (isOnline) {
            if (!isFirstLaunch) showSnackbar = NETWORK_ONLINE
            isFirstLaunch = false
            isOnline()
        } else {
            showSnackbar = NETWORK_OFFLINE
            isOffline()
        }
    }

    if (showSnackbar == NETWORK_ONLINE) {
        Box(
            modifier = modifier
                .background(Color.Green)
                .padding(6.dp)
        ) {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = stringResource(R.string.connection_online),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
        }

        LaunchedEffect(Unit) {
            delay(2000)
            showSnackbar = NETWORK_DISSMISS
        }
    } else if (showSnackbar == NETWORK_OFFLINE) {
        Box(
            modifier = modifier
                .background(Red)
                .padding(6.dp)
        ) {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = stringResource(R.string.connection_offline),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun NetworkPreview() {
    AppTheme {
        NetworkComponent(isOnline = { /*TODO*/ }) {
            
        }
    }
}

const val NETWORK_DISSMISS = 0
const val NETWORK_ONLINE = 1
const val NETWORK_OFFLINE = 2