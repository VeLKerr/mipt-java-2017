/**
 * Created by ilya on 02.10.17.
 */

package ru.mipt.java2017.homework.g695.gridasov.task1;

import org.junit.Assert;
import org.junit.Test;

public class MainTest extends AbstractCalculatorTest {
    @Override
    protected Calculator calc() {
        return new Calculator();
    }
}
