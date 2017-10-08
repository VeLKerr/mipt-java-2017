package ru.mipt.java2017.homework.g696.mikhaylov.task1;

import org.junit.Test;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class MyCalculatorTest extends AbstractCalculatorTest{

  @Override
  protected Calculator calc() {
    return new MyCalculator();
  }
}
