package fr.amapj.service.services.mescontrats;


import java.util.Date;

import fr.amapj.model.models.contrat.reel.EtatPaiement;

/**
 * Informations sur les paiements de ce contrat
 *
 */
public class DatePaiementDTO
{
	// Jamais null
	public Long idModeleContratDatePaiement;

	// Peut etre null pour une creation
	public Long idPaiement;

	// 
	public Date datePaiement;
	
	// Contient les montants
	public int montant;
	
	// Contient l'état du paiement
	public EtatPaiement etatPaiement;
	
	
	// Indique si cette date de paiement est exclue
	public boolean excluded;
	
	
	
}
