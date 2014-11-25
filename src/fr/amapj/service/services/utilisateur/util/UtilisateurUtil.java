package fr.amapj.service.services.utilisateur.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.common.LongUtils;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.service.services.mailer.MailerMessage;
import fr.amapj.service.services.mailer.MailerService;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.utilisateur.envoimail.EnvoiMailDTO;
import fr.amapj.service.services.utilisateur.envoimail.EnvoiMailUtilisateurDTO;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;

/**
 * 
 * 
 */
public class UtilisateurUtil
{
	
	/**
	 * Détermine si cet utilisateur a une adresse e mail valide
	 * 
	 * @param u
	 * @return
	 */
	static public boolean canSendMailTo(Utilisateur u)
	{
		String email = u.getEmail();
		if (email==null)
		{
			return false;
		}
		if (email.endsWith("#"))
		{
			return false;
		}
		return true;
	}
	
	
	/**
	 * Convertit une liste d'utilisateurs en une String
	 *  
	 * Exemple : ls = [ "Bob AAA" , "Marc BBBB" , "Paul CCC" ] 
	 * 
	 *  asStringPrenomFirst(ls," et ") =>  "Bob AAA et Marc BBB et Paul CCCC"
	 * 
	 */
	public static String asStringPrenomFirst(List<? extends IUtilisateur> ls,String sep)
	{
		if (ls.size()==0)
		{
			return "";
		}
		
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < ls.size()-1; i++)
		{
			IUtilisateur l = ls.get(i);
			str.append(l.getPrenom()+" "+l.getNom());
			str.append(sep);
		}
		
		IUtilisateur l = ls.get(ls.size()-1);
		str.append(l.getPrenom()+" "+l.getNom());
		return str.toString();
	}
	
	

}
