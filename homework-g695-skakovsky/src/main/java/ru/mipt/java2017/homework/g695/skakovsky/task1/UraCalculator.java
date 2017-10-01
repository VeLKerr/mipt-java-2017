package ru.mipt.java2017.homework.g695.skakovsky.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.ArrayList;

/**
 * That calculator is top calculator
 *
 * @author Uriy
 * @since 01.10.17
 */
public class UraCalculator implements Calculator {
  private static final double EPS = 1e-9;

  @Override
  public double calculate(String expression) throws ParsingException {
    return calculate(parseToTokens(expression));
  }

  private boolean isOperator(char argument) {
    return argument == '+' || argument == '-' || argument == '*' ||
           argument == '/';
  }

  private boolean isMathematicalSymbol(char argument) {
    return isOperator(argument) || argument == '(' || argument == ')';
  }

  private boolean isSymbolOfDecimalNotation(char arg) {
    return arg >= '0' && arg <= '9' || arg == '.';
  }

  private boolean isDecimalNumber(final String expression) {
    return expression.matches("[-+]?(?:[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+)");
  }

  private boolean isCorrectBracketSequence(String expression) {
    int currentBalance = 0;
    for (int i = 0; i < expression.length(); ++i) {
      if (expression.charAt(i) == '(' || expression.charAt(i) == '{') {
        ++currentBalance;
      }
      if (expression.charAt(i) == ')') {
        --currentBalance;
        if (currentBalance < 0) {
          return false;
        }
      }
    }
    return currentBalance == 0;
  }

  private int operatorPriority(char argument) throws ParsingException {
    switch (argument) {
      case '+':
        return 0;
      case '-':
        return 0;
      case '*':
        return 1;
      case '/':
        return 1;
      case '!':
        return -1;
      case '(':
        return -2;
      case '{':
        return -2;
      case ')':
        return 2;
      default:
        throw new ParsingException("Unknown operator");
    }
  }

  private double operate(double firstArgument, double secondArgument, char operator) throws ParsingException {
    switch (operator) {
      case '+':
        return firstArgument + secondArgument;
      case '-':
        return firstArgument - secondArgument;
      case '*':
        return firstArgument * secondArgument;
      case '/':
        return firstArgument / secondArgument;
      default:
        return new ParsingException("Unknown operator.");
    }
    return 0;
  }

  private ArrayList<String> parseToTokens(String expression) throws
      ParsingException {
    if (expression == "" || expression == null) {
      throw new ParsingException("Expression is empty.");
    }
    if (!expression.matches("[ \\-/+/0-9./*/()\t\n]+")) {
      throw new ParsingException("Unknown symbol in expression.");
    }
    expression = expression.replaceAll("[ \t\n]", "");
    if (expression.matches(
            "[^ ]*[+\\-*\\/][*\\/][^ ]*|[^ ]*[+\\-][+\\-][^ ]*")) {
      throw new ParsingException("Contract operators in the expression.");
    }
    expression = "(" + expression + ")";
    if (expression.matches("[^ ]*[(][)][^ ]*")) {
      throw new ParsingException("Empty braces.");
    }
    if (expression.matches("[^ ]*[(][*\\/][^ ]*|[^ ]*[+\\-*\\/][)][^ ]*")) {
      throw new ParsingException(
          "Multiplication or division operator at the border of the block.");
    }
    if (!isCorrectBracketSequence(expression)) {
      throw new ParsingException("The staple balance is violated.");
    }
    ArrayList<String> tokens = new ArrayList<String>(0);
    int startOfCurrentToken = 0;
    for (int i = 0; i < expression.length(); ++i) {
      if (isSymbolOfDecimalNotation(expression.charAt(i))) {
        if (!isSymbolOfDecimalNotation(
                expression.charAt(startOfCurrentToken))) {
          startOfCurrentToken = i;
        }
      } else {
        if (isSymbolOfDecimalNotation(expression.charAt(startOfCurrentToken))) {
          tokens.add(expression.substring(startOfCurrentToken, i));
          startOfCurrentToken = i;
        }
        switch (expression.charAt(i)) {
          case '(':
            tokens.add("(");
            break;
          case ')':
            tokens.add(")");
            break;
          case '+':
            tokens.add("+");
            break;
          case '-':
            tokens.add("-");
            break;
          case '*':
            tokens.add("*");
            break;
          case '/':
            tokens.add("/");
            break;
          default:
            throw new ParsingException("Unknown symbol in expression.");
        }
      }
    }
    for (int i = 0; i < tokens.size(); ++i) {
      if (!isMathematicalSymbol(tokens.get(i).charAt(0)) &&
          !isDecimalNumber(tokens.get(i))) {
        throw new ParsingException("Invalid decimal number: " + tokens.get(i));
      }
    }
    return tokens;
  }

  private double calculate(ArrayList<String> tokens) throws ParsingException {
    ArrayList<Character> operators = new ArrayList<Character>();
    ArrayList<Double> arguments = new ArrayList<Double>();
    for (int i = 0; i < tokens.size(); ++i) {
      if (tokens.get(i).equals("(")) {
        operators.add(tokens.get(i).charAt(0));
        continue;
      }
      if (isDecimalNumber(tokens.get(i))) {
        arguments.add(Double.parseDouble(tokens.get(i)));
        continue;
      }
      if (isOperator(tokens.get(i).charAt(0))) {
        char currentOperator = tokens.get(i).charAt(0);
        int j = operators.size() - 1;
        if (currentOperator == '-' && (tokens.get(i - 1).equals("(")) ||
            tokens.get(i - 1).equals("*") || tokens.get(i - 1).equals("/")) {
          if (isDecimalNumber(tokens.get(i + 1))) {
            tokens.set(i + 1,
                       Double.toString(-Double.parseDouble(tokens.get(i + 1))));
          } else {
            tokens.set(i + 1, "{");
          }
          tokens.remove(i);
          --i;
          continue;
        }
        if (currentOperator == '+' && (tokens.get(i - 1).equals("(")) ||
            tokens.get(i - 1).equals("*") || tokens.get(i - 1).equals("/")) {
          continue;
        }
        while ((operators.get(j) != '(' || operators.get(j) != '{') &&
               operatorPriority(operators.get(j)) >
                   operatorPriority(currentOperator)) {
          --j;
        }
        ++j;
        while (j != operators.size()) {
          int indexOfNewArgument =
              arguments.size() - (operators.size() - j) - 1;
          arguments.set(
              indexOfNewArgument,
              operate(arguments.get(indexOfNewArgument),
                      arguments.get(indexOfNewArgument + 1), operators.get(j)));
          arguments.remove(indexOfNewArgument + 1);
          if (operators.get(j) == '{') {
            arguments.set(arguments.size() - 1,
                          -arguments.get(arguments.size() - 1));
          }
          operators.remove(j);
        }
        operators.add(currentOperator);
      } else {
        int j = operators.size() - 1;
        while (operatorPriority(operators.get(j)) == 1) {
          --j;
        }
        ++j;
        while (j != operators.size()) {
          int indexOfNewArgument =
              arguments.size() - (operators.size() - j) - 1;
          arguments.set(
              indexOfNewArgument,
              operate(arguments.get(indexOfNewArgument),
                      arguments.get(indexOfNewArgument + 1), operators.get(j)));
          arguments.remove(indexOfNewArgument + 1);
          if (operators.get(j) == '{') {
            arguments.set(arguments.size() - 1,
                          -arguments.get(arguments.size() - 1));
          }
          operators.remove(j);
        }
        j = operators.size() - 1;
        while (operatorPriority(operators.get(j)) == 0) {
          --j;
        }
        ++j;
        while (j != operators.size()) {
          int indexOfNewArgument =
              arguments.size() - (operators.size() - j) - 1;
          arguments.set(
              indexOfNewArgument,
              operate(arguments.get(indexOfNewArgument),
                      arguments.get(indexOfNewArgument + 1), operators.get(j)));
          arguments.remove(indexOfNewArgument + 1);
          if (operators.get(j) == '{') {
            arguments.set(arguments.size() - 1,
                          -arguments.get(arguments.size() - 1));
          }
          operators.remove(j);
        }
        operators.remove(operators.size() - 1);
      }
    }
    return arguments.get(0);
  }

}
