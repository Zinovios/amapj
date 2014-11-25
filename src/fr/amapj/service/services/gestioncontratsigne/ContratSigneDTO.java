package fr.amapj.service.services.gestioncontratsigne;


import java.util.Date;

import fr.amapj.view.engine.tools.TableItem;

/**
 * Represente un contrat signe
 *
 */
public class ContratSigneDTO implements TableItem
{
	public String nomUtilisateur;
	
	public String prenomUtilisateur;
	
	public Date dateCreation;
	
	public Date dateModification;
	
	public Long idUtilisateur;
	
	public Long idContrat;
	
	public Long idModeleContrat;
	
	// Montant des produits commandés
	public int mntCommande;
	
	// Montant de l'avoir initial
	public int mntAvoirInitial;
	
	// Montant du solde
	public int mntSolde;
	
	public int nbChequePromis;
	
	public int nbChequeRecus;
	
	public int nbChequeRemis;

	
	
	
	

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
	}

	public Date getDateCreation()
	{
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation)
	{
		this.dateCreation = dateCreation;
	}

	public Date getDateModification()
	{
		return dateModification;
	}

	public void setDateModification(Date dateModification)
	{
		this.dateModification = dateModification;
	}

	public Long getIdContrat()
	{
		return idContrat;
	}

	public void setIdContrat(Long idContrat)
	{
		this.idContrat = idContrat;
	}

	
	

	
	public int getMntCommande()
	{
		return mntCommande;
	}

	public void setMntCommande(int mntCommande)
	{
		this.mntCommande = mntCommande;
	}


	public int getNbChequeRemis()
	{
		return nbChequeRemis;
	}

	public void setNbChequeRemis(int nbChequeRemis)
	{
		this.nbChequeRemis = nbChequeRemis;
	}


	/**
	 * Element permettant de distinguer les lignes
	 */
	public Long getId()
	{
		return idContrat;
	}

	public int getMntAvoirInitial()
	{
		return mntAvoirInitial;
	}

	public void setMntAvoirInitial(int mntAvoirInitial)
	{
		this.mntAvoirInitial = mntAvoirInitial;
	}

	public int getMntSolde()
	{
		return mntSolde;
	}

	public void setMntSolde(int mntSolde)
	{
		this.mntSolde = mntSolde;
	}

	public int getNbChequePromis()
	{
		return nbChequePromis;
	}

	public void setNbChequePromis(int nbChequePromis)
	{
		this.nbChequePromis = nbChequePromis;
	}

	public int getNbChequeRecus()
	{
		return nbChequeRecus;
	}

	public void setNbChequeRecus(int nbChequeRecus)
	{
		this.nbChequeRecus = nbChequeRecus;
	}
	
	
	
	
	
	

}
