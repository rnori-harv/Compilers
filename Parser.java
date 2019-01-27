package parser;

import java.io.*;

import ast.Operation;
import ast.Display;
import ast.Expression;
import ast.If;
import ast.Number;
import ast.Read;
import ast.Statement;
import ast.Variable;
import ast.While;
import ast.Assignment;
import environment.Environment;
import java.util.ArrayList;
import java.util.List;

import scanner.ScanErrorException;
import scanner.Scanner;

/**
 * A top-down recursive descent parser that interprets the SIMPLE grammmar and
 * looks ahead tokens.
 * 
 * @author Rakesh Nori
 * @version 6/4/2018
 *
 */
public class Parser
{
    Scanner sc;
    static String currToken;

    /**
     * The constructor for a parser that takes in a scanner and creates a
     * current Token for parsing through and a map to store variables.
     * 
     * @param s the scanner being passed through into the constructor.
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
     * @throws ScanErrorException if a scanning error occurs.
     */
    public Parser(Scanner s) throws IOException, ScanErrorException
    {
        sc = s;
        currToken = sc.nextToken();
    }

    /**
     * moves one token ahead in the Scanner. Also checks if the string in the
     * parameter is the expected one in currToken.
     * 
     * @param str the String that is expected to be in currToken
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
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
            throw new IllegalArgumentException("Expected " + str + ", but had " 
                                                + currToken + " instead.");
    }

    /**
     * Parses through a value, accounting for parentheses also.
     * 
     * @precondition current token is an integer.
     * @postcondition the number has been eaten.
     * @return the parsed Integer or a variable in the form of an Expression.
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
     * @throws ScanErrorException if a scanning error occurs.
     */
    private Expression parseValue() throws ScanErrorException, IOException
    {
        int num = 0;
        if (currToken.equals("("))
        {
            eat(currToken);
            Expression val = parseExpr();
            eat(")");
            return val;
        }
        else if (isIdentifier(currToken))
        {
            String curr = currToken;
            eat(curr);
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
     * Parses through a NegExpr from the grammar, accounting for negative signs.
     * 
     * @return the Expression representing the NegExpr.
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
     * @throws ScanErrorException if a scanning error occurs.
     */
    public Expression parseNegExpr() throws ScanErrorException, IOException
    {
        if (currToken.equals("-"))
        {
            eat(currToken);
            return new Operation("-", new Number(0), parseValue());
        }
        else
        {
            return parseValue();
        }
    }

    /**
     * Accounts for the * and / signs in the expression.
     * 
     * @return an Expression as a Operation or NegExpr
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
     * @throws ScanErrorException if a scanning error occurs.
     */
    public Expression parseMultExpr() throws IOException, ScanErrorException
    {
        Expression init = parseNegExpr();
        while (currToken.equals("*") || currToken.equals("/"))
        {
            if (currToken.equals("*"))
            {
                eat("*");
                init = new Operation("*", init, parseMultExpr());
            }
            else
            {
                eat("/");
                init = new Operation("/", init, parseMultExpr());
            }
        }
        return init;
    }

    /**
     * Parses an Expression and accounts for + or - operators. Uses
     * parseMultExpr as a helper.
     * 
     * @return Operation object representing the value of the term when
     *         evaluated.
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
     * @throws ScanErrorException if a scanning error occurs.
     */
    public Expression parseAddExpr() throws ScanErrorException, IOException
    {
        Expression init = parseMultExpr();
        while (currToken.equals("+") || currToken.equals("-"))
        {
            if (currToken.equals("+"))
            {
                eat("+");
                init = new Operation("+", init, parseAddExpr());
            }
            else
            {
                eat("-");
                init = new Operation("-", init, parseAddExpr());
            }
        }
        return init;
    }

    /**
     * parseSt1 deals with the reading in values for a given variable.
     * 
     * @return the Read statement that will read in a value when executed.
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
     * @throws ScanErrorException if a scanning error occurs.
     */
    public Statement parseSt1() throws ScanErrorException, IOException
    {
        if (currToken.equals("read"))
        {
            eat("read");
            String x = currToken;
            eat(x);
            return new Read(x);
        }
        return null;
    }

    /**
     * Deals with the else portion in an If statement.
     * 
     * @return the sub-Program that corresponds to the body of the else
     *         statement.
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
     * @throws ScanErrorException if a scanning error occurs.
     */
    public List<Statement> parseSt2() throws ScanErrorException, IOException
    {
        if (currToken.equals("else"))
        {
            eat("else");
            return parseProgram();
        }
        return null;
    }

    /**
     * parses an Expression, accounting for all the operations by using
     * parseTerm and parseFactor.
     * 
     * @return the Operation representing the value of the Expression when
     *         evaluated.
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
     * @throws ScanErrorException if a scanning error occurs.
     */
    public Expression parseExpr() throws ScanErrorException, IOException
    {
        Expression init = parseAddExpr();

        while (isRelOp(currToken))
        {
            String rel = currToken;
            eat(currToken);
            init = new Operation(rel, init, parseAddExpr());
        }
        return init;
    }

    /**
     * Checks if the String is a Relative Operator.
     * 
     * @param str the String being checked.
     * @return true if the string is a RelOp; otherwise, false.
     */
    public boolean isRelOp(String str)
    {
        return (str.equals("<") || str.equals(">") || str.equals("<>") 
                || str.equals("<=") || str.equals(">=") || str.equals("="));

    }

    /**
     * Checks if a given token is an identifier or not.
     * 
     * @param s the token being checked for being an identifier
     * @return true if it is an identifier; otherwise, false.
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
     * Parses all the possible Statements and prints the output.
     * 
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
     * @throws ScanErrorException if a scanning error occurs.
     * @return A single Statement that follows one of the possible paths in the
     *         grammar
     */
    public Statement parseStatement() throws ScanErrorException, IOException
    {
        while (!(currToken.equals("end")) || currToken.equals("EOF"))
        {
            if (currToken.equals("display"))
            {
                eat("display");
                Expression n = parseExpr();
                Statement res = parseSt1();
                // System.out.println(currToken);
                return new Display(n, res);
            }
            else if (currToken.equals("if"))
            {
                eat("if");
                Expression a = parseExpr();
                eat("then");
                List<Statement> follow = parseProgram();
                List<Statement> second = parseSt2();
                return new If(a, follow, second);
            }
            else if (currToken.equals("while"))
            {
                eat("while");
                Expression e1 = parseExpr();
                eat("do");
                List<Statement> go = parseProgram();
                return new While(e1, go);
            }
            else if (currToken.equals("assign"))
            {
                eat("assign");
                String x = currToken;
                eat(x);
                eat("=");
                Expression val = parseExpr();
                return new Assignment(val, x);
            }
        }
        return null;
    }

    /**
     * Parses the whole text file under a Program object.
     * 
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
     * @throws ScanErrorException if a scanning error occurs.
     * @return a list of statements to be executed.
     */
    public List<Statement> parseProgram() throws ScanErrorException, IOException
    {
        List<Statement> all = new ArrayList<Statement>();
        while ((currToken.equals("display") || currToken.equals("assign") || 
               currToken.equals("while") || currToken.equals("if")))
        {
            all.add(parseStatement());
        }
        if (currToken.equals("end"))
            eat("end");
        return all;
    }

    /**
     * The main method that creates a parser for the ParserTest.txt file.
     * Interprets the file and converts the code to MIPS in a different fi;le.
     * 
     * @param args the arguments from the command line.
     * @throws IOException if the BufferedReader in the scanner runs into an
     *             error.
     * @throws ScanErrorException if a scanning error occurs.
     */
    public static void main(String[] args) throws IOException, ScanErrorException
    {
        FileInputStream inStream = new FileInputStream(new File("simple1.txt"));
        Scanner sc = new Scanner(inStream);
        Parser p = new Parser(sc);
        Environment env = new Environment(null);
        List<Statement> parse = p.parseProgram();
        for (Statement now : parse)
            now.exec(env);
        // parse.exec(env);
        // parse.compile(new Emitter("/Users/RakeshNori/Desktop/2017-18 School
        // Year/Compilers & Interpreters/Code Generation Lab/result.asm"));

    }
}
