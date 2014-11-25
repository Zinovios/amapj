package fr.amapj.view.views.compte;

import com.vaadin.data.util.BeanItem;

import fr.amapj.service.services.moncompte.MonCompteService;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.view.engine.popup.formpopup.FormPopup;

/**
 * Permet à un utilisateur de mettre à jour ses coordonnées
 * 
 *
 */
@SuppressWarnings("serial")
public class PopupSaisieCoordonnees extends FormPopup
{

	private UtilisateurDTO utilisateurDTO;



	/**
	 * 
	 */
	public PopupSaisieCoordonnees(UtilisateurDTO utilisateurDTO)
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Modification de mes coordonnées";

		this.utilisateurDTO = utilisateurDTO;
		item = new BeanItem<UtilisateurDTO>(utilisateurDTO);

	}
	
	@Override
	protected void addFields()
	{
		
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
	protected void performSauvegarder()
	{
		new MonCompteService().updateCoordoonees(utilisateurDTO);
	}

	
}
