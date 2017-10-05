package ru.mipt.java2017.homework.g696.mosyagin.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * Manages a single token of a mathematical expression.
 *
 * @author Mosyagin Mikhail
 * @see TokenType
 */
public class Token {

  private TokenType type;
  private double value;

  /**
   * Creates a token without a type.
   */
  Token() {
    type = null;
  }

  /**
   * Creates a number token of given value.
   *
   * @param value the {@code double} representing the value
   */
  Token(double value) {
    setValue(value);
  }

  /**
   * Creates token of given operaiton type.
   * Unary {@code +} and {@code -} operations are represented by '|' and '~' respectively.
   * Supported binary operations are {@code +}, {@code -}, {@code *} and {@code /}.
   *
   * @param operation the character representing the operation
   */
  Token(char operation) throws ParsingException {
    switch (operation) {
      case '|':
        setType(TokenType.UNARY_PLUS);
        break;
      case '~':
        setType(TokenType.UNARY_MINUS);
        break;
      case '+':
        setType(TokenType.BINARY_PLUS);
        break;
      case '-':
        setType(TokenType.BINARY_MINUS);
        break;
      case '*':
        setType(TokenType.MULTIPLICATION);
        break;
      case '/':
        setType(TokenType.DIVISION);
        break;
      case ';':
        setType(TokenType.END_OF_EXPRESSION);
        break;
      default:
        throw new ParsingException("Unknown operation " + operation);
    }
  }

  /**
   * Getter for token type.
   *
   * @return token type
   * @see TokenType
   */
  public TokenType getType() {
    return type;
  }

  /**
   * Setter for token type.
   *
   * @param type the token type to be set
   * @see TokenType
   */
  public void setType(TokenType type) {
    this.type = type;
  }

  /**
   * Getter for token value.
   * Cannot be called on a non-number token.
   *
   * @return token's value
   * @throws ParsingException if called on a non-number token
   */
  public double getValue() throws ParsingException {
    if (type != TokenType.NUMBER) {
      throw new ParsingException("Cannot get value of a non-number token");
    }
    return value;
  }

  /**
   * Setter for token value.
   * Converts the token into a number.
   *
   * @param value the {@code double} representing the new value
   */
  public void setValue(double value) {
    type = TokenType.NUMBER;
    this.value = value;
  }
}
