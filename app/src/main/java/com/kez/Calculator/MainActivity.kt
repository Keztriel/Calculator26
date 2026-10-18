package com.kez.Calculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.kez.Calculator.ui.components.CalculatorScreen
import kotlin.math.roundToLong

enum class ExpressionState  {
    Idle,
    RightInput,
    OperatorInput,
    LeftInput,
    Error
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var displayText by rememberSaveable { mutableStateOf("0") }
            var state by rememberSaveable { mutableStateOf<ExpressionState>(ExpressionState.Idle) }
            var operand by rememberSaveable { mutableStateOf<Double?>(null) }
            var pendingOperation by rememberSaveable { mutableStateOf<String?>(null) }
            var hasPoint by rememberSaveable { mutableStateOf(false) }

            fun formatDouble(value: Double): String {

                if (value.isNaN() || value.isInfinite()) {
                    state = ExpressionState.Error
                    return "0"
                }
                val rounded = (value * 1e12).roundToLong() / 1e12
                return if (rounded % 1.0 == 0.0) rounded.toLong().toString() else rounded.toString()
            }

            fun reset() {
                displayText = "0"
                state = ExpressionState.Idle
                pendingOperation = ""
                hasPoint = false
            }

            fun calculate(validNum: Double) {
//                if (pendingOperation == "/" && validNum == 0.0) {
//                    state = ExpressionState.Error
//                    return
//                }
                when(pendingOperation) {
                    "+" -> operand = operand?.plus(validNum)
                    "-" -> operand = operand?.minus(validNum)
                    "*" -> operand = operand?.times(validNum)
                    "/" -> operand = operand?.div(validNum)
                }
                state = ExpressionState.RightInput
                operand?.let { displayText = formatDouble(it) }
            }

            val onButtonClick: (String) -> Unit = { symbol ->
                when(state) {
                    ExpressionState.Idle -> when(symbol) {
                        in "1".."9" -> {
                            displayText = symbol
                            state = ExpressionState.RightInput
                        }
                        "." -> {
                            hasPoint = true
                            displayText = "0."
                            state = ExpressionState.RightInput
                        }
                        "-" -> {
                            displayText = "-"
                            state = ExpressionState.RightInput
                        }
                        "+","/","*" -> {
                            pendingOperation = symbol
                            state = ExpressionState.OperatorInput
                        }
                    }
                    ExpressionState.RightInput -> when(symbol) {
                        in "0".."9" -> {
                            displayText += symbol
                        }
                        "+","-","/","*" -> {
                            pendingOperation = symbol
                            state = ExpressionState.OperatorInput
                        }
                        "." -> {
                            if (!hasPoint){
                                hasPoint = true
                                displayText += symbol
                            }
                        }
                        "C" -> reset()
                    }
                    ExpressionState.OperatorInput -> when(symbol) {
                        in "0".."9","-" -> displayText.toDoubleOrNull()?.let {
                            validNum -> operand = validNum
                            displayText = symbol
                            hasPoint = false
                            state = ExpressionState.LeftInput
                        } ?: run {
                            state = ExpressionState.Error
                        }
//                        "+","-","/","*" -> {
//                            pendingOperation = symbol
//                        }
                        "C" -> reset()
                    }
                    ExpressionState.LeftInput -> when(symbol) {
                        "0" -> {
                            if (displayText != "0") displayText += symbol
                        }
                        in "1".."9" -> {
                            displayText += symbol
                        }
                        "." -> {
                           if (!hasPoint) {
                                hasPoint = true
                                displayText += symbol
                            }
                        }
                        "+","-","/","*" -> displayText.toDoubleOrNull()?.let{validNum ->
                            calculate(validNum)
                            pendingOperation = symbol
                            state = ExpressionState.OperatorInput

                        }
                        "=" -> displayText.toDoubleOrNull()?.let{
                            validNum -> calculate(validNum)
                        }
                        "C" -> reset()
                    }
                    ExpressionState.Error -> {
                        reset()
                    }
                }
            Log.d("State", state.toString())
            }

            CalculatorScreen(
                displayText = displayText,
                isError = state == ExpressionState.Error,
                onButtonClick = onButtonClick
            )
        }
    }
}
