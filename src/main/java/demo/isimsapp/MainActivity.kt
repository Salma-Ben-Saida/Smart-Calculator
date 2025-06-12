package demo.isimsapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import demo.isimsapp.databinding.ActivityMainBinding
import kotlin.math.*

class MainActivity : AppCompatActivity(){

    private var canAddOperation = false
    private var canAddDecimal = true
    private lateinit var binding: ActivityMainBinding




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBs.setOnClickListener {
            backSpaceAction(it)
        }

        binding.btnBs.setOnLongClickListener {
            allClearAction(it)
            true
        }
    }




    fun numberAction(view: View){
        if(view is Button){
            if(view.text=="."){
                if(canAddDecimal)
                    binding.workings.append(view.text)

                canAddDecimal=false
            }
            else
                binding.workings.append(view.text)

            canAddOperation=true
        }
    }

    fun clickedOperation(view:View){
        if(view is Button && canAddOperation){
            binding.workings.append(view.text)
            canAddOperation=false
            canAddDecimal=true
        }
    }

    fun allClearAction(view:View){
        binding.workings.text=""
        binding.results.text=""
    }

    fun backSpaceAction(view: View){
        val l=binding.workings.length()
        if(l>0)
            binding.workings.text=binding.workings.text.subSequence(0,l-1)
    }

    //Handling parentheses

    fun parOpenAction(view: View) {
        binding.workings.append("(")
    }

    fun parCloseAction(view: View) {
        binding.workings.append(")")
    }
    private fun areParenthesesBalanced(text: String): Boolean {
        var count = 0
        for (char in text) {
            if (char == '(') count++
            else if (char == ')') {
                count--
                if (count < 0) return false // More closing than opening
            }
        }
        return count == 0
    }

    // Adding Smart buttons to the working text view

    fun piAction(view: View){
        binding.workings.append("3.14")
    }
    fun pow2Action(view: View){
        binding.workings.append("^2")
    }
    fun pow3Action(view: View){
        binding.workings.append("^3")
    }
    fun powAction(view: View){
        binding.workings.append("^")
    }
    fun sqrtAction(view: View){
        binding.workings.append("√")
    }
    fun sinAction(view: View){
        binding.workings.append("sin()")
    }
    fun cosAction(view: View){
        binding.workings.append("cos()")
    }
    fun tanAction(view: View){
        binding.workings.append("tan()")
    }



    fun equalsButton(view: View){
        val inputList=getInputList()
        if(inputList.isNotEmpty())
            binding.results.text=calculate(inputList)
    }

    private fun getInputList():MutableList<Any>{

        val expression = binding.workings.text.toString()
        val list = mutableListOf<Any>()

        if (!areParenthesesBalanced(expression))
            Toast.makeText(this, "Error: Parentheses are unbalanced", Toast.LENGTH_SHORT).show()
        else{

            var currentDigit = ""
            for (c in expression) {
                if (c.isDigit() || c == '.') {
                    currentDigit += c
                } else {
                    if (currentDigit.isNotEmpty()) {
                        list.add(currentDigit)
                        currentDigit = ""
                    }
                    list.add(c.toString())
                }
            }
            if (currentDigit.isNotEmpty()) {
                list.add(currentDigit)
            }
        }
        return list
    }
    private fun calculate(inputList: MutableList<Any>): String{

        val simpleIn=calcPar(inputList)

        val powRes=powerOperations(simpleIn)
        if(powRes.isEmpty()) return ""

        val multipDivResult = multipDiv(powRes)
        if(multipDivResult.isEmpty()) return ""

        val res= addSubs(multipDivResult)
        return res.toString()

    }

    private fun calcPar(inputList: MutableList<Any>): MutableList<Any> {
        while (inputList.contains("(")) {
            val innerMostPar = inputList.lastIndexOf("(")
            val subListAfter = inputList.subList(innerMostPar + 1, inputList.size)
            val closestIndexInSub = subListAfter.indexOf(")")

            if (closestIndexInSub == -1) break // unmatched parenthesis

            val closestIndex = closestIndexInSub + innerMostPar + 1

            val subExpression = inputList.subList(innerMostPar + 1, closestIndex)
            val res = calculate(subExpression.toMutableList())

            val funcIndex = innerMostPar - 1
            if (funcIndex >= 0 && inputList[funcIndex] is String) {
                val funcName = (inputList[funcIndex] as String).lowercase()
                if (funcName in listOf("sin", "cos", "tan", "log", "exp")) {
                    val funcResult = applyScientificFunction(funcName, res)

                    for (i in closestIndex downTo funcIndex) {
                        inputList.removeAt(i)
                    }
                    inputList.add(funcIndex, funcResult)
                    continue
                }
            }

            for (i in closestIndex downTo innerMostPar) {
                inputList.removeAt(i)
            }
            inputList.add(innerMostPar, res)
        }
        return inputList
    }


    private fun applyScientificFunction(funcName: String, argument: String): String {
        val argValue = argument.toDoubleOrNull() ?: return ""
        return when(funcName) {
            "sin" -> kotlin.math.sin(Math.toRadians(argValue)).toString()
            "cos" -> kotlin.math.cos(Math.toRadians(argValue)).toString()
            "tan" -> kotlin.math.tan(Math.toRadians(argValue)).toString()
            "log" -> kotlin.math.log10(argValue).toString()
            "exp" -> kotlin.math.exp(argValue).toString()
            "√" -> kotlin.math.sqrt(argValue).toString()
            else -> ""
        }
    }
    private fun powerOperations(inputList: MutableList<Any>): MutableList<Any> {
        var index = 0
        while (index < inputList.size) {
            val token = inputList[index]

            if (token is String && token == "!") {
                if (index > 0 && inputList[index - 1] is String) {
                    val numberStr = inputList[index - 1] as String
                    val number = numberStr.toIntOrNull()
                    if (number != null && number >= 0) {
                        val factResult = factorial(number)

                        inputList.removeAt(index)
                        inputList.removeAt(index - 1)
                        inputList.add(index - 1, factResult.toString())
                        index = maxOf(0, index - 2)
                        continue
                    }
                }
            }

            if (token is String && token == "^") {
                if (index > 0 && index + 1 < inputList.size) {
                    val baseStr = inputList[index - 1] as? String
                    val exponentStr = inputList[index + 1] as? String

                    val base = baseStr?.toDoubleOrNull()
                    val exponent = exponentStr?.toDoubleOrNull()

                    if (base != null && exponent != null) {
                        val powResult = base.pow(exponent)
                        inputList.removeAt(index + 1)
                        inputList.removeAt(index)
                        inputList.removeAt(index - 1)
                        inputList.add(index - 1, powResult.toString())
                        index = maxOf(0, index - 2)
                        continue
                    }
                }
            }
            index++
        }
        return inputList
    }

    private fun factorial(n: Int): Long {
        if (n <= 1) return 1
        var result = 1L
        for (i in 2..n) {
            result *= i
        }
        return result
    }
    private fun multipDiv(inputList: MutableList<Any>): MutableList<Any> {
        var index = 0
        while (index < inputList.size) {
            val token = inputList[index]

            if (token is String && (token == "×" || token == "*" || token == "÷" || token == "/")) {
                if (index > 0 && index + 1 < inputList.size) {
                    val leftStr = inputList[index - 1] as? String
                    val rightStr = inputList[index + 1] as? String

                    val left = leftStr?.toDoubleOrNull()
                    val right = rightStr?.toDoubleOrNull()

                    if (left != null && right != null) {
                        val result = when (token) {
                            "×", "*" -> left * right
                            "÷", "/" -> {
                                if (right == 0.0) {
                                    // Handle division by zero if needed
                                    return mutableListOf("Error: Division by zero")
                                } else {
                                    left / right
                                }
                            }
                            else -> 0.0 // Should never reach here
                        }

                        // Remove left operand, operator, right operand
                        inputList.removeAt(index + 1) // right operand
                        inputList.removeAt(index)     // operator
                        inputList.removeAt(index - 1) // left operand

                        // Insert result as string
                        inputList.add(index - 1, result.toString())

                        // Reset index to start over to handle consecutive ops
                        index = maxOf(0, index - 2)
                        continue
                    }
                }
            }
            index++
        }
        return inputList
    }

    private fun addSubs(inputList: MutableList<Any>): String {
        var index = 0
        while (index < inputList.size) {
            val token = inputList[index]

            if (token is String && (token == "+" || token == "-")) {
                if (index > 0 && index + 1 < inputList.size) {
                    val leftStr = inputList[index - 1] as? String
                    val rightStr = inputList[index + 1] as? String

                    val left = leftStr?.toDoubleOrNull()
                    val right = rightStr?.toDoubleOrNull()

                    if (left != null && right != null) {
                        val result = when (token) {
                            "+" -> left + right
                            "-" -> left - right
                            else -> 0.0
                        }

                        inputList.removeAt(index + 1)
                        inputList.removeAt(index)
                        inputList.removeAt(index - 1)
                        inputList.add(index - 1, result.toString())
                        index = maxOf(0, index - 2)
                        continue
                    }
                }
            }
            index++
        }
        return inputList.firstOrNull().toString()
    }




}



