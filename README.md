# Calculator
Simple calculator that supports "+ - * / ( )"

Input an expression as a String argument to the constructor, the result can be printed using printResult(). The input doesn't allow "=", typical legal input is like "(2+3)/4+5".

The most annoying part is to handle the corner cases, I believe I have handled most corner cases by throwing an IllegalArgumentException and print an error message.

Time complexity is around θ(3n), n is the total count of input signs and numbers.
