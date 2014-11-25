package fr.amapj.service.services.saisiepermanence;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Informations sur les distributions d'un utilisateur
 *
 */
public class MesPermanencesDTO
{
	// Toutes les distributions futures pour cet utilisateur
	public List<PermanenceDTO> permanencesFutures = new ArrayList<PermanenceDTO>();
	
	
	// Pour la semaine courante : date de début et de fin de la semaine
	public Date dateDebut;
	
	public Date dateFin;
	
	public List<PermanenceDTO> permanencesSemaine = new ArrayList<PermanenceDTO>();

}
