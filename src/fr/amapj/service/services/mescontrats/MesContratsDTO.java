package fr.amapj.service.services.mescontrats;


import java.util.ArrayList;
import java.util.List;

/**
 * Informations sur les contrats d'un utilisateur
 *
 */
public class MesContratsDTO
{
	// Liste des nouveaux contrats auxquels peut subscrire l'utilisateur
	List<ContratDTO> newContrats;
	
	// Liste des contrats déjà souscrits
	List<ContratDTO> existingContrats;
	
	
	public MesContratsDTO()
	{
		newContrats = new ArrayList<ContratDTO>();
		existingContrats = new ArrayList<ContratDTO>();
	}


	public List<ContratDTO> getNewContrats()
	{
		return newContrats;
	}


	public void setNewContrats(List<ContratDTO> newContrats)
	{
		this.newContrats = newContrats;
	}


	public List<ContratDTO> getExistingContrats()
	{
		return existingContrats;
	}


	public void setExistingContrats(List<ContratDTO> existingContrats)
	{
		this.existingContrats = existingContrats;
	}
	
	

}
