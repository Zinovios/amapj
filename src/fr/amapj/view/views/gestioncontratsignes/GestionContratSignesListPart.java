package fr.amapj.view.views.gestioncontratsignes;

import java.util.ArrayList;
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
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.dbservice.DbService;
import fr.amapj.service.services.excelgenerator.EGBilanCompletCheque;
import fr.amapj.service.services.excelgenerator.EGCollecteCheque;
import fr.amapj.service.services.excelgenerator.EGContratUtilisateur;
import fr.amapj.service.services.excelgenerator.EGFeuilleLivraison;
import fr.amapj.service.services.excelgenerator.EGSyntheseContrat;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.service.services.gestioncontratsigne.ContratSigneDTO;
import fr.amapj.service.services.gestioncontratsigne.GestionContratSigneService;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.view.engine.excelgenerator.TelechargerPopup;
import fr.amapj.view.engine.menu.MenuList;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.okcancelpopup.OKCancelPopup;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.engine.searcher.SearcherConverterIdentifiable;
import fr.amapj.view.engine.tools.DateTimeToStringConverter;
import fr.amapj.view.engine.tools.TableTools;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;
import fr.amapj.view.views.common.contratselector.ContratSelectorPart;
import fr.amapj.view.views.common.contratselector.IContratSelectorPart;
import fr.amapj.view.views.saisiecontrat.SaisieContrat;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.ModeSaisie;
import fr.amapj.view.views.searcher.SearcherList;
import fr.amapj.view.views.utilisateur.ChoixActionUtilisateur;


/**
 * Gestion des contrats signes
 *
 * 
 *
 */
public class GestionContratSignesListPart extends VerticalLayout implements ComponentContainer , View ,  PopupSuppressionListener , IContratSelectorPart
{
	
	private TextField searchField;
	private String textFilter;
	
	private ContratSelectorPart contratSelectorPart;

	private Button newButton;
	private Button deleteButton;
	private Button voirButton;
	private Button editButton;
	private Button receptChequeButton;
	private Button modifChequeButton;
	private Button saisieAvoirButton;
	private Button telechargerButton;
	private Button moreButton;
	

	private BeanItemContainer<ContratSigneDTO> mcInfos;

	private Table cdesTable;
	
	private Status status;
	
	public enum Status
	{
		STANDARD, CHEQUE ;
	}

	public GestionContratSignesListPart()
	{
	}
	
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		// Détermination du status de la page
		status = Status.CHEQUE;
		if (event.getViewName().equalsIgnoreCase("/"+MenuList.GESTION_CONTRAT_SIGNES.name()))
		{
			status = Status.STANDARD;
		}
		
		setSizeFull();
		buildMainArea();
	}
	

	private void buildMainArea()
	{
		// 
		mcInfos = new BeanItemContainer<ContratSigneDTO>(ContratSigneDTO.class);
			
		// Bind it to a component
		cdesTable = new Table("", mcInfos);
		
		// Titre des colonnes
		if (status==Status.STANDARD)
		{
			cdesTable.setVisibleColumns(new Object[] { "nomUtilisateur", "prenomUtilisateur", "dateCreation" , "dateModification" , "mntCommande" });
		}
		else
		{
			cdesTable.setVisibleColumns(new Object[] { "nomUtilisateur", "prenomUtilisateur", "mntCommande" ,"nbChequePromis" , "nbChequeRecus" , "nbChequeRemis" , "mntSolde" , "mntAvoirInitial"});
		}
		
		
		cdesTable.setColumnHeader("nomUtilisateur","Nom");
		cdesTable.setColumnHeader("prenomUtilisateur","Prénom");
		cdesTable.setColumnHeader("dateCreation","Date création");
		cdesTable.setColumnHeader("dateModification","Date modification");
		
		cdesTable.setColumnHeader("mntAvoirInitial","Avoir initial(en €)");
		cdesTable.setColumnAlignment("mntAvoirInitial",Align.RIGHT);
		cdesTable.setColumnHeader("mntCommande","Commandé(en €)");
		cdesTable.setColumnAlignment("mntCommande",Align.RIGHT);
		cdesTable.setColumnHeader("mntSolde","Solde final(en €)");
		cdesTable.setColumnAlignment("mntSolde",Align.RIGHT);

		
		cdesTable.setColumnHeader("nbChequePromis","Chèques promis");
		cdesTable.setColumnAlignment("nbChequePromis",Align.CENTER);
		cdesTable.setColumnHeader("nbChequeRecus","Chèques reçus");
		cdesTable.setColumnAlignment("nbChequeRecus",Align.CENTER);
		cdesTable.setColumnHeader("nbChequeRemis","Chèques remis");
		cdesTable.setColumnAlignment("nbChequeRemis",Align.CENTER);
		
		
		//
		cdesTable.setConverter("dateCreation", new DateTimeToStringConverter());
		cdesTable.setConverter("dateModification", new DateTimeToStringConverter());
		cdesTable.setConverter("mntAvoirInitial", new CurrencyTextFieldConverter());
		cdesTable.setConverter("mntCommande", new CurrencyTextFieldConverter());
		cdesTable.setConverter("mntSolde", new CurrencyTextFieldConverter());
		

		cdesTable.setSelectable(true);
		cdesTable.setImmediate(true);

		// Activation au desactivation des boutons delete et edit
		cdesTable.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				buttonBarEditMode(event.getProperty().getValue() != null);
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

		
		
		// Partie titre
		String str = "Liste des contrats signés";
		if (status==Status.CHEQUE)
		{
			str = "Réception des chèques";
		}
		
		
		Label title2 = new Label(str);
		title2.setSizeUndefined();
		title2.addStyleName("h1");
			
		// Partie choix du contrat
		contratSelectorPart = new ContratSelectorPart(this);
		HorizontalLayout toolbar1 = contratSelectorPart.getChoixContratComponent();
		
		// Partie bouton
		HorizontalLayout toolbar2 = new HorizontalLayout();
		
		
		newButton = new Button("Ajouter un contrat signé");
		newButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAjouter();
			}
		});
		
		voirButton = new Button("Visualiser un contrat signé");
		voirButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleVoir();
			}
		});
		
		
		
		editButton = new Button("Modifier les quantités d'un contrat signé");
		editButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleEditer();
			}
		});
		
		receptChequeButton = new Button("Réceptionner les chèques");
		receptChequeButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleReceptionCheque();
			}
		});
		
		modifChequeButton = new Button("Modifier les chèques");
		modifChequeButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleModifierCheque();
			}
		});
		
		saisieAvoirButton = new Button("Saisir un avoir");
		saisieAvoirButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSaisirAvoir();
			}
		});
		
		telechargerButton = new Button("Télécharger ...");
		if (status==Status.STANDARD)
		{
			telechargerButton.addClickListener(new Button.ClickListener()
			{
	
				@Override
				public void buttonClick(ClickEvent event)
				{
					handleTelechargerContrat();
				}
			});		
		}
		else
		{
			telechargerButton.addClickListener(new Button.ClickListener()
			{
	
				@Override
				public void buttonClick(ClickEvent event)
				{
					handleTelechargerFeuilleCheque();
				}
			});
		}
		
		
	

		deleteButton = new Button("Supprimer un contrat signé");
		deleteButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSupprimer();
			}
		});
		
		
		moreButton = new Button("Autre...");
		moreButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleMore();
			}
		});
		
		
	
		buttonBarFull(false);

		searchField = new TextField();
		searchField.setInputPrompt("Rechercher par nom ou prénom");
		searchField.addTextChangeListener(new TextChangeListener()
		{

			@Override
			public void textChange(TextChangeEvent event)
			{
				textFilter = event.getText();
				updateFilters();
			}
		});

		if (status==Status.STANDARD)
		{
			toolbar2.addComponent(newButton);
			toolbar2.addComponent(voirButton);
			toolbar2.addComponent(editButton);
			toolbar2.addComponent(deleteButton);
		}
		else
		{
			toolbar2.addComponent(voirButton);
			toolbar2.addComponent(receptChequeButton);
			toolbar2.addComponent(modifChequeButton);
			toolbar2.addComponent(saisieAvoirButton);
		}
		
		toolbar2.addComponent(moreButton);
		toolbar2.addComponent(telechargerButton);
		toolbar2.addComponent(searchField);
		toolbar2.setWidth("100%");
		toolbar2.setExpandRatio(searchField, 1);
		toolbar2.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

		
		addComponent(title2);
		addComponent(toolbar1);
		addComponent(toolbar2);
		addComponent(cdesTable);
		setExpandRatio(cdesTable, 1);
		setSizeFull();
		
		setMargin(true);
		setSpacing(true);
		
		contratSelectorPart.fillAutomaticValues();

	}
	
	
	private void handleMore()
	{
		Long idModeleContrat = contratSelectorPart.getModeleContratId();
		
		if (status==Status.STANDARD)
		{
			ChoixActionContratSigne.open(new ChoixActionContratSigne(idModeleContrat), this);
		}
		else
		{
			ChoixActionReceptionCheque.open(new ChoixActionReceptionCheque(idModeleContrat), this);
		}
	}

	
	protected void handleTelechargerContrat()
	{
		ContratSigneDTO contratSigneDTO = (ContratSigneDTO) cdesTable.getValue();
		if (contratSigneDTO!=null)
		{
			TelechargerPopup popup = new TelechargerPopup();
			popup.addGenerator(new EGContratUtilisateur(contratSigneDTO.idContrat));
			CorePopup.open(popup,this);
		}
		
	}

	

	protected void handleTelechargerFeuilleCheque()
	{
		Long idModeleContrat = contratSelectorPart.getModeleContratId();
		
		TelechargerPopup popup = new TelechargerPopup();
		popup.addGenerator(new EGFeuilleLivraison(idModeleContrat));
		popup.addGenerator(new EGCollecteCheque(idModeleContrat));
		popup.addGenerator(new EGBilanCompletCheque(idModeleContrat));
		popup.addGenerator(new EGSyntheseContrat(idModeleContrat));
				
		CorePopup.open(popup,this);
		
	}


	protected void handleSaisirAvoir()
	{
		ContratSigneDTO c = (ContratSigneDTO) cdesTable.getValue();
		CorePopup.open(new PopupSaisieAvoir(c),this);
	}


	protected void handleModifierCheque()
	{
		ContratSigneDTO c = (ContratSigneDTO) cdesTable.getValue();
		
		String message = "<h2> Modification des chèques de "+c.prenomUtilisateur+" "+c.nomUtilisateur+"</h2>";
	
		ContratDTO contratDTO = new MesContratsService().loadContrat(c.idModeleContrat,c.idContrat);
		SaisieContrat.saisieContrat(contratDTO,c.idUtilisateur,message,ModeSaisie.CHEQUE_SEUL,this);
		
	}


	protected void handleReceptionCheque()
	{
		ContratSigneDTO c = (ContratSigneDTO) cdesTable.getValue();
		CorePopup.open(new ReceptionChequeEditorPart(c),this);
	}


	protected void handleVoir()
	{
		ContratSigneDTO c = (ContratSigneDTO) cdesTable.getValue();
		
		String message = "<h2> Visualisation du contrat de "+c.prenomUtilisateur+" "+c.nomUtilisateur+"</h2>";
	
		ContratDTO contratDTO = new MesContratsService().loadContrat(c.idModeleContrat,c.idContrat);
		SaisieContrat.saisieContrat(contratDTO,c.idUtilisateur,message,ModeSaisie.READ_ONLY,this);
		
	}


	protected void handleEditer()
	{
		ContratSigneDTO c = (ContratSigneDTO) cdesTable.getValue();
		
		String message = "<h1> !!!!! Contrat de "+c.prenomUtilisateur+" "+c.nomUtilisateur+" !!!</h1>";
		
		ContratDTO contratDTO = new MesContratsService().loadContrat(c.idModeleContrat,c.idContrat);
		SaisieContrat.saisieContrat(contratDTO,c.idUtilisateur,message,ModeSaisie.QTE_SEUL,this);
	}

	protected void handleSupprimer()
	{
		ContratSigneDTO contratSigneDTO = (ContratSigneDTO) cdesTable.getValue();
		String text = "Etes vous sûr de vouloir supprimer le contrat de "+contratSigneDTO.prenomUtilisateur+" "+contratSigneDTO.nomUtilisateur+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,contratSigneDTO.idContrat);
		SuppressionPopup.open(confirmPopup, this);		
	}
	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new MesContratsService().deleteContrat(idItemToSuppress);
	}


	protected void handleAjouter()
	{
		final Long idModeleContrat = contratSelectorPart.getModeleContratId();
		final PopupSaisieUtilisateur popup = new PopupSaisieUtilisateur(idModeleContrat);
		final PopupListener listenerFinal = this;
		OKCancelPopup.open(popup,new PopupListener()
		{
			
			@Override
			public void onPopupClose()
			{
				Long userId = popup.getUserId();
				if (userId!=null)
				{
					Utilisateur u = (Utilisateur) new DbService().getOneElement(Utilisateur.class, userId);
					String message = "<h1> !!!!! Contrat de "+u.getPrenom()+" "+u.getNom()+" !!!</h1>";
					
					ContratDTO contratDTO = new MesContratsService().loadContrat(idModeleContrat,null);
					SaisieContrat.saisieContrat(contratDTO,userId,message,ModeSaisie.STANDARD,listenerFinal);
				}					
			}
		});
	}

	private void updateFilters()
	{
		mcInfos.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			Or or = new Or(new Like("nomUtilisateur", textFilter + "%", false), new Like("prenomUtilisateur", textFilter + "%", false));
			mcInfos.addContainerFilter(or);
		}
	}
	
	/**
	 * Permet de rafraichir la table
	 */
	public void refreshTable()
	{
		Long idModeleContrat = contratSelectorPart.getModeleContratId();
		
		// Calcul du tableau à afficher
		List<ContratSigneDTO> res = new ArrayList<>();
		if (idModeleContrat!=null)
		{
			res = new GestionContratSigneService().getAllContratSigne(idModeleContrat);
		}
		
		// Tris par nom prénom
		String[] sortColumns = new String[] { "nomUtilisateur" , "prenomUtilisateur"    };
		boolean[] sortAscending = new boolean[] { true, true } ;
		
		// Update de la table
		boolean enabled = TableTools.updateTable(cdesTable, res, sortColumns, sortAscending);
		
		if (idModeleContrat!=null)
		{
			buttonBarEditMode(enabled);
		}
		else
		{
			buttonBarFull(false);
		}
	}
	
	@Override
	public void onPopupClose()
	{
		refreshTable();
		
	}
	
	
	/**
	 * Permet d'activer ou de désactiver toute la barre des boutons
	 * 
	 */
	public void buttonBarFull(boolean enable)
	{
		newButton.setEnabled(enable);
		telechargerButton.setEnabled(enable);
		moreButton.setEnabled(enable);
		
		buttonBarEditMode(enable);
	}
	
	/**
	 * Permet d'activer ou de désactiver les boutons de la barre 
	 * qui sont relatifs au mode édition, c'est à dire les boutons 
	 * Edit et Delete
	 */
	public void buttonBarEditMode(boolean enable)
	{
		voirButton.setEnabled(enable);
		deleteButton.setEnabled(enable);
		editButton.setEnabled(enable);
		receptChequeButton.setEnabled(enable);
		modifChequeButton.setEnabled(enable);
		saisieAvoirButton.setEnabled(enable);		
	}

	
}
