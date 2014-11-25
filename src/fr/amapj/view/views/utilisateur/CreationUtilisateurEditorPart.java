package fr.amapj.view.views.utilisateur;

import java.util.List;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;

import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.popup.messagepopup.MessagePopup;
import fr.amapj.view.views.importdonnees.UtilisateurImporter;

/**
 * Permet uniquement de creer des contrats
 * 
 *
 */
@SuppressWarnings("serial")
public class CreationUtilisateurEditorPart extends WizardFormPopup
{

	private UtilisateurDTO utilisateurDTO;



	public enum Step
	{
		SAISIE, ENVOI;
	}

	/**
	 * 
	 */
	public CreationUtilisateurEditorPart()
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Création d'un utilisateur";

		utilisateurDTO = new UtilisateurDTO();
		item = new BeanItem<UtilisateurDTO>(utilisateurDTO);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case SAISIE:
			addFieldSaisie();
			break;

		case ENVOI:
			addFieldEnvoi();
			break;

		default:
			break;
		}
	}

	private void addFieldSaisie()
	{
		// Titre
		setStepTitle("les informations du nouvel utilisateur");
		
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

	private void addFieldEnvoi()
	{
		UtilisateurImporter importer = new UtilisateurImporter();
		List<String> check = importer.checkThisElement(utilisateurDTO);
		
		if (check!=null)
		{
			setBackOnlyMode();
			
			// Titre
			setErrorTitle("Impossible de créer cet utilisateur");
			
			addLabel("Il n'est pas possible de créer cet utilisateur.Raison : ", ContentMode.HTML);
			
			for (String str : check)
			{
				addLabel(str, ContentMode.HTML);
			}
		}
		else
		{
			
			// Titre
			setStepTitle("confirmation avant envoi");
			
			String str = 	"Vous avez demandé à créer un nouvel utilisateur.<br/><br/>"+
							"Nom = <b>"+utilisateurDTO.nom+"</b><br/>"+
							"Prenom= <b>"+utilisateurDTO.prenom+"</b><br/>"+
							"E mail = <b>"+utilisateurDTO.email+"</b><br/><br/>";
			
			ParametresDTO param = new ParametresService().getParametres();
			if (param.serviceMailActif==true)
			{
				str = str+"Un mot de passe va être automatiquement généré et un e mail sera envoyé à l'utilisateur";
			}
			else
			{
				str = str+"Votre service de mail n'est pas actif, le mot de passe va être affiché et vous devrez le transmettre à l'utilisateur";
			}
			str = str+"<br/><br/>Cliquez sur Sauvegarder pour terminer, ou Annuler pour ne rien faire";
			
			addLabel(str, ContentMode.HTML);
		}
	
	}


	@Override
	protected void performSauvegarder()
	{
		String clearPassword = new UtilisateurService().createNewUser(utilisateurDTO,true);
		if (clearPassword!=null)
		{
			MessagePopup popup = new MessagePopup("Mot de passe", "Le mot de passe est "+clearPassword);
			MessagePopup.open(popup);
		}
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
