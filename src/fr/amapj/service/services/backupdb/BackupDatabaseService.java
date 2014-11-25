package fr.amapj.service.services.backupdb;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.model.engine.transaction.DbUtil;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.service.engine.deamons.DeamonsImpl;
import fr.amapj.service.engine.deamons.DeamonsUtils;
import fr.amapj.service.services.mailer.MailerAttachement;
import fr.amapj.service.services.mailer.MailerMessage;
import fr.amapj.service.services.mailer.MailerService;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.ui.AppConfiguration;

/**
 * Sauvegarde de la base
 *
 */
public class BackupDatabaseService implements Job
{
	private final static Logger logger = Logger.getLogger(BackupDatabaseService.class.getName());
	
	
	public BackupDatabaseService()
	{
		
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		DeamonsUtils.executeAsDeamon(getClass().toString(), new DeamonsImpl()
		{
			@Override
			public void perform()
			{
				backupDatabase();
			}
		});
	}
	
	
	/**
	 * Permet de faire le backup de la base
	 * Ceci est vérifié dans une transaction en ecriture  
	 */
	@DbWrite
	public void backupDatabase()
	{
		EntityManager em = TransactionHelper.getEm();
		
		String dbName = DbUtil.getCurrentDbName();
		
		logger.info("Debut de la sauvegarde de la base pour "+dbName);
		
		String backupDir = AppConfiguration.getConf().getBackupDirectory();
		if (backupDir==null)
		{
			throw new RuntimeException("Le répertoire de stockage des sauvegardes n'est pas défini");
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String fileName = backupDir+"/"+dbName+"_"+df.format(new Date())+".tar.gz";
		
		em.createNativeQuery("BACKUP DATABASE TO '"+fileName+"' BLOCKING").executeUpdate();
		
		File file= new File(fileName);
		if (file.canRead()==false)
		{
			throw new RuntimeException("Erreur lors de la sauvegarde pour "+dbName);
		}
	
		ParametresDTO param = new ParametresService().getParametres();
		String htmlContent = "Sauvegarde de la base "+param.nomAmap;
		MailerMessage message = new MailerMessage(param.backupReceiver,"Backup de la base de "+param.nomAmap,htmlContent);
		message.addAttachement(new MailerAttachement(file));
		
		new MailerService().sendHtmlMail(message);
		
		logger.info("Fin de la sauvegarde de la base pour "+dbName);
		
		
	}
	
	public static void main(String[] args)
	{
		TestTools.init();
		
		new BackupDatabaseService().backupDatabase();
	}


}
