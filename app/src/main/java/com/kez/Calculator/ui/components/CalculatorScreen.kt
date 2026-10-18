package com.kez.Calculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kez.Calculator.R

@Composable
fun CalculatorScreen(
    displayText: String,
    isError: Boolean,
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val textToDisplay = if (isError) {
        stringResource(id = R.string.error_msg)
    } else {
        displayText
    }

    val opColor = colorResource(id = R.color.calc_op_color)
    val numColor = colorResource(id = R.color.calc_num_color)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = textToDisplay,
                fontSize = 48.sp
            )
        }

        val buttons = listOf(
            listOf("1", "2", "3", "-"),
            listOf("4", "5", "6", "*"),
            listOf("7", "8", "9", "/"),
            listOf("C", "0", "=", "+")
        )

        val buttonColors = listOf(
            listOf(numColor, numColor, numColor, opColor),
            listOf(numColor, numColor, numColor, opColor),
            listOf(numColor, numColor, numColor, opColor),
            listOf(opColor, numColor, opColor, opColor)
        )

        buttons.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEachIndexed { colIndex, symbol ->
                    val currentBgColor = buttonColors[rowIndex][colIndex]
                    val currentTextColor = androidx.compose.ui.graphics.Color.White

                    Button(
                        onClick = { onButtonClick(symbol) },
                        colors = ButtonDefaults.buttonColors(containerColor = currentBgColor),
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    ) {
                        Text(
                            text = symbol,
                            fontSize = 24.sp,
                            color = currentTextColor
                        )
                    }
                }
            }
        }
    }
}
