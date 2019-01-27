package ast;
import java.util.Scanner;
import environment.Environment;

/**
 * The Readln class uses user input to instantiate a variable. 
 * @author Rakesh Nori
 * @version 3/26/20188
 *
 */
public class Readln extends Statement
{
	private String name;
	private int val;
	
	/***
	 * Instantiates a Readln object.
	 * @param nm the name of the variable.
	 */
	public Readln(String nm)
	{
		name = nm;
	}
	
	/**
	 * Gives a value for the variable and passes it into the environment.
	 * @param env the Environment has a map that stores variables.
	 */
	public void exec(Environment env)
	{
		System.out.println("Input int val for the variable");
		Scanner sc = new Scanner(System.in);
		val = sc.nextInt();
		env.setVariable(name, val);
		sc.close();
		
	}
}
