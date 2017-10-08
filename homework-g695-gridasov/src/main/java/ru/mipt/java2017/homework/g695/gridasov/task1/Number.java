/**
 * Created by ilya on 04.10.17.
 */
package ru.mipt.java2017.homework.g695.gridasov.task1;

public class Number implements Operation {
    Number(double value) {
        this.value = value;
    }
    private double value;
    public double evaluate() {
        return value;
    }
}
