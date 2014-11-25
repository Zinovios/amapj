package fr.amapj.service.services.authentification;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.codec.binary.Base64;

import com.vaadin.ui.UI;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.model.models.stats.LogAccess;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.mailer.MailerMessage;
import fr.amapj.service.services.mailer.MailerService;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.service.services.session.SessionParameters;
import fr.amapj.view.engine.menu.RoleList;


public class PasswordManager
{
	private final static Logger logger = Logger.getLogger(PasswordManager.class.getName());
	
	public PasswordEncryptionService passwordEncryptionService = new PasswordEncryptionService();
	
	public PasswordManager()
	{
		
	}
	
	
	/**
	 * Permet de verifier le user password
	 * Ceci est vérifié dans une transaction en ecriture
	 * 
	 * Retourne null si tout est ok, une explication sur l'erreur sinon
	 */
	@DbWrite
	public String checkUser(String email,String password)
	{
		EntityManager em = TransactionHelper.getEm();
		
		if ((password==null) || password.equals(""))
		{
			return "Vous n'avez pas saisi le mot de passe";
		}
		
		Utilisateur u = findUser(email, em);
		
		if (u==null)
		{
			return "Adresse e-mail ou mot de passe incorrect";
		}
		
		if (u.getEtatUtilisateur()==EtatUtilisateur.INACTIF)
		{
			return "Votre compte a été désactivé car vous n'êtes plus membre de l'AMAP.";
		}
		
		byte[] encryptedPassword = toByteArray(u.getPassword());
		byte[] salt = toByteArray(u.getSalt());

		
		// Verification du password
		if (passwordEncryptionService.authenticate(password, encryptedPassword, salt)==false)
		{
			return "Adresse e-mail ou mot de passe incorrect";
		}
		
		// Si password ok :
		
		// On mémorise l'accès dans le fichier
		LogAccess logAccess = new LogAccess();
		logAccess.setBrowser(SessionManager.getAgentName(UI.getCurrent()));
		logAccess.setDateIn(new Date());
		logAccess.setUtilisateur(u);
		logAccess.setIp(UI.getCurrent().getPage().getWebBrowser().getAddress());
		em.persist(logAccess);
		
		
		SessionParameters p = new SessionParameters();
		p.userId = u.getId();
		p.userRole = new AccessManagementService().getUserRole(u,em);
		p.userNom = u.getNom();
		p.userPrenom = u.getPrenom();
		p.userEmail = email;
		p.dateConnexion = logAccess.getDateIn();
		p.logId = logAccess.getId();
		SessionManager.setSessionParameters(p);
		
		
		
		return null;
	}
	
	
	/**
	 * Retrouve l'utilisateur avec cet e-mail
	 * Retourne null si non trouvé ou autre problème
	 */
	private Utilisateur findUser(String email,EntityManager em)
	{	
		if ((email==null) || email.equals(""))
		{
			return null;
		}
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Utilisateur> cq = cb.createQuery(Utilisateur.class);
		Root<Utilisateur> root = cq.from(Utilisateur.class);
		
		// On ajoute la condition where 
		cq.where(cb.equal(root.get(Utilisateur.P.EMAIL.prop()),email));
		
		List<Utilisateur> us = em.createQuery(cq).getResultList();
		if (us.size()==0)
		{
			return null;
		}
		
		if (us.size()>1)
		{
			logger.severe("Il y a plusieurs utilisateurs avec l'adresse "+email);
			return null;
		}

		return us.get(0);
	}
	
	
	/**
	 * Permet de changer le password
	 * Ceci est fait dans une transaction en ecriture  
	 */
	@DbWrite
	public boolean setUserPassword(final Long userId,final String clearPassword)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Utilisateur r = em.find(Utilisateur.class, userId);
		if (r==null)
		{
			logger.severe("Impossible de retrouver l'utilisateur avec l'id "+userId);
			return false;
		}
		
		if (r.getSalt()==null)
		{
			r.setSalt(fromByteArray(passwordEncryptionService.generateSalt()));
		}
		
		byte[] salt = toByteArray(r.getSalt());
		byte[] encryptedPass = passwordEncryptionService.getEncryptedPassword(clearPassword, salt);
		r.setPassword(fromByteArray(encryptedPass));
		
		// A chaque changement du mot de passe on supprime la ré initilisation par mail
		r.setResetPasswordDate(null);
		r.setResetPasswordSalt(null);
		
		return true;
	
	}
	

	/**
	 * Permet de transformer une chaine base 64 en tableau de byte
	 * @param password
	 * @return
	 */
	private byte[] toByteArray(String base64str)
	{
		return Base64.decodeBase64(base64str.getBytes());
	}
	
	/**
	 * Permet de transformer un tableau de byte  en une chaine en base 64  
	 * @param password
	 * @return
	 */
	private String fromByteArray(byte[] flux)
	{
		return new String(Base64.encodeBase64(flux));
	}

	
	

	@DbWrite
	public String sendMailForResetPassword(final String email)
	{
		EntityManager em = TransactionHelper.getEm();
		
		
		Utilisateur u = findUser(email, em);
		
		if (u==null)
		{
			return "Votre adresse e mail est inconnue";
		}
		
		if (u.getEtatUtilisateur()==EtatUtilisateur.INACTIF)
		{
			return "Votre compte a été désactivé car vous n'êtes plus membre de l'AMAP.";
		}
		
		u.setResetPasswordDate(new Date());
		u.setResetPasswordSalt(generateResetPaswordSalt());

		ParametresDTO parametresDTO = new ParametresService().getParametres(); 
		
		String link = parametresDTO.getUrl()+"?resetPassword="+u.getResetPasswordSalt();
		
		StringBuffer buf = new StringBuffer();
		buf.append("<h2>"+parametresDTO.nomAmap+"</h2>");
		buf.append("<br/>");
		buf.append("Vous avez demandé la ré initialisation de votre mot de passe");
		buf.append("<br/>");
		buf.append("Merci de cliquer sur le lien ci dessous pour saisir votre nouveau mot de passe");
		buf.append("<br/>");
		buf.append("<br/>");
		buf.append("<a href=\""+link+"\">Cliquez ici pour réinitiliser le mot de passe</a>");
		buf.append("<br/>");
		buf.append("<br/>");
		buf.append("Si vous n'avez pas demandé à changer de mot de passe, merci de ne pas tenir compte de ce mail");
		buf.append("<br/>");
		
		new MailerService().sendHtmlMail(new MailerMessage(email, "Re  initialisation de votre mot de passe", buf.toString()));
		
		return null;
		
	}
	
	
	/**
	 * Génère une clépour le reset du password , de 20 caractères en minuscules
	 * @return
	 */
	private String generateResetPaswordSalt()
	{
		int len = 20;
		byte[] buf = new byte[len];
		
		for (int i = 0; i < 20; i++)
		{
			int random = ( (int) (Math.random()*26.0) ) % 26; 
			buf[i] = (byte) ('a'+random);
		}
		
		return new String(buf);
	}

	
	/**
	 * Retrouve l'utilisateur avec ce resetPasswordSald
	 * Retourne null si non trouvé ou autre problème
	 */
	@DbRead
	public Utilisateur findUserWithResetPassword(String resetPasswordSalt)
	{
		EntityManager em = TransactionHelper.getEm();
		
		if ((resetPasswordSalt==null) || resetPasswordSalt.equals(""))
		{
			return null;
		}
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Utilisateur> cq = cb.createQuery(Utilisateur.class);
		Root<Utilisateur> root = cq.from(Utilisateur.class);
		
		// On ajoute la condition where 
		cq.where(cb.equal(root.get(Utilisateur.P.RESETPASSWORDSALT.prop()),resetPasswordSalt));
		
		List<Utilisateur> us = em.createQuery(cq).getResultList();
		if (us.size()==0)
		{
			return null;
		}
		
		if (us.size()>1)
		{
			logger.severe("Il y a plusieurs utilisateurs avec le salt "+resetPasswordSalt);
			return null;
		}

		Utilisateur u =  us.get(0);
		
		if (u.getEtatUtilisateur()==EtatUtilisateur.INACTIF)
		{
			return null;
		}
		
		return u;
	}
	
	
	/**
	 * Permet de signifier la deconnexion d'un utilisateur
	 * Ceci est fait dans une transaction en ecriture  
	 */
	@DbWrite
	public void disconnect()
	{
		EntityManager em = TransactionHelper.getEm();
		
		SessionParameters p = SessionManager.getSessionParameters();
		if (p==null)
		{
			return ;
		}
		Long id = p.logId;
		LogAccess logAccess = em.find(LogAccess.class, id);
		logAccess.setDateOut(new Date());
		
		SessionManager.setSessionParameters(null);
	}
	
	


	public static void main(String[] args)
	{
		//new PasswordManager().setUserPassword(new Long(1051), "j");
		//new PasswordManager().setUserPassword(new Long(1052), "e");
		
		String str = new PasswordManager().generateResetPaswordSalt();
		System.out.println("str="+str);
	}
	
	
}
