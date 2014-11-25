package fr.amapj.model.samples.query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.Utilisateur;

/**
 * Utilisation de requete HSQL
 * 
 */
public class SelectDbUtilisateurHsql
{

	/**
	 * Permet de lister simplement tous les utilisteurs
	 */
	@DbRead
	public void listAllUser()
	{
		EntityManager em = TransactionHelper.getEm();
		
		Query q = em.createQuery("select u from Utilisateur u");
		List<Utilisateur> us = q.getResultList();
		for (Utilisateur u : us)
		{
			System.out.println("Utilisateur: Nom ="+u.getNom()+" Prenom ="+u.getPrenom());
		}
	}
	
	/**
	 * Permet de lister tous les utilisteurs ayant pour nom AA
	 */
	@DbRead
	public void listUserWithNameAA()
	{
		EntityManager em = TransactionHelper.getEm();
		
		Query q = em.createQuery("select u from Utilisateur u WHERE u.nom=:nom");
		q.setParameter("nom","nom_a");
		List<Utilisateur> us = q.getResultList();
		for (Utilisateur u : us)
		{
			System.out.println("Utilisateur: Nom ="+u.getNom()+" Prenom ="+u.getPrenom());
		}
	}
	
	
	
	
	
	/**
	 * Retourne la liste des utilisateurs ayant au moins un contrat 
	 */
	@DbRead
	public void listUserWithAContrat()
	{
		EntityManager em = TransactionHelper.getEm();
		
		Query q = em.createQuery("select u from Utilisateur u WHERE EXISTS (select c from Contrat c where c.utilisateur = u)");
		//q.setParameter("mc","nom_a");
		List<Utilisateur> us = q.getResultList();
		for (Utilisateur u : us)
		{
			System.out.println("Utilisateur: Nom ="+u.getNom()+" Prenom ="+u.getPrenom());
		}

	}
	
	@DbRead
	public void complexrequest() throws ParseException
	{
		EntityManager em = TransactionHelper.getEm();
		
		//
		Long idUtilisateur = new Long(1052);
		
		//
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		List<Date> dates = new ArrayList<>();
		dates.add(df.parse("12/06/14"));
		dates.add(df.parse("19/06/14"));
		dates.add(df.parse("26/06/14"));
		dates.add(df.parse("03/07/14"));
		
		//
		Query q = em.createQuery("select distinct(c.modeleContratDate.dateLiv) from ContratCell c WHERE " +
				"c.contrat.utilisateur=:u and " +
				"c.modeleContratDate.dateLiv in :dates " +
				"order by c.modeleContratDate.dateLiv");
		q.setParameter("u", em.find(Utilisateur.class, idUtilisateur));
		q.setParameter("dates", dates);
		
		
		List<Date> ds = q.getResultList();
		System.out.println("");
		for (Date date : ds)
		{
			System.out.println("Date: Nom ="+df.format(date));
		}
		
	}
	
	

	public static void main(String[] args)
	{
		TestTools.init();
		
		SelectDbUtilisateurHsql selectUtilisateur = new SelectDbUtilisateurHsql();
		System.out.println("Requete dans la base avec HSQL..");
		selectUtilisateur.listAllUser();
		System.out.println("Fin de la requete");

	}

}
