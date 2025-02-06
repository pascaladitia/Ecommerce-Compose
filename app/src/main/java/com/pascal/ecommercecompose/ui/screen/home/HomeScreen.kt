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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.domain.model.dummy.DataDummy.productList
import com.pascal.ecommercecompose.domain.model.dummy.Product
import com.pascal.ecommercecompose.ui.component.form.Search
import com.pascal.ecommercecompose.ui.theme.AppTheme
import com.pascal.ecommercecompose.ui.theme.lightGrey
import com.pascal.ecommercecompose.ui.theme.lightblack
import com.pascal.ecommercecompose.ui.theme.lightorange
import com.pascal.ecommercecompose.ui.theme.orange
import com.pascal.ecommercecompose.ui.theme.subTitleTextColor
import com.pascal.ecommercecompose.ui.theme.titleTextColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = koinViewModel(),
    onDetail: (Product) -> Unit
) {
    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        HomeContent {
            onDetail(it)
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onDetail: (Product) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(30.dp)) {
            TopAppBarHeader()
            Spacer(modifier = Modifier.padding(10.dp))
            OurProductsWithSearch()
            Spacer(modifier = Modifier.padding(20.dp))
            ProductCategory()
            Spacer(modifier = Modifier.padding(20.dp))
            ProductWidget() {
                onDetail(it)
            }
        }
    }
}

@Composable
fun TopAppBarHeader() {
    val context = LocalContext.current
    val pref = PreferencesLogin.getLoginResponse(context)

    Row(
        modifier = Modifier
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
                        .data(data = pref?.photo_url?.toUri())
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

@Composable
fun OurProductsWithSearch() {
    var search by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
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
                search = it
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
fun ProductCategory() {
    val itemList = listOf("Sneakers", "Jacket", "Watch", "Watch")
    val categoryImagesList = listOf<Int>(
        R.drawable.shoe_thumb_2,
        R.drawable.jacket,
        R.drawable.watch,
        R.drawable.watch
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(itemList.size) { item ->
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .border(
                        color = if (item == 0) orange else lightGrey,
                        width = 2.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(categoryImagesList[item]),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp, 30.dp)
                    )
                    Text(
                        modifier = Modifier
                            .padding(
                                start = 5.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            ),
                        text = itemList[item],
                        color = if (item == 0) lightblack else Color.LightGray
                    )
                }

            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun ProductWidget(
    modifier: Modifier = Modifier,
    onDetail: (Product) -> Unit
) {
    val productList = productList

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentPadding = PaddingValues(horizontal = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(productList) { item ->
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
                        .clickable { onDetail(item) }
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(12.dp)
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "",
                            tint = lightGrey
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
                            painter = painterResource(item.imageID),
                            contentDescription = "",
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
                            text = item.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = titleTextColor
                        )

                        Text(
                            text = item.category,
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
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    AppTheme {
        HomeContent() {}
    }
}
