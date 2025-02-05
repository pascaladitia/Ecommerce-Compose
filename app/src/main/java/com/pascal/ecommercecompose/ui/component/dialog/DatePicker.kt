package com.pascal.ecommercecompose.ui.component.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    modifier: Modifier = Modifier,
    totalValue: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val dateRangePickerState = rememberDateRangePickerState()
    var startDate by remember {
        mutableLongStateOf(calendar.timeInMillis)
    }
    var endDate by remember {
        mutableLongStateOf(calendar.timeInMillis)
    }

        DatePickerDialog(
            modifier = modifier.shadow(6.dp, RoundedCornerShape(16.dp)),
            onDismissRequest = {
                onDismiss()
            },
            shape = RoundedCornerShape(4.dp),
            confirmButton = {
                TextButton(onClick = {
                    if (dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null) {
                        onDismiss()
                        startDate = dateRangePickerState.selectedStartDateMillis ?: startDate
                        endDate = dateRangePickerState.selectedEndDateMillis ?: endDate

                        val formatter = SimpleDateFormat("yyyy-mm-dd", Locale.ROOT)
                        onConfirm(formatter.format(Date(startDate)), formatter.format(Date(endDate)))

                        val totalMillis = endDate - startDate
                        val totalDays = TimeUnit.MILLISECONDS.toDays(totalMillis)
                        totalValue("$totalDays")
                    }
                }) {
                    Text(text = "Confirm")
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                title = {},
                headline = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Select Date Filter",
                        textAlign = TextAlign.Center
                    )
                },
                showModeToggle = false,
                modifier = Modifier.height(height = 500.dp)
            )
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val dateRangePickerState = rememberDatePickerState()
    var startDate by remember {
        mutableLongStateOf(calendar.timeInMillis)
    }

    DatePickerDialog(
        modifier = modifier.shadow(6.dp, RoundedCornerShape(16.dp)),
        onDismissRequest = {
            onDismiss()
        },
        shape = RoundedCornerShape(16.dp),
        confirmButton = {
            TextButton(onClick = {
                if (dateRangePickerState.selectedDateMillis != null) {
                    onDismiss()
                    startDate = dateRangePickerState.selectedDateMillis ?: startDate

                    val formatter = SimpleDateFormat("yyyy-mm-dd", Locale("id"))
                    onConfirm(formatter.format(Date(startDate)))
                }
            }) {
                Text(text = "Confirm")
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = dateRangePickerState,
            title = {},
            headline = {
                Text(
                    modifier = Modifier.padding(start = 32.dp, bottom = 8.dp),
                    text = "Select Date",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerRefComponent(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    val dateRangePickerState = rememberDatePickerState()
    var startDate by remember {
        mutableLongStateOf(calendar.timeInMillis)
    }

    DatePickerDialog(
        modifier = modifier.shadow(6.dp, RoundedCornerShape(16.dp)),
        onDismissRequest = {
            onDismiss()
        },
        shape = RoundedCornerShape(16.dp),
        confirmButton = {
            TextButton(onClick = {
                if (dateRangePickerState.selectedDateMillis != null) {
                    onDismiss()
                    startDate = dateRangePickerState.selectedDateMillis ?: startDate

                    onConfirm(Date(startDate))
                }
            }) {
                Text(text = "Confirm")
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = dateRangePickerState,
            title = {},
            headline = {
                Text(
                    modifier = Modifier.padding(start = 32.dp, bottom = 8.dp),
                    text = "Select Date",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            },
        )
    }
}
