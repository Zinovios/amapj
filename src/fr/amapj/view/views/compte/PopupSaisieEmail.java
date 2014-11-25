package fr.amapj.view.views.compte;

import com.vaadin.data.util.ObjectProperty;

import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.moncompte.MonCompteService;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.view.engine.popup.formpopup.FormPopup;

/**
 * Popup pour la saisie de la nouvelle adresse e mail
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisieEmail extends FormPopup
{
		
	private UtilisateurDTO u;


	/**
	 * 
	 */
	public PopupSaisieEmail(UtilisateurDTO u)
	{
		popupTitle = "Changement de votre e-mail";
		this.u = u;
		
	}
	
	
	protected void addFields()
	{
		// Contruction de l'item
		item.addItemProperty("email", new ObjectProperty<String>(u.getEmail()));
		

		// Construction des champs
		addTextField("Votre nouvel e-mail", "email");
		
		
		
	}

	protected void performSauvegarder()
	{
		String newValue = (String) item.getItemProperty("email").getValue();
		new MonCompteService().setNewEmail(u.getId(),newValue);
	}

	
	

}
