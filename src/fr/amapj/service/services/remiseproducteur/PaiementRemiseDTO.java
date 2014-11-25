package fr.amapj.service.services.remiseproducteur;

import fr.amapj.model.models.contrat.reel.EtatPaiement;



/**
 * 
 *
 */
public class PaiementRemiseDTO 
{
	public Long idPaiement;
	
	// Montant du paiement
	public int montant;
	
	public String nomUtilisateur;;
	
	public String prenomUtilisateur;
	
	public EtatPaiement etatPaiement;
	

	public int getMontant()
	{
		return montant;
	}

	public void setMontant(int montant)
	{
		this.montant = montant;
	}

	public String getNomUtilisateur()
	{
		return nomUtilisateur;
	}

	public void setNomUtilisateur(String nomUtilisateur)
	{
		this.nomUtilisateur = nomUtilisateur;
	}

	public String getPrenomUtilisateur()
	{
		return prenomUtilisateur;
	}

	public void setPrenomUtilisateur(String prenomUtilisateur)
	{
		this.prenomUtilisateur = prenomUtilisateur;
	};
		
	
	
	
	
	
	
	
	

}
