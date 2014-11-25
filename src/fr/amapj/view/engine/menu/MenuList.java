package fr.amapj.view.engine.menu;
/**
 * Contient la liste des Menus disponibles dans l'application
 * 
 * Si le titre n'est pas précisé, alors c'est simplement le nom de l'entrée
 * 
 */
public enum MenuList
{ 	
	
	// Partie standard
	
	MES_CONTRATS("Mes contrats"),
	
	MES_LIVRAISONS("Mes livraisons"),
	
	MES_PAIEMENTS("Mes paiements"),
	
	MON_COMPTE("Mon compte"),
	
	LISTE_PRODUCTEUR_REFERENT("Producteurs / Référents"),
	
	LISTE_ADHERENTS("Liste des adhérents"),
	
	PLANNING_DISTRIBUTION("Planning des permanences"),
	
	HISTORIQUE_CONTRATS("Historique de mes contrats"),
	
	HISTORIQUE_PAIEMENTS("Historique de mes paiements"),
	
	
	// Partie producteur
	
	LIVRAISONS_PRODUCTEUR("Livraisons d'un producteur") ,
	
	CONTRATS_PRODUCTEUR("Contrats d'un producteur"),
	
	
	// Partie référents
	
	GESTION_CONTRAT("Gestion des contrats vierges"),
	
	GESTION_CONTRAT_SIGNES("Gestion des contrats signés"),
	
	RECEPTION_CHEQUES("Réception des chèques"),
	
	REMISE_PRODUCTEUR("Remise aux producteurs"),
	
	PRODUIT("Gestion des produits") ,
	
	SAISIE_PLANNING_DISTRIBUTION("Planification des permanences") ,
	
	// Partie trésorier
	
	UTILISATEUR("Gestion des utilisateurs") , 
	
	PRODUCTEUR("Gestion des producteurs") ,
	
	IMPORT_DONNEES("Import des données"),
	
	LISTE_TRESORIER("Liste des trésoriers"),
	
	
	// Partie admnistrateur
	
	PARAMETRES("Paramètres généraux"),
	
	LISTE_ADMIN("Liste des administrateurs"),
	
	SUIVI_ACCES("Suivi accés"),
	
	MAINTENANCE("Maintenance"),
	
	ENVOI_MAIL("Envoyer un mail");

	
	
	
	
	private String title;   
	   
	MenuList(String title) 
    {
        this.title = title;
    }
    public String getTitle() 
    { 
    	if (title==null)
    	{
    		return name();
    	}
    	else
    	{
    		return title;
    	}
    	 
    }
	
}
