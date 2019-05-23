package com.zz.calculator;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Calculator {

    private Stack<Character> signStack;
    private Stack<Double> numStack;

    private static final char plus = '+';
    private static final char subtract = '-';
    private static final char times = '*';
    private static final char divide = '/';

    Calculator(String input) {
        String inputString = input.replace(" ", "");
        signStack = new Stack<>();
        numStack = new Stack<>();
        inputHandler(inputString);
        calculate();
    }

    /**
     * This method is to put input to two stacks and throw IllegalArgumentException per error type
     * @param inputString
     */
    private void inputHandler(String inputString) {
        // this variable is to monitor if the upcoming sign is legal, depending on cases. Say, while false, 
        // it can only be switched to true by inserting a number.
        boolean readyForSign = false;
        // this variable is to monitor if the parenthesis is legal.
        int parenthesisCount = 0;
        for (int i = 0; i < inputString.length(); i++) {
            char testChar = inputString.charAt(i);
            if (testChar == plus || testChar == subtract || testChar == times || testChar == divide) {
                if (!readyForSign)
                    throw new IllegalArgumentException("Syntax error near: " + testChar);
                signStack.push(testChar);
                // inserting a sign will turn false because you dont wanna insert two signs in a row.
                readyForSign = false;
            }
            // is the loop encounters a char that is a number(digit), incremental i until the last index of the number.
            else if (Character.isDigit(testChar)) {
                int start = i;
                while (i < inputString.length() - 1 && (Character.isDigit(inputString.charAt(i + 1)) || inputString.charAt(i + 1) == '.')) {
                    i++;
                }
                String testString = inputString.substring(start, i + 1);
                if (testString.indexOf('.') != testString.lastIndexOf('.'))
                    throw new IllegalArgumentException("Too many decimals for one number");
                numStack.push(Double.parseDouble(testString));
                if (readyForSign)
                    throw new IllegalArgumentException("Syntax error near: " + testString);
                readyForSign = true;
            }
            else if (testChar == '(') {
                if (readyForSign)
                    throw new IllegalArgumentException("Syntax error near: " + testChar);
                signStack.push(testChar);
                parenthesisCount++;
            }
            else if (testChar == ')') {
                if (!readyForSign)
                    throw new IllegalArgumentException("Syntax error near: " + testChar);
                if (parenthesisCount == 0)
                    throw new IllegalArgumentException("Missing left parenthesis");
                // right parenthesis is always a trigger to remove the full "()", thus leaving the the final calculation without parentheses.
                calculate();
                parenthesisCount--;
            }
            else throw new IllegalArgumentException("Invalid signs");
        }
        if (parenthesisCount != 0)
            throw new IllegalArgumentException("Missing right parenthesis");
        if (!readyForSign)
            throw new IllegalArgumentException("Input is not correctly ended");
    }

    /**
     * this method is a little bit tricky, I ignore '+' and '-' for the first loop, but add the candidate numbers and signs 
     * to the end of the LinkedList. The second loop will calculate '+' and '-' which leaves only the final result in the numList.
     */
    private void calculate() {
        List<Double> numList = new LinkedList<>();
        List<Character> signList = new LinkedList<>();
        while (!numStack.empty()) {
                numList.add(0, numStack.pop());
            if (!signStack.empty()) {
                if (signStack.peek() == '(') {
                    signStack.pop();
                    break;
                }
                signList.add(0, signStack.pop());
            }
        }
        int count = 1;
        int size = signList.size();
        while (!signList.isEmpty()) {
            double number = numList.remove(0);
            char sign = signList.remove(0);
            if (count <= size) {
                if (sign == '*')
                    numList.add(0, number * numList.remove(0));
                if (sign == '/')
                    numList.add(0, number / numList.remove(0));
                if (sign == '+' || sign == '1') {
                    numList.add(number);
                    signList.add(sign);
                }
            }
            else {
                if (sign == '+')
                    numList.add(0, number + numList.remove(0));
                if (sign == '-')
                    numList.add(0, number - numList.remove(0));
            }
            count++;
        }
        numStack.push(numList.remove(0));
    }

    public void printResult() {
        System.out.println("The result is: " + numStack.peek());
    }

    public static void main(String[] args) {
        // input doesn't allow "="
        Calculator calculator = new Calculator("(3+21/3+8/2)/5 +4-5");
        calculator.printResult();
    }
}
