package fr.amapj.service.services.meslivraisons;


import java.util.ArrayList;
import java.util.List;

import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.reel.Contrat;
import fr.amapj.model.models.contrat.reel.ContratCell;
import fr.amapj.model.models.fichierbase.Producteur;

/**
 * Informations sur les contrats d'un utilisateur
 *
 */
public class ProducteurLivraisonsDTO
{
	public String producteur;
	
	public String modeleContrat; 
	
	public Long idModeleContrat;
	
	public Long idModeleContratDate;
	
	public List<QteProdDTO> produits = new ArrayList<QteProdDTO>();
	
	

}
