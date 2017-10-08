package ru.mipt.java2017.homework.g697.moiseeva.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author Moiseeva Anastasia @alloky
 * @since 04.10.17
  */
public class MyCalculator implements Calculator {

  // Стек чисел.
  private Stack<Double> numbers = new Stack<>();
  // Стек операций.
  private Stack<String> operations = new Stack<>();
  // Позиция в выражении.
  private Integer position = 0;

  /**
   * @param str строка-выражение.
   * @return строка, заключённая в скобки.
   */
  private String concludeStringInParentheses(String str) {
    String string1 = "(";
    String string2 = ")";
    String string = string1.concat(str.concat(string2));
    return string;
  }

  /**
   * @param s строка.
   * @return результат преобразования строки в число (-1 - если невозможно преобразовать).
   */
  private double invertToDouble(String s) {
    try {
      return Double.parseDouble(s);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  /**
   * Определим какую операцию встретили.
   *
   * @param part операция.
   * @return номер операции.
   * @throws ParsingException сообщение об ошибке.
   */
  private int definitionOfSymbol(String part) throws ParsingException {
    switch (part) {
      case "(":
        return 1;
      case ")":
        return 2;
      case "+":
        return 3;
      case "-":
        return 4;
      case "*":
        return 5;
      case "/":
        return 6;
      default:
        throw new ParsingException("Invalid operation");
    }
  }

  // Достаём операцию.
  private static void pop(Stack<Double> numbers, Stack<String> operations) throws ParsingException {
    double b = numbers.pop();
    double a = numbers.pop();
    switch (operations.pop()) {
      case "+":
        numbers.push(a + b);
        break;
      case "-":
        numbers.push(a - b);
        break;
      case "*":
        numbers.push(a * b);
        break;
      case "/":
        numbers.push(a / b);
        break;
      default:
        throw new ParsingException("Invalid operation");
    }
  }

  /**
   * Проверка на возможность доставать операции из стека.
   *
   * @param str текущая операция.
   * @param operations стек операций.
   * @return результат проверки.
   * @throws ParsingException сообщение об ошибке.
   */
  private static boolean canPop(String str, Stack<String> operations)throws ParsingException {
    boolean f;
    if (operations.size() == 0) {
      f = false;
    } else {
      int p1 = priority(str);
      int p2 = priority(operations.peek());
      f = (p1 >= 0 && p2 >= 0 && p1 >= p2);
    }
    return f;
  }

  /**
   * Определение приоритета операции.
   *
   * @param str текущая операция.
   * @return приоритет.
   * @throws ParsingException сообщение об ошибке.
   */
  private static int priority(String str) throws ParsingException {
    switch (str) {
      case "(":
        return -1;
      case "+":
        return 2;
      case "-":
        return 2;
      case "*":
        return 1;
      case "/":
        return 1;
      default:
        throw new ParsingException("Invalid operation");
    }
  }

  /**
   * @param str строка-выражение.
   * @return значение выражения, если оно правильно построено.
   * @throws ParsingException сообщение об ошибке.
   */
  public double calculate(String str) throws ParsingException {
    if (str == null || str.equals("")) {
      throw new ParsingException("Null expression");
    }
    // Заключаем строку в скобки.
    String string = concludeStringInParentheses(str);

    // Строка разделённая на части.
    StringTokenizer st = new StringTokenizer(string, "+-/*()\t\n", true);

    // Кол-во "(".
    int number1 = 0;

    // Кол-во ")".
    int number2 = 0;

    // Показывает текущую операцию (0 - число, 1 - "(", 2 - ")", 3 - "+", 4 - " - ", 5 - "*", 6 - "/".
    int flag = 0;
    // Показывает прошлую операцию.
    int flagLast = 0;

    while (st.hasMoreTokens()) {
      String part = st.nextToken();
      // Инвертируем в double.
      double n = invertToDouble(part);
      // Если не получилось инвертировать
      if (n == -1) {
        if (part.equals("+") || part.equals("-") || part.equals("*")
            || part.equals("/") || part.equals("(") || part.equals(")")) {
          String o = part;
          flagLast = flag;

          // Определим символ.
          flag = definitionOfSymbol(part);

          //  Вычислим кол-во скобок.
          if (flag == 1) {
            number1++;
          }
          if (flag == 2) {
            number2++;
          }
          // Если кол-во")" > кол-во"(".
          if (number2 > number1) {
            throw new ParsingException("Invalid brackets");
          }
          // Если неправльно расставлены операции (Например: ++1).
          if ((flagLast == 1 && (flag == 2 || flag == 5 || flag == 6))
              || (flagLast == 2 && flag == 1) || ((flagLast == 3 || flagLast == 4) && (flag != 1))
              || ((flagLast == 5 || flagLast == 6) && (flag == 2 || flag == 5 || flag == 6))) {
            throw new ParsingException("Invalid operation");
          }
          if (flagLast == 1 && (flag == 3 || flag == 4)) {
            n = 0;
            numbers.push(n);
          }

          if (flagLast == 6 && flag == 4) {
            double num = numbers.pop();
            num = (-1) * num;
            numbers.push(num);
          } else {
            if (flag == 2) {
              while (operations.size() > 1 && !operations.peek().equals("(")) {
                pop(numbers, operations);

              }
              // Удаляем "(".
              operations.pop();
            } else {
              while (canPop(part, operations)) {
                pop(numbers, operations);
              }
              operations.push(part);
            }
          }
        } else {
          if (!part.equals("\t") && !part.equals("\n") && !part.equals(" ")) {
            throw new ParsingException("Invalid symbol");
          }
        }
        // Если получилось инвертировать.
      } else {
        flag = 0;
        flagLast = 0;
        numbers.push(n);
      }
    }
    // Если кол-во"(" != кол-ву")".
    if (number2 != number1) {
      throw new ParsingException("Invalid brackets");
    }

    // Результат.
    Double result = numbers.pop();
    return result;
  }
}
