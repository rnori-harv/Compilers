package emitter;
import java.io.*;

import ast.ProcedureDeclaration;

/**
 * The Emitter is responsible for outputting MIPS code to a different file
 * that can compile and have the same functionality as the current program.
 * @author Rakesh Nori
 * @version 4/20/2018
 */
public class Emitter
{
	private PrintWriter out;
	private int start = 1;
	private int loopstart = 1;
	private int loopend = 1;
	private int excessStackHeight = 0;
	private ProcedureDeclaration remember;

	/**
	 * Creates a new Emitter object.
	 * @param outputFileName the name of the output file.
	 */
	public Emitter(String outputFileName)
	{
		try
		{
			out = new PrintWriter(new FileWriter(outputFileName), true);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * prints one line of code to file (with non-labels indented)
	 * @param code the MIPS code.
	 */
	public void emit(String code)
	{
		if (!code.endsWith(":"))
			code = "\t" + code;
		out.println(code);
	}

	/**
	 * pushes the value of a register on to a stack.
	 * @param reg the register being pushed on to the stack.
	 */
	public void emitPush(String reg)
	{
		emit("subu $sp, $sp, 4" + "  #stores register " + reg +" onto the stack.");
		emit("sw " + reg +", ($sp)");
		excessStackHeight++;
	}
	
	/**
	 * Pops a value off the stack and on to the register.
	 * @param reg the register that will contain the value that was popped off the stack.
	 */
	public void emitPop(String reg)
	{
		emit("lw " + reg +", ($sp)" + "  #removes register " + reg + " from the stack.");
		emit("addu $sp, $sp, 4");
		excessStackHeight--;
	}
	/**
	 * Closes the file after all emit calls have been completed.
	 */
	public void close()
	{
		out.close();
	}
	
	/**
	 * Gets the next label number for an if statement.
	 * @return the next available if label number.
	 */
	public int nextLabelID()
	{
		int ret = start;
		start++;
		return ret;
	}
	
	/**
	 * Gets the next label number for a loop statement.
	 * @return the next available loop label number.
	 */
	public int loopLabelID()
	{
		int ret = loopstart;
		loopstart++;
		return ret;
	}
	
	/**
	 * Gets the next label number for an endLoop.
	 * @return the next available end loop label number.
	 * @return
	 */
	public int endLoopLabelID()
	{
		int ret = loopend;
		loopend++;
		return ret;
	}
	
     /**
	 * remember proc as current procedure context
	 * @param proc the ProcedureDeclaration being set as current procedure.
	 */
	 public void setProcedureContext(ProcedureDeclaration proc)
	 {
		  remember = proc;
		  excessStackHeight = 0;
	 }
	 /**
	  * clear current procedure context (remember = null)
	  * @postcondition the current ProcedureDeclaration is null.
	  */
	 public void clearProcedureContext()
	 {
		 remember = null;
	 }
	 
	 /**
	  * Checks if the variable of a given string is present 
	  * in the current ProcedureDeclaration object.
	  * @param varName the name of the variable.
	  * @return true if the variable is local; otherwise,
	  * 		false.
	  */
	 public boolean isLocalVariable(String varName)
	 {
		if (remember != null)
		{
			if (varName.equals(remember.getName()))
				return true;
			if (remember.getArgs().contains(varName) || remember.getVars().contains(varName))
			{
				//System.out.println(varName + " is LOCAL.");
				return true;
			}
		}
		//System.out.println(varName + " NOT LOCAL.");
		return false;
	 }
	 
	 /**
	  * Determines a variable's position on the stack.
	  * @param a the name of the variable
	  * @return the integer representing the depth of the variable.
	  */
	 public int getOffset(String a)
	 {
		 // System.out.println(excessStackHeight + " " + a);
		 int loc = 0;
		 if (isLocalVariable(a))
		 {
			 if (remember.getArgs().contains(a))
			 {
				 loc += excessStackHeight + remember.getArgs().indexOf(a) + 1;
			 }
			 else if (remember.getVars().contains(a))
			 {
				 loc += remember.getVars().indexOf(a);
			 }
		 }
		// System.out.println(a + " is at " + loc * 4);

		 return loc * 4;
	 }
}