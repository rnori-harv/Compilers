package ast;

import environment.Environment;

/**
 * The Operation class manages ALL the operators between the values of two
 * expressions.
 * 
 * @author Rakesh Nori
 * @version 3/20/2018
 */
public class Operation extends Expression
{
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * The constructor for Operation
     * 
     * @param in the actual operand that identifies the expression to be used
     *            (ex: "*", "/")
     * @param a the first / left expression of the BinOp.
     * @param b the second / right expression of the BinOp.
     */
    public Operation(String in, Expression a, Expression b)
    {
        op = in;
        exp1 = a;
        exp2 = b;
    }

    /**
     * @Override Identifies the operator being used and uses it to evaluate the
     *           two expressions.
     * @param env the environment containing variables that can be used.
     * @return the Integer result between the two expressions after the operator
     *         has been used.
     */
    public int eval(Environment env)
    {
        if (op.equals("%"))
            return (exp1).eval(env) % (exp2).eval(env);
        else if (op.equals("*"))
            return (exp1).eval(env) * (exp2).eval(env);
        else if (op.equals("/"))
            return (exp1).eval(env) / (exp2).eval(env);
        else if (op.equals("-"))
            return (exp1).eval(env) - (exp2).eval(env);
        else if (op.equals("+"))
            return (exp1).eval(env) + (exp2).eval(env);
        else if (op.equals("="))
        {
            if (exp1.eval(env) == exp2.eval(env))
                return 1;
        }
        else if (op.equals("<>"))
        {
            if (exp1.eval(env) != exp2.eval(env))
                return 1;
        }
        else if (op.equals("<"))
        {
            if (exp1.eval(env) < exp2.eval(env))
                return 1;
        }
        else if (op.equals(">"))
        {
            if (exp1.eval(env) > exp2.eval(env))
                return 1;
        }
        else if (op.equals("<="))
        {
            if (exp1.eval(env) <= exp2.eval(env))
                return 1;
        }
        else if (op.equals(">="))
        {
            if (exp1.eval(env) >= exp2.eval(env))
                return 1;
        }
        return 0;
    }

    /**
     * Prints the Operation object's contents.
     * 
     * @return the String representing the expressions and operator.
     */
    public String toString()
    {
        return exp1 + " " + op + " " + exp2;
    }

}
