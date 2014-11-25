package fr.amapj.service.services.mespaiements;


import java.util.Date;

/**
 * 
 *
 */
public class PaiementHistoriqueDTO
{

	// Nom du producteur
	public String nomProducteur;

	
	// Nom du contrat
	public String nomContrat;

	
	// Date d'encaissement prévu
	public Date datePrevu;
	
	// Date d'encaissement réelle
	public Date dateReelle;
	
	
	// Montant du paiement
	public int montant;


	public String getNomProducteur()
	{
		return nomProducteur;
	}


	public void setNomProducteur(String nomProducteur)
	{
		this.nomProducteur = nomProducteur;
	}


	public String getNomContrat()
	{
		return nomContrat;
	}


	public void setNomContrat(String nomContrat)
	{
		this.nomContrat = nomContrat;
	}


	public Date getDatePrevu()
	{
		return datePrevu;
	}


	public void setDatePrevu(Date datePrevu)
	{
		this.datePrevu = datePrevu;
	}


	public Date getDateReelle()
	{
		return dateReelle;
	}


	public void setDateReelle(Date dateReelle)
	{
		this.dateReelle = dateReelle;
	}


	public int getMontant()
	{
		return montant;
	}


	public void setMontant(int montant)
	{
		this.montant = montant;
	}
	
	
	
	

}
