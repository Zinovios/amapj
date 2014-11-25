package fr.amapj.view.views.gestioncontrat.listpart;

import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
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

import fr.amapj.model.engine.IdentifiableUtil;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.excelgenerator.EGBilanCompletCheque;
import fr.amapj.service.services.excelgenerator.EGCollecteCheque;
import fr.amapj.service.services.excelgenerator.EGFeuilleLivraison;
import fr.amapj.service.services.excelgenerator.EGSyntheseContrat;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.excelgenerator.TelechargerPopup;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.tools.TableTools;
import fr.amapj.view.views.gestioncontrat.editorpart.ChoixModifEditorPart;
import fr.amapj.view.views.gestioncontrat.editorpart.GestionContratEditorPart;
import fr.amapj.view.views.saisiecontrat.SaisieContrat;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.ModeSaisie;


/**
 * Gestion des modeles de contrats : création, diffusion, ...
 *
 */
public class GestionContratListPart extends VerticalLayout implements ComponentContainer , View ,  PopupSuppressionListener
{

	private TextField searchField;

	private Button newButton;
	private Button newButtonFrom;
	private Button deleteButton;
	private Button editButton;
	private Button testButton;
	private Button telechargerButton;
	private Button changeStateButton;
	

	private String textFilter;

	private BeanItemContainer<ModeleContratSummaryDTO> mcInfos;

	private Table cdesTable;
	
	private List<Producteur> allowedProducteurs;	

	public GestionContratListPart()
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
		allowedProducteurs = new AccessManagementService().getAccessLivraisonProducteur(SessionManager.getUserRoles(),SessionManager.getUserId());
		
		// 
		mcInfos = new BeanItemContainer<ModeleContratSummaryDTO>(ModeleContratSummaryDTO.class);
			
		// Bind it to a component
		cdesTable = new Table("", mcInfos);
		
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "etat", "nom", "nomProducteur" ,"finInscription","dateDebut" , "dateFin" , "nbLivraison" ,"nbInscrits"});
		cdesTable.setColumnHeader("etat","Etat");
		cdesTable.setColumnHeader("nom","Nom");
		cdesTable.setColumnHeader("nomProducteur","Producteur");
		cdesTable.setColumnHeader("finInscription","Fin inscription");
		cdesTable.setColumnHeader("dateDebut","Première livraison");
		cdesTable.setColumnHeader("dateFin","Dernière livraison");
		cdesTable.setColumnHeader("nbLivraison","Nb de livraisons");
		cdesTable.setColumnHeader("nbInscrits","Nb de contrats signés");
		
		
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

		HorizontalLayout toolbar = new HorizontalLayout();
		
		
		Label title2 = new Label("Liste des contrats vierges");
		title2.setSizeUndefined();
		title2.addStyleName("h1");	
		
		newButton = new Button("Créer");
		newButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAjouter();
			}
		});
		
		newButtonFrom = new Button("Créer à partir de ...");
		newButtonFrom.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleCreerFrom();
			}
		});
			
		editButton = new Button("Modifier");
		editButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleEditer();

			}
		});
		
		testButton = new Button("Tester");
		testButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleTester();

			}
		});
		
		
		

		deleteButton = new Button("Supprimer");
		deleteButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSupprimer();

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
		
	
		changeStateButton = new Button("Changer l'état");
		changeStateButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleChangeState();
			}
		});
		

		searchField = new TextField();
		searchField.setInputPrompt("Rechercher par nom ou producteur");
		searchField.addTextChangeListener(new TextChangeListener()
		{

			@Override
			public void textChange(TextChangeEvent event)
			{
				textFilter = event.getText();
				updateFilters();
			}
		});

		
		toolbar.addComponent(newButton);
		toolbar.addComponent(newButtonFrom);
		toolbar.addComponent(editButton);
		toolbar.addComponent(testButton);
		toolbar.addComponent(deleteButton);
		toolbar.addComponent(changeStateButton);
		toolbar.addComponent(telechargerButton);
		
		toolbar.addComponent(searchField);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

		
	
		addComponent(title2);
		addComponent(toolbar);
		addComponent(cdesTable);
		setExpandRatio(cdesTable, 1);
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		refreshTable();

	}

	protected void handleTelecharger()
	{
		ModeleContratSummaryDTO mcDto = (ModeleContratSummaryDTO) cdesTable.getValue();
		
		TelechargerPopup popup = new TelechargerPopup();
		popup.addGenerator(new EGFeuilleLivraison(mcDto.id));
		popup.addGenerator(new EGCollecteCheque(mcDto.id));
		popup.addGenerator(new EGBilanCompletCheque(mcDto.id));
		popup.addGenerator(new EGSyntheseContrat(mcDto.id));
				
		CorePopup.open(popup,this);
	}


	protected void handleChangeState()
	{
		ModeleContratSummaryDTO mcDto = (ModeleContratSummaryDTO) cdesTable.getValue();
		FormPopup.open(new PopupSaisieEtat(mcDto.id),this);
	}

	protected void handleAjouter()
	{
		GestionContratEditorPart.open(new GestionContratEditorPart(null,allowedProducteurs),this);
	}

	protected void handleCreerFrom()
	{
		ModeleContratSummaryDTO mcDto = (ModeleContratSummaryDTO) cdesTable.getValue();
		GestionContratEditorPart.open(new GestionContratEditorPart(mcDto.id,allowedProducteurs),this);
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


	protected void handleEditer()
	{
		Long id = ((ModeleContratSummaryDTO) cdesTable.getValue()).getId();
		//ModifEnteteContratEditorPart.open(new ModifEnteteContratEditorPart(id),this);
		ChoixModifEditorPart.open(new ChoixModifEditorPart(id),this);
	}

	protected void handleSupprimer()
	{
		ModeleContratSummaryDTO mcDTO = (ModeleContratSummaryDTO) cdesTable.getValue();
		String text = "Etes vous sûr de vouloir supprimer le contrat vierge "+mcDTO.nom+" de "+mcDTO.nomProducteur+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,mcDTO.id);
		SuppressionPopup.open(confirmPopup, this);		
	}
	
	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new GestionContratService().deleteContrat(idItemToSuppress);
	}




	private void updateFilters()
	{
		mcInfos.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			Or or = new Or(new Like("nom", textFilter + "%", false), new Like("nomProducteur", textFilter + "%", false));
			mcInfos.addContainerFilter(or);
		}
	}
	
	/**
	 * Permet de rafraichir la table
	 */
	public void refreshTable()
	{
		String[] sortColumns = new String[] { "etat" , "nomProducteur" , "dateDebut"   };
		boolean[] sortAscending = new boolean[] { true, true ,true} ;
		
		List<ModeleContratSummaryDTO> res = new GestionContratService().getModeleContratInfo(); 
		boolean enabled = TableTools.updateTable(cdesTable, res, sortColumns, sortAscending);
		
		enableButtonBar(enabled);		
	}
	

	
	
	@Override
	public void onPopupClose()
	{
		refreshTable();
		
	}
	
	
	private void enableButtonBar(boolean enable)
	{
		// Si on demande d'activer la ligne mais que l'utilisateur n'a pas le droit à cette ligne
		// alors on desactive bien
		if ( (enable==true) && isAllowed()==false)
		{
			enable = false;
		}
				
		newButtonFrom.setEnabled(enable);
		deleteButton.setEnabled(enable);
		testButton.setEnabled(enable);
		editButton.setEnabled(enable);
		telechargerButton.setEnabled(enable);
		changeStateButton.setEnabled(enable);
	}


	/**
	 * Retourne true si l'utilisateur courant a le droit de manipuler ce contrat
	 * @return
	 */
	private boolean isAllowed()
	{
		ModeleContratSummaryDTO mcDto = (ModeleContratSummaryDTO) cdesTable.getValue();
		if (mcDto==null)
		{
			return false;
		}
		
		return IdentifiableUtil.contains(allowedProducteurs, mcDto.producteurId);
	}
	
	
	
}
