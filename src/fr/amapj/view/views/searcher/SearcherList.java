package fr.amapj.view.views.searcher;

import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.view.engine.searcher.BasicSearcher;
import fr.amapj.view.engine.searcher.SearcherDefinition;


/**
 * Contient la liste de tous les searchers de l'application 
 *
 */
public class SearcherList 
{
	
	static public SearcherDefinition MODELE_CONTRAT = new BasicSearcher("Modéle de contrat", ModeleContrat.class, ModeleContrat.P.NOM);
	
	static public SearcherDefinition PRODUCTEUR = new BasicSearcher("Producteur", Producteur.class, Producteur.P.NOM);
	
	static public SearcherDefinition PRODUIT = new SDProduit();
	
	static public SearcherDefinition UTILISATEUR_SANS_CONTRAT = new SDUtilisateurSansContrat();
	
	static public SearcherDefinition UTILISATEUR_ACTIF = new SDUtilisateur();
		
}
