package fr.amapj.model.engine.ddl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;

/**
 * Cette classe permet de cr�er des roles dans la base de donn�es
 */
public class GenerateSqlSchema
{

	public void createData()
	{
		
		

		Map<String, Object> mp = new HashMap<String, Object>(); 
		
		
		
		mp.put("eclipselink.jdbc.platform","org.eclipse.persistence.platform.database.HSQLPlatform");
		
		
		
		mp.put("javax.persistence.jdbc.driver","org.hsqldb.jdbcDriver" );
		mp.put("javax.persistence.jdbc.url","jdbc:hsqldb:hsql://localhost/amap1" );
		mp.put(PersistenceUnitProperties.JDBC_USER, "SA");
		mp.put(PersistenceUnitProperties.JDBC_PASSWORD, "");
		mp.put("eclipselink.logging.level" ,"FINE" );
		
		mp.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.DROP_AND_CREATE);
		mp.put(PersistenceUnitProperties.DDL_GENERATION_MODE,PersistenceUnitProperties.DDL_SQL_SCRIPT_GENERATION );
		mp.put("eclipselink.create-ddl-jdbc-file-name","create-script.sql" );
		mp.put("eclipselink.application-location","db/");
		
		
		
		
		
		
		
		
	
		
		EntityManager em = Persistence.createEntityManagerFactory("pu00",mp).createEntityManager();

		em.getTransaction().begin();
		em.getTransaction().commit();
		
	}

	public static void main(String[] args)
	{
		GenerateSqlSchema generateSqlSchema = new GenerateSqlSchema();
		System.out.println("Debut de la generation du schema sql");
		generateSqlSchema.createData();
		System.out.println("Fin de la generation du schema sql");

	}

}
