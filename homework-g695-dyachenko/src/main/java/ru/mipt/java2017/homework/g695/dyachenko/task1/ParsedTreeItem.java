package ru.mipt.java2017.homework.g695.dyachenko.task1;

public class ParsedTreeItem {

  private final byte sign;
  private final byte operator;
  private final double value;
  private final ParsedTreeLine container;

  public byte getOperator() {
    return operator;
  }

  public double evaluateValue() {
    // numbers are positive by default
    byte trueSign = sign == 0 ? 1 : sign;

    if (container == null) {
      return trueSign * value;
    }
    return trueSign * container.evaluateValue();
  }

  public ParsedTreeItem(byte o, byte s, ParsedTreeLine c) {
    this.sign = s;
    this.operator = o;
    this.value = 0.0;
    this.container = c;
  }

  public ParsedTreeItem(byte o, byte s, double n) {
    this.sign = s;
    this.operator = o;
    this.value = n;
    this.container = null;
  }
}
