package ast;

import environment.Environment;

/**
 * 
 * @author Rakesh Nori
 * @version 3/20/2018 The abstract Expression class is a general form of
 *          evaluating operations from a given file.
 */
public abstract class Expression
{
    /**
     * Evaluates the expression in a given environment/
     * 
     * @param env The Environment containing variables and operations to be
     *        used.
     * @return the Integer result after the evaluation has occurred.
     */
    public abstract int eval(Environment env);

}
