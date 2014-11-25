package fr.amapj.service.services.access;

import fr.amapj.view.engine.tools.TableItem;

/**
 * Permet la gestion des droits dans le fichier de base
 * 
 */
public class AdminTresorierDTO implements TableItem
{
	public Long id;
	
	public Long utilisateurId;
	

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getUtilisateurId()
	{
		return utilisateurId;
	}

	public void setUtilisateurId(Long utilisateurId)
	{
		this.utilisateurId = utilisateurId;
	}

	
	
}
