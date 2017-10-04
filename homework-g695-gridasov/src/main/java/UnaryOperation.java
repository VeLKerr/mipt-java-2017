/**
 * Created by ilya on 02.10.17.
 */
public class UnaryOperation implements Operation {
    private Operation operand;
    private char operator;

    UnaryOperation(char operator, Operation operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public double evaluate() {
        return operator == '+' ? operand.evaluate() : (-1) * operand.evaluate();
    }
}
