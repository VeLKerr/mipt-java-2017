package ru.mipt.homework.g696.pak.task1;

import sun.security.pkcs.ParsingException;

public class Main {

    public static int AAA() {
        return 0;
    }

    public static void main(String[] args) throws ParsingException {
    Calculator calculator = new Calculator();
    System.out.println(calculator.calculate("1+5 -(-4)/5 * (5-4*(-1))+(4*1/2) - 44 - ((-5)/5 *6+(6*8-4))"));
    double tmp = 1+5 -1.0*(-4)/5 * (5-4*(-1))+(4*1/2) - 44 - ((-5)/5 *6+(6*8-4));
    System.out.println(tmp);

    }

}

