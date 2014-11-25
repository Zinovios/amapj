/**
 * Permet de definir l'état d'un paiement
 * 
 * 
 */
package fr.amapj.model.models.contrat.reel;

public enum EtatPaiement
{
	// Le paiement n'a pas encore été donné par l'utilisateur
	A_FOURNIR ,
	
	// le paiement a été réceptionné à l'AMAP
	AMAP ,
	
	// Le paiement a été donné au producteur
	PRODUCTEUR
	;

}
