package fr.amapj.model.engine.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerConfiguration;
import org.hsqldb.server.ServerConstants;

import fr.amapj.common.StackUtils;
import fr.amapj.model.engine.transaction.DbUtil;
import fr.amapj.view.engine.ui.AppConfiguration;

public class DbManager
{
	
	private final static Logger logger = Logger.getLogger(DbManager.class.getName());
	
	// Reference vers le serveur base de données
	private Server server;

	public void startDatabase()
	{
		AppConfiguration conf = AppConfiguration.getConf();
		
		
		if (conf.getInternalDbConf()!=null)
		{
			configureJPA(conf.getInternalDbConf());
			startDataBase(conf.getInternalDbConf());
		}
		else
		{
			configureJPA(conf.getExternalDbConf());
			startDataBase(conf.getExternalDbConf());
		}
		
	}


	private void configureJPA(InternalDbConf conf)
	{
		List<String> urls = conf.getUrl();
		List<String> names = conf.getNames();
		int size = names.size();
		for (int i = 0; i < size; i++)
		{
			logger.info("Nom de la base : "+names.get(i)+" Numero de la base : "+(i+1)+"/"+size);
			DbUtil.addDataBase(i,names.get(i), urls.get(i), conf.getUser(), conf.getPassword());
		}
	}
	
	private void configureJPA(ExternalDbConf conf)
	{
		List<String> names = conf.getNames();
		int size = names.size();
		for (int i = 0; i < size; i++)
		{
			logger.info("Nom de la base : "+names.get(i)+" Numero de la base : "+(i+1)+"/"+size);
			DbUtil.addDataBase(i,names.get(i), conf.getUrl().get(i), conf.getUser().get(i), conf.getPassword().get(i));
		}
		
	}


	private void startDataBase(InternalDbConf conf)
	{
		HsqlProperties argProps = new HsqlProperties();

		// 
		List<String> names = conf.getNames();
		int size = names.size();
		for (int i = 0; i < size; i++)
		{
			argProps.setProperty("server.database."+i, "file:"+conf.getContentDirectory()+"/"+names.get(i));
			argProps.setProperty("server.dbname."+i, names.get(i));
		}
		
		argProps.setProperty("server.no_system_exit", "true");
		argProps.setProperty("server.port", conf.getPort());
		

		ServerConfiguration.translateAddressProperty(argProps);

		// finished setting up properties;
		server = new Server();

		try
		{
			server.setProperties(argProps);
		} 
		catch (Exception e)
		{
			logger.severe("Impossible de paramétrer correctement la base de données" + StackUtils.asString(e));
			throw new RuntimeException("Impossible de démarrer la base");
		}

		server.start();
		server.checkRunning(true);
		
	}

	private void startDataBase(ExternalDbConf externalDbConf)
	{
		// Nothing to do
	}

	public void stopDatabase()
	{
		AppConfiguration conf = AppConfiguration.getConf();
		
		if (conf.getInternalDbConf()!=null)
		{
			stopDatabase(conf.getInternalDbConf());
		}
		else
		{
			stopDatabase(conf.getExternalDbConf());
		}
		
	}
		

	private void stopDatabase(ExternalDbConf externalDbConf)
	{
		// Notning to do
	}


	private void stopDatabase(InternalDbConf conf)
	{
		List<String> urls = conf.getUrl();
		for (String url : urls)
		{
			stopOneBase(url,conf);
		}
		
		// On attend ensuite la fin de la base (attente max de 15 secondes) 
		for(int i=0;i<15;i++)
		{
			if (server.getState()==ServerConstants.SERVER_STATE_SHUTDOWN)
			{
				return ;
			}
			try
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e)
			{
				// Nothing to do
			}
			logger.info("Attente de l'arret complet de la base "+i+"/15");
		}
		
	}


	private void stopOneBase(String url, InternalDbConf conf)
	{
		try
		{
			Connection conn = DriverManager.getConnection(url,conf.getUser(),conf.getPassword());
			Statement st = conn.createStatement();
			st.execute("SHUTDOWN");
			conn.close();
		} 
		catch (SQLException e)
		{
			// Do nothing, only log
			logger.severe("Impossible d'arreter correctement la base de données "+url+ StackUtils.asString(e));
		}   
		
	}
	
}
