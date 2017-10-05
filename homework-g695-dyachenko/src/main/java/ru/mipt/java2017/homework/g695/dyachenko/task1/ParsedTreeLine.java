package ru.mipt.java2017.homework.g695.dyachenko.task1;

import java.util.ArrayList;

public class ParsedTreeLine {

  private ArrayList<ParsedTreeItem> items;

  public void addItem(ParsedTreeItem item) {
    items.add(item);
  }

  public double evaluateValue() {
    int len = items.size();
    double sum = 0.0;
    double currentValue = 0.0;

    for (int i = 0; i < len; i++) {
      ParsedTreeItem current = items.get(i);

      byte operator = current.getOperator();
      double value = current.evaluateValue();

      if (operator == 0) {
        sum += currentValue;
        currentValue = value;
      } else if (operator == 2) {
        currentValue *= value;
      } else {
        currentValue /= value;
      }
    }
    return sum + currentValue;
  }

  public ParsedTreeLine() {
    this.items = new ArrayList<ParsedTreeItem>();
  }
}
