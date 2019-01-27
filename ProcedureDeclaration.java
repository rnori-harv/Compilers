package ast;

import java.util.List;

import emitter.Emitter;
import environment.Environment;

/**
 * The ProcedureDeclaration class deals with the declaration of procedures 
 * before the program begins. It sets them into its environment's map, 
 * which can be used for the ProcedureCalls in the later statements.
 * @author Rakesh Nori
 * @version 4/6/2018
 */
public class ProcedureDeclaration extends Statement
{
	private String nm;
	private Statement body;
	private List<String> args;
	private List<String> vars;
	private Environment localEnv;
	/**
	 * Creates a new ProcedureDeclaration object that has a name 
	 * (used as a key in the map), body statement, and a list of arguments.
	 * @param id the name of the Procedure
	 * @param in the body of the Procedure
	 * @param a the list of arguments needed for the procedure.
	 */
	public ProcedureDeclaration(String id, Statement in, List<String> a, List<String> locals)
	{
		nm = id;
		body = in;
		args = a;
		vars = locals;
	}
	
	/**
	 * Retrieves the arguments of the Procedure.
	 * @return the list of String arguments.
	 */
	public List<String> getArgs()
	{
		return args;
	}
	
	/**
	 * Retrieves the local vars of the Procedure.
	 * @return the list of String arguments.
	 */
	public List<String> getVars()
	{
		return vars;
	}
	/**
	 * Retrieves the body of the procedure.
	 * @return the statement that represents the action the procedure takes.
	 */
	public Statement getBody()
	{
		return body;
	}
	
	/**
	 * Gets name of Procedure.
	 * @return procedure's name.
	 */
	public String getName()
	{
		return nm;
	}
	
	/**
	 * Gets the local environment, which holds local variables for the ProcedureDeclaration.
	 * @return the local environment
	 */
	public Environment getLocalEnvironment()
	{
		return localEnv;
	}
	
	/**
	 * Sets the procedure in the environment's HashMap.
	 * @param a the environment being used.
	 */
	public void exec(Environment a)
	{
		a.proSet(nm, this);
		/**below is new**/
		localEnv = new Environment(a);
		for (String curr : vars)
			localEnv.declareVariable(curr, 0);
	}
	
	/**
	 * Converts a Procedure Declaration to MIPS and pushes the return address
	 * and local variables onto the stack. When the procedure is finished, 
	 * it pops them back off.
	 * @param e the Emitter that prints code in MIPS format.
	 */
	public void compile(Emitter e)
	{
		e.emit("proc" + nm + ":");
		e.emitPush("$v0");
		e.setProcedureContext(this);
		e.emitPush("$ra");
		e.emit("li $t7, 0");
		for (int i = 0; i <= vars.size() - 1; i++)
		{
			e.emitPush("$t7");
		}
		body.compile(e);
		for (int i = 0; i <= args.size() - 1; i++)
		{
			e.emitPop("$t7");
		}
		e.emitPop("$ra");
		e.emitPop("$v0");
		for (int i = 0; i < args.size(); i++)
			e.emitPop("$t0");
		e.emit("jr $ra");
		e.clearProcedureContext();
	}
}
