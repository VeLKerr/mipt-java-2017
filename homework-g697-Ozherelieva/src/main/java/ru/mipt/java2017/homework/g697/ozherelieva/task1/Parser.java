package ru.mipt.java2017.homework.g697.ozherelieva.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * Prepare expression for parsing to make conversion into prefix easier.
 *
 * Add spaces between operators and numbers, delete useless symbols.
 * Check the valid of expression:
 *  bracket balance,
 *  valid numbers,
 *  valid operators.
 *
 *
 */

public class Parser {

  public static final char UNARYMINUS = '$';

  private static boolean isOperator(Character c) {
    return (c == ')' || c == '(' || c == '+' || c == '-' || c == '*' || c == '/');
  }

  private static void addNumber(StringBuilder answer, StringBuilder number)
    throws ParsingException {
    // In case of number ends with a dot.
    if (number.charAt(number.length() - 1) == '.') {
      throw new ParsingException("Incorrect number");
    }
    number.append(" ");
    answer.append(number);
    // Clear number.
    number.delete(0, number.length());
  }

  /**
   * @param expression the string expression.
   * @return answer the valid string expression.
   */

  public static StringBuilder expressionPreparation(String expression) throws ParsingException {
    expression = expression.replace('\n', ' ').replace('\t', ' ');
    StringBuilder answer = new StringBuilder();

    int bracketBalance = 0;
    // Is there a dot in number.
    boolean isDot = false;
    boolean isUnary = true;
    boolean previousIsNum = false;
    boolean previousIsOperator = true;
    StringBuilder number = new StringBuilder();
    for (int i = 0; i < expression.length(); i++) {
      Character c = expression.charAt(i);

      if (Character.isDigit(c)) {
        number.append(c);

        isUnary = false;
        previousIsNum = true;
        previousIsOperator = false;
      } else if (c == '.') {
        // In case of more, than two dots in a number or starting with a dot.
        if (isDot || !previousIsNum) {
          throw new ParsingException("Incorrect number");
        }
        isDot = true;
        previousIsNum = false;
        number.append('.');
      } else if (isOperator(c)) {

        if (number.length() != 0) {
          addNumber(answer, number);
          isDot = false;
        }
        if (c == '(' || c == ')') {
          // In case of ()
          if (c == '(') {
            if (answer.length() > 0) {
              // -2 because of the spaces between operators
              if (answer.charAt(answer.length() - 2) == ')' || !previousIsOperator) {
                throw new ParsingException("Incorrect expression");
              }
            }
            bracketBalance += 1;
          } else {
            if (answer.length() > 0) {
              if (answer.charAt(answer.length() - 2) == '(' || !previousIsNum) {
                throw new ParsingException("Incorrect expression");
              }
            }
            bracketBalance -= 1;
          }
          if (bracketBalance < 0) {
            throw new ParsingException("Bracket balance is incorrect");
          }
          answer.append(c + " ");
        } else if (c == '-') {
          if (isUnary) {
            answer.append(UNARYMINUS + " ");
          } else {
            answer.append(c + " ");
          }

          isUnary = true;
          previousIsNum = false;
          previousIsOperator = true;
        } else {
          if (previousIsOperator) {
            throw new ParsingException("Incorrect expression");
          }

          isUnary = true;
          previousIsNum = false;
          previousIsOperator = true;
          answer.append(c + " ");
        }

      } else {
        if (c != ' ') {
          throw new ParsingException("Invalid symbol");
        }
      }
    }
    if (number.length() != 0) {
      addNumber(answer, number);
    }
    if (bracketBalance != 0) {
      throw new ParsingException("Bracket balance is incorrect");
    }
    if (answer.length() == 0) {
      throw new ParsingException("Expression is null");
    }

    return answer;
  }
}
