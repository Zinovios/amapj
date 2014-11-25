package fr.amapj.service.engine.deamons;

import java.util.List;
import java.util.logging.Logger;

import fr.amapj.common.StackUtils;
import fr.amapj.model.engine.transaction.DbUtil;



/**
 * Utilitaires pour les demons
 * 
 */
public class DeamonsUtils 
{
	private final static Logger logger = Logger.getLogger(DeamonsUtils.class.getName());

	
	static public void executeAsDeamon(String deamonName,DeamonsImpl deamon)
	{
		List<String> dbNames = DbUtil.getAllDbNames();
		for (String dbName : dbNames)
		{
			logger.info("Démarrage du démon "+deamonName+" pour la base "+dbName);
			DbUtil.setDbNameForDeamonThread(dbName);
			try
			{
				deamon.perform();
				logger.info("Fin du démon "+deamonName+" pour la base "+dbName);
			}
			catch(Throwable t)
			{
				logger.info("Erreur sur le démon "+deamonName+" pour la base "+dbName+"\n"+StackUtils.asString(t));
			}
			DbUtil.setDbNameForDeamonThread(null);
		}
	}
	
	
}
