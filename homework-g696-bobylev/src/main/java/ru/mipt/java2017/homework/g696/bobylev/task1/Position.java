package ru.mipt.java2017.homework.g696.bobylev.task1;

import java.util.function.IntPredicate;

class Position {

  private String text;
  private int index;

  Position(String text) {
    this(text, 0);
  }

  int getIndex() {
    return this.index;
  }

  String getText() {
    return text;
  }

  int getChar() {
    return index < text.length() ? text.codePointAt(index) : -1;
  }

  boolean satisfies(IntPredicate p) {
    return p.test(getChar());
  }

  void skip() {
    int c = getChar();
    switch (c) {
      case -1:
        return;
      default:
        this.index++;
    }
  }

  Position next() {
    int c = getChar();
    switch (c) {
      case -1:
        return this;
      default:
        return new Position(text, index + 1);
    }
  }

  void skipWhile(IntPredicate p) {
    while (this.satisfies(p)) {
      this.skip();
    }
  }

  public String toString() {
    return String.format("(%d)", index);
  }

  private Position(String text, int index) {
    this.text = text;
    this.index = index;
  }
}