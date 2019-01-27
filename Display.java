package ast;

import environment.Environment;

/**
 * Display is a type of Statement that is responsible for displaying the
 * evaluation of a given expression in addition to reading in values.
 * 
 * @author Rakesh Nori
 * @version 3/20/2018
 */
public class Display extends Statement
{
    private Expression exp;
    private Statement read;

    /**
     * Instantiates a new Display object that holds an Expression, which is
     * designed to be printed when executed.
     * 
     * @param in the Expression that will be printed in the Display object.
     * @param ask the potential Read statement.
     */
    public Display(Expression in, Statement ask)
    {
        exp = in;
        read = ask;
    }

    /**
     * @Override Prints out the evaluation of the expression stored in the
     *           Display object. After that, it can potentially read in a value
     *           after also.
     * @param env the Environment that holds variables and their values, which
     *            is used in evaluating an expression.
     * @postcondition the Integer value of the expression has been printed to
     *                the console.
     */
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
        if (read != null)
            read.exec(env);
    }

}
