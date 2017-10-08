package ru.mipt.java2017.homework.g695.polyanskiy.task1;


import org.junit.Test;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class MyCalculatorTest extends AbstractCalculatorTest {

  @Override
  protected Calculator calc() {
    return new MyCalculator();
  }

  @Test
  public void SomeTest() throws ParsingException {
    test("0+0.0+0", 0);
    test("+(-(+(-(0)))) - 1", -1);
  }

}

