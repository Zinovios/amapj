package fr.amapj.view.views.saisiepermanence;

import com.vaadin.data.util.BeanItem;

import fr.amapj.service.services.saisiepermanence.PermanenceDTO;
import fr.amapj.service.services.saisiepermanence.PermanenceService;
import fr.amapj.service.services.saisiepermanence.PermanenceUtilisateurDTO;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Permet à un utilisateur de mettre à jour ses coordonnées
 * 
 *
 */
@SuppressWarnings("serial")
public class PopupSaisiePermanence extends FormPopup
{

	private PermanenceDTO dto;
	private boolean create;



	/**
	 * 
	 */
	public PopupSaisiePermanence(PermanenceDTO dto)
	{
		popupWidth = "80%";
		popupHeight = "60%";
		this.create = (dto==null);
		
		if (create)
		{
			popupTitle = "Création d'une permanence";
			this.dto = new PermanenceDTO();
		}
		else
		{
			popupTitle = "Modification d'une permanence";
			this.dto = dto;
		}

		
		item = new BeanItem<PermanenceDTO>(this.dto);

	}
	
	@Override
	protected void addFields()
	{
		
		// Champ 1
		addDateField("Date de la permanence", "datePermanence");
		
		//
		CollectionEditor<PermanenceUtilisateurDTO> f1 = new CollectionEditor<PermanenceUtilisateurDTO>("Liste des participants", (BeanItem) item, "permanenceUtilisateurs", PermanenceUtilisateurDTO.class);
		f1.addSearcherColumn("idUtilisateur", "Nom des participants",FieldType.SEARCHER, null,SearcherList.UTILISATEUR_ACTIF,null);
		f1.addColumn("numSession", "Numéro de la session (optionnel)",FieldType.INTEGER, new Integer(0));
		binder.bind(f1, "permanenceUtilisateurs");
		form.addComponent(f1);
		
	}

	

	@Override
	protected void performSauvegarder()
	{
		new PermanenceService().updateorCreateDistribution(dto, create);
	}

	
}
