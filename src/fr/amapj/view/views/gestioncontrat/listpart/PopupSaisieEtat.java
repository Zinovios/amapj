package fr.amapj.view.views.gestioncontrat.listpart;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.ComboBox;

import fr.amapj.model.models.contrat.modele.EtatModeleContrat;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.view.engine.popup.formpopup.FormPopup;

/**
 * Popup pour la saisie de la nouvelle adresse e mail
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisieEtat extends FormPopup
{
		
	private EtatModeleContrat etat = EtatModeleContrat.CREATION;
	
	private Long idModeleContrat;


	/**
	 * 
	 */
	public PopupSaisieEtat(Long idModeleContrat)
	{
		popupTitle = "Saisie de l'état du contrat";
		this.idModeleContrat = idModeleContrat;
		
	}
	
	
	protected void addFields()
	{
		// Contruction de l'item
		item.addItemProperty("etat", new ObjectProperty<EtatModeleContrat>(etat));
		
		// Construction des champs
		// TODO om serait mieux de prendre pour valeur par defaut la valeur actuelle 
		ComboBox box = addComboEnumField("Nouvel état", EtatModeleContrat.CREATION , "etat");
		box.setRequired(true);	
	}

	protected void performSauvegarder()
	{
		EtatModeleContrat newValue = (EtatModeleContrat) item.getItemProperty("etat").getValue();
		new GestionContratService().updateEtat(newValue,idModeleContrat);
	}
}
