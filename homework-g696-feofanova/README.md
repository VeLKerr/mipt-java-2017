# homework-g696-feofanova
*Homework-1 for java-2017: calculator.*

This calculator used [shunting-yard](https://en.wikipedia.org/wiki/Shunting-yard_algorithm) algorithm.

#### The structure:
* **AbstractTokenCalculator** implements interface *Calculator* from base.
This class realises expression parsing using StringTokenizer. Also it used interface *ExpressionHandler*, that calculate expressions.
* **ExpressionHandler** is an interface, that must do smth with numbers and operands, and give the answer in the end.
* **ShuntingYardHandler** implements *ExpressionHandler*. It's a specific implementation of handler, that using shunting-yard algorithm with 2 stacks: for numbers and for operands.
* **ShuntingYardTokenCalculator** extends *AbstractTokenCalculator*. It's a final class, that solves the task.
* **ShuntingYardTokenCalculatorTest** extends *AbstractCalculatorTest*. Tests my calculator.
