package ast;

import java.util.HashMap;
import java.util.List;

import emitter.Emitter;
import environment.Environment;

/**
 * The Procedure Call class is responsible for executing a Procedure 
 * with the corresponding name and parameters as declared in the object.
 * @author Rakesh Nori
 * @version 4/6/2018
 */
public class ProcedureCall extends Expression
{
	private String s;
	private List<Expression> exps;
	
	/**
	 * Makes a new ProcedureCall object with the String 
	 * that represents a procedure that was already declared.
	 * @param name the String representing the declared Procedure to be executed.
	 * @param params the parameters of the procedure being passed in.
	 */
	public ProcedureCall(String name, List<Expression> params)
	{
		s = name;
		exps = params;
	}
	
	/**
	 * Evaluates the Procedure Call and sets the values of all the arguments.
	 * by taking the resulting statement from the declaration and executing it.
	 * @return the value of the variable associated with the procedure call.
	 * @param env the environment that the current call is in.
	 */
	public int eval (Environment env)
	{
		Environment child = new Environment(env);
		child.declareVariable(s, 0);
		ProcedureDeclaration a = child.proGet(s);
		a.getLocalEnvironment().setPadre(child);
		List<String> corr = a.getArgs();
		if (corr != null && exps != null)
		{
			if (corr.size() != exps.size())
				throw new IllegalArgumentException("Parameters stated "
						+ "in the declaration of the method "
						+ "do not match arguments given.");
			for (int i = 0; i < corr.size(); i++)
				child.declareVariable(corr.get(i), exps.get(i).eval(env));
		}
		a.getBody().exec(a.getLocalEnvironment());
		List<String> variablesInDeclaration = a.getVars();
		for (String curr : variablesInDeclaration)
			a.getLocalEnvironment().setVariable(curr, 0);
	    return child.getVariable(s);
	}
	
	/**
	 * Compiles all the parameters in the ProcedureCall, 
	 * then jumps to the corresponding ProcedureDeclaration label in MIPS.
	 * @param e the Emitter that prints MIPS code.
	 */
	public void compile(Emitter e)
	{
		for (int a = exps.size() - 1; a >= 0; a--)
		{
			exps.get(a).compile(e);
			e.emitPush("$v0");		}
		e.emit("jal proc"+s);
	}
}
