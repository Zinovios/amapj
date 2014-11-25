package fr.amapj.view.views.maintenance;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.services.maintenance.MaintenanceService;
import fr.amapj.view.engine.popup.okcancelpopup.OKCancelPopup;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.engine.tools.BaseUiTools;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Popup pour deplacement totalement un contrat dans le temps
 *  
 */
@SuppressWarnings("serial")
public class PopupDecalageTemporelContrat extends OKCancelPopup
{
	
	private Searcher box;
	
	private TextField qte;
	
	
	/**
	 * 
	 */
	public PopupDecalageTemporelContrat()
	{
		popupTitle = "Selection du contrat à décaler ";
		saveButtonTitle = "OK";	
	}
	
	
	@Override
	protected void createContent(VerticalLayout contentLayout)
	{
		FormLayout f = new FormLayout();
		
		
		box = new Searcher(SearcherList.MODELE_CONTRAT);
		box.setWidth("80%");
		f.addComponent(box);
		
		
		qte = BaseUiTools.createIntegerField("Nombre de mois de décalage");
		qte.setWidth("80%");
		f.addComponent(qte);
		
		
		
		contentLayout.addComponent(f);
		
		
		
	}

	protected boolean performSauvegarder()
	{
		Long idModele =  (Long) box.getConvertedValue();
		int deltaInMonth = ((Integer) qte.getConvertedValue()).intValue();
		new MaintenanceService().shiftDateModeleContratAndContrats(idModele,deltaInMonth);
		return true;
	}

	
}
