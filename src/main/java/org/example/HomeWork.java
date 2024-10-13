package org.example;

import java.util.*;

public class HomeWork {

    /**
     * <h1>Задание 1.</h1>
     * Требуется реализовать метод, который по входной строке будет вычислять математические выражения.
     * <br/>
     * Операции: +, -, *, / <br/>
     * Функции: sin, cos, sqr, pow <br/>
     * Разделители аргументов в функции: , <br/>
     * Поддержка скобок () для описания аргументов и для группировки операций <br/>
     * Пробел - разделитель токенов, пример валидной строки: "1 + 2 * ( 3 - 4 )" с результатом -1.0 <br/>
     * <br/>
     * sqr(x) = x^2 <br/>
     * pow(x,y) = x^y
     */
    double calculate(String expr) {
        Stack<Double> stack = new Stack<>();

        for (String token : translate(expr)) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                double b = stack.pop();
                double a = stack.pop();
                stack.push(applyOperator(a, b, token));
            } else if (isFunction(token)) {
                List<Double> args = new ArrayList<>();
                if (token.equals("pow")) {
                    double exponent = stack.pop();
                    double base = stack.pop();
                    args.add(base);
                    args.add(exponent);
                } else {
                    args.add(stack.pop());
                }
                stack.push(applyFunction(token, args));
            }
        }

        return stack.pop();
    }

    private static List<String> translate(String inputString) {
        List<String> output = new ArrayList<>();
        Deque<String> stack = new LinkedList<>();
        var inputStringFormated = inputString.replaceAll(",", " ")
                .replaceAll("\\(", " ( ")
                .replaceAll("\\)", " ) ");
        var input = inputStringFormated.split(" ");

        for (String cur : input) {
            if (isNumber(cur)) {
                output.add(cur);
            } else if (isFunction(cur)) {
                stack.push(cur);
            } else if (cur.equals("(")) {
                stack.push(cur);
            } else if (cur.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                stack.pop();
                if (!stack.isEmpty() && isFunction(stack.peek())) {
                    output.add(stack.pop());
                }
            } else if (isOperator(cur)) {
                while (!stack.isEmpty() && isOperator(stack.peek()) &&
                        getPrecedence(stack.peek()) >= getPrecedence(cur)) {
                    output.add(stack.pop());
                }
                stack.push(cur);
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }

        return output;
    }


    private static boolean isNumber(String number) {
        try{
            Integer.valueOf(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private static boolean isFunction(String token) {
        return token.equals("sin") || token.equals("cos") || token.equals("sqrt") || token.equals("pow");
    }

    private static int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return -1;
        }
    }

    private static double applyOperator(double a, double b, String operator) {
        switch (operator) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return a / b;
            default: throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    private static double applyFunction(String function, List<Double> args) {
        switch (function) {
            case "sin": return Math.sin(args.get(0));
            case "cos": return Math.cos(args.get(0));
            case "sqrt": return Math.sqrt(args.get(0));
            case "pow": return Math.pow(args.get(0), args.get(1));
            default: throw new IllegalArgumentException("Unknown function: " + function);
        }
    }
}
