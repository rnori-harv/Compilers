package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * A type of statement that contains a condition and a Statement to be executed 
 * if that same condition evaluates to true, or another statement (not necessary) 
 * if it evaluates to false.
 * @author Rakesh Nori
 * @version 3/20/2018
 *
 */
public class If extends Statement
{
	private Condition c;
	private Statement res;
	private Statement other;
	/**
	 * Instantiates the condition and statement of the class.
	 * @param in The Condition 
	 * 		  that will be evaluated to tell if the Statement should be executed or not.
	 * @param a the Statement that will be executed if the Condition evaluates to true.
	 */
	public If(Condition in, Statement a, Statement b)
	{
		c = in;
		res = a;
		other = b;
	}
	/**
	 * @Override
	 * Checks if the condition is true. If so, executes the Statement in the If class.
	 * @param env the Environment containing variables that are used in execution.
	 * @postcondition The Condition has been evaluated to true and the  Statement has 
	 * 				  been executed, or the Condition has evaluated to false.
	 */
	public void exec(Environment env)
	{
		if (c.eval(env)==1)
		{
			res.exec(env);
		}
		else
		{
			if (other != null)
				other.exec(env);
		}
	}
	
	/**
	 * Converts the body statement(s) and the condition in the if statement to MIPS.
	 * @Emitter e the Emitter that converts current code to MIPS.
	 */
	public void compile(Emitter e)
	{
		String temp = "endif" + e.nextLabelID();
		c.compile(e, temp);
		res.compile(e);
		e.emit(temp +":");
		if (other != null)
			other.compile(e);
	}
}
