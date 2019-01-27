package ast;

import environment.Environment;

/**
 * Class does the task of associating an expression with a variable.
 * 
 * @author Rakesh Nori
 * @version 3/20/2018
 */
public class Assignment extends Statement
{
    private Expression exp;
    private String var;

    /**
     * Instantiates the exp and var instance variables, which are the two
     * components of an Assignment object.
     * 
     * @param e the expression to be associated with a variable
     * @param in the name of the variable
     */
    public Assignment(Expression e, String in)
    {
        exp = e;
        var = in;
    }

    /**
     * @Override Executes the Assignment of the variable in the environment.
     * @param env the environment which manages and stores the variables and
     *            their values.
     * @postcondition the variable and its corresponding expression has been
     *                stored in the Environment's map.
     */
    public void exec(Environment env)
    {
        env.setVariable(var, exp.eval(env));
    }
}
