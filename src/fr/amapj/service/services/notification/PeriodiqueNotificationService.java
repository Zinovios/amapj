package fr.amapj.service.services.notification;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.common.DateUtils;
import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.model.engine.transaction.Call;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.NewTransaction;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.model.models.param.ChoixOuiNon;
import fr.amapj.model.models.stats.NotificationDone;
import fr.amapj.model.models.stats.TypNotificationDone;
import fr.amapj.service.services.mailer.MailerMessage;
import fr.amapj.service.services.mailer.MailerService;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.service.services.utilisateur.util.UtilisateurUtil;

/**
 * Notification pour les permanences
 *
 */
public class PeriodiqueNotificationService 
{
	private final static Logger logger = Logger.getLogger(PeriodiqueNotificationService.class.getName());
	
	
	public PeriodiqueNotificationService()
	{
		
	}
	
	
	@DbRead
	public void sendPermanenceNotification()
	{		
		EntityManager em = TransactionHelper.getEm();
		ParametresDTO param = new ParametresService().getParametres();
		
		if (param.envoiMailPeriodique==ChoixOuiNon.NON)
		{
			return ;
		}
		
		// On recherche la date de la dernière notification (dans le passé ou le jour même)
		Date d = getLastNotificationDate(param);
		
		if (d==null)
		{
			return;
		}
		
		// On recherche la liste des utilisateurs qui n'ont pas eu de notification ce jour là et qui sont actif
		List<Utilisateur> utilisateurs = getUtilisateursActifWithNoNotification(d,em);
		
		for (Utilisateur utilisateur : utilisateurs)
		{
			if (UtilisateurUtil.canSendMailTo(utilisateur))
			{
				sendMail(utilisateur,d,em,param);
			}
		}
	}
	
	
	
	private List<Utilisateur> getUtilisateursActifWithNoNotification(Date d, EntityManager em)
	{
		Query q = em.createQuery("select u from Utilisateur u where "
				+ "	u.etatUtilisateur=:etat and "
				+ " NOT EXISTS (select d from NotificationDone d where d.typNotificationDone=:typNotif and d.dateMailPeriodique=:d and d.utilisateur=u) "
				+ " order by u.nom,u.prenom");
		
		q.setParameter("etat", EtatUtilisateur.ACTIF);
		q.setParameter("d", d);
		q.setParameter("typNotif", TypNotificationDone.MAIL_PERIODIQUE);

		List<Utilisateur> us = q.getResultList();
		
		return us;
	}


	/**
	 * On prend les dates sur les 7 jours précédant le jour courant 
	 * et on vérifie si cette date est une date pour la notification periodique
	 * 
	 * @param param
	 * @return
	 */
	private Date getLastNotificationDate(ParametresDTO param)
	{
		Date ref = DateUtils.suppressTime(new Date());
		
		for (int i = 0; i < 7; i++)
		{
			Date d = DateUtils.addDays(ref, -i);
			
			if (DateUtils.getDayInMonth(d)==param.numJourDansMois)
			{
				return d;
			}
		}
		
		return null;
	}


	private void sendMail(Utilisateur utilisateur, Date d, EntityManager em, ParametresDTO param)
	{	
		// Construction du message
		MailerMessage message  = new MailerMessage();
		
		String titre = replaceWithContext(param.titreMailPeriodique, em, d, utilisateur,param);
		String content = replaceWithContext(param.contenuMailPeriodique, em, d, utilisateur,param);
		
		message.setTitle(titre);
		message.setContent(content);
		message.setEmail(utilisateur.getEmail());
		sendMessageAndMemorize(message,d,utilisateur.getId());
		
	}
	
	
	
	

	/**
	 * On réalise chaque envoi dans une transaction indépendante 
	 * 
	 * @param email
	 * @param title
	 * @param content
	 * @param generator
	 */
	private void sendMessageAndMemorize(final MailerMessage message, final Date d, final Long utilisateurId)
	{
		NewTransaction.write(new Call()
		{
			@Override
			public Object executeInNewTransaction(EntityManager em)
			{
				sendMessageAndMemorize(em,message,d,utilisateurId);
				return null;
			}
		});
		
	}

	protected void sendMessageAndMemorize(EntityManager em, MailerMessage message, Date d, Long utilisateurId)
	{
		// On mémorise dans la base de données que l'on va envoyer le message
		NotificationDone notificationDone = new NotificationDone();
		notificationDone.setTypNotificationDone(TypNotificationDone.MAIL_PERIODIQUE);
		notificationDone.setDateMailPeriodique(d);
		notificationDone.setUtilisateur(em.find(Utilisateur.class, utilisateurId));
		notificationDone.setDateEnvoi(new Date());
		em.persist(notificationDone);
		
		// On envoie le message
		new MailerService().sendHtmlMail(message);
		//System.out.println("titre="+message.getTitle()+" email="+message.getEmail());
		//System.out.println("content="+message.getContent());
	}



	
	private String replaceWithContext(String in,EntityManager em,Date d,Utilisateur u,ParametresDTO param)
	{
		// Calcul du contexte
		String link = param.getUrl()+"?username="+u.getEmail();
		
		in = in.replaceAll("#NOM_AMAP#", param.nomAmap);
		in = in.replaceAll("#VILLE_AMAP#", param.villeAmap);
		in = in.replaceAll("#LINK#", link);
		
		
		return in;
		
	}
	
	public static void main(String[] args)
	{
		TestTools.init();
		PeriodiqueNotificationService service = new PeriodiqueNotificationService();
		service.sendPermanenceNotification();
	}


}
