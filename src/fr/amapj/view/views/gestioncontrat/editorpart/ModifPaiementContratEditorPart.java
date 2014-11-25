package fr.amapj.view.views.gestioncontrat.editorpart;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

import fr.amapj.common.DateUtils;
import fr.amapj.common.DebugUtil;
import fr.amapj.model.models.contrat.modele.GestionPaiement;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Permet de modifier les infos de paiements
 * 
 *
 */
@SuppressWarnings("serial")
public class ModifPaiementContratEditorPart extends WizardFormPopup
{
	private ModeleContratDTO modeleContrat;

	public enum Step
	{
		CHOIX_TYPE, PAIEMENT;
	}

	/**
	 * 
	 */
	public ModifPaiementContratEditorPart(Long id)
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Modification des conditions de paiement d'un contrat";
		
		// Chargement de l'objet  à modifier
		modeleContrat = new GestionContratService().loadModeleContrat(id);
		
		item = new BeanItem<ModeleContratDTO>(modeleContrat);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case CHOIX_TYPE:
			addFieldChoixTypPaiement();
			break;

		case PAIEMENT:
			addFieldPaiement();
			break;

		default:
			break;
		}
	}

	private void addFieldChoixTypPaiement()
	{
		// Titre
		setStepTitle("le type de paiement");
		
		//
		addComboEnumField("Gestion des paiements", GestionPaiement.NON_GERE, "gestionPaiement");

	}

	private void addFieldPaiement()
	{
		setStepTitle("les informations sur le paiement");
		
		if (modeleContrat.gestionPaiement==GestionPaiement.GESTION_STANDARD)
		{	
			addTextField("Ordre du chèque", "libCheque");
			
			if (modeleContrat.frequence==FrequenceLivraison.UNE_SEULE_LIVRAISON)
			{
				PopupDateField p = addDateField("Date de remise du chèque", "dateRemiseCheque");
				if (modeleContrat.getDateRemiseCheque()==null)
				{
					p.setValue(modeleContrat.dateDebut);
				}
			}
			else
			{
				PopupDateField p = addDateField("Date de remise des chèques", "dateRemiseCheque");
				if (modeleContrat.getDateRemiseCheque()==null)
				{
					p.setValue(modeleContrat.dateFinInscription);
				}
				
				p = addDateField("Date du premier paiement", "premierCheque");
				if (modeleContrat.getPremierCheque()==null)
				{
					p.setValue(DateUtils.firstDayInMonth(modeleContrat.dateDebut));
				}
				
				
				p = addDateField("Date du dernier paiement", "dernierCheque");
				if (modeleContrat.getDernierCheque()==null)
				{
					p.setValue(DateUtils.firstDayInMonth(modeleContrat.dateFin)); 
				}
			}
		}
		else
		{
			TextField f = (TextField) addTextField("Texte affiché dans la fenêtre paiement", "textPaiement");
			f.setMaxLength(2048);
			f.setHeight(5, Unit.CM);
		}
	}


	@Override
	protected void performSauvegarder()
	{
		new GestionContratService().updateInfoPaiement(modeleContrat);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
