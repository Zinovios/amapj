package fr.amapj.service.services.producteur;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.common.LongUtils;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.EtatNotification;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.ProducteurReferent;
import fr.amapj.model.models.fichierbase.ProducteurUtilisateur;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;

/**
 * Permet la gestion des producteurs
 * 
 */
public class ProducteurService
{
	
	
	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES PRODUCTEURS
	
	/**
	 * Permet de charger la liste de tous les producteurs
	 */
	@DbRead
	public List<ProducteurDTO> getAllProducteurs()
	{
		EntityManager em = TransactionHelper.getEm();
		
		List<ProducteurDTO> res = new ArrayList<>();
		
		Query q = em.createQuery("select p from Producteur p");
			
		List<Producteur> ps = q.getResultList();
		for (Producteur p : ps)
		{
			ProducteurDTO dto = createProducteurDto(em,p);
			res.add(dto);
		}
		
		return res;
		
	}

	
	public ProducteurDTO createProducteurDto(EntityManager em, Producteur p)
	{
		ProducteurDTO dto = new ProducteurDTO();
		
		dto.id = p.getId();
		dto.nom = p.getNom();
		dto.delaiModifContrat = p.getDelaiModifContrat();
		dto.referents = getReferents(em,p);
		dto.utilisateurs = getUtilisateur(em,p);
		
		return dto;
	}


	public List<ProdUtilisateurDTO> getReferents(EntityManager em, Producteur p)
	{
		List<ProdUtilisateurDTO> res = new ArrayList<>();
		
		Query q = em.createQuery("select c from ProducteurReferent c WHERE c.producteur=:p order by c.referent.nom,c.referent.prenom");
		q.setParameter("p", p);
		List<ProducteurReferent> prs =  q.getResultList();
		for (ProducteurReferent pr : prs)
		{
			ProdUtilisateurDTO dto = new ProdUtilisateurDTO();
			dto.idUtilisateur = pr.getReferent().getId();
			dto.nom = pr.getReferent().getNom();
			dto.prenom = pr.getReferent().getPrenom();
			
			res.add(dto);
		}
		return res;
	}


	public List<ProdUtilisateurDTO> getUtilisateur(EntityManager em, Producteur p)
	{
		List<ProdUtilisateurDTO> res = new ArrayList<>();
		
		Query q = em.createQuery("select c from ProducteurUtilisateur c WHERE c.producteur=:p order by c.utilisateur.nom,c.utilisateur.prenom");
		q.setParameter("p", p);
		List<ProducteurUtilisateur> pus =  q.getResultList();
		for (ProducteurUtilisateur pu : pus)
		{
			ProdUtilisateurDTO dto = new ProdUtilisateurDTO();
			dto.idUtilisateur = pu.getUtilisateur().getId();
			dto.nom = pu.getUtilisateur().getNom();
			dto.prenom = pu.getUtilisateur().getPrenom();
			dto.etatNotification = pu.getNotification()==EtatNotification.AVEC_NOTIFICATION_MAIL;
			
			res.add(dto);
		}
		return res;
	}


	// PARTIE MISE A JOUR DES PRODUCTEURS
	@DbWrite
	public void update(final ProducteurDTO dto,final boolean create)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Producteur p;
		
		if (create)
		{
			p = new Producteur();
		}
		else
		{
			p = em.find(Producteur.class, dto.id);
		}
		
		p.setNom(dto.nom);
		p.setDescription(dto.description);
		p.setDelaiModifContrat(dto.delaiModifContrat);
		
		if (create)
		{
			em.persist(p);
		}
		
		// La liste des utilisateurs producteurs 
		updateUtilisateur(dto,em,p);
			
		
		// La liste des référents
		updateReferent(dto,em,p);
		
	}

	
	private void updateUtilisateur(ProducteurDTO dto, EntityManager em, Producteur p)
	{
		// Suppression de tous les référents
		Query q = em.createQuery("select c from ProducteurUtilisateur c WHERE c.producteur=:p");
		q.setParameter("p", p);
		List<ProducteurUtilisateur> prs =  q.getResultList();
		for (ProducteurUtilisateur pr : prs)
		{
			em.remove(pr);
		}
		
		// On recree les nouveaux
		for (ProdUtilisateurDTO util : dto.utilisateurs)
		{
			ProducteurUtilisateur pr = new ProducteurUtilisateur();
			pr.setProducteur(p);
			pr.setUtilisateur(em.find(Utilisateur.class, util.idUtilisateur));
			if (util.etatNotification==true)
			{
				pr.setNotification(EtatNotification.AVEC_NOTIFICATION_MAIL);
			}
			else
			{
				pr.setNotification(EtatNotification.SANS_NOTIFICATION_MAIL);
			}
			
			em.persist(pr);
		}	
	}

	
	private void updateReferent(ProducteurDTO dto, EntityManager em, Producteur p)
	{
		// Suppression de tous les référents
		Query q = em.createQuery("select c from ProducteurReferent c WHERE c.producteur=:p");
		q.setParameter("p", p);
		List<ProducteurReferent> prs =  q.getResultList();
		for (ProducteurReferent pr : prs)
		{
			em.remove(pr);
		}
		
		// On recree les nouveaux
		for (ProdUtilisateurDTO referent : dto.referents)
		{
			ProducteurReferent pr = new ProducteurReferent();
			pr.setProducteur(p);
			pr.setReferent(em.find(Utilisateur.class, referent.idUtilisateur));
			em.persist(pr);
		}	
	}


	// PARTIE SUPPRESSION

	/**
	 * Permet de supprimer un producteur 
	 * Ceci est fait dans une transaction en ecriture
	 */
	@DbWrite
	public void delete(final Long id)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Producteur p = em.find(Producteur.class, id);

		int r = countContrat(p,em);
		if (r>0)
		{
			throw new UnableToSuppressException("Cet producteur posséde "+r+" contrats.");
		}
		
		r = countProduit(p,em);
		if (r>0)
		{
			throw new UnableToSuppressException("Cet producteur posséde "+r+" produits. Vous devez d'abord les supprimer.");
		}
		
		
		em.remove(p);
	}


	private int countContrat(Producteur p, EntityManager em)
	{
		Query q = em.createQuery("select count(c) from Contrat c WHERE c.modeleContrat.producteur=:p");
		q.setParameter("p", p);
			
		return LongUtils.toInt(q.getSingleResult());
	}
	
	private int countProduit(Producteur p, EntityManager em)
	{
		Query q = em.createQuery("select count(c) from Produit c WHERE c.producteur=:p");
		q.setParameter("p", p);
			
		return LongUtils.toInt(q.getSingleResult());
	}
	

	

}
