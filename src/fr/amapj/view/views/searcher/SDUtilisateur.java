package fr.amapj.view.views.searcher;

import java.util.List;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.gestioncontratsigne.GestionContratSigneService;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.view.engine.searcher.SearcherDefinition;

public class SDUtilisateur implements SearcherDefinition
{
	@Override
	public String getTitle()
	{
		return "Utilisateur";
	}

	@Override
	public List<? extends Identifiable> getAllElements(Object params)
	{
		return  new UtilisateurService().getAllUtilisateursActif();
	}


	@Override
	public String toString(Identifiable identifiable)
	{
		Utilisateur u = (Utilisateur) identifiable;
		return u.getNom()+" "+u.getPrenom();
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
		return false;
	}

}
