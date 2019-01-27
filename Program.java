package ast;

import java.util.List;

import emitter.Emitter;
import environment.Environment;

/**
 * The Program class holds Procedures and Statements that are contained
 * in a program. It executes all of them when called upon.
 * @author Rakesh Nori
 * @version 4/5/2018
 */
public class Program extends Statement
{
	private List <String> vars;
	private List <ProcedureDeclaration> proceds;
	private Statement s;
	
	/**
	 * Establishes all the procedures and Statements in the program.
	 * @param in the list of Procedures
	 * @param f the Statement(s) called.
	 */
	public Program(List <String> variables, List <ProcedureDeclaration> in, Statement f)
	{
		proceds = in;
		s = f;
		vars = variables;
	}
	
	/**
	 * Executes all the procedure declarations and the statement after, 
	 * which is basically the same thing as running the program.
	 * @param env the Global environment in which the program will be executed in.
	 */
	public void exec(Environment env)
	{
		for (ProcedureDeclaration p : proceds)
			p.exec(env);
		s.exec(env);
	}
	/**
	 * Converts the whole program file to MIPS.
	 * @param e the Emitter that will convert the code to MIPS.
	 */
	public void compile (Emitter e)
	{
		e.emit("#MIPS program for the PASCAL code.");
		e.emit("#@author Rakesh Nori");
		e.emit("#@version 5/2/2018");
		e.emit(".data");
		for (String curr : vars)
		{
			e.emit("\t" + "var" + curr + ": .word	0");
		}
		e.emit("\t nl: .asciiz \"\\n\"");
		e.emit(".text");
		e.emit(".globl main");
		e.emit("main:");
		s.compile(e);
		e.emit("li $v0, 10");
		e.emit("syscall");
		for (ProcedureDeclaration p : proceds)
			p.compile(e);
	}
}
