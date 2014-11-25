package fr.amapj.service.services.gestioncontrat;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.amapj.model.models.contrat.modele.GestionPaiement;
import fr.amapj.view.views.gestioncontrat.editorpart.FrequenceLivraison;

/**
 * Bean permettant l'edition des modeles de contrats
 *
 */
public class ModeleContratDTO
{
	public Long id;
	
	public String nom;
	
	public String description;

	public Long producteur;
	
	public Date dateFinInscription;
	
	public FrequenceLivraison frequence;
	
	public GestionPaiement gestionPaiement;
	
	public Date dateDebut;
	
	public Date dateFin;
	
	public String libCheque;
	
	public Date dateRemiseCheque;
	
	public Date premierCheque;
	
	public Date dernierCheque;
	
	public String textPaiement;
	
	
	public List<DateModeleContratDTO> dateLivs = new ArrayList<DateModeleContratDTO>();

	public List<LigneContratDTO> produits = new ArrayList<LigneContratDTO>();

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

	public Long getProducteur()
	{
		return producteur;
	}

	public void setProducteur(Long producteur)
	{
		this.producteur = producteur;
	}

	public Date getDateFinInscription()
	{
		return dateFinInscription;
	}

	public void setDateFinInscription(Date dateFinInscription)
	{
		this.dateFinInscription = dateFinInscription;
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

	public List<LigneContratDTO> getProduits()
	{
		return produits;
	}

	public void setProduits(List<LigneContratDTO> produits)
	{
		this.produits = produits;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public FrequenceLivraison getFrequence()
	{
		return frequence;
	}

	public void setFrequence(FrequenceLivraison frequence)
	{
		this.frequence = frequence;
	}

	public String getLibCheque()
	{
		return libCheque;
	}

	public void setLibCheque(String libCheque)
	{
		this.libCheque = libCheque;
	}

	public Date getDateRemiseCheque()
	{
		return dateRemiseCheque;
	}

	public void setDateRemiseCheque(Date dateRemiseCheque)
	{
		this.dateRemiseCheque = dateRemiseCheque;
	}

	public Date getPremierCheque()
	{
		return premierCheque;
	}

	public void setPremierCheque(Date premierCheque)
	{
		this.premierCheque = premierCheque;
	}

	public Date getDernierCheque()
	{
		return dernierCheque;
	}

	public void setDernierCheque(Date dernierCheque)
	{
		this.dernierCheque = dernierCheque;
	}

	public GestionPaiement getGestionPaiement()
	{
		return gestionPaiement;
	}

	public void setGestionPaiement(GestionPaiement gestionPaiement)
	{
		this.gestionPaiement = gestionPaiement;
	}

	public String getTextPaiement()
	{
		return textPaiement;
	}

	public void setTextPaiement(String textPaiement)
	{
		this.textPaiement = textPaiement;
	}

	public List<DateModeleContratDTO> getDateLivs()
	{
		return dateLivs;
	}

	public void setDateLivs(List<DateModeleContratDTO> dateLivs)
	{
		this.dateLivs = dateLivs;
	}
	
	

	
	
}
