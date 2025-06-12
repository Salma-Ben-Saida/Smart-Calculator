# Smart-Calculator
An Android calculator app built with Kotlin , Android Studio . 

The app supports a wide range of mathematical operations and features a clean, user-friendly interface designed for both basic and advanced calculations.

**Features:

Basic Arithmetic: Addition, subtraction, multiplication, and division.

Decimal Numbers: Support for floating-point input and operations.

Parentheses Handling: Allows nested parentheses to define operation precedence.

Scientific Functions: Includes trigonometric functions (sin, cos, tan), logarithmic (log), exponential (exp), and square root (√).

Power Operations: Supports exponentiation including square, cube, and custom powers.

Factorial Function: Calculates factorials for positive integers.

Input Validation: Prevents invalid inputs like multiple decimals in one number or unbalanced parentheses.

Backspace and Clear: Button for deleting last input and clearing the entire expression.

Long-press Clear: Long press on backspace clears the entire expression instantly.

Error Handling: Detects errors such as division by zero or unbalanced parentheses and displays appropriate messages.

Expression Parsing and Evaluation: Parses user input into tokens and computes the result with correct operator precedence.

Real-time Calculation Preview (To add later): Can be extended to show ongoing results as the user types.

** Architecture and Implementation

Language: Kotlin

UI Framework: Android Jetpack ViewBinding for type-safe UI interactions.

Input Processing: The app converts the input expression into a list of tokens, handling numbers and operators separately.

Expression Evaluation: Implements custom logic for operator precedence, parentheses evaluation, and scientific functions without relying on built-in expression evaluators.

Extensibility: Designed with modular functions to easily add new operations or improve existing ones

** Usage

Tap number and operator buttons to build your expression.

Use parentheses buttons to group expressions.

Press the equals button to compute the result.

Use backspace to correct mistakes, or long press backspace to clear all.

**Future Improvements

Add memory functions (M+, M-, MR).

Implement a history feature to save previous calculations.

Support for more advanced math functions (e.g., hyperbolic trig, factorial for larger numbers).

Improve UI/UX with animations and better themes.

Add real-time result updating while typing.


<img width="216" alt="HomeScreen" src="https://github.com/user-attachments/assets/29353635-1613-4cdd-b87f-7f25f226c150" />

<img width="267" alt="Calculator" src="https://github.com/user-attachments/assets/65e537a2-4759-45a2-833c-df77087e7f52" />






