package fr.amapj.model.engine.transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import fr.amapj.service.services.session.SessionManager;

/**
 * Utilitaires divers pour l'accés à la base de données
 *
 */
public class DbUtil
{
	static private DbUtil mainInstance = new DbUtil();
	
	private Map<String, EntityManagerFactory> entityManagerFactorys;
	
	/*
	 * Permet le stockage du nom de la base dans le cas des démons
	 */
	private ThreadLocal<String> demonDbName = new ThreadLocal<String>();
	
	private DbUtil()
	{
		entityManagerFactorys = new ConcurrentHashMap<>();
	}
	
	static public void addDataBase(int dbNumber,String dbName,String url,String user,String password)
	{
		mainInstance.addDataBaseInternal(dbNumber,dbName,url, user, password);
	}
	
	
	/**
	 * Permet de créer un entity manager
	 * 
	 * Cette méthode doit être utilisée uniquement par TransactionHelper
	 * 
	 */
	static EntityManager createEntityManager()
	{
		String dbName = getCurrentDbName();
		
		EntityManagerFactory entityManagerFactory = mainInstance.entityManagerFactorys.get(dbName);
				
		EntityManager em = entityManagerFactory.createEntityManager();
		
		return em;
	}
	
	/**
	 * Retourne le nom de la base de données courante
	 */
	static public String getCurrentDbName()
	{
		// Le nom de la base provient soit d'une variable positionnée par le démon, soit du contexte de la session
		String dbName = mainInstance.demonDbName.get();
		if (dbName==null)
		{
			dbName = SessionManager.getDbName();
		}
		return dbName;
	}
	
	
	
	/**
	 * La factory est créée une seule fois, étant ressource consuming
	 */
	private void addDataBaseInternal(int puNubmber,String dbName,String url,String user,String password)
	{
		Map<String, Object> mp = new HashMap<String, Object>(); 
	
		mp.put("eclipselink.jdbc.platform","org.eclipse.persistence.platform.database.HSQLPlatform");
		mp.put("javax.persistence.jdbc.driver","org.hsqldb.jdbcDriver" );
		mp.put("javax.persistence.jdbc.url",url);
		mp.put(PersistenceUnitProperties.JDBC_USER, user);
		mp.put(PersistenceUnitProperties.JDBC_PASSWORD, password);
		mp.put("eclipselink.logging.level" ,"FINE" );
		mp.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.NONE);
		
		DecimalFormat df = new DecimalFormat("00");
		String puName = "pu"+df.format(puNubmber);
	
		EntityManagerFactory entityManagerFactory =  Persistence.createEntityManagerFactory(puName,mp);
		
		entityManagerFactorys.put(dbName, entityManagerFactory);

	
	}

	/**
	 * Indique si il existe une base de données avec ce nom
	 */
	public static boolean checkBbName(String dbName)
	{
		return mainInstance.entityManagerFactorys.containsKey(dbName);
	}
	
	/*
	 * Partie specificque pour les demons
	 */
	
	/**
	 * Permet d'indiquer le nom de la base sur laquelle s'execute le demon
	 */
	public static void setDbNameForDeamonThread(String dbName)
	{
		mainInstance.demonDbName.set(dbName);
	}
	
	/**
	 * Retourne la liste de toutes les bases, triées par ordre alphabetique
	 * @return
	 */
	public static List<String> getAllDbNames()
	{
		List<String> strs = new ArrayList<>(); 
		strs.addAll(mainInstance.entityManagerFactorys.keySet());
		Collections.sort(strs);
		return strs;
	}
	
	
}
