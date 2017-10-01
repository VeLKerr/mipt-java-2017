package ru.mipt.java2017.homework.g695.gazizullin.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.HashMap;

/**
* Some parser to make calculations easier.
*
* Add useful spaces, delete unnecessary spaces, change unary operations to binary,
* be sure about operation types.
*
*
*/

public class Parser {
  static final String REGEX_OPERATORS = "\\+|\\-|\\*|\\/|\\(|\\)";
  static final HashMap<String, Integer> PRIORITIES = new HashMap<String, Integer>();

  static {
    PRIORITIES.put("+", 1);
    PRIORITIES.put("-", 1);
    PRIORITIES.put("*", 2);
    PRIORITIES.put("/", 2);
    PRIORITIES.put("(", 0);
  }



  /**
   *
   * Make all unary operations binary by adding zero before.
   * F.e. - 2 --> 0 - 2
   * @param expression with unary operations
   * @return expression without unary operations
   */
  public static String remakeUnaryToBinary(String expression) {
    expression = expression.replaceAll("([\\/|\\*])[\\s]*([\\+|\\-])[\\s]*(\\()", "$1 ( $2 ");
    expression = expression.replaceAll("(\\()[\\s]*([\\+|\\-])[\\s]*", "$1 -0.0 $2 ");
    expression = expression.replaceAll("([\\/|\\*])[\\s]*([\\+|\\-])[\\s]*(\\d*\\.?\\d*)", "$1 ( -0.0 $2 $3 )");
    return expression;
  }


  /**
   * Check if some token is double number or not
   *
   * @param expression-token
   * @return True if it's double, else  False
   */
  public static boolean isNumber(String expression) {
    try {
      Double.parseDouble(expression);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Returns double from string token
   *
   * @param expression - token
   * @return double from string
   * @throws ParsingException if cannot parse string
   */

  public static double getNumber(String expression) throws ParsingException {
    try {
      return Double.parseDouble(expression);
    } catch (NumberFormatException e) {
      throw new ParsingException("Can not parse to double", e.getCause());
    }
  }

  /**
   * Checks if string is ( or ).
   * @param expression - token
   * @return True if it's bracket, else False
   */
  public static boolean isBracket(String expression) {
    return expression.matches("\\(|\\)");
  }

  /**
   * Checks if string is ( .
   * @param expression - token
   * @return True if it's ), else False
   */
  public static boolean isOpenBracket(String expression) {
    return expression.matches("\\(");
  }

  /**
   * Checks if string is ) .
   * @param expression - token
   * @return True if it's ), else False
   */
  public static boolean isCloseBracket(String expression) {
    return expression.matches("\\)");
  }

  /**
   * Checks if string is  operator :   (, ), +, -, *, / .
   * @param expression - token
   * @return True if it's operator, else False
   */
  public static boolean isOperator(String expression) {
    return expression.matches(REGEX_OPERATORS);
  }

  /**
   * Checks if string is - .
   * @param expression - token
   * @return True if it's -, else False
   */
  public static boolean isMinus(String expression) {
    return expression.matches("\\-");
  }

  /**
   * Checks if string is + .
   * @param expression - token
   * @return True if it's +, else False
   */
  public static boolean isPlus(String expression) {
    return expression.matches("\\+");
  }

  /**
   * Checks if string is * .
   * @param expression - token
   * @return True if it's *, else False
   */
  public static boolean isMultiply(String expression) {
    return expression.matches("\\*");
  }

  /**
   * Checks if string is / .
   * @param expression - token
   * @return True if it's /, else False
   */
  public static boolean isDivide(String expression) {
    return expression.matches("\\/");
  }

  /**
   * Checks if firstExpression priority is less or equal to secondExpression operator
   * @param firstExpression - first toke
   * @param secondExpression - second token
   * @return True if firstExpression priority is less or equal to secondExpression, else False;
   * @throws ParsingException if some f them is not an operator
   */
  public static boolean isLesserThan(String firstExpression, String secondExpression) throws ParsingException {
    if (isOperator(firstExpression) && isOperator(secondExpression)) {
      return PRIORITIES.get(firstExpression) >= PRIORITIES.get(secondExpression);
    } else {
      throw new ParsingException("Some of them or both are not operators!");
    }
  }

  /**
   * Get all operations that parser provide in regex
   * @return regex string with all operators
   */
  public static String getRegexOperators() {
    return REGEX_OPERATORS;
  }

  /**
   * Adds spaces between operators
   * @param expression - without useful spaces
   * @return expression with useful spaces
   */
  public static String addUsefulSpaces(String expression) {
    return expression.replaceAll(REGEX_OPERATORS, " $0 ");
  }

}
