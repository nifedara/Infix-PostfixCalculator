package com.example.rpncalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_postfix_style.*

class InfixStyle : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infix_style) }


    // ---- DISPLAY ENTRY (0-9, .) ---- //
    fun displayEntry(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal){
                    if (input_screen.text.isEmpty())
                        input_screen.append(0.toString())
                    input_screen.append(view.text)}

                canAddDecimal = false
            } else
                input_screen.append(view.text)

            canAddOperation = true }}


    // ---- OPERATOR DISPLAY ---- //
    fun displayOperator(view: View) {

        if (view is Button && canAddOperation && result_screen.text.isNotEmpty() && input_screen.text.isEmpty()) {
            input_screen.text = result_screen.text
            input_screen.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        } else {
            if (view is Button && canAddOperation) {
                input_screen.append(view.text)
                canAddOperation = false
                canAddDecimal = true }
        }}

    // ---- EQUALS BUTTON ---- //
    fun equalsButton(view: View) {
        result_screen.text = calculateResults()
        input_screen.text = calculateResults() }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString() }


    // ---- MEMORY ACTIONS ---- //
    private var memory = 0.0
    fun memoryPlusAction(view: View) {
        memory += result_screen.text.toString().toDouble()

        if (memory > 0.0) {

            memory_recall.isEnabled = true
            memory_recall.isClickable = true
            memory_recall?.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))

            memory_clear.isClickable = true
            memory_clear.isEnabled = true
            memory_clear?.setBackgroundColor(ContextCompat.getColor(this, R.color.orange)) }}

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

    // ---- CLEAR ALL ACTION ---- //
    fun allClearAction(view: View) {
        input_screen.text = ""
        result_screen.text = "" }


    // ---- BACKSPACE ACTION ---- //
    fun clearAction(view: View)
    {
        val length = input_screen.length()
        if(length > 0)
            input_screen.text = input_screen.text.subSequence(0, length - 1)
    }


    // ---- PERFORMS ADDITION AND SUBTRACTION ---- //
    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit }
        }
        return result }


    // ---- PERFORMS MULTIPLICATION AND DIVISION ---- //
    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {
            list = calcTimesDiv(list) }
        return list }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }}}

            if (i > restartIndex)
                newList.add(passedList[i])}
        return newList }


    // ---- OPERATES ON DIGITS ---- //
    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in input_screen.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character) }
        }

        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list }

    // ---- SWITCHES CALCULATOR STYLE ---- //
    fun switchCalculationStyle(view: View) {
        if (!infix_or_postfix.isChecked) {
            val intent = Intent(this, PostfixStyle::class.java).apply { }
            startActivity(intent)
        }
    }
}