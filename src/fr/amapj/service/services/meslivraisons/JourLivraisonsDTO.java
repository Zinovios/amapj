package fr.amapj.service.services.meslivraisons;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.amapj.service.services.saisiepermanence.PermanenceDTO;

/**
 * Informations sur les contrats d'un utilisateur
 *
 */
public class JourLivraisonsDTO
{
	public Date date;
	
	public List<ProducteurLivraisonsDTO> producteurs = new ArrayList<ProducteurLivraisonsDTO>();
	
	// Si non null : indique que l'utilisateur doit réaliser la distribution 	
	public PermanenceDTO distribution;

}
