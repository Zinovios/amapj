package fr.amapj.service.services.listeadherents;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.model.models.fichierbase.Utilisateur;

/**
 * Permet d'afficher la liste des adherents
 * 
 *  
 *
 */
public class ListeAdherentsService
{

	public ListeAdherentsService()
	{

	}

	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES  ADHERENTS

	/**
	 *  Retourne la liste de tous les utilisateurs 
	 */
	@DbRead
	public List<Utilisateur> getAllUtilisateurs(boolean includeInactif)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Query q ;
		if (includeInactif)
		{
			q = em.createQuery("select u from Utilisateur u " +
					"order by u.nom,u.prenom");
		}
		else
		{
			q = em.createQuery("select u from Utilisateur u " +
					"where u.etatUtilisateur=:etat " +
					"order by u.nom,u.prenom");
				q.setParameter("etat", EtatUtilisateur.ACTIF);
		}
		
		List<Utilisateur> us = q.getResultList();
		return us;
	}
	
	/**
	 *  Retourne la liste de tous les emails des utilisateurs actifs 
	 */
	@DbRead
	public String getAllEmails()
	{
		StringBuffer buf = new StringBuffer();
		List<Utilisateur> us = getAllUtilisateurs(false);
		if (us.size()==0)
		{
			return "";
		}
		
	
		for (int i = 0; i < us.size()-1; i++)
		{
			Utilisateur u = us.get(i);
			buf.append(u.getEmail());
			buf.append(',');
		}
		
		Utilisateur u = us.get(us.size()-1);
		buf.append(u.getEmail());
		return buf.toString();
	}
	
	
	
	
}
