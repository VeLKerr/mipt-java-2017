package task1;

import task1.UraCalculator;

public class Main{
	public static void main(String[] args) throws ParsingException {
		UraCalculator t = new UraCalculator();
		System.out.println(t.calculate(args[0]));
	}
}
