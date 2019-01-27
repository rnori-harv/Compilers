package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The class that is responsible for using a relative operator in order 
 * to return a boolean (1 or 0) that describes the relation between two expressions.
 * @author Rakesh Nori
 * @version 3/20/2018
 *
 */
public class Condition extends Expression
{
	private Expression e1;
	private Expression e2;
	private String relop;
	/**
	 * Instantiates the expressions and the relative operator being used in the
	 * Condition object.
	 * @param a the first Expression being compared.
	 * @param op the String representing the relative operator being used.
	 * @param b the second Expression being compared.
	 */
	public Condition(Expression a, String op, Expression b)
	{
		e1 = a;
		relop = op;
		e2 = b;
	}
	
	/**
	 * @Override
	 * Evaluates the two expressions determines their relation.
	 * @param env the Environment containing all the variables that can be used.
	 * @return The boolean result of the expression in form of an integer
	 * 		   (1 = true, 0 = false).
	 */
	public int eval(Environment env)
	{
		if (relop.equals("="))
		{
			if (e1.eval(env) == e2.eval(env))
				return 1;
		}
		else if (relop.equals("<>"))
		{
			if (e1.eval(env) != e2.eval(env))
				return 1;
		}
		else if (relop.equals("<"))
		{
			if (e1.eval(env) < e2.eval(env))
				return 1;
		}
		else if (relop.equals(">"))
		{
			if (e1.eval(env) > e2.eval(env))
				return 1;
		}
		else if (relop.equals("<="))
		{
			if (e1.eval(env) <= e2.eval(env))
				return 1;
		}
		else if (relop.equals(">="))
		{
			if (e1.eval(env) >= e2.eval(env))
				return 1;
		}
		return 0;
	}
	
	/**
	 * Converts code of a condition to MIPS and uses the targetlabel 
	 * for its conditional statements as a label.
	 * @param e the Emitter that converts the curreng language to the target one.
	 * @param targetLabel the name of the label that is used for 
	 * 		  the conditional statement in MIPS.
	 */
	public void compile(Emitter e, String targetLabel)
	{
		e1.compile(e);
		e.emitPush("$v0");
		e2.compile(e);
		e.emitPop("$t0");
		if (relop.equals("="))
		{
			e.emit("bne $t0, $v0, " + targetLabel);
		}
		else if (relop.equals("<>"))
		{
			e.emit("beq $t0, $v0, " + targetLabel);
		}
		else if (relop.equals("<"))
		{
			e.emit("bge $t0, $v0, " + targetLabel);
		}
		else if (relop.equals(">"))
		{
			e.emit("ble $t0, $v0, " + targetLabel);
		}
		else if (relop.equals("<="))
		{
			e.emit("bgt $t0, $v0, " + targetLabel);
		}
		else if (relop.equals(">="))
		{
			e.emit("blt $t0, $v0, " + targetLabel);
		}
		
	}
}
