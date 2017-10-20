package ru.mipt.java2017.seminars.seminar3;

import org.junit.Test;

public class TrivialExpressionParserTest {

    @Test(expected = ExpressionParsingException.class)
    public void testInvalidExpression() {
        TrivialExpressionParser parser = new TrivialExpressionParser();

        parser.parse("maven");
    }
}
