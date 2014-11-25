package fr.amapj.view.views.searcher;

import java.util.List;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.service.services.searcher.SearcherService;
import fr.amapj.view.engine.searcher.SearcherDefinition;

/**
 * Affichage de tous les produits d'un producteur donné, avec nom et conditionnement
 *  
 *
 */
public class SDProduit implements SearcherDefinition
{
	@Override
	public String getTitle()
	{
		return "Produit";
	}

	@Override
	public List<? extends Identifiable> getAllElements(Object params)
	{
		Long idProducteur = (Long) params;
		return  new SearcherService().getAllProduits(idProducteur);
	}


	@Override
	public String toString(Identifiable identifiable)
	{
		Produit u = (Produit) identifiable;
		return u.getNom()+","+u.getConditionnement();
	}
	
	@Override
	public String getPropertyId()
	{
		return null;
	}

	@Override
	public Class getClazz()
	{
		return null;
	}

	@Override
	public boolean needParams()
	{
		return true;
	}

}
