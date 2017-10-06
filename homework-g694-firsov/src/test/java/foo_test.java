
import org.junit.Test;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.tests.task1.AbstractCalculatorTest;

public class foo_test extends AbstractCalculatorTest {

    @Override
    protected Calculator calc() {
        return new foo();
    }
}
