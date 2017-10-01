package ru.mipt.java2017.homework.g695.skakovsky.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public class Main{
	public static void main(String[] args) throws ParsingException {
		UraCalculator t = new UraCalculator();
		System.out.println(t.calculate(args[0]));
	}
}
