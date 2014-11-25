package fr.amapj.service.services.notification;

import java.util.logging.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import fr.amapj.common.StackUtils;
import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.service.engine.deamons.DeamonsImpl;
import fr.amapj.service.engine.deamons.DeamonsUtils;


public class NotificationService implements Job
{
	private final static Logger logger = Logger.getLogger(NotificationService.class.getName());
	
	
	public NotificationService()
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
				sendAllNotificationMail();
			}
		});
	}
	
	
	/**
	 * Permet d'envoyer tous les mails de notification
	 * 
	 */
	
	public void sendAllNotificationMail()
	{
		try
		{
			new ProducteurNotificationService().sendProducteurNotification();
		}
		catch(Throwable t)
		{
			logger.info("Erreur dans la notication producteur "+"\n"+StackUtils.asString(t));
		}
		
		try
		{
			new PermanenceNotificationService().sendPermanenceNotification();
		}
		catch(Throwable t)
		{
			logger.info("Erreur dans la notication permanence "+"\n"+StackUtils.asString(t));
		}
		
		
	}




}
