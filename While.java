package ast;

import java.util.List;
import environment.Environment;

/**
 * The While class uses a condition in order to repeat a given statement.
 * 
 * @author Rakesh Nori
 * @version 5/22/2018
 *
 */
public class While extends Statement
{
    private Expression c;
    private List<Statement> res;

    /**
     * Instantiates the While object by defining the condition being checked and
     * the resulting statement.
     * 
     * @param in the Expression that will be repeatedly checked in order to
     *            execute.
     * @param a the Statement that will be looped through and executed while the
     *            expression evaluates to true(1).
     */
    public While(Expression in, List<Statement> a)
    {
        c = in;
        res = a;
    }

    /**
     * @Override Checks if its Condition is true then executes the result
     *           statement until the condition is false.
     * @precondition the Condition must be something that can eventually become
     *               false after execution of the result Statement.
     * @postcondition the Condition has been evaluated to false.
     * @param env the Environment in which the method is executed.
     */
    public void exec(Environment env)
    {
        while (c.eval(env) == 1)
        {
            for (Statement curr : res)
                curr.exec(env);
        }
    }

}
