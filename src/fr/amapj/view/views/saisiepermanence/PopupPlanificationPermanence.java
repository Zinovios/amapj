package fr.amapj.view.views.saisiepermanence;

import com.vaadin.data.util.BeanItem;

import fr.amapj.service.services.saisiepermanence.planif.PlanifDTO;
import fr.amapj.service.services.saisiepermanence.planif.PlanifDateDTO;
import fr.amapj.service.services.saisiepermanence.planif.PlanifPermanenceService;
import fr.amapj.service.services.saisiepermanence.planif.PlanifUtilisateurDTO;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.views.gestioncontrat.editorpart.FrequenceLivraison;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Popup pour la planification des permanences
 * 
 *
 */
@SuppressWarnings("serial")
public class PopupPlanificationPermanence extends WizardFormPopup
{

	private PlanifDTO planif;


	public enum Step
	{
		INFO_GENERALES, CHOIX_DATES , UTILISATEURS;
	}

	/**
	 * 
	 */
	public PopupPlanificationPermanence()
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Planification des permanences";

		// Chargement de l'objet à créer
		planif = new PlanifDTO();
		item = new BeanItem<PlanifDTO>(planif);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case INFO_GENERALES:
			addFieldInfoGenerales();
			break;
			
		case CHOIX_DATES:
			addFieldChoixDates();
			break;
			

		case UTILISATEURS:
			addFieldUtilisateurs();
			break;

		default:
			break;
		}
	}

	private void addFieldInfoGenerales()
	{
		// Titre
		setStepTitle("les informations générales pour la planification.");
		
		addDateField("Date de début", "dateDebut");
		
		// 
		addDateField("Date de fin", "dateFin");
		
		//
		addComboEnumField("Fréquence des permanences", FrequenceLivraison.UNE_FOIS_PAR_SEMAINE, "frequence");
		
		
		// Champ 
		addIntegerField("Nombre de personnes par permanence", "nbPersonne");

	}
	
	
	private void addFieldChoixDates()
	{
		// Chargement des données
		new PlanifPermanenceService().fillPlanifInfo(planif);

		// Titre
		setStepTitle("le choix des dates");
		
		//
		CollectionEditor<PlanifDateDTO> f1 = new CollectionEditor<PlanifDateDTO>("Liste des dates", (BeanItem) item, "dates", PlanifDateDTO.class);
		f1.addColumn("datePermanence","Date",FieldType.DATE,null);
		binder.bind(f1, "dates");
		form.addComponent(f1);

	}
	
	
	
	
	

	private void addFieldUtilisateurs()
	{
		// Titre
		setStepTitle("les utilisateurs de permanences");
		
	
		//
		CollectionEditor<PlanifUtilisateurDTO> f1 = new CollectionEditor<PlanifUtilisateurDTO>("Liste des utilisateurs", (BeanItem) item, "utilisateurs", PlanifUtilisateurDTO.class);
		f1.addSearcherColumn("idUtilisateur", "Nom de l'utilisateur",FieldType.SEARCHER, null,SearcherList.UTILISATEUR_ACTIF,null);
		f1.addColumn("actif","Actif",FieldType.CHECK_BOX,true);
		f1.addColumn("bonus", "Bonus",FieldType.QTE, null);
		binder.bind(f1, "utilisateurs");
		form.addComponent(f1);
		
		

	}

	

	@Override
	protected void performSauvegarder()
	{
		new PlanifPermanenceService().savePlanification(planif);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
