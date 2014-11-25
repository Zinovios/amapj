package fr.amapj.view.views.suiviacces;

import com.vaadin.data.util.ObjectProperty;

import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.popup.formpopup.FormPopup;

/**
 * Popup pour la saisie d'un message a envoyer a tout le monde
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisieMessage extends FormPopup
{
	
	String message = "";

	/**
	 * 
	 */
	public PopupSaisieMessage()
	{
		popupTitle = "Envoi d'un message à tous les utilisateurs";
		saveButtonTitle = "Envoyer";
		
	}
	
	
	protected void addFields()
	{
		// Contruction de l'item
		item.addItemProperty("msg", new ObjectProperty<String>(message));
		

		// Construction des champs
		addTextAeraField("Message", "msg");
		
		
		
	}

	protected void performSauvegarder()
	{
		String newValue = (String) item.getItemProperty("msg").getValue();
		SessionManager.broadcast(newValue);
	}

	
	

}
