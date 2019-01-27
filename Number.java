package ast;
import emitter.Emitter;
import environment.Environment;

/**
 * The Number class holds an Integer that can be lated used in other evaluations.
 * @author Rakesh Nori
 * @version 3/20/2018
 */
public class Number extends Expression
{
	private int val;
	
	/**
	 * Gives a value to the Number object.
	 * @param in the Integer that will be associated with the Number object.
	 */
	public Number(int in)
	{
		val = in;
	}
	
	/**
	 * @Override
	 * Gives the number
	 * @param env the environment containing variables that can be used during evaluation.
	 * @return the int that is associated with the Number object.
	 */
	public int eval(Environment env)
	{
		return val;
	}
	
	/**
	 * Loads the number into the $v0 register. 
	 * @param e the Emitter used to convert the Pascal code to MIPS.
	 */
	public void compile(Emitter e)
	{
		e.emit("li $v0, " + val);
	}
	
	/**
	 * toString method for Number used in debugging.
	 * @return the String containing the integer value of the Number object.
	 */
	public String toString()
	{
		return val+"";
	}
}
