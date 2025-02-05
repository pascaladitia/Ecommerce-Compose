package com.pascal.ecommercecompose.ui.component.form

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.ecommercecompose.ui.theme.AppTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search

@Composable
fun Search(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()
    val border = if (isFocused.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

    var searchText by remember { mutableStateOf("") }

    BasicTextField(
        modifier = modifier
            .heightIn(min = 40.dp)
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp)),

        value = searchText,
        onValueChange = {
            searchText = it
        },
        textStyle = MaterialTheme.typography.bodySmall,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(searchText)
            }
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = FeatherIcons.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Cari Tugas Disini",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    AppTheme {
        Search() {

        }
    }
}