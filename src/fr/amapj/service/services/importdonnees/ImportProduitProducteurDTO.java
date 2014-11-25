package fr.amapj.service.services.importdonnees;

import fr.amapj.view.engine.tools.TableItem;


/**
 * Permet la gestion des utilisateurs en masse
 * ou du changement de son état
 * 
 */
public class ImportProduitProducteurDTO implements TableItem
{
	
	public String producteur;
	public String produit;
	public String conditionnement;
	
	public Long getId()
	{
		return null;
	}

}
