package fr.amapj.service.engine.appinitializer;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerConfiguration;
import org.hsqldb.server.ServerConstants;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import fr.amapj.common.StackUtils;
import fr.amapj.model.engine.db.DbManager;
import fr.amapj.service.services.backupdb.BackupDatabaseService;
import fr.amapj.service.services.notification.NotificationService;
import fr.amapj.view.engine.ui.AppConfiguration;

/**
 * Initialisation de l'application
 * 
 * Réalise dans l'ordre : 
 * -> le chargement du fichier de configuration 
 * -> le démarrage de la base 
 * -> le démarrage des démons
 * 
 * 
 */
public class AppInitializer implements ServletContextListener
{
	private final static Logger logger = Logger.getLogger(AppInitializer.class.getName());

	private DbManager dbManager = new DbManager();

	//
	Scheduler sched;

	@Override
	public void contextDestroyed(ServletContextEvent event)
	{
		logger.info("Debut de l'arret de l'application");

		// Arret des deamons
		logger.info("Debut de l'arret des démons");
		stopDeamons();
		logger.info("Demons arretes");

		// Arret de la base
		logger.info("Debut de l'arret de la base");
		dbManager.stopDatabase();
		logger.info("Base arretee");

		// De enregistrement des drivers
		deregisterDriver();
	}

	private void deregisterDriver()
	{
		// This manually deregisters JDBC driver, which prevents Tomcat 7 from
		// complaining about memory leaks wrto this class
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements())
		{
			Driver driver = drivers.nextElement();
			try
			{
				DriverManager.deregisterDriver(driver);
				logger.info(String.format("deregistering jdbc driver: %s", driver));
			}
			catch (SQLException e)
			{
				logger.severe(String.format("Error deregistering driver %s", driver) + e.getMessage());
			}

		}

	}

	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		logger.info("Debut de l'initialisation de l'application");

		// Chargement du fichier de configuration
		AppConfiguration.load(new StandardServletParameter(event.getServletContext()));

		// Demarrage de la base
		logger.info("Debut de démarrage de la base");
		dbManager.startDatabase();
		logger.info("Base démarrée");

		// Demarrage des deamons
		logger.info("Debut de démarrage des démons");
		startDeamons();
		logger.info("Demons démarrés");

	}

	private void startDeamons()
	{

		try
		{
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			sched = schedFact.getScheduler();

			sched.start();

			// Déclaration du service de notification, qui s'active toutes les heures
			JobDetail job = newJob(NotificationService.class).withIdentity("myJob1", "group1").build();

			Trigger trigger = newTrigger().withIdentity("myTrigger1", "group1").startNow()
					.withSchedule(simpleSchedule().withIntervalInHours(1).repeatForever()).build();

			sched.scheduleJob(job, trigger);

			// Déclaration du service de backup, qui s'active tous les matins à 5:00
			job = newJob(BackupDatabaseService.class).withIdentity("myJob2", "group2").build();
			trigger = newTrigger().withIdentity("myTrigger2", "group2").startNow().withSchedule(dailyAtHourAndMinute(5, 0)).build();
			sched.scheduleJob(job, trigger);

		}
		catch (SchedulerException e)
		{
			logger.severe("Impossible de demarrer correctement des démons" + StackUtils.asString(e));
		}
	}

	private void stopDeamons()
	{
		try
		{
			sched.shutdown();
		}
		catch (SchedulerException e)
		{
			logger.severe("Impossible d'arreter correctement des démons" + StackUtils.asString(e));
		}

	}
}
