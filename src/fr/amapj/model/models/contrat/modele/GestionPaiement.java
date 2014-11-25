/**
 * Permet de definir l'état d'un modele de contrat
 * 
 * 
 */
package fr.amapj.model.models.contrat.modele;

import fr.amapj.model.engine.Mdm;

public enum GestionPaiement implements Mdm
{
	// Pas de gestion des paiements
	NON_GERE("Pas de gestion des paiements"),
	
	// gestion standard
	GESTION_STANDARD("Gestion standard") ;
	
	private String propertyId;   
	   
	GestionPaiement(String propertyId) 
    {
        this.propertyId = propertyId;
    }
	
    public String prop() 
    { 
    	return propertyId; 
    }

}
