package fr.amapj.service.services.access;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.model.models.fichierbase.RoleAdmin;
import fr.amapj.model.models.fichierbase.RoleTresorier;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.produit.ProduitDTO;
import fr.amapj.view.engine.menu.RoleList;
import fr.amapj.view.views.droits.DroitsAdministrateurBasicForm;

/**
 * Permet la gestion des droits d'accès
 * 
 *  
 *
 */
public class AccessManagementService
{
	

	/**
	 * Cette méthode détermine la liste des rôles de cet utilisateur
	 * @param u
	 * @param em
	 * @return
	 */
	public List<RoleList> getUserRole(Utilisateur u, EntityManager em)
	{
		List<RoleList> res = new ArrayList<RoleList>();
		
		if (isAdmin(em,u))
		{
			res.add(RoleList.ADMIN);
			res.add(RoleList.TRESORIER);
			res.add(RoleList.PRODUCTEUR);
			res.add(RoleList.REFERENT);
			res.add(RoleList.ADHERENT);
			return res;
		}
		
		if (isTresorier(em,u))
		{
			res.add(RoleList.TRESORIER);
			res.add(RoleList.PRODUCTEUR);
			res.add(RoleList.REFERENT);
			res.add(RoleList.ADHERENT);
			return res;
		}
		
		if (isReferent(em,u))
		{
			res.add(RoleList.REFERENT);
			res.add(RoleList.PRODUCTEUR);
			res.add(RoleList.ADHERENT);
			return res;
		}
		
		if (isProducteur(em,u))
		{
			res.add(RoleList.PRODUCTEUR);
			res.add(RoleList.ADHERENT);
			return res;
		}
		
		res.add(RoleList.ADHERENT);
		return res;
	}
	
	
	/**
	 Permet d'identifier facilement les roles d'un utilisateur
	 */
	public String getRoleAsString(EntityManager em, Utilisateur u)
	{
		if (isAdmin(em,u))
		{
			return "ADMIN";
		}
		
		if (isTresorier(em,u))
		{
			return "TRESORIER";
		}
		
		boolean ref = isReferent(em,u);
		boolean prod = isProducteur(em, u);
		
		if (ref && prod)
		{
			return "REFERENT et PRODUCTEUR";
		}
		
		if (ref)
		{
			return "REFERENT";
		}
		
		if (prod)
		{
			return "PRODUCTEUR";
		}
		
		return "ADHERENT";
	}
	
	

	private boolean isAdmin(EntityManager em, Utilisateur u)
	{
		Query q = em.createQuery("select r.id from RoleAdmin r  WHERE r.utilisateur=:u");
		q.setParameter("u", u);
		return q.getResultList().size()>=1;
	}



	private boolean isTresorier(EntityManager em, Utilisateur u)
	{
		Query q = em.createQuery("select r.id from RoleTresorier r  WHERE r.utilisateur=:u");
		q.setParameter("u", u);
		return q.getResultList().size()>=1;
	}



	private boolean isReferent(EntityManager em, Utilisateur u)
	{
		Query q = em.createQuery("select r.id from ProducteurReferent r  WHERE r.referent=:u");
		q.setParameter("u", u);
		return q.getResultList().size()>=1;
	}



	private boolean isProducteur(EntityManager em, Utilisateur u)
	{
		Query q = em.createQuery("select r.id from ProducteurUtilisateur r  WHERE r.utilisateur=:u");
		q.setParameter("u", u);
		return q.getResultList().size()>=1;

	}



	// PARTIE REQUETAGE POUR LES PRODUCTEURS AUTORISES

	/**
	 * Permet de charger la liste de tous les producteurs autorisés pour cet utilisateur 
	 * dans une transaction en lecture
	 */
	@DbRead
	public List<Producteur> getAccessLivraisonProducteur(List<RoleList> roles,Long idUtilisateur)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Utilisateur user = em.find(Utilisateur.class, idUtilisateur);
		Query q;
		List<Producteur> res = new ArrayList<Producteur>();
		
		if ( (roles.contains(RoleList.ADMIN)) ||  (roles.contains(RoleList.TRESORIER)) )
		{
			// Recherche tous les producteurs
			q = em.createQuery("select p from Producteur p order by p.nom");
			res.addAll( (List<Producteur>) q.getResultList() );
			return res;
		}
		
		
		// Recherche en tant que producteur
		q = em.createQuery("select distinct(c.producteur) from ProducteurUtilisateur c WHERE " +
						"c.utilisateur=:u "+
						"order by c.producteur.nom");
				q.setParameter("u", user);
		res.addAll( (List<Producteur>) q.getResultList() );
				
		// Recherche en tant que referent
		q = em.createQuery("select distinct(c.producteur) from ProducteurReferent c WHERE " +
						"c.referent=:u " +
						"order by c.producteur.nom");
				q.setParameter("u", user);
		res.addAll( (List<Producteur>) q.getResultList() );				
			
		return res;

	}

	// RESTRICTION DES ACCES AUX INACTIFS
	
	
	/**
	 * 
	 */
	public Boolean getAccessInactif(List<RoleList> roles)
	{
		if ( (roles.contains(RoleList.ADMIN)) ||  (roles.contains(RoleList.TRESORIER)) )
		{
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}



	// PARTIE fichier de base

	/**
	 * Permet de charger un administrateur
	 */
	@DbRead
	public AdminTresorierDTO getAdminDto(Long id)
	{	
		EntityManager em = TransactionHelper.getEm();
		
		if (id==null)
		{
			return new AdminTresorierDTO();
		}
		
		RoleAdmin p = em.find(RoleAdmin.class, id);

		AdminTresorierDTO dto = new AdminTresorierDTO();
		
		dto.id = p.getId();
		dto.utilisateurId = p.getUtilisateur().getId();
		
		return dto;		
	}

	/**
	 * Mise à jour ou création d'un administrateur
	 * @param dto
	 * @param create
	 */
	@DbWrite
	public void updateAdminDto(AdminTresorierDTO dto,boolean create)
	{
		EntityManager em = TransactionHelper.getEm();
		
		RoleAdmin p;
		
		if (create)
		{
			p = new RoleAdmin();
		}
		else
		{
			p = em.find(RoleAdmin.class, dto.id);
		}
		
		
		p.setUtilisateur(em.find(Utilisateur.class, dto.utilisateurId));
		
		if (create)
		{
			em.persist(p);
		}
	}
	
	
	/**
	 * Permet de charger un tresorier
	 */
	@DbRead
	public AdminTresorierDTO getTresorierDto(Long id)
	{	
		EntityManager em = TransactionHelper.getEm();
		
		if (id==null)
		{
			return new AdminTresorierDTO();
		}
		
		RoleTresorier p = em.find(RoleTresorier.class, id);

		AdminTresorierDTO dto = new AdminTresorierDTO();
		
		dto.id = p.getId();
		dto.utilisateurId = p.getUtilisateur().getId();
		
		return dto;		
	}

	/**
	 * Mise à jour ou création d'un administrateur
	 * @param dto
	 * @param create
	 */
	@DbWrite
	public void updateTresorierDto(AdminTresorierDTO dto,boolean create)
	{
		EntityManager em = TransactionHelper.getEm();
		
		RoleTresorier p;
		
		if (create)
		{
			p = new RoleTresorier();
		}
		else
		{
			p = em.find(RoleTresorier.class, dto.id);
		}
		
		
		p.setUtilisateur(em.find(Utilisateur.class, dto.utilisateurId));
		
		if (create)
		{
			em.persist(p);
		}
	}
	
	
	
	

	
}
