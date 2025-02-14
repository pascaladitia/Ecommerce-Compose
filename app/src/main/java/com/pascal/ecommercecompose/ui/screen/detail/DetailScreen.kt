package com.pascal.ecommercecompose.ui.screen.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.domain.model.product.ProductDetails
import com.pascal.ecommercecompose.domain.model.product.Review
import com.pascal.ecommercecompose.ui.component.dialog.ShowDialog
import com.pascal.ecommercecompose.ui.component.screenUtils.LoadingScreen
import com.pascal.ecommercecompose.ui.component.screenUtils.RatingBar
import com.pascal.ecommercecompose.ui.component.screenUtils.TopAppBarWithBack
import com.pascal.ecommercecompose.ui.theme.AppTheme
import com.pascal.ecommercecompose.ui.theme.grey
import com.pascal.ecommercecompose.ui.theme.lightGrey
import com.pascal.ecommercecompose.ui.theme.lightblack
import com.pascal.ecommercecompose.ui.theme.lightgraybg
import com.pascal.ecommercecompose.ui.theme.orange
import com.pascal.ecommercecompose.ui.theme.titleTextColor
import com.pascal.ecommercecompose.ui.theme.white
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    productId: String? = "",
    viewModel: DetailViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadProductsDetail(productId)
    }

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isSuccess) onNavBack()
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

        DetailContent(
            uiState = uiState,
            uiEvent = DetailUIEvent(
                onCart = {
                    coroutine.launch {
                        viewModel.getCart(context, it)
                    }
                },
                onFavorite = { isFav, item ->
                    coroutine.launch {
                        viewModel.saveFavorite(isFav, item)
                    }
                },
                onNavBack = {
                    onNavBack()
                }
            )
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    uiState: DetailUIState,
    uiEvent: DetailUIEvent
) {
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.product) {
        isFavorite = uiState.product?.isFavorite ?: false
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBarWithBack(
                isFavorite = isFavorite,
                onFavorite = {
                    isFavorite = !isFavorite
                    uiEvent.onFavorite(isFavorite, uiState.product)
                },
                onBackClick = {
                    uiEvent.onNavBack()
                }
            )
        },
        containerColor = lightgraybg,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { uiEvent.onCart(uiState.product) },
                containerColor = orange
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Add To Cart",
                    tint = white
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (imagesliderref, addtocartref) = createRefs()

                    Box(modifier = Modifier
                        .height(280.dp)
                        .constrainAs(imagesliderref) {
                            top.linkTo(imagesliderref.top)
                            bottom.linkTo(imagesliderref.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    ) {
                        HeaderImagesSlider(
                            product = uiState.product
                        )
                    }

                    Surface(
                        color = white,
                        shape = RoundedCornerShape(40.dp).copy(
                                bottomStart = ZeroCornerSize,
                                bottomEnd = ZeroCornerSize
                            ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 300.dp)
                            .constrainAs(addtocartref) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(30.dp)
                        ) {
                            ProductTitle(product = uiState.product)

                            Spacer(modifier = Modifier.padding(10.dp))

                            ProductAvailableSize(product = uiState.product)

                            Spacer(modifier = Modifier.padding(10.dp))

                            ProductItemColorWithDesc(product = uiState.product)

                            Spacer(modifier = Modifier.padding(10.dp))

                            ProductReviews(product = uiState.product)

                            Spacer(modifier = Modifier.padding(30.dp))
                        }
                    }
                }
            }
        }
    )

}


@Composable
fun HeaderImagesSlider(
    modifier: Modifier = Modifier,
    product: ProductDetails? = null
) {
    var isSelect by remember { mutableIntStateOf(0) }
    var showImage by remember { mutableStateOf(product?.images?.get(0)) }

    LaunchedEffect(product) {
        showImage = product?.images?.get(0)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = showImage ?: "")
                    .error(R.drawable.no_thumbnail)
                    .placeholder(R.drawable.loading)
                    .apply { crossfade(true) }
                    .build()
            ),
            contentScale = ContentScale.Fit,
            contentDescription = "",
            modifier = Modifier
                .size(230.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(product?.images ?: emptyList()) { index, item ->
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(62.dp)
                        .border(
                            color = if (index == isSelect) orange else lightGrey,
                            width = 2.dp,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            isSelect = index
                            showImage = item
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = item)
                                .placeholder(R.drawable.loading)
                                .apply { crossfade(true) }
                                .build()
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp, 50.dp)
                            .padding(6.dp)
                    )

                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun ProductTitle(
    modifier: Modifier = Modifier,
    product: ProductDetails? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            color = grey,
            modifier = Modifier
                .height(4.dp)
                .width(40.dp)
        )

        Spacer(modifier = Modifier.padding(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = product?.title ?: "",
                color = titleTextColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Column(modifier = Modifier.wrapContentHeight()) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                orange,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("$ ")
                        }
                        withStyle(
                            style = SpanStyle(
                                titleTextColor
                            )
                        ) {
                            append(product?.price.toString())
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier,
                    fontSize = 16.sp

                )


            }
        }
    }
}

@Composable
fun ProductAvailableSize(
    modifier: Modifier = Modifier,
    product: ProductDetails? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = "Tags Product",
            color = titleTextColor,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.padding(10.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(product?.tags ?: emptyList()) { item ->
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(70.dp)
                        .border(
                            color = lightGrey,
                            width = 2.dp,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { }) {
                    Text(
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                                end = 16.dp,
                                top = 10.dp,
                                bottom = 8.dp
                            ),
                        text = item,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )


                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun ProductItemColorWithDesc(
    modifier: Modifier = Modifier,
    product: ProductDetails? = null
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Description",
            color = titleTextColor,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = product?.description ?: "",
            color = lightblack,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ProductReviews(
    modifier: Modifier = Modifier,
    product: ProductDetails? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Reviews",
            color = titleTextColor,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.padding(10.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(product?.reviews ?: emptyList()) { item ->
                Box(
                    modifier = Modifier
                        .border(
                            color = lightGrey,
                            width = 2.dp,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = item.reviewerName ?: "No Name",
                            fontWeight = FontWeight.Bold,
                            color = titleTextColor,
                            fontSize = 12.sp
                        )

                        Text(
                            text = item.comment ?: "-",
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )

                        RatingBar(rating = item.rating?.toDouble() ?: 0.0)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailPreview() {
    AppTheme {
        DetailContent(
            uiState = DetailUIState(
                product = ProductDetails(
                    tags = listOf("Tags1", "Tags2"),
                    description = "Sample desc",
                    reviews = listOf(Review(), Review())
                )
            ),
            uiEvent = DetailUIEvent()
        )
    }
}