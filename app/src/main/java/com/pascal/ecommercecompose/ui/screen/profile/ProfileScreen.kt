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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.data.local.entity.CartEntity
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.domain.model.transaction.TransactionModel
import com.pascal.ecommercecompose.domain.model.user.User
import com.pascal.ecommercecompose.ui.component.button.ButtonComponent
import com.pascal.ecommercecompose.ui.component.dialog.ShowDialog
import com.pascal.ecommercecompose.ui.component.screenUtils.LoadingScreen
import com.pascal.ecommercecompose.ui.theme.AppTheme
import com.pascal.ecommercecompose.ui.theme.lightsilverbox
import com.pascal.ecommercecompose.ui.theme.orange
import com.pascal.ecommercecompose.ui.theme.subTitleTextColor
import com.pascal.ecommercecompose.ui.theme.titleTextColor
import kotlinx.coroutines.launch
import org.bouncycastle.asn1.x500.style.RFC4519Style.title
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: ProfileViewModel = koinViewModel(),
    onVerified: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val pref = PreferencesLogin.getLoginResponse(context)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadTransaction(pref?.id ?: "")
        viewModel.loadVerified(pref?.id ?: "")
    }

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) LoadingScreen()
        if (uiState.isError) {
            ShowDialog(
                message = uiState.message,
                textButton = stringResource(R.string.close),
                color = MaterialTheme.colorScheme.primary
            ) {
                viewModel.setError(false)
            }
        }

        ProfileContent(
            pref = pref,
            uiState = uiState,
            uiEvent = ProfileUIEvent(
                onVerified = {
                    onVerified()
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
    pref: User? = null,
    uiState: ProfileUIState,
    uiEvent: ProfileUIEvent
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(60.dp),
                    contentDescription = null,
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = pref?.photo_url?.toUri())
                            .error(R.drawable.no_profile)
                            .apply { crossfade(true) }
                            .build()
                    )
                )

                if (uiState.isVerified) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(24.dp),
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color.Green
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    modifier = Modifier,
                    text = pref?.name ?: "No Name",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 20.sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = pref?.email ?: "No Email",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 16.sp,
                    ),
                    color = Color.Gray
                )
            }
        }

        if (!uiState.isVerified) {
            Box(
                modifier = modifier
                    .padding(top = 24.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { uiEvent.onVerified() }
                    .padding(vertical = 10.dp, horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "No Verified",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                    withStyle(
                        style = SpanStyle(
                            color = subTitleTextColor,
                            fontSize = 24.sp
                        )
                    ) {
                        append("History\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = titleTextColor,
                            fontSize = 24.sp
                        )
                    ) {
                        append("Transaction")
                    }

                }
            }
        )

        Spacer(modifier = Modifier.padding(10.dp))

        TransactionItemList(
            uiState = uiState
        )

        ButtonComponent(text = "Logout") {
            uiEvent.onLogout()
        }
    }
}

@Composable
fun TransactionItemList(
    modifier: Modifier = Modifier,
    uiState: ProfileUIState,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp),
        contentPadding = PaddingValues(horizontal = 5.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        itemsIndexed(uiState.transactionList) { i, it ->
            TransactionItemText(
                title = "Date",
                value = it.date ?: "-"
            )

            Spacer(Modifier.height(8.dp))

            TransactionItems(
                modifier = Modifier,
                backgroundColor = lightsilverbox,
                item = it
            )

            Spacer(Modifier.height(8.dp))

            TransactionItemText(
                title = "Total",
                value = "$${it.total}",
                isTotal = true
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        }
    }
}

@Composable
fun TransactionItems(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    item: TransactionModel? = null
) {
    item?.products?.forEach { product ->
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .fillMaxWidth(0.2f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = product.imageID)
                            .error(R.drawable.no_thumbnail)
                            .placeholder(R.drawable.loading)
                            .apply { crossfade(true) }
                            .build()
                    ),
                    contentDescription = "",
                    modifier = Modifier.padding(8.dp),
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = product.name ?: "",
                    fontSize = 18.sp,
                    color = titleTextColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    orange,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("$")
                            }
                            withStyle(
                                style = SpanStyle(
                                    titleTextColor
                                )
                            ) {
                                append(product.price.toString())
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier,
                        fontSize = 16.sp

                    )
                    Box(
                        modifier = Modifier
                            .size(35.dp, 35.dp)
                            .clip(CircleShape)
                            .background(backgroundColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = product.qty.toString(),
                            fontSize = 14.sp,
                            color = titleTextColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItemText(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    isTotal: Boolean = false
) {
    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isTotal) titleTextColor else MaterialTheme.colorScheme.onSurface,
                fontSize = if (isTotal) 16.sp else 12.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(
                color = if (isTotal) titleTextColor else MaterialTheme.colorScheme.secondary,
                fontSize = if (isTotal) 16.sp else 12.sp
            ),
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ProfilePreview() {
    AppTheme {
        ProfileContent(
            uiState = ProfileUIState(
                transactionList = listOf(
                    TransactionModel(products = listOf(CartEntity()))
                )
            ),
            uiEvent = ProfileUIEvent()
        )
    }
}