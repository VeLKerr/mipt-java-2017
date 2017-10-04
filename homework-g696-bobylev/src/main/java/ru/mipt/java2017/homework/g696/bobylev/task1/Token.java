package ru.mipt.java2017.homework.g696.bobylev.task1;

import java.util.Arrays;
import ru.mipt.java2017.homework.base.task1.ParsingException;

enum Tag {
  NUMBER,
  LPAREN,
  RPAREN,
  END_OF_TEXT,
  ADD,
  SUB,
  MUL,
  DIV;

  public String toString() {
    switch (this) {
      case NUMBER:
        return "number";
      case LPAREN:
        return "'('";
      case RPAREN:
        return "')'";
      case END_OF_TEXT:
        return "end of text";
      case ADD:
        return "+";
      case SUB:
        return "-";
      case MUL:
        return "*";
      case DIV:
        return "|";
      default:
        throw new RuntimeException("unreachable code");
    }
  }
}

class Token {

  private Tag tag;
  private Position start;
  private Position follow;
  private String word;

  Token(String text) throws ParsingException {
    this(new Position(text));
  }

  protected Tag getTag() {
    return tag;
  }

  protected String getWord() {
    return word;
  }

  public String toString() {
    return this.word;
  }

  protected boolean matches(Tag... tags) {
    return Arrays.stream(tags).anyMatch(t -> tag == t);
  }

  protected Token next() throws ParsingException {
    return new Token(follow);
  }

  protected boolean isFinal() {
    return this.tag == Tag.END_OF_TEXT;
  }

  protected void throwError(String msg) throws ParsingException {
    throw new ParsingException(msg);
  }

  private Token(Position cur) throws ParsingException {
    start = cur;
    start.skipWhile(Character::isWhitespace);
    follow = start.next();
    switch (start.getChar()) {
      case -1:
        tag = Tag.END_OF_TEXT;
        break;
      case '(':
        tag = Tag.LPAREN;
        break;
      case ')':
        tag = Tag.RPAREN;
        break;
      case '-':
        tag = Tag.SUB;
        break;
      case '+':
        tag = Tag.ADD;
        break;
      case '*':
        tag = Tag.MUL;
        break;
      case '/':
        tag = Tag.DIV;
        break;
      default:
        if (start.satisfies(Character::isDigit)) {
          follow.skipWhile(Character::isDigit);
          if (follow.getChar() == '.') {
            follow.skip();
            follow.skipWhile(Character::isDigit);
          }
          tag = Tag.NUMBER;
        } else {
          throwError("invalid character");
        }
    }
    if (start.getIndex() < start.getText().length()) {
      word = start.getText().substring(start.getIndex(), follow.getIndex());
    } else {
      word = "#NOTHING";
    }
  }
}