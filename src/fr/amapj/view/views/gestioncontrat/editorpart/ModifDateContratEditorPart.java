package fr.amapj.view.views.gestioncontrat.editorpart;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;

import fr.amapj.service.services.gestioncontrat.DateModeleContratDTO;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Permet uniquement de creer des contrats
 * 
 *
 */
@SuppressWarnings("serial")
public class ModifDateContratEditorPart extends WizardFormPopup
{
	private ModeleContratDTO modeleContrat;

	public enum Step
	{
		CHOIX_FREQUENECE, DATE_LIVRAISON;
	}

	/**
	 * 
	 */
	public ModifDateContratEditorPart(Long id)
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Modification des dates d'un contrat";
		
		// Chargement de l'objet  à modifier
		modeleContrat = new GestionContratService().loadModeleContrat(id);
		
		item = new BeanItem<ModeleContratDTO>(modeleContrat);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case CHOIX_FREQUENECE:
			addFieldChoixFrequence();
			break;

		case DATE_LIVRAISON:
			addFieldDateLivraison();
			break;

		default:
			break;
		}
	}

	private void addFieldChoixFrequence()
	{
		// Titre
		setStepTitle("frequence des livraisons");
		
		//
		addComboEnumField("Fréquence des livraisons", FrequenceLivraison.UNE_FOIS_PAR_SEMAINE, "frequence");
		
		//
		addLabel("Nota : si vous souhaitez modifier uniquement une date ou deux dans la liste et ne pas tout recalculer,<br/>"
				+ "merci de choisir \"Autre ...\" dans la liste déroulante ci dessus.", ContentMode.HTML);

	}

	private void addFieldDateLivraison()
	{
		// Titre
		setStepTitle("les dates de livraison");
		
		if (modeleContrat.frequence==FrequenceLivraison.UNE_SEULE_LIVRAISON)
		{
			addDateField("Date de la livraison", "dateDebut");
		}
		else if (modeleContrat.frequence==FrequenceLivraison.AUTRE)
		{
			//
			CollectionEditor<DateModeleContratDTO> f1 = new CollectionEditor<DateModeleContratDTO>("Liste des dates", (BeanItem) item, "dateLivs", DateModeleContratDTO.class);
			f1.addColumn("dateLiv", "Date",FieldType.DATE, null);
			binder.bind(f1, "dateLivs");
			form.addComponent(f1);
		}
		else
		{
			addDateField("Date de la première livraison", "dateDebut");
			addDateField("Date de la dernière livraison", "dateFin");
		}
		

	}


	@Override
	protected void performSauvegarder()
	{
		new GestionContratService().updateDateModeleContrat(modeleContrat);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
