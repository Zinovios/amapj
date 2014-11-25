package fr.amapj.view.views.remiseproducteur;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Notification;

import fr.amapj.service.services.remiseproducteur.PaiementRemiseDTO;
import fr.amapj.service.services.remiseproducteur.RemiseDTO;
import fr.amapj.service.services.remiseproducteur.RemiseProducteurService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;

/**
 * Permet de saisir les remises
 * 
 *
 */
@SuppressWarnings("serial")
public class RemiseEditorPart extends WizardFormPopup
{
	private RemiseDTO remiseDTO;

	public enum Step
	{
		ACCUEIL, AFFICHAGE , CONFIRMATION;
	}

	/**
	 * 
	 */
	public RemiseEditorPart(RemiseDTO remiseDTO)
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Réalisation d'une remise";
		
		this.remiseDTO = remiseDTO;
		
		item = new BeanItem<RemiseDTO>(remiseDTO);

	}
	
	@Override
	protected void addFields(Enum step)
	{	
		switch ( (Step) step)
		{
		case ACCUEIL:
			addFieldAccueil();
			break;

		case AFFICHAGE:
			addFieldAffichage();
			break;
			
		case CONFIRMATION :
			addFieldConfirmation();
			break;
			
		default:
			break;
		}
	}

	private void addFieldAccueil()
	{
		// Titre
		setStepTitle("saisie de la date de la remise");
		
		String montant = new CurrencyTextFieldConverter().convertToString(remiseDTO.mnt)+" €";
		String text = 	"Vous allez valider une remise de chèques à un producteur<br/>"+
						"Mois de la remise : "+remiseDTO.moisRemise+"<br/>"+
						"Montant total de la remise "+montant+"<br/><br/>"+
						"Merci de saisir ci dessous la date réelle de remise des chèques";
		
		addLabel(text, ContentMode.HTML);
		
		//
		addDateField("Date réelle de la remise", "dateReelleRemise");

	}

	private void addFieldAffichage()
	{
		// Titre
		setStepTitle("les chèques à inclure dans la remise");
		
		
		String text = 	"Voici la liste des chèques à inclure dans la remise :<br/>";
		
		for (PaiementRemiseDTO paiement : remiseDTO.paiements)
		{
			String montant = new CurrencyTextFieldConverter().convertToString(paiement.montant)+" €";
			text = text+paiement.nomUtilisateur+" "+paiement.prenomUtilisateur+" - Montant = "+montant+"<br/>";
		}
		
		addLabel(text, ContentMode.HTML);
		
	}
	
	
	private void addFieldConfirmation()
	{
		// Titre
		setStepTitle("confirmation");
		
		String text = 	"Confirmez vous avoir tous les chèques ? <br/>";
		
		addLabel(text, ContentMode.HTML);
		
	}


	@Override
	protected void performSauvegarder()
	{
		new RemiseProducteurService().performRemise(remiseDTO);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
