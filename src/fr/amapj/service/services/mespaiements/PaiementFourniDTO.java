package fr.amapj.service.services.mespaiements;


import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 */
public class PaiementFourniDTO
{
	
	// Nom du mois d'encaissement
	public String moisPaiement;
	
	// Total à payer pour le mois
	public int totalMois;
	
	// Liste de paiements
	public List<DetailPaiementFourniDTO> paiements = new ArrayList<>();
		
	
}
