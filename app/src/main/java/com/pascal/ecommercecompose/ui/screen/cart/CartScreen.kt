package com.pascal.ecommercecompose.ui.screen.cart

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pascal.ecommercecompose.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.ui.screen.home.TopAppBarHeader
import com.pascal.ecommercecompose.ui.theme.lightGrey
import com.pascal.ecommercecompose.ui.theme.lightsilverbox
import com.pascal.ecommercecompose.ui.theme.orange
import com.pascal.ecommercecompose.ui.theme.subTitleTextColor
import com.pascal.ecommercecompose.ui.theme.titleTextColor
import com.pascal.ecommercecompose.ui.theme.white

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: CartViewModel = koinViewModel(),
    onDetail: () -> Unit
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCart()
    }

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        CartContent(
            product = uiState.product,
            uiEvent = CartUIEvent()
        )
    }
}

@Composable
fun CartContent(
    product: List<ProductEntity> = emptyList(),
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
                .padding(30.dp)
        ) {
            TopAppBarHeader()
            Spacer(modifier = Modifier.padding(5.dp))
            DeleteCart()
            Spacer(modifier = Modifier.padding(20.dp))
            CartItemList(
                product = product,
                uiEvent = uiEvent
            )
            Spacer(modifier = Modifier.padding(20.dp))
            NextButtonWithTotalItems(
                product = product,
                uiEvent = uiEvent
            )
        }
    }
}


@Composable
fun DeleteCart() {
    Row(
        modifier = Modifier.fillMaxWidth(),
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

        IconButton(onClick = { }) {
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
    product: List<ProductEntity>,
    uiEvent: CartUIEvent
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
                imagePainter = painterResource(id = it.imageID),
                title = it.name,
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
    imagePainter: Painter,
    title: String = "",
    price: String = "",
    pricetag: String = "",
    count: String = "",
    backgroundColor: Color = Color.Transparent,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                painter = imagePainter,
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
    product: List<ProductEntity>,
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
                text = "$${product.sumOf { it.price * it.qty }}",
                fontSize = 18.sp,
                color = titleTextColor,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = {
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