package com.pascal.ecommercecompose.ui.screen.home

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.domain.model.user.User
import com.pascal.ecommercecompose.ui.component.dialog.ShowDialog
import com.pascal.ecommercecompose.ui.component.form.Search
import com.pascal.ecommercecompose.ui.component.screenUtils.LoadingScreen
import com.pascal.ecommercecompose.ui.component.screenUtils.NetworkComponent
import com.pascal.ecommercecompose.ui.component.screenUtils.PullRefreshComponent
import com.pascal.ecommercecompose.ui.component.screenUtils.TopAppBarHeader
import com.pascal.ecommercecompose.ui.theme.AppTheme
import com.pascal.ecommercecompose.ui.theme.lightGrey
import com.pascal.ecommercecompose.ui.theme.lightorange
import com.pascal.ecommercecompose.ui.theme.orange
import com.pascal.ecommercecompose.ui.theme.subTitleTextColor
import com.pascal.ecommercecompose.ui.theme.titleTextColor
import com.pascal.ecommercecompose.utils.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = koinViewModel(),
    onDetail: (String?) -> Unit
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val pref = PreferencesLogin.getLoginResponse(context)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadCategory(context)
    }

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) LoadingScreen()

        if (uiState.isError) {
            ShowDialog(
                message = uiState.message,
                textButton = stringResource(R.string.close)
            ) {
                viewModel.setError(false)
            }
        }

        PullRefreshComponent(
            onRefresh = {
                viewModel.loadProducts(context)
            }
        ) {
            Box {
                HomeContent(
                    user = pref,
                    uiState = uiState,
                    uiEvent = HomeUIEvent(
                        onSearch = {
                            viewModel.searchProduct(it)
                        },
                        onCategory = {
                            coroutine.launch {
                                if (viewModel.isOnline(context)) {
                                    viewModel.loadProducts(context, it)
                                } else {
                                    showToast(context, context.getString(R.string.connection_offline))
                                }
                            }
                        },
                        onFavorite = { isFav, item ->
                            coroutine.launch {
                                viewModel.saveFavorite(isFav, item)
                            }
                        },
                        onDetail = {
                            onDetail(it?.id.toString())
                        }
                    )
                )

                NetworkComponent(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    isOnline = {
                        coroutine.launch {
                            viewModel.loadProducts(context)
                        }
                    },
                    isOffline = {
                        coroutine.launch {
                            viewModel.loadProducts(context)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    user: User? = null,
    uiState: HomeUIState,
    uiEvent: HomeUIEvent,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
        ) {

            TopAppBarHeader(user = user)

            Spacer(modifier = Modifier.padding(10.dp))

            OurProductsWithSearch(uiEvent = uiEvent)

            Spacer(modifier = Modifier.padding(16.dp))

            ProductCategory(
                category = uiState.category,
                uiEvent = uiEvent
            )

            Spacer(modifier = Modifier.padding(16.dp))

            ProductWidget(
                uiState = uiState,
                uiEvent = uiEvent
            )
        }
    }
}

@Composable
fun OurProductsWithSearch(
    modifier: Modifier = Modifier,
    uiEvent: HomeUIEvent
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
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
                        append("Our\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = titleTextColor,
                            fontSize = 24.sp
                        )
                    ) {
                        append("Products")
                    }
                }
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .padding(top = 30.dp)
        ) {
            Search(
                modifier = Modifier.weight(1f)
            ) {
                uiEvent.onSearch(it)
            }
            Spacer(modifier = Modifier.width(5.dp))
            Card(
                modifier = Modifier
                    .width(60.dp)
                    .padding(start = 16.dp)
                    .clickable { },
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(12.dp),
                colors =  CardDefaults.cardColors(Color.White)
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(R.drawable.filter_list),
                        contentDescription = "Filter Icon",
                        modifier = Modifier.size(20.dp, 20.dp)
                    )

                }
            }
        }
    }
}

@Composable
fun ProductCategory(
    modifier: Modifier = Modifier,
    category: List<String>? = null,
    uiEvent: HomeUIEvent
) {
    var isSelect by remember { mutableIntStateOf(-1) }

    LazyRow(
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(category ?: emptyList()) { index, item ->

            if (index != 0) Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        isSelect = index
                        uiEvent.onCategory(item)
                    }
                    .height(40.dp)
                    .border(
                        color = if (index == isSelect) orange else lightGrey,
                        width = 2.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    color = if (index == isSelect) orange else Color.LightGray
                )
            }
        }
    }
}

@Composable
fun ProductWidget(
    modifier: Modifier = Modifier,
    uiState: HomeUIState,
    uiEvent: HomeUIEvent,
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(uiState.product ?: emptyList()) { item ->

            var isFavorite by remember { mutableStateOf(item.isFavorite ?: false) }

            Card(
                modifier = Modifier
                    .width(180.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors =  CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { uiEvent.onDetail(item) }
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(12.dp)
                ) {
                    IconButton(onClick = {
                        isFavorite = !isFavorite
                        uiEvent.onFavorite(isFavorite, item)
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription = "",
                            tint = if (isFavorite) orange else lightGrey
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .clip(CircleShape)
                            .background(lightorange),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .size(100.dp),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "",
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(data = item.thumbnail ?: "")
                                    .error(R.drawable.no_thumbnail)
                                    .placeholder(R.drawable.loading)
                                    .apply { crossfade(true) }
                                    .build()
                            )
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = item.title ?: "No Title",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = titleTextColor,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = item.category ?: "No Category",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = orange,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

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
                                    append(item.price.toString())
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

        item {
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    AppTheme {
        HomeContent(
            uiState = HomeUIState(
                category = listOf("Category 1", "Category 2"),
                product = listOf(ProductEntity(), ProductEntity(), ProductEntity()),
            ),
            uiEvent = HomeUIEvent()
        )
    }
}
