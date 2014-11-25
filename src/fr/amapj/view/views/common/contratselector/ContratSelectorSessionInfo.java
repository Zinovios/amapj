package fr.amapj.view.views.common.contratselector;


/**
 * 
 *
 */
public class ContratSelectorSessionInfo
{
	
	public Long idProducteur;
	
	public Long idModeleContrat;	
	
	
	public ContratSelectorSessionInfo()
	{
		
	}
	
	public void save(Long idProducteur,Long idModeleContrat)
	{
		this.idProducteur = idProducteur;
		this.idModeleContrat = idModeleContrat;
	}
	
	
}
