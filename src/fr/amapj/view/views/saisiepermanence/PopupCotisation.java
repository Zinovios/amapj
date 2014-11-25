package fr.amapj.view.views.saisiepermanence;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.util.BeanItem;

import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.saisiepermanence.planif.PlanifDTO;
import fr.amapj.service.services.saisiepermanence.planif.PlanifPermanenceService;
import fr.amapj.service.services.saisiepermanence.planif.PlanifUtilisateurDTO;
import fr.amapj.service.services.utilisateur.CotisationDTO;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.service.services.utilisateur.UtilisateurService;
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
public class PopupCotisation extends WizardFormPopup
{

	private CotisationDTO cotisation;


	public enum Step
	{
		INFO_GENERALES;
	}

	/**
	 * 
	 */
	public PopupCotisation()
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Planification des permanences";

		// Chargement de l'objet à créer
		cotisation = new CotisationDTO();
		cotisation.utilisateurs = new ArrayList<>();
		List<Utilisateur> us = new UtilisateurService().getAllUtilisateursActif();
		for (Utilisateur u : us)
		{
			UtilisateurDTO dto = new UtilisateurDTO();
			dto.id = u.getId();
			dto.cotisation = u.getCotisation();
			cotisation.utilisateurs.add(dto);
		}
		
		
		
		
		item = new BeanItem<CotisationDTO>(cotisation);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case INFO_GENERALES:
			addFieldInfoGenerales();
			break;

		default:
			break;
		}
	}

	private void addFieldInfoGenerales()
	{
		// Titre
		setStepTitle("les utilisateurs ayant cotisés");
		
		
		//
		CollectionEditor<UtilisateurDTO> f1 = new CollectionEditor<UtilisateurDTO>("Liste des utilisateurs", (BeanItem) item, "utilisateurs", UtilisateurDTO.class);
		f1.addSearcherColumn("id", "Nom de l'utilisateur",FieldType.SEARCHER, null,SearcherList.UTILISATEUR_ACTIF,null);
		f1.addColumn("cotisation","Cotisation",FieldType.COMBO,EtatUtilisateur.ACTIF);
		binder.bind(f1, "utilisateurs");
		form.addComponent(f1);
		
		

	}

	

	@Override
	protected void performSauvegarder()
	{
		new UtilisateurService().saveCotisation(cotisation);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
