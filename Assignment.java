package ast;
import emitter.Emitter;
import environment.Environment;
/**
 * Class does the task of associating an expression with a variable. 
 * @author Rakesh Nori
 * @version 3/20/2018
 */
public class Assignment extends Statement
{
	private Expression exp; 
	private String var;
	/**
	 * Instantiates the exp and var instance variables, which are the two components 
	 * of an Assignment object.
	 * @param e the expression to be associated with a variable
	 * @param in the name of the variable
	 */
	public Assignment(Expression e, String in)
	{
		exp = e;
		var = in;
	}
	/**
	 * @Override
	 * Executes the Assignment of the variable in the environment.
	 * @param env the environment which manages and stores 
	 * 		  the variables and their values.
	 * @postcondition the variable and its corresponding expression has been stored 
	 * 				  in the Environment's map.
	 */
	public void exec(Environment env)
	{
		env.setVariable(var, exp.eval(env));
	}
	
	/**
	 * Converts the code for the assignemnt operation from the current language to MIPS.
	 * @param e the Emitter that writes to the MIPS file.
	 */
	public void compile(Emitter e)
	{
		exp.compile(e);
		if (e.isLocalVariable(var))
			e.emit("sw $v0, "+ e.getOffset(var) + "($sp)");
		else
			e.emit("sw $v0, var" + var);
	}
}
