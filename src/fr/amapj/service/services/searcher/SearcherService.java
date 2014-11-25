package fr.amapj.service.services.searcher;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.IdentifiableUtil;
import fr.amapj.model.engine.transaction.Call;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.engine.transaction.NewTransaction;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.model.models.fichierbase.Utilisateur;


public class SearcherService
{
	public SearcherService()
	{
		
	}
	
	
	/**
	 * Permet de récuperer tous les elements d'une table pour le mettre dans le searcher
	 * Ceci est fait dans une transaction en lecture  
	 */
	
	@DbRead
	public List<Identifiable> getAllElements(Class clazz)
	{
		EntityManager em = TransactionHelper.getEm();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Identifiable> cq = cb.createQuery(clazz);
		List<Identifiable> roles = em.createQuery(cq).getResultList();
		return roles;
	}
	
	
	/**
	 * Permet de récuperer tous les produits d'un producteur
	 * Ceci est fait dans une transaction en lecture  
	 */
	@DbRead
	public List<Produit> getAllProduits(Long idProducteur)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Producteur producteur = em.find(Producteur.class, idProducteur);
		
		Query q = em.createQuery("select p from Produit p WHERE p.producteur=:prod order by p.nom,p.conditionnement");
		q.setParameter("prod",producteur);
		List<Produit> us = q.getResultList();
		return us;
	}
	
	
	

}
