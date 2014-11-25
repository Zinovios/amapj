package fr.amapj.service.services.gestioncontrat;


import java.util.Date;

import fr.amapj.view.engine.tools.TableItem;

/**
 * Bean permettant l'affichage des modeles de contrats
 *
 */
public class ModeleContratSummaryDTO implements TableItem
{
	public Long id;
	
	public String nom;

	public String nomProducteur;
	
	public Long producteurId;
	
	public Date dateDebut;
	
	public Date dateFin;

	public int nbLivraison;
	
	public int nbProduit;
	
	public Date finInscription;
	
	public String etat;
	
	// Nombre d'adherents ayant souscrit à ce contrat
	public int nbInscrits;

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

	public String getNomProducteur()
	{
		return nomProducteur;
	}

	public void setNomProducteur(String nomProducteur)
	{
		this.nomProducteur = nomProducteur;
	}

	public int getNbLivraison()
	{
		return nbLivraison;
	}

	public void setNbLivraison(int nbLivraison)
	{
		this.nbLivraison = nbLivraison;
	}

	public int getNbProduit()
	{
		return nbProduit;
	}

	public void setNbProduit(int nbProduit)
	{
		this.nbProduit = nbProduit;
	}

	public Date getFinInscription()
	{
		return finInscription;
	}

	public void setFinInscription(Date finInscription)
	{
		this.finInscription = finInscription;
	}

	public String getEtat()
	{
		return etat;
	}

	public void setEtat(String etat)
	{
		this.etat = etat;
	}

	public Date getDateDebut()
	{
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut)
	{
		this.dateDebut = dateDebut;
	}

	public Date getDateFin()
	{
		return dateFin;
	}

	public void setDateFin(Date dateFin)
	{
		this.dateFin = dateFin;
	}

	public int getNbInscrits()
	{
		return nbInscrits;
	}

	public void setNbInscrits(int nbInscrits)
	{
		this.nbInscrits = nbInscrits;
	}
	
	
}
