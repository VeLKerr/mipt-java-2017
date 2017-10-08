package ru.mipt.java2017.homework.g697.IvaninE.task1;

public class Token {

  private char symbol;
  private double value;

  Token(char character) {
    symbol = character;
  }

  Token(double val) {
    symbol = 'x';
    value = val;
  }

  public char symbol() {
    return symbol;
  }

  public double value() {
    return value;
  }

}
