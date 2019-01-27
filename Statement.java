package ast;
import emitter.Emitter;
import environment.Environment;
/**
 * The Statement class is a general form of executing Statements from a file.
 * 
 * @author Rakesh Nori
 * @version 3/20/2018
 *
 */
public abstract class Statement 
{
	/**
	 * General method that executes a Statement
	 * @param env the Environment containing variables that can be used in exec.
	 */
	public abstract void exec(Environment env);
	
	/**
	 * Converts the instructions to MIPS. 
	 * @param e the Emitter that writes the file.
	 */
	public void compile(Emitter e)
	{
		throw new RuntimeException("not implemented yet.");
	}
}
