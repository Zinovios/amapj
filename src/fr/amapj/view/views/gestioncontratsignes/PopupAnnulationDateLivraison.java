package fr.amapj.view.views.gestioncontratsignes;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;

import fr.amapj.service.services.gestioncontratsigne.AnnulationDateLivraisonDTO;
import fr.amapj.service.services.gestioncontratsigne.GestionContratSigneService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Popup 
 * 
 *
 */
@SuppressWarnings("serial")
public class PopupAnnulationDateLivraison extends WizardFormPopup
{

	private AnnulationDateLivraisonDTO annulationDto;


	public enum Step
	{
		INFO_GENERALES, SAISIE_DATE , CONFIRMATION;
	}

	/**
	 * 
	 */
	public PopupAnnulationDateLivraison(Long mcId)
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Mise à zéro des quantités commandées sur une ou plusieurs dates de livraison";

		// Chargement de l'objet à créer
		annulationDto = new GestionContratSigneService().getAnnulationDateLivraisonDTO(mcId);
		
		item = new BeanItem<AnnulationDateLivraisonDTO>(annulationDto);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case INFO_GENERALES:
			addFieldInfoGenerales();
			break;

		case SAISIE_DATE:
			addFieldSaisieDate();
			break;
			
		case CONFIRMATION:
			addFieldConfirmation();
			break;

		default:
			break;
		}
	}

	private void addFieldInfoGenerales()
	{
		// Titre
		setStepTitle("les informations générales.");
		
		String str = 	"Cet outil va vous permettre de mettre à zéro les quantités commandées sur une ou plusieurs dates de livraisons, pour tous les adhérents à ce contrat</br>"+
						"<br/>"+
						"Exemple de cas d'utilisation : un producteur a prévu de livrer ses produits pendant 4 mois à l'AMAP<br/>"+
						"20 adhérents ont souscrits à ce contrat sur les 4 mois<br/>"+
						"Suite à un problème agricole ou autre, le producteur cesse ses livraisons au bout de 3 mois<br/>"+
						"Cet outil permet alors de mettre à zéro les quantités commandées sur le dernier mois<br/>"+
						"Il faut ensuite gérer le trop payé des adhérents sous la forme d'un avoir<br/>"+
						"<br/><br/>"+
						"Autre exemple : un producteur ne peut assurer une livraison suite à un problème de dernière minute<br/>"+
						"Cet outil permet alors de mettre à zéro les quantités commandées sur cette livraison<br/>";
										
		
		addLabel(str, ContentMode.HTML);
		

	}
	
	

	private void addFieldSaisieDate()
	{
		// Titre
		setStepTitle("les dates de la première et de la dernière livraison à annuler");
		
		String str = 	"Toutes les dates de livraisons comprises entre ces deux dates seront annulées (quantités mise à zéro)</br>";
		addLabel(str, ContentMode.HTML);
		
		//
		addDateField("Date de la première date à annuler","dateDebut");
		
		addDateField("Date de la dernière date à annuler","dateFin");

	}
	
	private void addFieldConfirmation()
	{
		// Titre
		setStepTitle("confirmation avant suppression");
		
		//
		String str = new GestionContratSigneService().getAnnulationInfo(annulationDto);
		
		addLabel("Vous allez apporter les modifications suivantes sur ce contrat:", ContentMode.HTML);
		
		addLabel(str, ContentMode.HTML);
		
		addLabel("Appuyez sur Sauvegarder pour réaliser cette modification, ou Annuler pour ne rien modifier", ContentMode.HTML);
		
	}


	

	@Override
	protected void performSauvegarder()
	{
		new GestionContratSigneService().performAnnulationDateLivraison(annulationDto);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
	
	
	
}
