package com.example.rpncalculator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_postfix_style.*
import java.util.*

class PostfixStyle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postfix_style)
    }

    private var decimalAllowed = true
    private var operatorAllowed = false
    private var spaceAllowed = false

    @SuppressLint("SuspiciousIndentation")
    fun displayEntry(view: View) {
        operatorAllowed = false

        if (view is Button) {
            if (view.text == ".") {
                spaceAllowed = false

                if (decimalAllowed) {
                    if (input_screen.text.isEmpty())
                        input_screen.append(0.toString())
                    input_screen.append(view.text) }

                decimalAllowed = false
            } else
                input_screen.append(view.text)
                spaceAllowed = true

            if (input_screen.text.length > 2)
                operatorAllowed = true
        }
    }

    fun displayOperator(view: View) {
        if (view is Button && operatorAllowed && result_screen.text.isNotEmpty() && input_screen.text.isEmpty()) {
            input_screen.text = result_screen.text
            input_screen.append(view.text)
            decimalAllowed = true
        } else {
            if (view is Button && operatorAllowed) {
                input_screen.append(view.text)
                decimalAllowed = true }
        }
        spaceAllowed = true
    }

    fun spaceButton(view: View) {
        if (view is Button && spaceAllowed)
            input_screen.append(" ")

        spaceAllowed = false
    }

    private var memory = 0.0
    fun memoryPlusAction(view: View) {
        memory += result_screen.text.toString().toDouble()

        if (memory > 0.0) {

            memory_recall.isEnabled = true
            memory_recall.isClickable = true
            memory_recall?.setBackgroundColor(ContextCompat.getColor(this, R.color.green_light))

            memory_clear.isClickable = true
            memory_clear.isEnabled = true
            memory_clear?.setBackgroundColor(ContextCompat.getColor(this, R.color.green_light)) }}

    fun memoryRecallAction(view: View) {
        result_screen.text = memory.toString()}

    fun memoryClearAction(view: View) {
        memory = 0.0

        memory_recall.isEnabled = false
        memory_recall.isClickable = false
        memory_recall?.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_light))

        memory_clear.isClickable = false
        memory_clear.isEnabled = false
        memory_clear?.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_light)) }

    fun allClearAction(view: View) {
        input_screen.text = ""
        result_screen.text = "" }

    fun clearAction(view: View) {
        val length = input_screen.length()
        if(length > 0)
            input_screen.text = input_screen.text.subSequence(0, length - 1)}

    fun equalsButton(view: View) {
        result_screen.text = calculateResults()
        input_screen.text = calculateResults() }

    private fun calculateResults(): String {
        fun notOperator(ch: Char): Boolean = when (ch) {
            '+', '-', 'x', '/' -> false
            else -> true
        }

        fun evaluateExpression(expression: String): Double {
            var digit = ""
            val stack = Stack<Double>()

            for (character in expression)
            {
                if (notOperator(character) && character != ' ')
                {
                    digit += character }
                else if (character == ' ' && digit != "")
                {
                    stack.push(digit.toDouble())
                    digit = "" }
                else if (!notOperator(character))
                {
                    try{
                        val val1 = stack.pop()
                        val val2 = stack.pop()

                        when (character) {
                            '+' -> stack.push(val2 + val1)
                            '-' -> stack.push(val2 - val1)
                            '/' -> stack.push(val2 / val1)
                            'x' -> stack.push(val2 * val1) }
                    }catch(e: EmptyStackException){
                        return (0).toDouble()
                    }}
            }
            return try{
                stack.pop()
            } catch(e: EmptyStackException){
                (0).toDouble()
            }
        }
        val result = evaluateExpression(input_screen.text.toString())
        return result.toString()
    }

    fun switchCalculationStyle(view: View) {
        if (infix_or_postfix.isChecked){
            val intent = Intent(this, InfixStyle::class.java).apply {  }
            startActivity(intent)
        }
    }

}
