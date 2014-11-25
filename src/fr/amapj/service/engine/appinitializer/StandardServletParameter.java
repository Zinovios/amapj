package fr.amapj.service.engine.appinitializer;

import javax.servlet.ServletContext;


/**
 * Permet de lire les paramètres dans le servlet context
 * 
 */
public class StandardServletParameter implements ServletParameter
{
	private ServletContext servletContext;
	
	
	public StandardServletParameter(ServletContext servletContext)
	{
		this.servletContext = servletContext;
	}
	
	

	public String read(String paramName)
	{
		return servletContext.getInitParameter(paramName);
	}
	
	public String read(String paramName,String defaultValue)
	{
		String str= servletContext.getInitParameter(paramName);
		if ( (str==null) || (str.length()==0))
		{
			return defaultValue;
		}
		return str;
	}
}
