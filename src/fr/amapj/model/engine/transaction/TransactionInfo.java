package fr.amapj.model.engine.transaction;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Contient les informations sur la transaction en cours
 *
 */
public class TransactionInfo
{
	private final static Logger logger = Logger.getLogger(TransactionInfo.class.getName());
	
    enum TransactionInfoHolder {
		INSTANCE;
	
		public TransactionInfo createTransaction(){
		    return new TransactionInfo();
		}
    }
    
	public static TransactionInfo getInstance() {
		return TransactionInfoHolder.INSTANCE.createTransaction();
	}


	// Correspond au nombre d'appel de fonction avec des annotations @DbRead, ... (stack d'appel) 
	private int nbAppel = 0;
	private EntityManager em = null;
	private EntityTransaction transac = null;
	
	public int getNbAppel() {
		return nbAppel;
	}

	public void setNbAppel(int nbAppel) {
		this.nbAppel = nbAppel;
	}

	public EntityManager getEm() {
		return em;
	}


	public void setEm(EntityManager em) {
		this.em = em;
	}


	public EntityTransaction getTransac() {
		return transac;
	}


	public void setTransac(EntityTransaction transac) {
		this.transac = transac;
	}

	public enum Status
	{
		VIDE , LECTURE , ECRITURE ;
	}
	
	public Status getType()
	{
		if (transac!=null)
		{
			return Status.ECRITURE;
		}
		
		if (em!=null)
		{
			return Status.LECTURE;
		}
			
		return Status.VIDE;	
	}
	
	
	/**
	 * Permet de démarrer une session en lecture
	 */
	public void startSessionLecture()
	{
		em = DbUtil.createEntityManager();
		transac = null;
		logger.info("Début d'une transaction en lecture");
	}	

	/**
	 * Permet de démarrer une session en écriture
	 */
	public void startSessionEcriture()
	{
		em = DbUtil.createEntityManager();
		transac = em.getTransaction();
		transac.begin();
		logger.info("Début d'une transaction en écriture");
	}
	
	
	/**
	 * Permet de transformer une session de LECTURE à ECRITURE
	 */
	public void upgradeLectureToEcriture()
	{
		transac = em.getTransaction();
		transac.begin();
	}


	public void closeSession(boolean rollback)
	{
		// on mémorise le status de la session 
		Status type = getType();
		EntityTransaction transac1 = transac;
		EntityManager em1 = em;
		
		// On met à zéro tous les elements pour être sur que celle ci ait bien lieu
		nbAppel = 0;
		em = null;
		transac = null;

		// On commit ensuite
		switch (type)
		{
		case VIDE:
			logger.warning("Erreur dans les transactions !! Impossible de fermer une session vide");
			break;
			
		case LECTURE:
			if (rollback)
			{
				logger.info("Fin d'une transaction en lecture sur exception");
				em1.close();
			}
			else
			{
				logger.info("Fin d'une transaction en lecture");
				em1.close();
			}
			break;
			
		case ECRITURE:
			if (rollback)
			{
				logger.info("Rollback d'une transaction en ecriture");
				transac1.rollback();
				em1.close();
			}
			else
			{
				logger.info("Commit d'une transaction en ecriture");
				transac1.commit();
				em1.close();
			}
			break;
		}

	}
}
