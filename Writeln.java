package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Writeln is a type of Statement that is responsible  
 * for displaying the evaluation of a given expression. 
 * @author Rakesh Nori
 * @version 3/20/2018
 */
public class Writeln extends Statement
{
	private Expression exp;
	
	/**
	 * Instantiates a new Writeln object that holds an Expression, 
	 * which is designed to be printed when executed.
	 * @param in the Expression that will be printed in the Writeln object.
	 */
	public Writeln(Expression in)
    {
		exp = in;
    }
	
	/**
	 * @Override
	 * Prints out the evaluation of the expression stored in the Writeln object.
	 * @param env the Environment that holds variables and their values, 
	 * 		  which is used in evaluating an expression.
	 * @postcondition the Integer value of the expression has been printed 
	 * 				  to the console.
	 */
	public void exec(Environment env)
	{
		System.out.println(exp.eval(env));
	}
	
	/**
	 * @override
	 * compiles the expression inside the statement 
	 * and emits the code in MIPS to print the value of the expression.
	 */
	public void compile(Emitter e)
	{
		exp.compile(e);
		e.emit("move $a0, $v0");
		e.emit("li $v0, 1");
		e.emit("syscall");
		e.emit("la $a0, nl");
		e.emit("li $v0, 4");
		e.emit("syscall");
	}
}
