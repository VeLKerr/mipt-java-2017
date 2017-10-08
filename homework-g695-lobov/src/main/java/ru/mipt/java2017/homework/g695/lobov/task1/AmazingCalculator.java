package ru.mipt.java2017.homework.g695.lobov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;


import java.util.Scanner;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
import java.util.regex.Pattern;


public class AmazingCalculator implements Calculator {

  private static final Set<Character> OPERATIONS =
      new HashSet<>(Arrays.asList('+', '-', '/', '*'));
  private static final Set<Character> NUMBERS =
      new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'));

  @Override
  public double calculate(String expr) throws ParsingException { //Основной метод
    if (expr == null) {
      throw new ParsingException("Null expression");
    }

    String postfixConverted = convertToPostfix(expr.replaceAll("\\s", "")); //Преобразование
    return postfixCalculation(postfixConverted); // Считаем результат для постфиксной записи
  }


  private String convertToPostfix(String str) throws ParsingException {
    Stack<Character> stack = new Stack<>(); // стек с операторами
    StringBuilder result = new StringBuilder(); // Итоговая преобразованная строка
    boolean nextIsUnary = true;
    for (Character c : str.toCharArray()) { //Перебор
      if (NUMBERS.contains(c)) { // Если символ - часть числа(цифра или точка)
        nextIsUnary = false;
        result.append(c); // добавляем к итогу
      } else if (OPERATIONS.contains(c)) {
        if (nextIsUnary) {
          if (c.equals('-')) { // Кладем в стек унарный минус
            stack.push('&');
            nextIsUnary = false;
          } else if (c.equals('+')) {
            nextIsUnary = false;
          } else {
            throw new ParsingException("Expression is invalid");
          }
        } else {
          nextIsUnary = true;
          result.append(' ');
          while (!stack.empty()) {
            Character current = stack.pop();
            if (operandPriority(c) <= operandPriority(current)) {
              result.append(' ').append(current).append(' ');
            } else {
              stack.push(current);
              break;
            }
          }
          stack.push(c); // Помещаем оператор в стек
        }
      } else if (c.equals('+')) {
        nextIsUnary = false;
      } else if (c.equals('(')) {
        nextIsUnary = true;
        result.append(' ');
        stack.push(c); // Помещаем ее в стек
      } else if (c.equals(')')) {
        nextIsUnary = false;
        boolean openingBracketExists = false;
        while (!stack.empty()) { // Выталкиваем элементы из стека
          Character current = stack.pop();
          if (current.equals('(')) { // Пока не найдем (
            openingBracketExists = true;
            break;
          } else {
            result.append(' ').append(current).append(' ');
          }
        }
        if (!openingBracketExists) {
          throw new ParsingException("Invalid amount of brackets");
        }
      } else {
        throw new ParsingException("Invalid symbol");
      }
    }

    while (!stack.empty()) { // Выталкиваем оставшиеся элементы из стека
      Character current = stack.pop();
      if (OPERATIONS.contains(current) || current.equals('&')) {
        result.append(' ').append(current).append(' ');
      } else {
        throw new ParsingException("Invalid expression");
      }
    }
    return result.toString();
  }

  private double getOneCalculation(double v1, double v2, char operator)
      throws ParsingException { // Подсчет результата действия одного оператора
    switch (operator) {
      case '+':
        return v1 + v2;
      case '-':
        return v1 - v2;
      case '*':
        return v1 * v2;
      case '/':
        return v1 / v2;
      default:
        throw new ParsingException("Invalid symbol");
    }
  }

  private int operandPriority(char ch) throws ParsingException { // Приоритет оператора
    switch (ch) {
      case '+':
        return 1;
      case '-':
        return 1;
      case '*':
        return 2;
      case '/':
        return 2;
      case '(':
        return 0;
      case ')':
        return 0;
      case '&':
        return 3;
      default:
        throw new ParsingException("Invalid symbol");
    }
  }

  private double postfixCalculation(String expression) throws ParsingException {
    try (Scanner sc = new Scanner(expression)) {
      Stack<Double> stack = new Stack<>(); // Стек (temporary)
      while (sc.hasNext()) {
        String s = sc.next();
        if (s.length() == 1 && OPERATIONS.contains(s.charAt(0))) { // Если это бинарный оператор
          if (stack.size() >= 2) { // То применяем его к двум верхним элементам стека
            double operand2 = stack.pop();
            double operand1 = stack.pop();
            double result = getOneCalculation(operand1, operand2, s.charAt(0));
            stack.push(result); // И кладем в стек
          } else {
            throw new ParsingException("Invalid expression");
          }
        } else if (s.length() == 1 && s.charAt(0) == '&') {
          if (stack.size() >= 1) {
            double operand = stack.pop();
            stack.push(-1 * operand);
          } else {
            throw new ParsingException("Invalid expression");
          }
        } else if (Pattern.matches("[-+]?[0-9]*\\.?[0-9]", s)) {
          double current = Double.parseDouble(s); // Иначе это число
          stack.push(current); // Кладем его в  стек
        } else {
          throw new ParsingException("Invalid expression");
        }
      }

      if (stack.size() == 1) { // В коонце в стеке должен остаться один элемент
        return stack.pop(); // Это результат
      } else {
        throw new ParsingException("Invalid expression");
      }
    }
  }
}