package fr.amapj.model.engine.db;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import fr.amapj.service.engine.appinitializer.ServletParameter;


/**
 * Paramètres de configuration si base externe
 * 
 */
public class ExternalDbConf
{
	// name
	private List<String> names =  new ArrayList<>();
	
	// url
	private List<String> url= new ArrayList<>();
		
	// user
	private List<String> user= new ArrayList<>();
	
	// user
	private List<String> password= new ArrayList<>();
	
	
	public void load(ServletParameter param)
	{
		String multi = param.read("database.external.multi-tenancy");
		
		if ((multi!=null) && multi.equalsIgnoreCase("on"))
		{
			String name = param.read("database.external.name");
			StringTokenizer st = new StringTokenizer(name,",");
			while (st.hasMoreElements())
			{
				names.add(st.nextToken());
			}
			
			int size = names.size();
			for (int i = 0; i < size; i++)
			{
				url.add(param.read("database.external.url"+i));
				
				user.add(param.read("database.external.user"+i));
				
				password.add(param.read("database.external.password"+i));
			}
			
		}
		else
		{
			names.add("amap1");
			
			url.add(param.read("database.external.url"));
			
			user.add(param.read("database.external.user"));
			
			password.add(param.read("database.external.password"));
		}
			
	}


	public List<String> getNames()
	{
		return names;
	}


	public List<String> getUrl()
	{
		return url;
	}


	public List<String> getUser()
	{
		return user;
	}


	public List<String> getPassword()
	{
		return password;
	}


	

	
	
	
	
}
