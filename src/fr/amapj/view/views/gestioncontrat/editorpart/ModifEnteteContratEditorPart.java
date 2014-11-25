package fr.amapj.view.views.gestioncontrat.editorpart;

import com.vaadin.data.util.BeanItem;

import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.view.engine.popup.formpopup.FormPopup;

/**
 * Permet de modifier l'entete du contrat, c'est à dire son nom
 * et la date limite d'inscription 
 */
@SuppressWarnings("serial")
public class ModifEnteteContratEditorPart extends FormPopup
{
	private ModeleContratDTO modeleContrat;
	
	/**
	 * 
	 */
	public ModifEnteteContratEditorPart(Long id)
	{
		popupTitle = "Modification d'un contrat";
		popupWidth = "80%";
				
		// Chargement de l'objet  à modifier
		modeleContrat = new GestionContratService().loadModeleContrat(id);
		
		item = new BeanItem<ModeleContratDTO>(modeleContrat);
		
	}
	
	protected void addFields()
	{
		// Champ 1
		addTextField("Nom du contrat", "nom");
		
		// Champ 2
		addTextField("Description du contrat", "description");
			
		// Champ 3
		addDateField("Date de fin des inscriptions","dateFinInscription");
	}
	
	


	protected void performSauvegarder()
	{
		// Sauvegarde du contrat
		new GestionContratService().updateEnteteModeleContrat(modeleContrat);
	}
	
}
