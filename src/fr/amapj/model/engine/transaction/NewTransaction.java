package fr.amapj.model.engine.transaction;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Outil permettant de faire une nouvelle transaction basique au sein d'une transaction existante
 * 
 * Attention : dans la méthode appelée, il ne faire aucun appel de service, car ils seraient
 * appeler dans l'ancienne transaction
 * 
 */
public class NewTransaction
{
	
	private final static Logger logger = Logger.getLogger(NewTransaction.class.getName());
	
	/**
	 * Outil permettant de faire une nouvelle transaction basique au sein d'une transaction existante
	 * 
	 * Attention : dans la méthode appelée, il ne faire aucun appel de service, car ils seraient
	 * appeler dans l'ancienne transaction
	 */
	static public Object write(Call fn)
	{
		EntityManager em = DbUtil.createEntityManager();
		EntityTransaction transac = em.getTransaction();
		transac.begin();
		
		Object result = null;
		
		
		try
		{
			logger.info("Début d'une NOUVELLE transaction en ecriture");
			result = fn.executeInNewTransaction(em);
		}
		catch(Throwable t)
		{
			logger.info("Rollback d'une NOUVELLE transaction en ecriture");
			transac.rollback();
			em.close();
			throw t;
		}
		
		logger.info("Commit d'une NOUVELLE transaction en ecriture");
		transac.commit();
		em.close();
		return result;
		
	}
	

}
