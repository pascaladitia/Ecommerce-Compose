package com.pascal.ecommercecompose.ui.screen.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.data.local.entity.CartEntity
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.domain.model.user.User
import com.pascal.ecommercecompose.ui.component.screenUtils.TopAppBarHeader
import com.pascal.ecommercecompose.ui.screen.cart.component.CartPayment
import com.pascal.ecommercecompose.ui.theme.AppTheme
import com.pascal.ecommercecompose.ui.theme.lightGrey
import com.pascal.ecommercecompose.ui.theme.lightsilverbox
import com.pascal.ecommercecompose.ui.theme.orange
import com.pascal.ecommercecompose.ui.theme.subTitleTextColor
import com.pascal.ecommercecompose.ui.theme.titleTextColor
import com.pascal.ecommercecompose.ui.theme.white
import com.pascal.ecommercecompose.utils.calculateTotalPrice
import com.pascal.ecommercecompose.utils.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: CartViewModel = koinViewModel(),
    onFinish: (List<CartEntity?>?) -> Unit
) {
    val context = LocalContext.current
    val pref = PreferencesLogin.getLoginResponse(context)
    val coroutine = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var snapUrl by remember { mutableStateOf<String?>(null) }
    var listProduct by remember { mutableStateOf<List<CartEntity?>?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getCart()
    }

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        if (snapUrl == null) {
            CartContent(
                product = uiState.product,
                uiEvent = CartUIEvent(
                    onDelete = {
                        coroutine.launch {
                            viewModel.deleteCart()
                        }
                    },
                    onNext = {
                        coroutine.launch {
                            if (viewModel.isOnline(context)) {
                                listProduct = it
                                snapUrl = viewModel.createSnapTransaction(calculateTotalPrice(it).toDouble())
                            } else {
                                showToast(context, context.getString(R.string.connection_offline))
                            }
                        }
                    }
                )
            )
        } else {
            CartPayment(
                snapUrl ?: ""
            ) {
                onFinish(listProduct)
            }
        }
    }
}

@Composable
fun CartContent(
    user: User? = null,
    product: List<CartEntity> = emptyList(),
    uiEvent: CartUIEvent
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            TopAppBarHeader(user = user)
            Spacer(modifier = Modifier.padding(5.dp))
            DeleteCart(uiEvent = uiEvent)
            Spacer(modifier = Modifier.padding(20.dp))
            CartItemList(product = product)
            Spacer(modifier = Modifier.padding(20.dp))
            NextButtonWithTotalItems(
                product = product,
                uiEvent = uiEvent
            )
        }
    }
}


@Composable
fun DeleteCart(
    modifier: Modifier = Modifier,
    uiEvent: CartUIEvent
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                    withStyle(
                        style = SpanStyle(
                            color = subTitleTextColor,
                            fontSize = 24.sp
                        )
                    ) {
                        append("Shopping\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = titleTextColor,
                            fontSize = 24.sp
                        )
                    ) {
                        append("Cart")
                    }

                }
            }
        )

        IconButton(onClick = { uiEvent.onDelete() }) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "",
                tint = orange
            )

        }
    }
}

@Composable
fun CartItemList(
    modifier: Modifier = Modifier,
    product: List<CartEntity>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp),
        contentPadding = PaddingValues(horizontal = 5.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        items(product) {
            ProductCartItems(
                modifier = Modifier,
                imagePainter = it.imageID ?: "",
                title = it.name ?: "",
                price = it.price.toString(),
                pricetag = "$",
                count = it.qty.toString(),
                backgroundColor = lightsilverbox
            )
        }
    }
}

@Composable
fun ProductCartItems(
    modifier: Modifier = Modifier,
    imagePainter: String = "",
    title: String = "",
    price: String = "",
    pricetag: String = "",
    count: String = "",
    backgroundColor: Color = Color.Transparent,
) {
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
                        .data(data = imagePainter)
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
                text = title,
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
                            append(pricetag)
                        }
                        withStyle(
                            style = SpanStyle(
                                titleTextColor
                            )
                        ) {
                            append(price)
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
                        text = count,
                        fontSize = 14.sp,
                        color = titleTextColor
                    )
                }
            }

        }
    }
}

@Composable
fun NextButtonWithTotalItems(
    modifier: Modifier = Modifier,
    product: List<CartEntity>,
    uiEvent: CartUIEvent
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(color = lightGrey, thickness = 2.dp)
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${product.size} Items",
                fontSize = 14.sp,
                color = lightGrey
            )

            Text(
                text = "$${calculateTotalPrice(product)}",
                fontSize = 18.sp,
                color = titleTextColor,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = {
                uiEvent.onNext(product)
            },
            colors = ButtonDefaults.buttonColors(orange),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 30.dp,
                    bottom = 34.dp
                )
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Next",
                color = white,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun CartPreview() {
    AppTheme {
        CartContent(
            uiEvent = CartUIEvent()
        )
    }
}