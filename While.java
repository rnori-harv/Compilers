package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The While class uses a condition in order to repeat a given statement.
 * @author Rakesh Nori
 * @version 3/22/2018
 *
 */
public class While extends Statement
{
	private Condition c;
	private Statement res;
	/**
	 * Instantiates the While object by defining the condition being checked 
	 * and the resulting statement.
	 * @param in the Condition that will be repeatedly checked in order to execute.
	 * @param a the Statement that will be looped through and executed 
	 * 		  while the condition is true.
	 */
	public While(Condition in, Statement a)
	{
		c = in;
		res = a;
	}
	/**
	 * @Override
	 * Checks if its Condition is true then executes the result statement 
	 * until the condition is false.
	 * @precondition the Condition must be something that can eventually become 
	 *				 false after execution of the result Statement.
	 * @postcondition the Condition has been evaluated to false.
	 */
	public void exec(Environment env) 
	{
		while (c.eval(env)==1)
		{
			res.exec(env);
		}
	}
	
	/**
	 * Converts the code for a While loop to MIPS.
	 * @param e the Emitter that prints MIPS code.
	 */
	public void compile(Emitter e)
	{
		String temp = "loop" + e.loopLabelID();
		String etemp = "endloop" + e.endLoopLabelID();
		e.emit(temp + ":");
		c.compile(e, etemp);
		res.compile(e);
		e.emit("j " + temp);
		e.emit(etemp + ":");
	}

}
