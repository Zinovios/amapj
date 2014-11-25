package fr.amapj.service.services.produit;

import javax.persistence.EntityManager;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Produit;

/**
 * Permet la gestion des producteurs
 * 
 */
public class ProduitService
{
	
	
	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES PRODUCTEURS
	
	/**
	 * Permet de charger un produit
	 */
	@DbRead
	public ProduitDTO getProduitDto(Long id)
	{	
		EntityManager em = TransactionHelper.getEm();
		
		if (id==null)
		{
			return new ProduitDTO();
		}
		
		Produit p = em.find(Produit.class, id);

		ProduitDTO dto = new ProduitDTO();
		
		dto.id = p.getId();
		dto.nom = p.getNom();
		dto.conditionnement = p.getConditionnement();
		dto.producteurId = p.getProducteur().getId();
		
		return dto;
		
	}

	
	

	/**
	 * Mise à jour ou création d'un produit
	 * @param dto
	 * @param create
	 */
	@DbWrite
	public void update(ProduitDTO dto,boolean create)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Produit p;
		
		if (create)
		{
			p = new Produit();
		}
		else
		{
			p = em.find(Produit.class, dto.id);
		}
		
		p.setNom(dto.nom);
		p.setConditionnement(dto.conditionnement);
		p.setProducteur(em.find(Producteur.class, dto.producteurId));
		
		if (create)
		{
			em.persist(p);
		}
	}
}
