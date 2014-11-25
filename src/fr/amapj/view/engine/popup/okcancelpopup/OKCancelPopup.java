package fr.amapj.view.engine.popup.okcancelpopup;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.view.engine.popup.corepopup.CorePopup;

/**
 * Popup de base , avec deux boutons Sauvegarder et Annuler
 *  
 */
@SuppressWarnings("serial")
abstract public class OKCancelPopup extends CorePopup
{
	protected Button saveButton;
	protected String saveButtonTitle = "Sauvegarder";
	protected Button cancelButton;
	protected String cancelButtonTitle = "Annuler";
	
	protected void createButtonBar()
	{		
		saveButton = addDefaultButton(saveButtonTitle, new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSauvegarder();
			}
		});
				
		
		cancelButton = addButton(cancelButtonTitle, new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAnnuler();
			}
		});
	}
	

	protected void handleAnnuler()
	{
		close();
	}

	protected void handleSauvegarder()
	{
		boolean ret = performSauvegarder();
		if (ret)
		{
			close();
		}
	}
	
	/**
	 * Retourne true si on doit fermer la fenetre, false sinon
	 * @return
	 */
	abstract protected boolean performSauvegarder();

	abstract protected void createContent(VerticalLayout contentLayout);
	
}
