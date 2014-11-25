package fr.amapj.service.services.utilisateur;

import java.util.List;


/**
 * Permet la gestion des utilisateurs en masse
 * ou du changement de son état
 * 
 */
public class CotisationDTO 
{
	public List<UtilisateurDTO> utilisateurs;

	public List<UtilisateurDTO> getUtilisateurs()
	{
		return utilisateurs;
	}

	public void setUtilisateurs(List<UtilisateurDTO> utilisateurs)
	{
		this.utilisateurs = utilisateurs;
	}
	
	
}
