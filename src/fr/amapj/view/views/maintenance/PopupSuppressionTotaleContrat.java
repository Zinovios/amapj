package fr.amapj.view.views.maintenance;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.services.maintenance.MaintenanceService;
import fr.amapj.view.engine.popup.okcancelpopup.OKCancelPopup;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Popup pour la saisie de l'utilisateur
 *  
 */
@SuppressWarnings("serial")
public class PopupSuppressionTotaleContrat extends OKCancelPopup
{
	
	private Searcher box;
	
	
	
	
	/**
	 * 
	 */
	public PopupSuppressionTotaleContrat()
	{
		popupTitle = "Selection du contrat à supprimer ";
		saveButtonTitle = "Supprimer";	
	}
	
	
	@Override
	protected void createContent(VerticalLayout contentLayout)
	{
		FormLayout f = new FormLayout();
		
		
		box = new Searcher(SearcherList.MODELE_CONTRAT);
		
		box.setWidth("80%");
		f.addComponent(box);
		contentLayout.addComponent(f);
		
		
		
	}

	protected boolean performSauvegarder()
	{
		Long idModele =  (Long) box.getConvertedValue();
		new MaintenanceService().deleteModeleContratAndContrats(idModele);
		return true;
	}

	
}
