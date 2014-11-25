package fr.amapj.service.services.gestioncontrat;



/**
 * Bean permettant l'edition des modeles de contrats
 *
 */
public class LigneContratDTO
{
	public Long produitId;
	
	public String produitNom;
	
	public Integer prix;
	
	public Long getProduitId()
	{
		return produitId;
	}

	public void setProduitId(Long produitId)
	{
		this.produitId = produitId;
	}

	public Integer getPrix()
	{
		return prix;
	}

	public void setPrix(Integer prix)
	{
		this.prix = prix;
	}

	public String getProduitNom()
	{
		return produitNom;
	}

	public void setProduitNom(String produitNom)
	{
		this.produitNom = produitNom;
	}
	
}
