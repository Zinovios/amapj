package fr.amapj.service.services.mescontrats;


import java.util.ArrayList;
import java.util.List;

import fr.amapj.model.models.contrat.modele.GestionPaiement;

/**
 * Informations sur les paiements de ce contrat
 *
 */
public class InfoPaiementDTO
{
	// Lignes de paiement, ordonnées
	// Toutes les lignes sont présentes, même celles qui sont à zéro
	public List<DatePaiementDTO> datePaiements = new ArrayList<DatePaiementDTO>();
	
	public GestionPaiement gestionPaiement;
	
	public String textPaiement;
	
	public int avoirInitial;
	
	
	
}
