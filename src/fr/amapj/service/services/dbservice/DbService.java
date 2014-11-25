package fr.amapj.service.services.dbservice;

import java.util.logging.Logger;

import javax.persistence.EntityManager;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;

/**
 * Service generique pour des accès à la base 
 * 
 *
 */
public class DbService
{
	private final static Logger logger = Logger.getLogger(DbService.class.getName());
	
	
	public DbService()
	{
		
	}
	
	
	/**
	 * Permet de récuperer un element d'une table à partir de son id
	 * Ceci est fait dans une transaction en lecture  
	 */
	@DbRead
	public Identifiable getOneElement(Class clazz,Long id)
	{
		EntityManager em = TransactionHelper.getEm();
		
		return (Identifiable) em.find(clazz, id);
	}
	
	
	
	/**
	 * Permet de supprimer un element d'une table à partir de son id
	 * Ceci est fait dans une transaction en écriture 
	 */
	@DbWrite
	public void deleteOneElement(Class clazz,Long id)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Identifiable identifiable = (Identifiable) em.find(clazz, id);
		
		em.remove(identifiable);
		
	}
	
	

}
