package environment;

import java.util.HashMap;
import ast.ProcedureDeclaration;

/**
 * The Environment class is responsible for Storing and setting the values of
 * Variables through its HashMap. It uses child and parent environments 
 * to establish a hierarchy of local and global variables.
 * @author Rakesh Nori
 * @version 3/20/2018
 */
public class Environment 
{
	private HashMap<String, Integer> map;
	private HashMap<String, ProcedureDeclaration> proMap;
	private Environment padre;
	/**
	 * Instantiates the Map & procedure Map object by making 
	 * a new HashMap that uses String and Integers for the Key Value pair.
	 * @param parent the parent Environment of the current one.
	 */
	public Environment(Environment parent)
	{
		 map = new HashMap<String, Integer>();
		 proMap = new HashMap<String, ProcedureDeclaration>();
		 padre = parent;
	}
	
	/**
	 * Gets the parent environment.
	 * @return the parent environment.
	 */
	public Environment getPadre()
	{
		return padre;
	}
	
	/**
	 * Changes the parent environment.
	 * @param other the replacement environment.
	 */
	public void setPadre(Environment other)
	{
		padre = other;
	}
	
	/**
	 * Retrieves the map of the environment.
	 * @return the HashMap containing variables and their values 
	 * 		   (not the procedure map).
	 */
	public HashMap<String, Integer> getMap()
	{
		return map;
	}
	
	/**
	 * Associates the given variable name with the given value.
	 * @param variable name of the variable
	 * @param value its value of an Integer type.
	 */
	public void setVariable(String variable, int value)
	{
		if (!map.containsKey(variable))
		{
			Environment lol = this;
			while (lol.getPadre() != null)
			{
				lol = lol.getPadre();
			}
			lol.declareVariable(variable, value);
		}
		else
			map.put(variable, value);
		
	}
	
	/**
	 * Declares the given variable name with the given value
	 * @param variable name of the variable
	 * @param value its value of an Integer type.
	 */
	public void declareVariable(String variable, int value)
	{
		map.put(variable, value);
	}
	
	/**
	 * Associates the given variable name with the given value in the map.
	 * @param variable name of the variable
	 * @param value The statement value. 
	 */
	public void proSet(String variable, ProcedureDeclaration value)
	{
		if (padre != null)
			padre.proSet(variable, value);
		else
			proMap.put(variable, value);
	}
	
	/**
	 * Gets the value of the variable corresponding to its name.
	 * @param name the name of the variable that is to be retrieved.
	 * @return the Integer value of the String key that was given.
	 */
	public int getVariable(String name)
	{
		if (map.containsKey(name))
		{
			return map.get(name);
		}
		else if (padre != null)
		{

			return padre.getVariable(name);
		}
		throw new IllegalArgumentException("Variable for the corresponding String "
				+ "does not exist.");
	}
	
	/**
	 * Gets the value of the variable corresponding to its name 
	 * from the procedures HashMap.
	 * @param name the name of the variable that is to be retrieved.
	 * @return the Statement value of the String key that was given.
	 */
	public ProcedureDeclaration proGet(String name)
	{
		if (padre != null)
			return padre.proGet(name);
		return proMap.get(name);
	}
	
}
