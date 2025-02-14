package com.pascal.ecommercecompose.ui.screen.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.ui.component.button.ButtonComponent
import com.pascal.ecommercecompose.ui.component.screenUtils.CardComponent
import com.pascal.ecommercecompose.ui.theme.AppTheme
import com.pascal.ecommercecompose.utils.calculateTotalPrice
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronLeft
import compose.icons.feathericons.Download
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReportScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: ReportViewModel = koinViewModel(),
    product: List<ProductEntity>? = null,
    onNavBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadReport(product)
        viewModel.deleteCart()
    }

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        ReportContent(
            product = product,
            uiEvent = ReportUIEvent(
                onDownload = {
                    viewModel.generatePdfAndOpen(context, it)
                },
                onNavBack = {
                    onNavBack()
                }
            )
        )
    }
}

@Composable
fun ReportContent(
    modifier: Modifier = Modifier,
    product: List<ProductEntity>? = emptyList(),
    uiEvent: ReportUIEvent
) {
    val context = LocalContext.current
    val pref = PreferencesLogin.getLoginResponse(context)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable { uiEvent.onNavBack() },
                imageVector = FeatherIcons.ChevronLeft,
                contentDescription = null
            )

            Spacer(Modifier.width(16.dp))

            Text(
                text = "Back",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(Modifier.height(24.dp))

        CardComponent {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .width(150.dp),
                painter = painterResource(R.drawable.logo),
                contentDescription = null
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Report Transaction",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "General Information",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.height(16.dp))

            ReportItemText(
                title = "Name",
                value = pref?.name ?: "No Name"
            )

            ReportItemText(
                title = "Email",
                value = pref?.email ?: "Email"
            )

            Spacer(Modifier.height(24.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Transaction",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentPadding = PaddingValues(horizontal = 5.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(product ?: emptyList()) {
                    ReportItemText(
                        title = "Product name",
                        value = it.name ?: ""
                    )

                    ReportItemText(
                        title = "Quantity",
                        value = it.qty.toString()
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            ReportItemText(
                title = "Total Amount",
                value = calculateTotalPrice(product ?: emptyList())
            )

            Spacer(Modifier.width(32.dp))

            ButtonComponent(
                text = "Download",
                isIcon = 1,
                icon = FeatherIcons.Download
            ) {
                uiEvent.onDownload(product)
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun ReportItemText(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    isLast: Boolean = false
) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                modifier = Modifier.weight(1f),
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.secondary
                ),
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (!isLast) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ReportPreview() {
    AppTheme {
        ReportContent(
            uiEvent = ReportUIEvent()
        )
    }
}

