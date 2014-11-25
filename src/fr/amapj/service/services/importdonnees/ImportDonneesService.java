package fr.amapj.service.services.importdonnees;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Produit;

/**
 * Service pour l'import des données
 *  
 *
 */
public class ImportDonneesService
{
	private final static Logger logger = Logger.getLogger(ImportDonneesService.class.getName());
	
	
	public ImportDonneesService()
	{
		
	}

	/*
	 * Produits et producteurs
	 */
	
	@DbWrite
	public void insertDataProduits(List<ImportProduitProducteurDTO> prods)
	{
		EntityManager em = TransactionHelper.getEm();
		
		for (ImportProduitProducteurDTO importProduitProducteurDTO : prods)
		{
			insertDataProduits(em,importProduitProducteurDTO);
		}
	}

	private void insertDataProduits(EntityManager em, ImportProduitProducteurDTO dto)
	{
		Query q = em.createQuery("select p from Producteur p WHERE p.nom LIKE :nom");
		q.setParameter("nom",dto.producteur);
		
		List<Producteur> prods = q.getResultList();
		Producteur p = null;
		if (prods.size()==0)
		{
			p = new Producteur();
			p.setNom(dto.producteur);
			em.persist(p);
		}
		else if (prods.size()==1)
		{
			p = prods.get(0);
		}
		else
		{
			throw new RuntimeException("Deux producteurs avec le même nom");
		}
		
		
		Produit pr = new Produit();
		pr.setConditionnement(dto.conditionnement);
		pr.setNom(dto.produit);
		pr.setProducteur(p);
		em.persist(pr);
		
	}

	@DbRead
	public List<ImportProduitProducteurDTO> getAllProduits()
	{
		EntityManager em = TransactionHelper.getEm();
		
		List<ImportProduitProducteurDTO> res = new ArrayList<>();
		
		Query q = em.createQuery("select p from Produit p order by p.producteur.nom");
		List<Produit> prods = q.getResultList();
		
		for (Produit prod : prods)
		{
			ImportProduitProducteurDTO dto = new ImportProduitProducteurDTO();
			dto.producteur = prod.getProducteur().getNom();
			dto.produit = prod.getNom();
			dto.conditionnement = prod.getConditionnement();
			
			res.add(dto);
		}
		
		
		return res;
	}
}
