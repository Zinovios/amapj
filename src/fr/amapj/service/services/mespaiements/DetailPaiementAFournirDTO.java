package fr.amapj.service.services.mespaiements;

import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;


/**
 * 
 *
 */
public class DetailPaiementAFournirDTO
{
	// Mois du paiement sousla forme Janvier 2014 , Fevrier 2014 , ..
	public String moisPaiement;
	
	// Montant du paiement
	public int montant;
	
	// Nombre de cheque
	public int nbCheque;
	

	public String formatPaiement()
	{
		String mt = new CurrencyTextFieldConverter().convertToString(montant)+" €";
		String str ;
		if (nbCheque==1)
		{
			str = "1 chèque de "+mt+" qui sera débité en "+moisPaiement;
			
		}
		else	
		{
			str = ""+nbCheque+" chèques de "+mt+" qui seront débités en "+moisPaiement;
		}
		return str;
	}
	
			
}
