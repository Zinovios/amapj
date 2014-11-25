package fr.amapj.common;

import java.util.List;

import fr.amapj.service.services.saisiepermanence.PermanenceUtilisateurDTO;

public class StringUtils
{
	public static String sansAccent(String s)
	{
		final String accents = "ÀÁÂÃÄÅàáâãäåÈÉÊËèéêëîïùû"; 
		final String letters = "AAAAAAaaaaaaEEEEeeeeiiuu"; 

		StringBuffer buffer = null;
		for (int i = s.length() - 1; i >= 0; i--)
		{
			int index = accents.indexOf(s.charAt(i));
			if (index >= 0)
			{
				if (buffer == null)
				{
					buffer = new StringBuffer(s);
				}
				buffer.setCharAt(i, letters.charAt(index));
			}
		}
		return buffer == null ? s : buffer.toString();
	}
	
	
	public static boolean equalsIgnoreCase(String s1,String s2)
	{
		if (s1==null)
		{
			s1 = "";
		}
		
		if (s2==null)
		{
			s2 = "";
		}
		
		return s1.equalsIgnoreCase(s2);
	}
	
	
	
	/**
	 * Convertit une liste d'objet en une String 
	 * Exemple : ls = [ "Bob" , "Marc" , "Paul" ]
	 * 
	 *  asString(ls,",") =>  "Bob,Marc,Paul"
	 * 
	 */
	public static String asString(List ls,String sep)
	{
		if (ls.size()==0)
		{
			return "";
		}
		
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < ls.size()-1; i++)
		{
			Object l = ls.get(i);
			str.append(l.toString());
			str.append(sep);
		}
		
		Object l = ls.get(ls.size()-1);
		str.append(l.toString());
		return str.toString();
	}
	
	
	public static String asHtml(String htmlContent)
	{
		if (htmlContent==null)
		{
			return null;
		}
		
		// Mise en place des <br/>
		htmlContent = htmlContent.replaceAll("\r\n", "<br/>");
		htmlContent = htmlContent.replaceAll("\n", "<br/>");
		htmlContent = htmlContent.replaceAll("\r", "<br/>");
		
		return htmlContent;
	}
	

}
