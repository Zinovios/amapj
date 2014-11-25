package fr.amapj.service.services.meslivraisons;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Informations sur les contrats d'un utilisateur
 *
 */
public class MesLivraisonsDTO
{
	public Date dateDebut;
	
	public Date dateFin;
	
	public List<JourLivraisonsDTO> jours = new ArrayList<JourLivraisonsDTO>();

}
