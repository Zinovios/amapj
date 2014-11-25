package fr.amapj.model.engine.transaction;

import java.util.logging.Logger;

import javax.persistence.EntityManager;

import fr.amapj.model.engine.transaction.TransactionInfo.Status;

/**
 * Gestion des transactions
 *
 */
public class TransactionHelper
{
	
	private final static Logger logger = Logger.getLogger(TransactionHelper.class.getName());
	
	
    public static TransactionHelper mainInstance;
	private static ThreadLocal<TransactionInfo> threadLocal;
	
	private boolean debug = true;

    static {
	mainInstance = new TransactionHelper();
	threadLocal = new ThreadLocal<TransactionInfo>();
	threadLocal.set(TransactionInfo.getInstance());
    }
	
	public static EntityManager getEm()
	{
		TransactionInfo transactionInfo = mainInstance.threadLocal.get();
		if(transactionInfo == null) {
		    transactionInfo = TransactionInfo.getInstance();
		    threadLocal.set(transactionInfo);
		}
		return transactionInfo.getEm();
	}
	
	public void start_read()
	{
		TransactionInfo transactionInfo = threadLocal.get();
		
		//
		if (transactionInfo==null)
		{
		    transactionInfo = TransactionInfo.getInstance();
		    threadLocal.set(TransactionInfo.getInstance());
		}
		
		if (debug)
		{
		    logger.info("nbAppel = "+transactionInfo.getNbAppel() +" em = "+transactionInfo.getEm() +" transac = "+transactionInfo.getTransac());
		}
		
		
		transactionInfo.setNbAppel(transactionInfo.getNbAppel() + 1);
		
		// Si c'est le premier appel, on crée une session
		// Si ce n'est pas le premier appel, il n'y a rien à faire, on reste dans les conditions précédentes
		if (transactionInfo.getNbAppel()==1)
		{
			transactionInfo.startSessionLecture();
		}
	}
	
	public void stop_read(boolean rollback)
	{
		TransactionInfo transactionInfo = threadLocal.get();
		
		transactionInfo.setNbAppel(transactionInfo.getNbAppel() - 1);
		
		// Si c'est le dernier appel de la stack, alors on ferme le tout
		// Sinon on ne fait rien
		if (transactionInfo.getNbAppel()==0)
		{
			transactionInfo.closeSession(rollback);
		}
	}
	
	
	
	public void start_write()
	{
		TransactionInfo transactionInfo = threadLocal.get();
		
		//
		if (transactionInfo==null)
		{
			transactionInfo = new TransactionInfo();
			threadLocal.set(transactionInfo);
		}
		
		transactionInfo.setNbAppel(transactionInfo.getNbAppel() - 1);
		
		// Si c'est le premier appel , on crée directement une session en écriture
		if (transactionInfo.getNbAppel()==1)
		{
			transactionInfo.startSessionEcriture();
		}
		// Si ce n'est pas le premier appel et que la session est en lecture, on upgrade la session en écriture
		else 
		{ 
			if (transactionInfo.getType()==Status.LECTURE)
			{
				transactionInfo.upgradeLectureToEcriture();
			}
		}
	}
	
	
	public void stop_write(boolean rollback)
	{
		TransactionInfo transactionInfo = threadLocal.get();
		
		// Dans le cas d'une exception au commit , dans AspectJ on passe dans returning puis throwing
		// On se retrouve alors dans ce cas là, ou il ne faut rien faire 
		if ((rollback==true) && (transactionInfo.getNbAppel() == 0))
		{
			return ;
		}
		
		transactionInfo.setNbAppel(transactionInfo.getNbAppel() - 1);
		
		// Si c'est le dernier appel de la stack, alors on ferme le tout
		// Sinon on ne fait rien
		if (transactionInfo.getNbAppel() == 0)
		{
			transactionInfo.closeSession(rollback);
		}
	}
	
}
