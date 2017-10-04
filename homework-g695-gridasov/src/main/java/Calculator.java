/**
 * Created by ilya on 02.10.17.
 */
class Calculator {

    private AbstractParser parser;

    Calculator() {
        parser = new MainParser();
    }

    double calculate(String expression) throws ParsingException {
        Operation operation = parser.parse(expression);
        return operation.evaluate();
    }
}
