package fr.amapj.service.engine.appinitializer;



/**
 * Permet de lire les paramètres dans le servlet context
 * 
 */
public interface ServletParameter
{
	public String read(String paramName);
	
	public String read(String paramName,String defaultValue);
}
