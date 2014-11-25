package fr.amapj.view.views.utilisateur;

import com.vaadin.data.util.BeanItem;

import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.views.importdonnees.UtilisateurImporter;

/**
 * Permet de modifier les utilisateurs
 * 
 *
 */
@SuppressWarnings("serial")
public class ModificationUtilisateurEditorPart extends FormPopup
{

	private UtilisateurDTO utilisateurDTO;



	/**
	 * 
	 */
	public ModificationUtilisateurEditorPart(UtilisateurDTO utilisateurDTO)
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Modification d'un utilisateur";

		this.utilisateurDTO = utilisateurDTO;
		item = new BeanItem<UtilisateurDTO>(utilisateurDTO);

	}
	
	@Override
	protected void addFields()
	{
		// Champ 1
		addTextField("Nom", "nom");

		// Champ 2
		addTextField("Prenom", "prenom");

		// Champ 3
		addTextField("E mail", "email");
		
		// Champ 4
		addTextField("Téléphone 1", "numTel1");
		
		// Champ 5
		addTextField("Téléphone 2", "numTel2");

		// Champ 6
		addTextField("Adresse", "libAdr1");
		
		// Champ 7
		addTextField("Code postal", "codePostal");
		
		// Champ 8
		addTextField("Ville", "ville");

	}

	

	@Override
	protected void performSauvegarder() throws OnSaveException
	{
		// Verification
		new UtilisateurImporter().checkThisElementAsException(utilisateurDTO);
		
		// Sauvegarde
		new UtilisateurService().updateUtilisateur(utilisateurDTO);
		
	}

	
}
