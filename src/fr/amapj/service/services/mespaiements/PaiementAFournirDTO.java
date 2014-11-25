package fr.amapj.service.services.mespaiements;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 *
 */
public class PaiementAFournirDTO
{
	
	// Nom du contrat
	public String nomContrat;

	// Ordre du cheque
	public String libCheque;
	
	// Date limite de remise des paiements 
	public Date dateRemise; 
	
	// Liste de paiements
	public List<DetailPaiementAFournirDTO> paiements = new ArrayList<>();
		
	
}
