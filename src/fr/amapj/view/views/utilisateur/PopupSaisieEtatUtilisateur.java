package fr.amapj.view.views.utilisateur;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.ComboBox;

import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.view.engine.popup.formpopup.FormPopup;

/**
 * Popup pour la saisie de l'état d'un utilisateur
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisieEtatUtilisateur extends FormPopup
{
		
	private EtatUtilisateur etat = EtatUtilisateur.ACTIF;
	
	private UtilisateurDTO utilisateurDTO;


	/**
	 * 
	 */
	public PopupSaisieEtatUtilisateur(UtilisateurDTO utilisateurDTO)
	{
		popupTitle = "Saisie de l'état d'un utilisateur";
		this.utilisateurDTO = utilisateurDTO;
		
	}
	
	
	protected void addFields()
	{
		// Contruction de l'item
		item.addItemProperty("etat", new ObjectProperty<EtatUtilisateur>(etat));
		
		// Construction des champs
		ComboBox box = addComboEnumField("Nouvel état", utilisateurDTO.etatUtilisateur , "etat");
		box.setRequired(true);	
	}

	protected void performSauvegarder()
	{
		EtatUtilisateur newValue = (EtatUtilisateur) item.getItemProperty("etat").getValue();
		new UtilisateurService().updateEtat(newValue,utilisateurDTO.id);
	}
}
