package fr.amapj.view.views.gestioncontrat.editorpart;

import fr.amapj.model.engine.Mdm;

public enum FrequenceLivraison implements Mdm
{
	UNE_SEULE_LIVRAISON("Une seule livraison") ,
	UNE_FOIS_PAR_SEMAINE("Une fois par semaine") , 
	QUINZE_JOURS("Une fois tous les quinze jours") , 
	UNE_FOIS_PAR_MOIS("Une fois par mois") ,
	AUTRE ("Autre...") ;
	
	private String propertyId;   
	   
	FrequenceLivraison(String propertyId) 
    {
        this.propertyId = propertyId;
    }
    public String prop() 
    { 
    	return propertyId; 
    }
}
