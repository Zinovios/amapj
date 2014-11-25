package fr.amapj.view.views.gestioncontratsignes;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.view.engine.popup.okcancelpopup.OKCancelPopup;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Popup pour la saisie de l'utilisateur
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisieUtilisateur extends OKCancelPopup
{
	
	private Searcher box;
	
	private Long idUtilisateur = null;
	
	private Long idModelContrat;
	
	/**
	 * 
	 */
	public PopupSaisieUtilisateur(Long idModelContrat)
	{
		this.idModelContrat = idModelContrat;
		
		popupTitle = "Selection de l'utilisateur";
		saveButtonTitle = "Continuer ...";
		
	}
	
	
	@Override
	protected void createContent(VerticalLayout contentLayout)
	{
		FormLayout f = new FormLayout();
		
		
		box = new Searcher(SearcherList.UTILISATEUR_SANS_CONTRAT);
		box.setParams(idModelContrat);
		
		box.setWidth("80%");
		f.addComponent(box);
		contentLayout.addComponent(f);
		
		
		
	}

	protected boolean performSauvegarder()
	{
		idUtilisateur =  (Long) box.getConvertedValue();
		return true;
	}

	public Long getUserId()
	{
		return idUtilisateur;
	}
}
