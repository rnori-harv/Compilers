package ast;

import java.util.List;
import environment.Environment;

/**
 * A type of statement that contains a condition and a Statement to be executed
 * if that same condition evaluates to true, or another statement (not
 * necessary) if it evaluates to false.
 * 
 * @author Rakesh Nori
 * @version 5/22/2018
 *
 */
public class If extends Statement
{
    private Expression c;
    private List<Statement> res;
    private List<Statement> other;

    /**
     * Instantiates the condition and statement of the class.
     * 
     * @param in The Condition that will be evaluated to tell if the Statement
     *            should be executed or not.
     * @param a the first List of statements to be executed if the Expression
     *            evaluates to true.
     * @param b the second List of statements to be executed if the Expression
     *            evaluates to false.
     */
    public If(Expression in, List<Statement> a, List<Statement> b)
    {
        c = in;
        res = a;
        other = b;
    }

    /**
     * @Override Checks if the condition is true. If so, executes the Statement
     *           in the If class.
     * @param env the Environment containing variables that are used in
     *            execution.
     * @postcondition The Condition has been evaluated to true and the Statement
     *                has been executed, or the Condition has evaluated to
     *                false.
     */
    public void exec(Environment env)
    {
        if (c.eval(env) == 1)
        {
            for (Statement curr : res)
                curr.exec(env);
        }
        else
        {
            if (other != null)
            {
                for (Statement curr : other)
                    curr.exec(env);
            }
        }
    }

}
