package fr.amapj.service.services.mespaiements;

import fr.amapj.model.models.contrat.reel.EtatPaiement;

/**
 * 
 *
 */
public class DetailPaiementFourniDTO
{
	// Montant du paiement
	public int montant;

	// Nom du contrat
	public String nomContrat;

	// Ordre du cheque
	public String libCheque;
	
	// Etat du cheque
	public EtatPaiement etatPaiement;
}
