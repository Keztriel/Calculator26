package com.kez.Calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.kez.Calculator.ui.components.CalculatorScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var displayText by rememberSaveable { mutableStateOf("0") }
            var isError by rememberSaveable { mutableStateOf(false) }
            var operand by rememberSaveable { mutableStateOf<Double?>(null) }
            var pendingOperation by rememberSaveable { mutableStateOf<String?>(null) }
            var isReadyForNewInput by rememberSaveable { mutableStateOf(false) }

            val onButtonClick: (String) -> Unit = { symbol ->
                when (symbol) {
                    "C" -> {
                        displayText = "0"
                        isError = false
                        operand = null
                        pendingOperation = null
                        isReadyForNewInput = false
                    }
                    "+", "-", "*", "/" -> {
                        if (!isError) {
                            val currentValue = displayText.toDoubleOrNull()
                            if (currentValue != null) {
                                if (pendingOperation != null && !isReadyForNewInput) {
                                    val firstValue = operand ?: currentValue
                                    val result = when (pendingOperation) {
                                        "+" -> firstValue + currentValue
                                        "-" -> firstValue - currentValue
                                        "*" -> firstValue * currentValue
                                        "/" -> if (currentValue == 0.0) Double.NaN else firstValue / currentValue
                                        else -> currentValue
                                    }

                                    if (result.isNaN() || result.isInfinite()) {
                                        isError = true
                                        displayText = "0"
                                        operand = null
                                    } else {
                                        displayText = if (result % 1.0 == 0.0) result.toLong().toString() else result.toString()
                                        operand = result
                                    }
                                } else {
                                    operand = currentValue
                                }
                                pendingOperation = symbol
                                isReadyForNewInput = true
                            }
                        }
                    }
                    "=" -> {
                        if (!isError && pendingOperation != null) {
                            val currentValue = displayText.toDoubleOrNull()
                            val firstValue = operand
                            if (currentValue != null && firstValue != null) {
                                val result = when (pendingOperation) {
                                    "+" -> firstValue + currentValue
                                    "-" -> firstValue - currentValue
                                    "*" -> firstValue * currentValue
                                    "/" -> if (currentValue == 0.0) Double.NaN else firstValue / currentValue
                                    else -> currentValue
                                }

                                if (result.isNaN() || result.isInfinite()) {
                                    isError = true
                                    displayText = "0"
                                    operand = null
                                } else {
                                    displayText = if (result % 1.0 == 0.0) result.toLong().toString() else result.toString()
                                    operand = result
                                }
                                pendingOperation = null
                                isReadyForNewInput = true
                            }
                        }
                    }
                    else -> {
                        if (displayText == "0" || isReadyForNewInput || isError) {
                            displayText = symbol
                            isError = false
                            isReadyForNewInput = false
                        } else {
                            displayText += symbol
                        }
                    }
                }
            }

            CalculatorScreen(
                displayText = displayText,
                isError = isError,
                onButtonClick = onButtonClick
            )
        }
    }
}
