package fr.amapj.view.views.gestioncontrat.editorpart;

import java.util.List;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

import fr.amapj.common.DateUtils;
import fr.amapj.common.DebugUtil;
import fr.amapj.model.models.contrat.modele.GestionPaiement;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.service.services.gestioncontrat.DateModeleContratDTO;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.LigneContratDTO;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.service.services.saisiepermanence.PermanenceUtilisateurDTO;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Permet uniquement de creer des contrats
 * 
 *
 */
@SuppressWarnings("serial")
public class GestionContratEditorPart extends WizardFormPopup
{

	private ModeleContratDTO modeleContrat;

	private boolean creerAPartirDeMode;
	
	private Searcher prod;
	
	private List<Producteur> allowedProducteurs;

	public enum Step
	{
		INFO_GENERALES, DATE_LIVRAISON, CHOIX_PRODUITS, PAIEMENT;
	}

	/**
	 * 
	 */
	public GestionContratEditorPart(Long id,List<Producteur> allowedProducteurs)
	{
		this.allowedProducteurs = allowedProducteurs;
		
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Création d'un contrat";

		// Chargement de l'objet à créer
		// Si id est non null, alors on se sert de ce contenu pour précharger
		// les champs
		if (id == null)
		{
			modeleContrat = new ModeleContratDTO();
			creerAPartirDeMode = false;
		}
		else
		{
			modeleContrat = new GestionContratService().loadModeleContrat(id);
			modeleContrat.nom = modeleContrat.nom + "(Copie)";
			modeleContrat.id = null;
			modeleContrat.dateLivs.clear();
			creerAPartirDeMode = true;
		}
		item = new BeanItem<ModeleContratDTO>(modeleContrat);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case INFO_GENERALES:
			addFieldInfoGenerales();
			break;

		case DATE_LIVRAISON:
			addFieldDateLivraison();
			break;

		case CHOIX_PRODUITS:
			addFieldChoixProduits();
			break;

		case PAIEMENT:
			addFieldPaiement();
			break;

		default:
			break;
		}
	}

	private void addFieldInfoGenerales()
	{
		// Titre
		setStepTitle("les informations générales");
		
		// Champ 1
		addTextField("Nom du contrat", "nom");

		// Champ 2
		addTextField("Description du contrat", "description");

		// Champ 3
		prod = new Searcher(SearcherList.PRODUCTEUR,"Producteur",allowedProducteurs);
		prod.bind(binder, "producteur");
		form.addComponent(prod);
		// On ne peut pas changer le producteur quand on crée à partir d'un
		// autre contrat
		if (creerAPartirDeMode == true)
		{
			prod.setEnabled(false);
		}

		// Champ 4
		addDateField("Date de fin des inscriptions", "dateFinInscription");

		//
		addComboEnumField("Fréquence des livraisons", FrequenceLivraison.UNE_FOIS_PAR_SEMAINE, "frequence");
		
		//
		addComboEnumField("Gestion des paiements", GestionPaiement.NON_GERE, "gestionPaiement");

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

	private void addFieldChoixProduits()
	{
		// Si liste vide
		Long idProducteur = (Long) prod.getConvertedValue();
		if (modeleContrat.produits.size()==0 && idProducteur!=null)
		{
			modeleContrat.produits.addAll(new GestionContratService().getInfoProduitModeleContrat(idProducteur));
		}
		
		
		// Titre
		setStepTitle("la liste des produits et des prix");
				
		// Champ 7
		CollectionEditor<LigneContratDTO> f1 = new CollectionEditor<LigneContratDTO>("Produits", (BeanItem) item, "produits", LigneContratDTO.class);
		f1.addSearcherColumn("produitId", "Nom du produit",FieldType.SEARCHER, null,SearcherList.PRODUIT,prod);
		f1.addColumn("prix", "Prix du produit", FieldType.CURRENCY, null);
		binder.bind(f1, "produits");
		form.addComponent(f1);

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
				p.setValue(modeleContrat.dateDebut);
			}
			else
			{
				PopupDateField p = addDateField("Date de remise des chèques", "dateRemiseCheque");
				p.setValue(modeleContrat.dateFinInscription);
				
				p = addDateField("Date du premier paiement", "premierCheque");
				p.setValue(DateUtils.firstDayInMonth(modeleContrat.dateDebut));
				
				p = addDateField("Date du dernier paiement", "dernierCheque");
				p.setValue(DateUtils.firstDayInMonth(modeleContrat.dateFin)); 
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
		new GestionContratService().saveNewModeleContrat(modeleContrat);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
