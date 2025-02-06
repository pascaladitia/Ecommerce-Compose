package com.pascal.ecommercecompose.ui.screen.login

import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.pascal.ecommercecompose.BuildConfig
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.ui.component.button.ButtonComponent
import com.pascal.ecommercecompose.ui.component.dialog.ShowDialog
import com.pascal.ecommercecompose.ui.component.form.FormEmailComponent
import com.pascal.ecommercecompose.ui.component.form.FormPasswordComponent
import com.pascal.ecommercecompose.ui.component.screenUtils.LoadingScreen
import com.pascal.ecommercecompose.ui.theme.AppTheme
import com.pascal.ecommercecompose.utils.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: LoginViewModel = koinViewModel(),
    onLogin: () -> Unit = {},
    onRegister: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isContentVisible by remember { mutableStateOf(false) }

    val multiplePermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
            )
        )
    } else {
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
            )
        )
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val account: GoogleSignInAccount? = GoogleSignIn.getSignedInAccountFromIntent(result.data).result
            account?.idToken?.let { idToken ->
                coroutineScope.launch {
                    viewModel.loadGoogle(context, idToken)
                }

            }
        } catch (e: Exception) {
            showToast(context, e.message.toString())
        }

    }

    BackHandler {
        (context as? ComponentActivity)?.finish()
    }

    LaunchedEffect(Unit) {
        delay(200)
        isContentVisible = true
        multiplePermissionState.launchMultiplePermissionRequest()
    }

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) {
            LoadingScreen()
        }
        if (uiState.isError) {
            ShowDialog(
                message = uiState.message,
                textButton = stringResource(R.string.close),
                color = MaterialTheme.colorScheme.primary
            ) {
                viewModel.setError(false)
            }
        }
        if (uiState.isLogin) {
            onLogin()
        }

        LoginContent(
            isContentVisible = isContentVisible,
            uiEvent = LoginUIEvent(
                onLogin = { email, pass ->
                    coroutineScope.launch {
                        viewModel.loadLogin(context, email, pass)
                    }
                },
                onGoogle = {
                    coroutineScope.launch {
                        googleSignInLauncher.launch(viewModel.onGoogleSignInIntent())
                    }
                },
                onRegister = onRegister
            )
        )
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    isContentVisible: Boolean = true,
    uiEvent: LoginUIEvent
) {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isUserError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            }
    ) {
        val (imageBackground, card, imageLogo, textAPK) = createRefs()

        Box(
            modifier = Modifier
                .height(380.dp)
                .constrainAs(imageBackground) {
                    top.linkTo(parent.top)

                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.bgg_orange),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxSize()

            )
        }

        AnimatedVisibility(
            modifier = Modifier.constrainAs(imageLogo) {
                top.linkTo(parent.top, 48.dp)
                start.linkTo(parent.start, 20.dp)
                end.linkTo(parent.end, 20.dp)
            },
            visible = isContentVisible,
            enter = fadeIn(tween(durationMillis = 500)) + slideInVertically(),
            exit = fadeOut(tween(durationMillis = 500)) + slideOutVertically()
        ) {
            Row(
                modifier = Modifier.padding(vertical = 80.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .size(36.dp)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "My Ecommerce",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.background
                    )
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.constrainAs(card) {
                centerHorizontallyTo(parent)
                top.linkTo(imageLogo.bottom, 12.dp)
            },
            visible = isContentVisible,
            enter = fadeIn(tween(durationMillis = 500)) + slideInHorizontally(),
            exit = fadeOut(tween(durationMillis = 500)) + slideOutHorizontally()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .shadow(50.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                FormEmailComponent(
                    title = "Email / No. Telepon",
                    hintText = "Masukan Email atau No. Telepon",
                    value = user,
                    isShowTitle = true,
                    onValueChange = {
                        user = it
                        isUserError = false
                    },
                    isError = isUserError
                )

                Spacer(modifier = Modifier.height(17.dp))

                FormPasswordComponent(
                    title = "Password",
                    hintText = "Masukan Password",
                    value = password,
                    isShowTitle = true,
                    onValueChange = {
                        password = it
                        isPasswordError = false
                    },
                    isError = isPasswordError,
                    isPasswordVisible = isPasswordVisible,
                    onIconClick = { isPasswordVisible = !isPasswordVisible }
                ) {
                    if (user.isBlank()) {
                        isUserError = true
                    }
                    if (password.isBlank()) {
                        isPasswordError = true
                    }

                    if (user.isNotBlank() && password.isNotBlank()) {
                        uiEvent.onLogin(user, password)
                        keyboardController?.hide()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ButtonComponent(
                    text = "Log In"
                ) {
                    if (user.isBlank()) {
                        isUserError = true
                    }
                    if (password.isBlank()) {
                        isPasswordError = true
                    }

                    if (user.isNotBlank() && password.isNotBlank()) {
                        uiEvent.onLogin(user, password)
                        keyboardController?.hide()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { uiEvent.onGoogle() },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(height = 55.dp, width = 150.dp)

                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center

                    ) {
                        Image(
                            painter = painterResource(R.drawable.google_logo),
                            null,
                            modifier = Modifier.size(15.dp),
                            colorFilter = ColorFilter.tint(White)
                        )
                        Text(
                            text = "Google",
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .clickable { uiEvent.onRegister() },
                    text = "Belum punya akun? Register disini!",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(textAPK) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 60.dp)
                },
            visible = isContentVisible,
            enter = fadeIn(tween(durationMillis = 500)) + slideInVertically { fullHeight -> fullHeight },
            exit = fadeOut(tween(durationMillis = 500)) + slideOutVertically { fullHeight -> fullHeight }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "My Ecommerce",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "APK Version ${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    AppTheme {
        LoginContent(
            uiEvent = LoginUIEvent()
        )
    }
}