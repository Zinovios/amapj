package fr.amapj.view.engine.searcher;

import java.util.List;

import fr.amapj.model.engine.Identifiable;


/**
 * Un searcher doit pouvoir :
 * - récupérer tous les élements
 * - les afficher 
 * - accepter des paramètres en entrée pour les requetes
 */
public interface SearcherDefinition 
{
	public String getTitle();
	
	
	/**
	 * Indique si ce searcher a besoin de paramètres ou non 
	 * @return
	 */
	public boolean needParams();
	
	
	public List<? extends Identifiable> getAllElements(Object params);
	
	/**
	 * Si cette méthode retourne une valeur non nulle, alors la méthode
	 * toString(Identifiable identifiable) n'est jamais appelée
	 * 
	 */
	public String getPropertyId();
	
	/**
	 * 
	 */
	public String toString(Identifiable identifiable);
	
	/**
	 * Classe des élements de la liste 
	 * Peut retourner null dans le cas ou getPropertyId() retourne une valeur non nulle
	 */
	public Class getClazz();
	

}
