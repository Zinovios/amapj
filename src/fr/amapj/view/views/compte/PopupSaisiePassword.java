package fr.amapj.view.views.compte;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.view.engine.popup.formpopup.FormPopup;

/**
 * Popup pour la saisie de la nouvelle adresse e mail
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisiePassword extends FormPopup
{
		
	private Long u;


	/**
	 * 
	 */
	public PopupSaisiePassword(UtilisateurDTO u)
	{
		popupTitle = "Changement de votre password";
		this.u = u.getId();
		
	}
	
	public PopupSaisiePassword(Long idUtitilisateur)
	{
		popupTitle = "Changement de votre password";
		this.u = idUtitilisateur;
		
	}
	
	
	protected void addFields()
	{
		// Contruction de l'item
		item.addItemProperty("pwd", new ObjectProperty<String>(""));
		

		// Construction des champs
		PasswordField f = new PasswordField("Nouveau mot de passe");
		binder.bind(f, "pwd");
		// f.addValidator(new BeanValidator(getClazz(), propertyId));
		f.setNullRepresentation("");
		f.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
		f.setWidth("80%");
		form.addComponent(f);
		
		
		
		
	}

	protected void performSauvegarder()
	{
		String newValue = (String) item.getItemProperty("pwd").getValue();
		new PasswordManager().setUserPassword(u,newValue);
	}

	
	

}
