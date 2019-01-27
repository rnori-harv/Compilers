package ast;

import environment.Environment;

/**
 * The For Class uses assignment and conditions in tandem to execute loops. 
 * @author Rakesh Nori
 * @version 3/22/2018
 */
public class For extends Statement
{
	private Condition c;
	private Statement res;
	private String name;
	private Assignment a;
	private Expression top;
	/**
	 * The constructor creates a condition that will be checked every iteration  
     * of the loop.
	 * @param check the condition that is checked and always involves a < operator.
	 * @param whileTrue the statement that is executed as a result of the condition
	 * 		  evaluating to true.
	 * @param in the Assignment of the variable used in incrementing.
	 * @param nm the name of the Variable being used.
	 * @param lim the upper limit for the value of the variable being used.
	 */
	public For(Condition check, Statement whileTrue, Assignment in, String nm, 
			   Expression lim)
	{
		c = check;
		res = whileTrue;
		top = lim;
		name = nm;
		a = in;
	}
	
	/**
	 * @Override
	 * First the assignment is executed, then it executes the given Statement 
	 * while the condition is true. Unlike a "while" loop, this one increments 
	 * the variable being used every iteration until it makes  the condition 
	 * evaluate to false. 
	 * @postcondition the condition that checks whether the loop should keep running
	 * 				  is now false.
	 */
	public void exec(Environment env) 
	{
		a.exec(env);
		while (c.eval(env) == 1)
		{
			res.exec(env);
			env.setVariable(name, env.getVariable(name) + 1);
			Number increment = new Number(env.getVariable(name));
			c = new Condition(increment, "<", top);
		}
	}

}
