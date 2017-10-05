package ru.mipt.java2017.homework.g697.gorskaya.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.*;
import java.util.Stack;

/**
 * Calculator realisation
 *
 * @author Gorskaya Elena
 * @since 04.10.17
 */
class MyCalculator implements Calculator {

  //static final Calculator INSTANCE = new MyCalculator();
  private static final char signminus = '#';
  private static final String allnumbers = "0123456789.";
  private static final String allops = "+-*/";

  /**
   * The list of all suitable operators.
   */
  private static final HashSet<String> Alloperators = new HashSet<>(Arrays.asList(
    "+", "-", "*", "/"));

  /**
   * Checks whether an operation is from the range of Alloperations.
   */
  private boolean isOperator(String oper) {
    return Alloperators.contains(oper);
  }

  @Override
  public double calculate(String expression) throws ParsingException {
    if (!correctnessExpr(expression)) {
      throw new ParsingException("Invalid expression");
    }
    return calculatingprefix(strToPostfix(expression));
  }

  private enum SymbolType {
    OPENBRACK, CLOSEBRACK, NUMBER, OPERATOR, NOTHING
  }

  private boolean correctnessExpr(String expression) {
    if (expression == null) {
      return false;
    }
    SymbolType prevSymbol;
    SymbolType currSymbol = SymbolType.NOTHING;
    for (int i = 0; i < expression.length(); ++i) {
      char currentchar = expression.charAt(i);
      prevSymbol = currSymbol;
      if (currentchar == ' ' || currentchar == '\n' || currentchar == '\t') {
        if (prevSymbol == SymbolType.NUMBER && i != expression.length() - 1
          && allnumbers.indexOf(expression.charAt(i + 1)) != -1) {
          return false;
        }
      } else {
        if (allnumbers.indexOf(currentchar) != -1) {
          currSymbol = SymbolType.NUMBER;
          if (prevSymbol == SymbolType.CLOSEBRACK) {
            return false;
          }
        } else if (allops.indexOf(currentchar) != -1) {
          currSymbol = SymbolType.OPERATOR;
        } else if (currentchar == '(') {
          currSymbol = SymbolType.OPENBRACK;
          if (prevSymbol == SymbolType.NUMBER || prevSymbol == SymbolType.CLOSEBRACK) {
            return false;
          }
        } else if (currentchar == ')') {
          currSymbol = SymbolType.CLOSEBRACK;
          if (prevSymbol == SymbolType.OPENBRACK || prevSymbol == SymbolType.OPERATOR) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * The function is used to give priorities to operations
   * (the order of calculating depends on the priority of operation).
   */
  private static int findPriority(char operator) throws ParsingException {
    switch (operator) {
      case signminus:
        return 3;
      case '(':
        return 0;
      case ')':
        return 0;
      case '+':
        return 1;
      case '-':
        return 1;
      case '/':
        return 2;
      case '*':
        return 2;
      default:
        throw new ParsingException("Invalid operator");
    }
  }

  /**
   * The function is used to find out the result of operation between two numbers
   */
  private static double getResultOfOperation(double num1, double num2, char operator) throws ParsingException {
    switch (operator) {
      case '+':
        return num1 + num2;
      case '-':
        return num1 - num2;
      case '/':
        return num1 / num2;
      case '*':
        return num1 * num2;
      default:
        throw new ParsingException("Invalid operator");
    }
  }


  /**
   * Turns the string into postfix notation
   */
  private String strToPostfix(String expression) throws ParsingException {
    Character ch;  // The parameter of class "Charecter"
    StringBuilder result = new StringBuilder();     // That is our answer output
    Stack<Character> operators = new Stack<>();     // We will write operations here
    boolean numval = true;
    for (int i = 0; i < expression.length(); ++i) {
      char currentchar = expression.charAt(i);
      if (currentchar == ' ') {
        continue;
      }
      if (currentchar == '\n') {
        continue;
      }
      if (currentchar == '\t') {
        continue;
      }
      if (allnumbers.indexOf(currentchar) != -1) {
        numval = false;
        result.append(currentchar);
      } else if (allops.indexOf(currentchar) != -1) {
        if (!numval) {
          numval = true;
          while (!operators.empty()) {
            if (findPriority(currentchar) <= findPriority(operators.lastElement())) {
              result.append(' ');
              result.append(operators.pop());
            } else {
              break;
            }
          }
          operators.push(currentchar);
          result.append(' ');
        } else {
          if (currentchar == '+') {
            numval = false;
            result.append(' ');
          } else if (currentchar == '-') {
            numval = false;
            while (!operators.empty()) {
              if (findPriority(operators.lastElement()) == 3) {
                result.append(' ');
                result.append(operators.pop());
              } else {
                break;
              }
            }
            operators.push(signminus);
            result.append(' ');
          } else {
            throw new ParsingException("Invalid operator");
          }
        }
      } else if (currentchar == '(') {
        numval = true;
        operators.push('(');
        result.append(' ');
      } else if (currentchar == ')') {
        boolean openBracket = false;
        numval = false;
        result.append(' ');
        while (!operators.empty()) {
          char tempChar = operators.pop();
          if (tempChar == '(') {
            openBracket = true;
            break;
          } else {
            result.append(' ');
            result.append(tempChar);
          }
        }
        if (!openBracket) {
          throw new ParsingException("Invalid operator");
        }
      } else {
        throw new ParsingException("Invalid operator");
      }
    }
    while (!operators.empty()) {
      if (allops.indexOf(operators.lastElement()) != -1 || operators.lastElement().equals(signminus)) {
        result.append(' ');
        result.append(operators.pop());
      } else {
        throw new ParsingException("Invalid operator");
      }
    }
    return result.toString();
  }

  /**
   * Calculating the result with the help of postfix notation
   */
  private double calculatingprefix(String expression) throws ParsingException {
    Scanner inputstr = new Scanner(expression);
    Double num1;
    Double num2;
    Double res;
    Stack<Double> numbers = new Stack<>();
    while (inputstr.hasNext()) {
      String currentinput = inputstr.next();
      if (currentinput.length() == 1) {
        if (allops.indexOf(currentinput.charAt(0)) != -1) {
          if (numbers.size() > 1) {
            double numb1 = numbers.pop();
            double numb2 = numbers.pop();
            numbers.push(getResultOfOperation(numb2, numb1, currentinput.charAt(0)));
          } else {
            throw new ParsingException("Invalid operator");
          }
        } else if (currentinput.charAt(0) == signminus) {
          if (numbers.size() >= 1) {
            double number = numbers.pop();
            numbers.push(-number);
          } else {
            throw new ParsingException("Invalid operator");
          }
        } else if (allnumbers.indexOf(currentinput.charAt(0)) != -1) {
          Double currentnum;
          try {
            currentnum = Double.parseDouble(currentinput);
            numbers.push(currentnum);
          } catch (NumberFormatException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
          }
        } else {
          throw new ParsingException("Invalid operator");
        }
      } else {
        Double currentnum;
        try {
          currentnum = Double.parseDouble(currentinput);
          numbers.push(currentnum);
        } catch (NumberFormatException e) {
          throw new ParsingException(e.getMessage(), e.getCause());
        }

      }
    }
    if (numbers.size() == 1) {
      return numbers.pop();
    } else {
      throw new ParsingException("Invalid operator");
    }
  }
}