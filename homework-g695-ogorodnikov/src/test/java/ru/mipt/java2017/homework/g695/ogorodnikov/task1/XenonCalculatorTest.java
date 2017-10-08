package ru.mipt.java2017.homework.g695.ogorodnikov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class XenonCalculatorTest extends AbstractCalculatorTest {

  /**
   * Фабричный метод для создания объектов тестируемого класса
   *
   * @return экземпляр объекта для тестирования
   */
  @Override
  protected Calculator calc() {
    return new XenonCalculator();
  }
}
