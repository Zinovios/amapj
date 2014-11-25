package fr.amapj.view.views.droits;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.models.fichierbase.RoleAdmin;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.access.AdminTresorierDTO;
import fr.amapj.view.engine.basicform.BasicFormListPart;
import fr.amapj.view.engine.basicform.FormInfo;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.views.searcher.SearcherList;

public class DroitsAdministrateurBasicForm extends BasicFormListPart
{

	/*
	 * Partie vue Liste
	 */

	protected void createColumn()
	{
		addColumn("utilisateur.nom", "Nom");
		addColumn("utilisateur.prenom", "Prénom");
		orderBy("utilisateur.nom" , "utilisateur.prenom");
	}

	protected String getListPartTitle()
	{
		return "Liste des administrateurs";
	}

	protected String getListPartInputPrompt()
	{
		return "Rechercher par nom";
	}

	protected void setFilter(BeanItemContainer container, String textFilter)
	{
		Or or = new Or(new Like("utilisateur.nom", textFilter + "%", false), new Like("utilisateur.prenom", textFilter + "%", false));
		container.addContainerFilter(or);
	}

	/*
	 * Partie vue Form
	 */

	protected Class<? extends Identifiable> getClazz()
	{
		return RoleAdmin.class;
	}

	protected void createFormField(FormInfo formInfo)
	{
		
		addFieldSearcher(formInfo, "utilisateurId", "Utilisateur",new Searcher(SearcherList.UTILISATEUR_ACTIF));
		
	}
	
	protected String getEditorPartTitle(boolean addMode)
	{
		if (addMode==true)
		{
			return "Création d'un administrateur";
		}
		else
		{
			return "Modification d'un administrateur";
		}
	}
	
	

	@Override
	protected Object loadDTO(Long id)
	{
		return new AccessManagementService().getAdminDto(id);
	}

	@Override
	protected void saveDTO(Object dto, boolean isNew)
	{
		new AccessManagementService().updateAdminDto( (AdminTresorierDTO) dto, isNew);
		
	}
}
