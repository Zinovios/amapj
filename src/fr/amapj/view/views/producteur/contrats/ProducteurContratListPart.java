package fr.amapj.view.views.producteur.contrats;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.services.excelgenerator.EGFeuilleLivraison;
import fr.amapj.service.services.excelgenerator.EGSyntheseContrat;
import fr.amapj.service.services.excelgenerator.producteur.EGPaiementProducteur;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.view.engine.excelgenerator.TelechargerPopup;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.tools.TableTools;
import fr.amapj.view.views.producteur.ProducteurSelectorPart;
import fr.amapj.view.views.saisiecontrat.SaisieContrat;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.ModeSaisie;


/**
 * Gestion des modeles de contrats : création, diffusion, ...
 *
 */
public class ProducteurContratListPart extends VerticalLayout implements ComponentContainer , View , PopupListener
{

	private TextField searchField;

	private Button testButton;
	private Button telechargerButton;

	private String textFilter;

	private BeanItemContainer<ModeleContratSummaryDTO> mcInfos;

	private Table cdesTable;
	
	private ProducteurSelectorPart producteurSelector;

	public ProducteurContratListPart()
	{
	}
	
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		setSizeFull();
		buildMainArea();
	}
	

	private void buildMainArea()
	{
		//
		producteurSelector = new ProducteurSelectorPart(this);
		
		// Lecture dans la base de données
		mcInfos = new BeanItemContainer<ModeleContratSummaryDTO>(ModeleContratSummaryDTO.class);
			
		// Bind it to a component
		cdesTable = new Table("", mcInfos);
		
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "etat", "nom", "finInscription","dateDebut" , "dateFin" , "nbLivraison" ,"nbProduit"});
		cdesTable.setColumnHeader("etat","Etat");
		cdesTable.setColumnHeader("nom","Nom");
		cdesTable.setColumnHeader("nomProducteur","Producteur");
		cdesTable.setColumnHeader("finInscription","Fin inscription");
		cdesTable.setColumnHeader("dateDebut","Première livraison");
		cdesTable.setColumnHeader("dateFin","Dernière livraison");
		cdesTable.setColumnHeader("nbLivraison","Nb de livraisons");
		cdesTable.setColumnHeader("nbProduit","Nb de produits");
		
		
		//
		cdesTable.setConverter("finInscription", new DateToStringConverter());
		cdesTable.setConverter("dateDebut", new DateToStringConverter());
		cdesTable.setConverter("dateFin", new DateToStringConverter());

		cdesTable.setSelectable(true);
		cdesTable.setImmediate(true);

		// Activation au desactivation des boutons delete et edit
		cdesTable.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				setModificationsEnabled(event.getProperty().getValue() != null);
			}

			private void setModificationsEnabled(boolean b)
			{
				enableButtonBar(b);
			}
		});

		cdesTable.setSizeFull();

		cdesTable.addItemClickListener(new ItemClickListener()
		{
			@Override
			public void itemClick(ItemClickEvent event)
			{
				if (event.isDoubleClick())
				{
					cdesTable.select(event.getItemId());
				}
			}
		});

		
		Label title2 = new Label("Liste des contrats d'un producteur");
		title2.setSizeUndefined();
		title2.addStyleName("h1");	
		
		HorizontalLayout toolbar = new HorizontalLayout();
		
		testButton = new Button("Tester");
		testButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleTester();

			}
		});
		
		
		
		telechargerButton = new Button("Télécharger ...");
		telechargerButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleTelecharger();
			}
		});
		

		searchField = new TextField();
		searchField.setInputPrompt("Rechercher par nom");
		searchField.addTextChangeListener(new TextChangeListener()
		{

			@Override
			public void textChange(TextChangeEvent event)
			{
				textFilter = event.getText();
				updateFilters();
			}
		});

		
		
		toolbar.addComponent(testButton);
		toolbar.addComponent(telechargerButton);
		
		toolbar.addComponent(searchField);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

		
		addComponent(producteurSelector.getChoixProducteurComponent());
		addComponent(title2);
		addComponent(toolbar);
		addComponent(cdesTable);
		setExpandRatio(cdesTable, 1);
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		refreshTable();

	}

	private void handleTelecharger()
	{
		ModeleContratSummaryDTO mcDto = (ModeleContratSummaryDTO) cdesTable.getValue();
		
		TelechargerPopup popup = new TelechargerPopup();
		popup.addGenerator(new EGFeuilleLivraison(mcDto.id));
		popup.addGenerator(new EGPaiementProducteur(mcDto.id));
		popup.addGenerator(new EGSyntheseContrat(mcDto.id));
				
		CorePopup.open(popup,this);
	}


	private void handleTester()
	{
		if (cdesTable.getValue()==null)
		{
			return ;
		}
		ModeleContratSummaryDTO mcDto = (ModeleContratSummaryDTO) cdesTable.getValue();
		ContratDTO contratDTO = new MesContratsService().loadContrat(mcDto.id,null);
		SaisieContrat.saisieContrat(contratDTO,null,"<h1>Mode Test</h1>",ModeSaisie.FOR_TEST,this);
		
	}


	



	private void updateFilters()
	{
		mcInfos.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			Like like = new Like("nom", textFilter + "%", false);
			mcInfos.addContainerFilter(like);
		}
	}
	
	/**
	 * Permet de rafraichir la table
	 */
	public void refreshTable()
	{
		String[] sortColumns = new String[] { "etat" , "dateDebut"   };
		boolean[] sortAscending = new boolean[] { true, true} ;
		
		List<ModeleContratSummaryDTO> res = filter(new GestionContratService().getModeleContratInfo()); 
		boolean enabled = TableTools.updateTable(cdesTable, res, sortColumns, sortAscending);
		
		enableButtonBar(enabled);		
	}
	

	
	
	private List<ModeleContratSummaryDTO> filter(List<ModeleContratSummaryDTO> modeleContratInfo)
	{
		Long idProducteur = producteurSelector.getProducteurId();
		
		List<ModeleContratSummaryDTO> res = new ArrayList<>(); 
		
		// Si le producteur n'est pas défini : la table est vide
		if (idProducteur==null)
		{
			return res;
		}
		
		for (ModeleContratSummaryDTO dto : modeleContratInfo)
		{
			if (dto.producteurId.equals(idProducteur))
			{
				res.add(dto);
			}
		}
		return res;
	}


	@Override
	public void onPopupClose()
	{
		refreshTable();
		
	}
	
	
	private void enableButtonBar(boolean enable)
	{
		testButton.setEnabled(enable);
		telechargerButton.setEnabled(enable);
	}
	
	
	
}
