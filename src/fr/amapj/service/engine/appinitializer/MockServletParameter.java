package fr.amapj.service.engine.appinitializer;

import java.util.Properties;


/**
 * Permet de lire les paramètres dans le servlet context
 * 
 */
public class MockServletParameter implements ServletParameter
{
	
	private Properties prop;
	
	public MockServletParameter(Properties prop)
	{
		this.prop = prop;
	}
	
	

	public String read(String paramName)
	{
		return prop.getProperty(paramName);
	}
	
	public String read(String paramName,String defaultValue)
	{
		return prop.getProperty(paramName,defaultValue);
	}
}
