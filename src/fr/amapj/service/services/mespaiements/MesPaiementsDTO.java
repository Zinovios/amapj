package fr.amapj.service.services.mespaiements;


import java.util.ArrayList;
import java.util.List;

/**
 * Informations sur les contrats d'un utilisateur
 *
 */
public class MesPaiementsDTO
{
	// Liste des paiements que doit donner l'utilisateur
	public List<PaiementAFournirDTO> paiementAFournir = new ArrayList<>();
	
	// Liste des paiements déjà données par l'utilisateur et non encaissés
	public List<PaiementFourniDTO> paiementFourni;
	
	// Liste des paiements en historiquen c'est à dire qui ont été donné au producteur 
	public List<PaiementHistoriqueDTO> paiementHistorique;
	
	

}
