package parser;
import java.io.*;

import ast.BinOp;
import ast.Block;
import ast.Condition;
import ast.Expression;
import ast.For;
import ast.If;
import ast.Number;
import ast.ProcedureCall;
import ast.ProcedureDeclaration;
import ast.Program;
import ast.Readln;
import ast.Statement;
import ast.Variable;
import ast.While;
import ast.Writeln;
import emitter.Emitter;
import ast.Assignment;
import environment.Environment;
import java.util.ArrayList;
import java.util.List;

import scanner.ScanErrorException;
import scanner.Scanner;
/**
 * A top-down recursive descent parser that uses grammars and looks ahead tokens.
 * @author Rakesh Nori
 * @version 4/6/2018
 *
 */
public class Parser 
{
	Scanner sc;
	static String currToken;
	
	/**
	 * The constructor for a parser that takes in a scanner and creates a 
	 * current Token for parsing through and a map to store variables.
	 * @param s the scanner being passed through into the constructor.
	 * @throws IOException if the BufferedReader in the scanner runs into an error.
	 * @throws ScanErrorException if a scanning error occurs.
	 */
	public Parser(Scanner s) throws IOException, ScanErrorException
	{
		sc = s;
		currToken = sc.nextToken();
	}
	
	/**
	 * moves one token ahead in the Scanner. Also checks if the string in the parameter 
	 * is the expected one in currToken.
	 * @param str the String that is expected to be in currToken
	 * @throws IOException if the BufferedReader in the scanner runs into an error.
	 * @throws ScanErrorException if a scanning error occurs.
	 * @throws IllegalArgumentException If currToken and str do not match.
	 * @postcondition the token has moved one ahead.
	 */
	private void eat(String str) throws ScanErrorException, IOException
	{
		if (str.equals(currToken))
		{
			currToken = sc.nextToken();
			if (currToken.equals("mod"))
				currToken = "%";
		}
		else
			throw new IllegalArgumentException("Expected " + str + ", but had " +
												currToken +" instead.");
	}
	
	/**
	 * Parses through a number.
	 * @precondition current token is an integer.
	 * @postcondition the number has been eaten.
	 * @return the value of the parsed Integer or a variable in the form of an Expression.
	 * @throws IOException if the BufferedReader in the scanner runs into an error.
	 * @throws ScanErrorException if a scanning error occurs.
	 */
	private Expression parseNumber() throws ScanErrorException, IOException
	{
		int num = 0;
		if (isIdentifier(currToken))
		{
			String curr = currToken;
			eat(curr);
			if(currToken.equals("("))
			{
				eat("(");
				Expression call = null;
				if(currToken.equals(")"))
				{
					call = new ProcedureCall(curr, null);
				}
				else
				{
					List<Expression> args = new ArrayList<Expression>();
					while (!currToken.equals(")"))
					{
						args.add(parseExp());
						if (currToken.equals(","))
							eat(",");
					}
					call = new ProcedureCall(curr, args);
				}
				eat(")");
				return call;
			}
			Expression vr = new Variable(curr);
			return vr;
		}
		else
		{
			num = Integer.parseInt(currToken);
			eat(currToken);
		}
		return new Number(num);
	}
	
	/**
	 * Parses through a factor, accounting for negative signs and parentheses.
	 * @return the Expression representing the value of the factor after the method finishes.
	 * @throws IOException if the BufferedReader in the scanner runs into an error.
	 * @throws ScanErrorException if a scanning error occurs.
	 * @throws IllegalArgumentException if empty parentheses are present in the text.
	 */
	public Expression parseFactor() throws ScanErrorException, IOException
	{
		if (currToken.equals("("))
		{
			eat(currToken);
			Expression val = parseExp();
			eat(")");
			return val;
		}
		else if (currToken.equals("-"))
		{
			eat(currToken);
			return new BinOp("-", new Number(0), parseFactor());
		}
		else if (currToken.equals(")"))
		{
			throw new IllegalArgumentException("Empty incorrect ) in the text.");
		}
		else
		{
			return parseNumber();
		}
	}
	
	/**
	 * Parses through a term and accounts for multiplication, division, and mods.
	 * Uses parseFactor as a helper.
	 * @return BinOp object representing the value of the term when evaluated.
	 * @throws IOException if the BufferedReader in the scanner runs into an error.
	 * @throws ScanErrorException if a scanning error occurs.
	 */
	public Expression parseTerm() throws ScanErrorException, IOException
	{
		Expression init = parseFactor();
		//System.out.println(currToken);
		while (currToken.equals("*")|| currToken.equals("/") || currToken.equals("%"))
		{
			if (currToken.equals("*"))
			{
				eat("*");
				init = new BinOp("*", init, parseFactor());
			}
			else if (currToken.equalsIgnoreCase("%"))
			{
				eat("%");
				init = new BinOp("%", init, parseFactor());
			}
			else
			{
				eat("/");
				init = new BinOp("/", init, parseFactor());
			}
		}
		return init;
	}
	
	/**
	 * parses an Expression, accounting for all the operations by using parseTerm and parseFactor.
	 * @return the BinOp representing the value of the Expression when evaluated.
	 * @throws IOException if the BufferedReader in the scanner runs into an error.
	 * @throws ScanErrorException if a scanning error occurs.
	 */
	public Expression parseExp() throws ScanErrorException, IOException
	{
		Expression init = parseTerm();
		while (currToken.equals("+") || currToken.equals("-"))
		{
			if (currToken.equals("+"))
			{
				eat("+");
				init = new BinOp("+", init, parseTerm());
			}
			else
			{
				eat("-");
				init = new BinOp("-", init, parseTerm());
			}
		}
		return init; 
	}
	
	/**
	 * Checks if a given token is an identifier or not.
	 * @param s the token being checked for being an identifier
	 * @return true if it is an identifier; otherwise,
	 * 		   false.
	 */
	private boolean isIdentifier(String s)
	{
		char input = s.charAt(0); 
		if ((input >= 'A' && input <= 'Z') || (input >= 'a' && input <= 'z'))
		{
			for (int i = 1; i < currToken.length(); i++)
			{
				input = s.charAt(i);
				if (!((input >= 'A' && input <= 'Z') || (input >= 'a' && input <= 'z')
					|| (input >= '0' && input <= '9')))
					return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Parses all the Statements and prints the output. 
	 * @throws IOException if the BufferedReader in the scanner runs into an error.
	 * @throws ScanErrorException if a scanning error occurs.
	 * @return A single Statement or a Block, which contains multiple Statement objects, 
	 * 		   that prints out the information held in these objects after they are executed.
	 */
	public Statement parseStatement() throws ScanErrorException, IOException
	{
		if (currToken.equals("WRITELN"))
		{
			eat("WRITELN");
			eat("(");
			Expression n = parseExp();
			eat(")");
			eat(";");
			return new Writeln(n);
		}
		else if (currToken.equals("IF"))
		{
			eat("IF");
			Expression e1 = parseExp();
			String op = currToken;
			eat(op);
			Expression e2 = parseExp();
			eat("THEN");
			Statement res = parseStatement();
			Statement other = null;
			if (currToken.equals("ELSE"))
			{
				eat("ELSE");
				other = parseStatement();
			}
			Statement iffy = new If(new Condition(e1, op, e2), res, other);
			return iffy;
		}
		else if (currToken.equals("WHILE"))
		{
			eat("WHILE");
			Expression e1 = parseExp();
			String op = currToken;
			eat(op);
			Expression e2 = parseExp();
			eat("DO");
			Statement res = parseStatement();
			Statement willy = new While(new Condition(e1, op, e2), res);
			return willy;
		}
		else if (currToken.equals("FOR"))
		{
			eat("FOR");
			String checker = currToken;
			eat(checker);
			eat(":=");
			Expression val = parseExp();
			Assignment a = new Assignment(val, checker);
			eat("TO");
			Expression limit = parseExp();
			eat("DO");
			Statement res = parseStatement();
			Condition c = new Condition(val, "<", limit);
			Statement forey = new For(c, res, a, checker, limit);
			return forey;
		}
		else if (currToken.equals("READLN"))
		{
			eat(currToken);
			eat("(");
			String var = currToken;
			eat(var);
			Statement read = new Readln(var);
			eat(")");
			eat(";");
			return read;
		}
		else if (currToken.equals("BEGIN"))
		{
			eat("BEGIN");
			ArrayList<Statement> smnts = new ArrayList<Statement>();
			while(!currToken.equals("END"))
			{
				smnts.add(parseStatement());
			}
			Statement block = new Block(smnts);
			eat ("END");
			eat(";");
			return block;	
		}
		else if (isIdentifier(currToken))
		{
			String x = currToken;
			eat(x);
			eat(":=");
			Expression val = parseExp();
			eat(";");
			return new Assignment(val, x);  
		}
		return null;
	}
	
	/**
	 * Parses the whole text file under a Program object.
	 * @throws IOException if the BufferedReader in the scanner runs into an error.
	 * @throws ScanErrorException if a scanning error occurs.
	 * @return a new Program object that will does the whole file when executed.
	 */
	public Program parseProgram() throws ScanErrorException, IOException
	{
		List<String> vars = new ArrayList<String>();
		List<String> locals = new ArrayList<String>();
		while (currToken.equals("VAR"))
		{
			eat("VAR");
			while (!currToken.equals(";"))
			{
				vars.add(currToken);
				eat(currToken);
				if (currToken.equals(","))
					eat(",");
			}
			eat(";");
		}
		List<ProcedureDeclaration> prods = new ArrayList<ProcedureDeclaration>();
		List<String> params = new ArrayList<String>();
		while (currToken.equals("PROCEDURE"))
		{
			eat("PROCEDURE");
			String id = currToken;
			eat(id);
			eat("(");
			while (!currToken.equals(")"))
			{
				params.add(currToken);
				eat(currToken);
				if (currToken.equals(","))
					eat(",");
			}
			eat(")");
			eat(";");
			if (currToken.equals("VAR"))
			{
				eat("VAR");
				while (!currToken.equals(";"))
				{
					locals.add(currToken);
					eat(currToken);
					if (currToken.equals(","))
						eat(",");
				}
				eat(";");
			}
			Statement res = parseStatement();
			prods.add(new ProcedureDeclaration(id, res, params, locals));
		}
		Statement after = parseStatement();
		return new Program(vars, prods, after);
	}
	
	/**
	 * The main method that creates a parser for the ParserTest.txt file. 
	 * Interprets the file and converts the code to MIPS in a different fi;le.
	 * @param args the arguments from the command line.
	 * @throws IOException if the BufferedReader in the scanner runs into an error.
	 * @throws ScanErrorException if a scanning error occurs.
	 */
	public static void main(String [] args) throws IOException, ScanErrorException
	{
		FileInputStream inStream = new FileInputStream(new File("test.txt"));
    	Scanner sc = new Scanner(inStream);
		Parser p = new Parser(sc);
		Environment env = new Environment(null);
		Statement parse = p.parseProgram();
		parse.exec(env);
		parse.compile(new Emitter("/Users/RakeshNori/Desktop/2017-18 School Year/Compilers & Interpreters/Code Generation Lab/result.asm"));
		
		
	}
}
