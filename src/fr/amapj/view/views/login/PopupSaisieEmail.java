package fr.amapj.view.views.login;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.view.engine.popup.formpopup.FormPopup;

/**
 * Popup pour la saisie de la nouvelle adresse e mail
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisieEmail extends FormPopup
{
	/**
	 * 
	 */
	public PopupSaisieEmail()
	{
		popupTitle = "Perte de mot de passe";
		saveButtonTitle = "Confirmer";
	}
	
	
	protected void addFields()
	{
		// Contruction de l'item
		item.addItemProperty("email", new ObjectProperty<String>(""));
		

		
		
		// Construction des champs
		addLabel(	"Si vous avez perdu votre mot de passe, <br/>" +
					"merci de saisir votre adresse e mail ci dessous.<br/><br/>" +
					"Vous recevrez dans quelques minutes un mail qui <br/>" +
					"vous permettra de réinitialiser votre mot de passe.<br/><br/>",ContentMode.HTML);
		
		addTextField("Votre e-mail", "email");
		
		
		
	}

	protected void performSauvegarder()
	{
		String email = (String) item.getItemProperty("email").getValue();
		String msg = new PasswordManager().sendMailForResetPassword(email);
		if (msg==null)
		{
			Notification.show("Un mail vient de vous être envoyé. Merci de vérifier votre boîte mail", Type.WARNING_MESSAGE);
		}
		else
		{
			Notification.show(msg, Type.WARNING_MESSAGE);
		}
	}
}
