package fr.amapj.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * Simplifie la gestion des stacks d'erreurs
 * 
 *  
 */
public class StackUtils 
{
	
	public static final String SEP = System.getProperty("line.separator");

	/**
	 * 
	 */
	static public void popStack(List<String> messages,Throwable e1)
	{
		if (e1==null)
		{
			return ;
		}
		
		messages.add(e1.getClass().toString()+" : "+e1.getMessage());
		StackTraceElement[] elts = e1.getStackTrace();
		for (int i = 0; i < elts.length; i++)
		{
			messages.add("at "+elts[i].toString());
		}
		
		if (e1.getCause()!=null)
		{
			messages.add("Cause : ");
			popStack(messages,e1.getCause());
		}
	}
	
	/**
	 * Retourne la stack complete sous forme de String
	 * 
	 * @param e1
	 */
	static public String asString(Throwable e1)
	{
		StringBuffer result = new StringBuffer();
		List<String> messages = new ArrayList<String>();
		popStack(messages,e1);
		
		for (String str : messages)
		{
			result.append(str);
			result.append(SEP);
		}
		return result.toString();
	}
	
	
	/**
	 * Retourne la stack complete sous forme de String
	 * 
	 * @param e1
	 */
	static public String getConstraints(ConstraintViolationException e)
	{
		StringBuffer result = new StringBuffer();
		Set<ConstraintViolation<?>> set = e.getConstraintViolations();
		
		for (ConstraintViolation<?> constraintViolation : set)
		{
			result.append("Le champ ");
			result.append(constraintViolation.getPropertyPath());
			result.append(" : ");
			result.append(constraintViolation.getMessage());
			result.append(". Valeur incorrecte : ");
			result.append(constraintViolation.getInvalidValue());
			
			result.append(SEP);
		}
		
		return result.toString();
	}
	

}
