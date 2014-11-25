package fr.amapj.view.views.gestioncontrat.editorpart;

import com.vaadin.data.util.BeanItem;

import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.LigneContratDTO;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Permet de modifier l'entete du contrat, c'est à dire son nom
 * et la date limite d'inscription 
 */
@SuppressWarnings("serial")
public class ModifProduitContratEditorPart extends FormPopup
{
	private ModeleContratDTO modeleContrat;
	
	/**
	 * 
	 */
	public ModifProduitContratEditorPart(Long id)
	{
		popupTitle = "Modification des produits d'un contrat";
		popupWidth = "80%";
				
		// Chargement de l'objet  à modifier
		modeleContrat = new GestionContratService().loadModeleContrat(id);
		
		item = new BeanItem<ModeleContratDTO>(modeleContrat);
		
		
	}
	
	protected void addFields()
	{
		// Le producteur
		Searcher prod = new Searcher(SearcherList.PRODUCTEUR);
		prod.bind(binder, "producteur");
		form.addComponent(prod);
		prod.setEnabled(false);
		
		// Les produits
		CollectionEditor<LigneContratDTO> f1 = new CollectionEditor<LigneContratDTO>("Produits", (BeanItem) item, "produits", LigneContratDTO.class);
		f1.addSearcherColumn("produitId", "Nom du produit",FieldType.SEARCHER, null,SearcherList.PRODUIT,prod);
		f1.addColumn("prix", "Prix du produit", FieldType.CURRENCY, null);
		binder.bind(f1, "produits");
		form.addComponent(f1);
	}
	
	


	protected void performSauvegarder()
	{
		// Sauvegarde du contrat
		new GestionContratService().updateProduitModeleContrat(modeleContrat);
	}
	
}
