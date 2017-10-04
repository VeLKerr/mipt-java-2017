package ru.mipt.java2017.homework.g694.perchuk.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * Class {@code Parser} parses (surprise!) expression and delivers tokens to evaluator.
 */
class Parser {

  private Evaluator eval;

  /**
   * Specify {@code Evaluator} class instance to which we will deliver tokens.
   *
   * @param paramEval - {@code Evaluator} class instance.
   */
  Parser(Evaluator paramEval) {
    eval = paramEval;
  }

  /**
   * Replaces unary '-' with multiplication by -1.0 with highest priority.
   *
   * @throws ParsingException if unable to add necessary symbols.
   */
  private void multiplyByMinusOne() throws ParsingException {
    eval.addLeftBrace();
    eval.addDouble(-1.0);
    eval.addRightBrace();
    eval.addOperation('#');
  }

  /**
   * Parse expression into tokens, then deliver them to {@code eval}
   *
   * @param expression - input expression.
   * @throws ParsingException - if expression is invalid.
   */
  void parse(String expression) throws ParsingException {
    //surrounding all operators with spaces
    expression = ("(" + expression + ")").replaceAll("[\\+\\-\\*\\/\\(\\)]", " $0 ");
    //parse by spaces
    String[] noSpacesExpression = expression.trim().split("[\\s]+");

    String previous = "";
    for (String token : noSpacesExpression) {
      if (isDouble(token)) {
        eval.addDouble(Double.parseDouble(token));
      } else if (isLeftBrace(token)) {
        eval.addLeftBrace();
      } else if (isRightBrace(token)) {
        eval.addRightBrace();
      } else if (isOperation(token)) {
        if (token.equals("-")) {
          if (previous.equals("(") || isOperation(previous)) {
            if (previous.equals("-")) {
              throw new ParsingException("double minus");
            }
            multiplyByMinusOne();
          } else {
            eval.addOperation(token.charAt(0));
          }
        } else {
          eval.addOperation(token.charAt(0));
        }
      } else {
        throw new ParsingException("unknown symbol");
      }
      previous = token;
    }
  }

  /**
   * Double is a sequence of digits, probably separated with single dot.
   *
   * @param token - token to check.
   * @return is token a double?
   */
  private boolean isDouble(String token) {
    return token.matches("[0-9]+|[0-9]+\\.[0-9]+");
  }

  private boolean isOperation(String val) {
    return val.matches("[\\+\\-\\*\\/]");
  }

  private boolean isRightBrace(String val) {
    return val.equals(")");
  }

  private boolean isLeftBrace(String val) {
    return val.equals("(");
  }
}
