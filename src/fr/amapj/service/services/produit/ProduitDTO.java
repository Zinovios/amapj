package fr.amapj.service.services.produit;

import fr.amapj.view.engine.tools.TableItem;

/**
 * Permet la gestion des produits dans le fichier de base
 * 
 */
public class ProduitDTO implements TableItem
{
	public Long id;
	
	public String nom;
	
	public String conditionnement;
	
	public Long producteurId;
	
	

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public String getConditionnement()
	{
		return conditionnement;
	}

	public void setConditionnement(String conditionnement)
	{
		this.conditionnement = conditionnement;
	}

	public Long getProducteurId()
	{
		return producteurId;
	}

	public void setProducteurId(Long producteurId)
	{
		this.producteurId = producteurId;
	}
	
}
