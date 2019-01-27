package ast;

import environment.Environment;

/**
 * The Variable class holds a String name and has a value corresponding to it,
 * which is stored in the Environment.
 * 
 * @author Rakesh Nori
 * @version 3/20/2018
 */
public class Variable extends Expression
{
    private String name;

    /**
     * Instantiates the Variable Object and gives it a name that can be used
     * when accessing its value.
     * 
     * @param in the name of the String being used for the Variable object.
     */
    public Variable(String in)
    {
        name = in;
    }

    /**
     * @Override Looks in the environment and finds the value associated with
     *           the Variable object's name.
     * @param env the environment that holds a map that keeps track of Variable
     *            names and corresponding values.
     * @return the Integer value of the Variable object.
     */
    public int eval(Environment env)
    {
        return env.getVariable(name);
    }

}
