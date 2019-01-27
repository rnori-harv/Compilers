package ast;
import emitter.Emitter;
import environment.Environment;
/**
 * The BinOp class manages operators like +, -, and % 
 * between the values of two expressions.
 * 
 * @author Rakesh Nori
 * @version 3/20/2018
 */
public class BinOp extends Expression
{
	private String op;
	private Expression exp1;
	private Expression exp2;
	/**
	 * The constructor for BinOp
	 * @param in the actual operand that identifies the expression to be used 
	 * 		  (ex: "*", "/")
	 * @param a the first / left expression of the BinOp.
	 * @param b the second / right expression of the BinOp.
	 */
	public BinOp(String in, Expression a, Expression b) 
	{
		op = in;
		exp1 = a;
		exp2 = b;
	}
	
	/**
	 * @Override
	 * Identifies the binary operator being used and uses it to evaluate the two expressions.
	 * @param env the environment containing variables that can be used.
	 * @return the Integer result between the two expressions after 
	 * 		   the operator has been used.
	 */
	public int eval(Environment env)
	{
		if (op.equals("%"))
			return (exp1).eval(env) % (exp2).eval(env);
		else if (op.equals("*"))
			return (exp1).eval(env) * (exp2).eval(env);
		else if (op.equals("/"))
			return (exp1).eval(env) /(exp2).eval(env);
		else if (op.equals("-"))
			return (exp1).eval(env) - (exp2).eval(env);
		else if (op.equals("+"))
			return (exp1).eval(env) + (exp2).eval(env);
		else return 0;
	}
	
	/**
	 * Converts the operation for a BinOp from the current language to MIPS.
	 * @param e the Emitter that writes to the MIPS file.
	 */
	public void compile(Emitter e)
	{
		exp1.compile(e);
		e.emitPush("$v0");
		exp2.compile(e);
		e.emitPush("$v0");
		e.emitPop("$t1");
		e.emitPop("$t0");
		if (op.equals("*"))
		{
			e.emit("mult $t0, $t1");
			e.emit("mflo $v0");
		}
		if (op.equals("+"))
			e.emit("addu $v0, $t0, $t1");
		else if (op.equals("-"))
			e.emit("subu, $v0, $t0, $t1");
		else if (op.equals("/"))
		{
			e.emit("div $t0, $t1");
			e.emit("mflo $v0");
		}
	}
	
	public String toString()
	{
		return exp1 + " " + op + " " + exp2;
	}
	
}
