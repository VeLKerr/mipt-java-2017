package ru.mipt.java2017.homework.g696.mosyagin.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * A state machine that builds a {@code PolishNotation} object from an
 * infix expression represented as a sequence of characters.
 *
 * @author Mosyagin Mikhail
 * @see PolishNotation
 */
public class ParsingMachine {

  private StateTag state;
  private String lastNumber;
  private PolishNotation result;
  private ParsingMachine subautomata;

  /**
   * Initializes the state machine.
   */
  ParsingMachine() {
    state = StateTag.BEGIN;
    lastNumber = "";
    result = new PolishNotation();
    subautomata = null;
  }

  /**
   * Changes state of the machine depending on the character.
   * The characters should be passed in the same order as they
   * appear in the infix expression.
   * <p>
   * This method cannot be called after the parsing process
   * is finished.
   *
   * @param character the character of the expression string
   * @throws ParsingException if the expression becomes invalid
   * or the parsing process was finished before the call
   */
  public void transit(char character) throws ParsingException {
    if (!isAcceptable(character)) {
      throw new ParsingException("Unacceptable character: " + character);
    }

    if (state == StateTag.END) {
      throw new ParsingException("Cannot parse after the end of the expression");
    } else if (state == StateTag.SUBAUTOMATA) {
      handleSubautomata(character);
    } else if (character == '(') {
      handleOpeningBracket();
    } else if (character == ')') {
      handleClosingBracket();
    } else if (isOperation(character)) {
      handleOperation(character);
    } else if (Character.isDigit(character) || character == '.') {
      handleNumber(character);
    } else {
      handleWhtespace();
    }
  }

  /**
   * Checks if the parsing process is finished.
   *
   * @return {@code true} if the process is finished and {@code false} otherwise
   */
  public boolean hasFinished() {
    return state == StateTag.END;
  }

  /**
   * Get the result {@code PolishNotation} object.
   * <p>
   * Cannot be called before the parsing process is finished.
   *
   * @return {@code PolishNotation} object representing the
   * postfix equivalent of the parsed expression
   * @throws ParsingException if called before the parsing
   * process is finished
   */
  public PolishNotation getResult() throws ParsingException {
    if (!hasFinished()) {
      throw new ParsingException("Cannot return result before finished");
    }

    return result;
  }

  private void handleSubautomata(char character) throws ParsingException {
    subautomata.transit(character);
    if (subautomata.hasFinished()) {
      result.pushToken(new Token(subautomata.getResult().evaluateExpression()));
      subautomata = null;
      state = StateTag.NUMBER_END;
    }
  }

  private void handleOpeningBracket() throws ParsingException {
    if (state == StateTag.NUMBER || state == StateTag.NUMBER_END) {
      throw new ParsingException("Cannot parse '(' following a number");
    }
    state = StateTag.SUBAUTOMATA;
    subautomata = new ParsingMachine();
  }

  private void handleClosingBracket() throws ParsingException {
    if (state == StateTag.BEGIN
        || state == StateTag.BINARY_OPEARTION
        || state == StateTag.UNARY_OPERATION) {
      throw new ParsingException("Cannot parse ')' not following a number");
    }
    pushNumber();
    state = StateTag.END;
  }

  private void handleOperation(char operation) throws ParsingException {
    if (state == StateTag.UNARY_OPERATION) {
      throw new ParsingException("Cannot parse an operation following a unary operation");
    } else if (state == StateTag.BINARY_OPEARTION || state == StateTag.BEGIN) {
      pushOperation(makeUnary(operation));
      state = StateTag.UNARY_OPERATION;
    } else {
      pushNumber();
      pushOperation(operation);
      state = StateTag.BINARY_OPEARTION;
    }
  }

  private void handleNumber(char number) throws ParsingException {
    if (state == StateTag.NUMBER_END) {
      throw new ParsingException("Cannot handle two consecutive numbers");
    }
    lastNumber += number;
    state = StateTag.NUMBER;
  }

  private void handleWhtespace() throws ParsingException {
    if (state == StateTag.NUMBER) {
      pushNumber();
      state = StateTag.NUMBER_END;
    }
  }

  private void pushNumber() throws ParsingException {
    if (state != StateTag.NUMBER) {
      return;
    }

    try {
      result.pushToken(new Token(Double.parseDouble(lastNumber)));
    } catch (NumberFormatException exception) {
      throw new ParsingException("Cannot parse number " + lastNumber);
    }

    lastNumber = "";
  }

  private void pushOperation(char operationSymbol) throws ParsingException {
    result.pushToken(new Token(operationSymbol));
  }

  private char makeUnary(char operation) throws ParsingException {
    if (operation == '*' || operation == '/') {
      throw new ParsingException("No such unary operation: " + operation);
    }

    return operation == '+' ? '|' : '~'; // '|' stands for unary '+', '~' stands for unary '-'
  }

  private boolean isOperation(char character) {
    return character == '+' || character == '-' || character == '*' || character == '/';
  }

  private boolean isAcceptable(char character) {
    return character == '(' || character == ')' || character == '.'
        || Character.isDigit(character) || Character.isWhitespace(character)
        || isOperation(character);
  }

  private enum StateTag {
    BEGIN,
    BINARY_OPEARTION,
    UNARY_OPERATION,
    NUMBER,
    NUMBER_END,
    SUBAUTOMATA,
    END
  }
}
