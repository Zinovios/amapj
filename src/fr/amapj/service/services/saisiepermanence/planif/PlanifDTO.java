package fr.amapj.service.services.saisiepermanence.planif;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.amapj.view.views.gestioncontrat.editorpart.FrequenceLivraison;


/**
 * Permet la gestion des utilisateurs en masse
 * ou du changement de son état
 * 
 */
public class PlanifDTO 
{
	public Date dateDebut;

	public Date dateFin;
	
	public FrequenceLivraison frequence;
	
	// Nb de personnes par permanence
	public int nbPersonne;
	
	//
	public List<PlanifDateDTO> dates = new ArrayList<>();
	
	public List<PlanifUtilisateurDTO> utilisateurs = new ArrayList<>();

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

	public int getNbPersonne()
	{
		return nbPersonne;
	}

	public void setNbPersonne(int nbPersonne)
	{
		this.nbPersonne = nbPersonne;
	}

	public List<PlanifUtilisateurDTO> getUtilisateurs()
	{
		return utilisateurs;
	}

	public void setUtilisateurs(List<PlanifUtilisateurDTO> utilisateurs)
	{
		this.utilisateurs = utilisateurs;
	}

	public FrequenceLivraison getFrequence()
	{
		return frequence;
	}

	public void setFrequence(FrequenceLivraison frequence)
	{
		this.frequence = frequence;
	}

	public List<PlanifDateDTO> getDates()
	{
		return dates;
	}

	public void setDates(List<PlanifDateDTO> dates)
	{
		this.dates = dates;
	}

}
