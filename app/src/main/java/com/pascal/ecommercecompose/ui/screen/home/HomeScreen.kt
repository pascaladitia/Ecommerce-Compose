package com.pascal.ecommercecompose.ui.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pascal.ecommercecompose.ui.theme.AppTheme
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = koinInject<HomeViewModel>(),
    onDetail: () -> Unit
) {

}


@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    AppTheme {  }
}