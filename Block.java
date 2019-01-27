package ast;
import java.util.*;

import emitter.Emitter;
import environment.Environment;

/**
 * The Block class contains an ArrayList of statements located between its 
 * BEGIN and END bounds. 
 * 
 * @author Rakesh Nori
 * @version 3/20/2018
 */
public class Block extends Statement
{
	private List<Statement> stmnts;
	
	/**
	 * Instantiates the Block object with an ArrayList of statements.
	 * @param s the ArrayList of statements used to instantiate the Block's 
	 * 		  instance variable.
	 */
	public Block(ArrayList<Statement>s)
	{
		stmnts = s;
	}
	
	/**
	 * Adds a Statement to the block's ArrayList of statements.
	 * @param s the Statement to be added to the block's ArrayList.
	 * @postcondition the Statement has been added to the Block's list of statements.
	 */
	public void addTo(Statement s)
	{
		stmnts.add(s);
	}

	/**
	 * @Override
	 * Executes all the statements in the Block's List.
	 * @param env the environment being used to evaluate all the statements. 
	 * @postcondition all the statements in the Block have been executed.
	 */
	public void exec(Environment env) 
	{
		for (Statement s: stmnts)
		{
			s.exec(env);
		}
	}
	
	/**
	 * Converts the statements inside the block from the current language to MIPS.
	 * @param e the Emitter that writes to the MIPS file.
	 */
	public void compile(Emitter e)
	{
		for (Statement s : stmnts)
			s.compile(e);
	}
}
