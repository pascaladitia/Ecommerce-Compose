package com.pascal.ecommercecompose.ui.screen.register

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.pascal.ecommercecompose.BuildConfig
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.ui.component.button.ButtonComponent
import com.pascal.ecommercecompose.ui.component.dialog.ShowDialog
import com.pascal.ecommercecompose.ui.component.form.FormBasicComponent
import com.pascal.ecommercecompose.ui.component.form.FormEmailComponent
import com.pascal.ecommercecompose.ui.component.form.FormPasswordComponent
import com.pascal.ecommercecompose.ui.component.screenUtils.LoadingScreen
import com.pascal.ecommercecompose.ui.theme.AppTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronLeft
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: RegisterViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isContentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        isContentVisible = true
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
        if (uiState.isRegister) {
            onNavBack()
        }

        RegisterContent(
            isContentVisible = isContentVisible,
            uiEvent = RegisterUIEvent(
                onRegister = { name, user, password ->
                    coroutineScope.launch {
                        viewModel.loadRegister(context, name, user, password)
                    }
                },
                onNavBack = {
                    onNavBack()
                }
            )
        )
    }
}

@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    isContentVisible: Boolean = true,
    uiEvent: RegisterUIEvent
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var isNameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
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
        val (textBack, imageBackground, card, imageLogo, textAPK) = createRefs()

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

        Row(
            modifier = Modifier
                .padding(16.dp)
                .constrainAs(textBack) {
                    top.linkTo(parent.top)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable { uiEvent.onNavBack() },
                imageVector = FeatherIcons.ChevronLeft,
                contentDescription = null,
                tint = Color.White
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Back",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.background
                )
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
                modifier = Modifier.padding(vertical = 60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Register",
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
                FormBasicComponent(
                    title = buildAnnotatedString {
                        append("Nama")
                    },
                    hintText = "Masukan Nama",
                    value = name,
                    onValueChange = {
                        name = it
                        isNameError = false
                    },
                    isError = isNameError
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormEmailComponent(
                    title = "Email",
                    hintText = "Masukan Email",
                    value = email,
                    isShowTitle = true,
                    onValueChange = {
                        email = it
                        isEmailError = false
                    },
                    isError = isEmailError
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    if (name.isBlank()) {
                        isNameError = true
                    }
                    if (email.isBlank()) {
                        isEmailError = true
                    }
                    if (password.isBlank()) {
                        isPasswordError = true
                    }

                    if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        uiEvent.onRegister(name, email, password)
                        keyboardController?.hide()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ButtonComponent(
                    text = "Register"
                ) {
                    if (name.isBlank()) {
                        isNameError = true
                    }
                    if (email.isBlank()) {
                        isEmailError = true
                    }
                    if (password.isBlank()) {
                        isPasswordError = true
                    }

                    if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        uiEvent.onRegister(name, email, password)
                        keyboardController?.hide()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .clickable { uiEvent.onNavBack() },
                    text = "Sudah punya akun? Login disini!",
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
                    text = "Name",
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
private fun RegisterPreview() {
    AppTheme {
        RegisterContent(
            uiEvent = RegisterUIEvent()
        )
    }
}