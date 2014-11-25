package fr.amapj.model.engine.db;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import fr.amapj.service.engine.appinitializer.ServletParameter;


/**
 * Paramètres de configuration si base interne
 * 
 */
public class InternalDbConf
{
	// Répertoire pour le contenu de la base
	private String contentDirectory;
	
	// Port d'écoute de la base
	private int port;
	
	// noms des bases (dans le cas multi base)
	private List<String> names = new ArrayList<>();
	
	// user
	private String user;
	
	// user
	private String password;
	
	
	public void load(ServletParameter param)
	{
		// TODO verifier que c'est bien un directory
		contentDirectory = param.read("database.internal.dir");
	
		port =  Integer.parseInt(param.read("database.internal.port"));

		String name = param.read("database.internal.name");
		StringTokenizer st = new StringTokenizer(name,",");
		while (st.hasMoreElements())
		{
			names.add(st.nextToken());
		}
		
		user = param.read("database.internal.user");
		
		password = param.read("database.internal.password");
			
	}

	
	public List<String> getUrl()
	{
		List<String> urls = new ArrayList<>();
		for (String name : names)
		{
			urls.add("jdbc:hsqldb:hsql://localhost:"+port+"/"+name);
		}
		return urls;
	}
	
	
	/*
	 * Getters and setters
	 */

	public String getContentDirectory()
	{
		return contentDirectory;
	}


	public int getPort()
	{
		return port;
	}


	public List<String> getNames()
	{
		return names;
	}


	public String getUser()
	{
		return user;
	}


	public String getPassword()
	{
		return password;
	}

	
	
	
	
}
