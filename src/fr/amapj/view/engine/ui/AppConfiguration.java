package fr.amapj.view.engine.ui;

import java.util.Properties;
import java.util.logging.Logger;

import fr.amapj.model.engine.db.ExternalDbConf;
import fr.amapj.model.engine.db.InternalDbConf;
import fr.amapj.service.engine.appinitializer.MockServletParameter;
import fr.amapj.service.engine.appinitializer.ServletParameter;

/**
 * Paramètres de configuration de l'application
 * 
 */
public class AppConfiguration
{
	private final static Logger logger = Logger.getLogger(AppConfiguration.class.getName());

	static private AppConfiguration mainInstance;

	static public AppConfiguration getConf()
	{
		if (mainInstance == null)
		{
			throw new RuntimeException("Vous devez d'abord charger les parametres avec la methode load");
		}
		return mainInstance;
	}

	/**
	 * Permet le chargement des parametres
	 */
	static public void load(ServletParameter param)
	{
		if (mainInstance != null)
		{
			throw new RuntimeException("Impossible de charger deux fois les parametres");
		}

		mainInstance = new AppConfiguration();
		mainInstance.loadInternal(param);
	}

	private AppConfiguration()
	{
	}

	// Est on est mode de test ?
	private String testMode;

	// Répertoire pour la sauvegarde de la base
	private String backupDirectory;
	
	// Nom de l'onglet dans le navigateur
	private String pageTitle;

	//
	private InternalDbConf internalDbConf = null;

	private ExternalDbConf externalDbConf = null;

	private void loadInternal(ServletParameter param)
	{

		testMode = param.read("test");
		
		pageTitle = param.read("pageTitle", "AMAP");

		// TODO verifier que c'est bien un directory
		backupDirectory = param.read("database.backupdir");

		boolean isInternal = param.read("database.type", "internal").equalsIgnoreCase("internal");
		if (isInternal)
		{
			internalDbConf = new InternalDbConf();
			internalDbConf.load(param);
		}
		else
		{
			externalDbConf = new ExternalDbConf();
			externalDbConf.load(param);
		}

	}

	public String getTestMode()
	{
		return testMode;
	}

	public String getBackupDirectory()
	{
		return backupDirectory;
	}

	public String getPageTitle()
	{
		return pageTitle;
	}

	public InternalDbConf getInternalDbConf()
	{
		return internalDbConf;
	}

	public ExternalDbConf getExternalDbConf()
	{
		return externalDbConf;
	}

	/**
	 * Permet de créer une configuration pour les tests
	 * 
	 * ATTENTION : cette méthode doit être appelée uniquement par TestTools.init()
	 */
	public static void initializeForTesting()
	{
		mainInstance = new AppConfiguration();

		Properties prop = new Properties();
		prop.put("database.external.url", "jdbc:hsqldb:hsql://127.0.0.1/amap1");
		prop.put("database.external.user", "SA");
		prop.put("database.external.password", "");

		MockServletParameter param = new MockServletParameter(prop);
		
		ExternalDbConf conf = new ExternalDbConf();
		conf.load(param);
		mainInstance.externalDbConf = conf;
	}

}
