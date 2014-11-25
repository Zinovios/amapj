package fr.amapj.view.engine.ui;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.view.engine.popup.formpopup.FormPopup;

/**
 * Popup pour la saisie de la nouvelle adresse e mail
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisieNewPassword extends FormPopup
{
		
	String resetPasswordSalt;

	Utilisateur u;
	
	boolean valid = false;

	/**
	 * 
	 */
	public PopupSaisieNewPassword(String resetPasswordSalt)
	{
		popupTitle = "Changement de votre password"; 
		this.resetPasswordSalt = resetPasswordSalt;	
	}
	
	
	protected void addFields()
	{
		u = new PasswordManager().findUserWithResetPassword(resetPasswordSalt);
		
		if (u==null)
		{
			addLabel("Demande invalide", ContentMode.TEXT);
			return ;
		}
		
		
		Date datLimit = DateUtils.addDays(new Date(), -1);
		if (u.getResetPasswordDate().before(datLimit))
		{
			addLabel("Votre demande est trop ancienne", ContentMode.TEXT);
			return ;
		}
		
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
		
		valid = true;
	}

	protected void performSauvegarder()
	{
		if (valid)
		{
			String newValue = (String) item.getItemProperty("pwd").getValue();
			new PasswordManager().setUserPassword(u.getId(),newValue);
		}
	}
}
