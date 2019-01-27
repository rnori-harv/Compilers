package ast;

import java.util.Scanner;
import environment.Environment;

/**
 * The Read class uses user input to instantiate a variable.
 * 
 * @author Rakesh Nori
 * @version 3/26/20188
 *
 */
public class Read extends Statement
{
    private String name;
    private int val;

    /***
     * Instantiates a Read object.
     * 
     * @param nm the name of the variable.
     */
    public Read(String nm)
    {
        name = nm;
    }

    /**
     * Gives a value for the variable and passes it into the environment.
     * The scanner cannot be closed due to the possibility that Read may be 
     * called more than once.
     * 
     * @param env the Environment has a map that stores variables.
     */
    public void exec(Environment env)
    {
        System.out.println("Input int val for " + name);
        Scanner sc = new Scanner(System.in);
        val = sc.nextInt();
        env.setVariable(name, val);
       // sc.close();

    }
}
