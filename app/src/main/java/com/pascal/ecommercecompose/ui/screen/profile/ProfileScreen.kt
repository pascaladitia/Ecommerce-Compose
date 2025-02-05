package com.pascal.ecommercecompose.ui.screen.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pascal.ecommercecompose.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: ProfileViewModel = koinViewModel(),
    onDetail: () -> Unit
) {

}


@Preview(showBackground = true)
@Composable
private fun ProfilePreview() {
    AppTheme {  }
}