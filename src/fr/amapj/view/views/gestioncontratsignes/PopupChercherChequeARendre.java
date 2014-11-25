package fr.amapj.view.views.gestioncontratsignes;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;

import fr.amapj.service.services.gestioncontratsigne.AnnulationDateLivraisonDTO;
import fr.amapj.service.services.gestioncontratsigne.GestionContratSigneService;
import fr.amapj.service.services.mespaiements.MesPaiementsService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Popup 
 * 
 *
 */
@SuppressWarnings("serial")
public class PopupChercherChequeARendre extends WizardFormPopup
{
	
	private Long mcId;


	public enum Step
	{
		INFO_GENERALES,  AFFICHAGE;
	}

	/**
	 * 
	 */
	public PopupChercherChequeARendre(Long mcId)
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Rechercher les chèques à rendre aux amapiens";
		
		this.mcId = mcId;
	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case INFO_GENERALES:
			addFieldInfoGenerales();
			break;

		case AFFICHAGE:
			addFieldAffichage();
			break;

		default:
			break;
		}
	}

	private void addFieldInfoGenerales()
	{
		// Titre
		setStepTitle("les informations générales.");
		
		String str = 	"Cet outil va vous permettre de rechercher les chéques à rendre aux amapiens, dans le cas d'un trop payé.</br>"+
						"<br/>"+
						"Exemple de cas d'utilisation : un producteur a prévu de livrer ses produits pendant 4 mois à l'AMAP<br/>"+
						"20 adhérents ont souscrits à ce contrat sur les 4 mois<br/>"+
						"Suite à un problème quelconque, le producteur cesse ses livraisons au bout de 3 mois<br/>"+
						"Imaginons un amapien qui a réalisé 4 chéques de 20 euros, mais suite à l'arrêt des livraisons il n'a reçu que 50 euros de produits<br/>"+
						"<br/>"+
						"Dans ce cas, cet outil va indiquer qu'il faut rendre le dernier chèque de 20 euros<br/>";
						
										
		
		addLabel(str, ContentMode.HTML);
		

	}
	
	

	private void addFieldAffichage()
	{
		// Titre
		setStepTitle("affichage des chèques à rendre");
		
		String str = new MesPaiementsService().chercherChequeARendre(mcId);
		if (str.length()==0)
		{
			str = "Il n'y a aucun chèque à rendre";
		}
		
		addLabel(str, ContentMode.HTML);

	}
	

	

	@Override
	protected void performSauvegarder()
	{
		// Nothing to do
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
	
	
	
}
