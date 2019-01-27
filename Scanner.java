package scanner;
import java.io.*;

/**
 * Scanner is a simple scanner for Compilers and Interpreters (2014-2015) lab exercise 1
 * @author Rakesh Nori
 * @version 4/6/2018
 * Usage:
 * Use the Scanner to generate lexemes in a given file or String.
 *
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;
    private int line;
    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
        line = 1;
    }
    /**
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }
    /**
     * Processes the next char for in the input stream 
     * by using the BufferedReaders read method and TypeCasting.
     */
    private void getNextChar()
    {
    	int val = 0;
		try {
			val = in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally
		{
			
		}
    	if (val == -1 || (char)(val) == '.')
    	{
    		eof = true;
    	}
    	else
    	{
    		currentChar = (char)val;
    	}
    	
    	
    		
    }
    /**
     * Checks if the value of currentChar is correct.
     * @param expected the expected value for currentChar.
     * @throws ScanErrorException if the currentChar was not expected.
     * @postcondition the currentChar is correct, 
     * 				  or an error has been thrown.
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (expected != currentChar)
        	throw new ScanErrorException("Illegal character - expected " + expected + 
        			" and found " + currentChar);
        else
        	getNextChar();
    }
    
    /**
     * checks if the given char is a digit.
     * @param input the char being checked for being a digit [0, 9].
     * @return true if its value is between 
     * 		   the ascii values of 0 and 9 (inclusive), otherwise;
     * 		   false.
     */
    public static boolean isDigit(char input)
    {
    	return input >= '0' && input <= '9';
    }
    
    /**
     * Checks if the given char is a digit. Since the ascii values of 
     * the uppercase and lower case letters are not the same, 
     * it checks if its in either of the ranges separately 
     * in order to prevent any errors.
     * @param input the char being checked for being a letter or not.
     * @return true if the char is a letter; otherwise,
     * 		   false.
     */
    public static boolean isLetter(char input)
    {
    	return (input >= 'A' && input <= 'Z') || (input >= 'a' && input <= 'z');
    }
    
    /**
     * Checks if the input is a White Space by comparing it 
     * to the values that are a white space given into the pdf.
     * @param input the char being checked for being a whitespace.
     * @return true if the value is a whitespace; otherwise, 
     * 		   false.
     */
    public boolean isWhiteSpace(char input)
    {
    	if (input == ' '|| input == '\t' || input == '\r' || input == '\n')
    	{
    		if (input == '\n')
    			line++;
    		return true;
    	}
    	return false;	
    }
    
    /**
     * Checks if the input is an Operand.
     * @param input the char being checked for being an operand.
     * @return true if it is an operand; otherwise,
     * 		   false.
     */
    public static boolean isOperand(char input)
    {
    	return (input == '=' || input == '+' || input == '-' 
    			|| input == '*' || input == '/' || input == '%' ||
    			input == '(' || input == ')' || input == ';' || input == ':'
    			|| input == '<' || input == '>'|| input == ',');
    }
    
    /**
     * checks if the input char and the one after it are both '/'
     * @param input the char that possibly can be a comment.
     * @return true if the char and the one after it are '/', otherwise;
     * 		   false.
     * @throws IOException if problems occur with marking the stream.
     * @throws ScanErrorException if eat gets an unexpected char.
     */
    public boolean isSingleComment(char input) throws IOException, ScanErrorException
    {
    	if (input == '/')
    	{
    		in.mark(2);
    		eat(currentChar);
    		if (currentChar == '/')
    		{
    			in.reset();
    			currentChar = '/';
    			return true;
    		}
    		in.reset();
    		currentChar = '/';
    	}
    	return false;
    }
    
    /**
     * Checks if the next token will be an opening block comment.
     * @param input the last char that the BufferedReader read.
     * @return true if it is a block comment; otherwise,
     * 		   false.
     * @throws IOException if errors occur with marking and resetting 
     * 		   the bufferedReader.
     * @throws ScanErrorException if eat gets an unexpected value.
     */
    public boolean isBlockComment(char input) throws IOException, ScanErrorException
    {
    	if (input == '/')
    	{
    		in.mark(1);
    		eat(currentChar);
    		if (currentChar == '*')
    		{
    			return true;
    		}
    		in.reset();
    		currentChar = '/';
    	}
    	else if (input == '(')
    	{
    		in.mark(1);
    		eat(currentChar);
    		if (currentChar == '*')
    			return true;
    		in.reset();
    		currentChar = '(';
    	}
    	return false;
    }
    
    /**
     * Checks if the next token is a closing block comment.
     * @param input the last read char from the BufferedReader.
     * @return true if it is a closing block comment; otherwise,
     * 		   false.
     * @throws IOException if errors occur with marking and resetting 
     * 		   the BufferedReader
     * @throws ScanErrorException if eat gets an unexpected value. 
     */
    public boolean isClosingBlockComment(char input) throws IOException, ScanErrorException
    {
    	if (input == '*')
    	{
    		in.mark(1);
    		eat(currentChar);
    		if (currentChar == '/' || currentChar == ')')
    		{
    			return true;
    		}
    		in.reset();
    		currentChar = '*';
    	}
    	return false;
    }
    /**
     * checks if the Scanner is at the end of the file.
     * @return true if the Scanner has not reached the end of the file; otherwise, 
     * 		   false.
     */
    public boolean hasNext()
    {
       return !eof;
    }
    
    /**
     * scanOperand is  a helper for nextToken by generating
     * an Operand Token.
     * @return the operand present in the file as a String.
     * @throws ScanErrorException if eat retrieves an unexpected char.
     * @precondition currentChar must be an operand.
     */
    private String scanOperand() throws ScanErrorException, IOException
    {
    	String answer = "" + currentChar;
    	if (currentChar == ':')
    	{
    		in.mark(1);
    		eat(currentChar);
    		if (currentChar == '=')
    		{
    			answer += currentChar;
    			eat(currentChar);
    		}
    		else
    		{
    			in.reset();
    			currentChar = ':';
    			eat(currentChar);
    		}
    	}
    	else if (currentChar == '<')
    	{
    		in.mark(1);
    		eat(currentChar);
    		if (currentChar == '=')
    		{
    			answer += currentChar;
    			eat(currentChar);
    		}
    		else if (currentChar == '>')
    		{
    			answer += currentChar;
    			eat(currentChar);
    		}
    		else
    		{
    			in.reset();
    			currentChar = '<';
    			eat(currentChar);
    		}
    	}
    	else if (currentChar == '>')
    	{
    		in.mark(1);
    		eat(currentChar);
    		if (currentChar == '=')
    		{
    			answer += currentChar;
    			eat(currentChar);
    		}
    		else
    		{
    			in.reset();
    			currentChar = '>';
    			eat(currentChar);
    		}
    	}
    	else
    		eat(currentChar);
    	return answer;
    }
    
    /**
     * scanIdentifier is a helper for nextToken by generating an 
     * Identifier token. 
     * @return the identifier present in the file as a String.
     * @throws ScanErrorException if eat retrieves an unexpected char.
     * @precondition currentChar must be a letter.
     */
    private String scanIdentifier() throws ScanErrorException
    {
    	String answer = "" + currentChar;
    	eat(currentChar);
    	while ((isDigit(currentChar) || isLetter(currentChar)) && hasNext())
    	{
    		answer += currentChar;
    		eat(currentChar);
    	}
    	if (hasNext() == false)
    	{
    		answer = answer + "\nEOF";
    	}
    	return answer;
    }
    
    /**
     * scanNumber is a helper for the nextToken method by generating 
     * a Number token.
     * @return the number that is present in the file as a String.
     * @throws ScanErrorException if eat retrieves an unexpected char.
     * @precondition the currentChar must be a digit.
     */
    private String scanNumber() throws ScanErrorException
    {
    	String answer = "" + currentChar;
    	eat(currentChar);
    	while (isDigit(currentChar) && hasNext())
    	{
    		answer += currentChar;
    		eat(currentChar);
    	}
    	if (hasNext() == false)
    	{
    		answer = answer + "\nEOF";
    	}
    	return answer;
    }
    /**
     * nextToken finds the next token in a String format from the InputStream.
     * @return the nextToken generated (Identifier, Number, Operand) as a String.
     * @throws ScanErrorException if an illegal character such as '$' 
     *   	   is present.
     * @throws IOException if the BufferedReader's mark or reset method fail.
     * @throws ScanErrorException if the eat method encounters an unexpected character.
     */
    public String nextToken() throws ScanErrorException, IOException
    {
        while(isWhiteSpace(currentChar) && hasNext())
        	eat(currentChar);
        while (isSingleComment(currentChar) || isBlockComment(currentChar))
        {
        	if (isSingleComment(currentChar))
        	{
        		while (currentChar != '\n')
        			eat(currentChar);
        	}
        	else
        	{
        		while (!isClosingBlockComment(currentChar))
        			eat(currentChar);
        		eat(currentChar);
        	}
        	while(isWhiteSpace(currentChar) && hasNext())
        		eat(currentChar);
        }
        if (!hasNext())
        	return "EOF";
        else if (isLetter(currentChar))
        	return scanIdentifier();
        else if (isDigit(currentChar))
        	return scanNumber();
        else if (isOperand(currentChar))
        	return scanOperand();
        else
        	throw new ScanErrorException("Illegal character on line " + line + ": " + currentChar);
    }  
    
    /**
     * The main method for the Scanner class that tests with text files.
     * @param args list of arguments from the command line
     * @throws ScanErrorException if an illegal character is present in the text file.
     * @throws IOException if the BufferedReader encounters a problem in mark and reset.
     */
    public static void main(String [] args) throws ScanErrorException, IOException
    {
    	FileInputStream inStream = new FileInputStream(new File("ScannerTest.txt"));
    	Scanner sc = new Scanner(inStream);
    	while (sc.hasNext())
    	{
    		System.out.println(sc.nextToken());
    	}
    }
}
