package fr.amapj.model.engine.transaction;

import javax.persistence.EntityManager;

/**
 * Gestion des transactions
 *
 */
public interface Call
{
	
	public Object executeInNewTransaction(EntityManager em);

}
