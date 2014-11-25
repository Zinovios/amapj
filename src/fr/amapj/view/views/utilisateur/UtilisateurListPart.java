package fr.amapj.view.views.utilisateur;

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

import fr.amapj.service.services.excelgenerator.EGListeAdherent;
import fr.amapj.service.services.excelgenerator.EGListeAdherent.Type;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.view.engine.excelgenerator.LinkCreator;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.tools.TableTools;
import fr.amapj.view.views.compte.PopupSaisiePassword;


/**
 * Gestion des utilisateurs
 *
 */
public class UtilisateurListPart extends VerticalLayout implements ComponentContainer , View ,  PopupSuppressionListener
{

	private TextField searchField;

	private Button newButton;
	private Button deleteButton;
	private Button editButton;
	private Button changePasswordButton;
	private Button changeStateButton;
	private Button moreButton;
	

	private String textFilter;

	private BeanItemContainer<UtilisateurDTO> mcInfos;

	private Table cdesTable;

	public UtilisateurListPart()
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
		// Lecture dans la base de données
		mcInfos = new BeanItemContainer<UtilisateurDTO>(UtilisateurDTO.class);
			
		// Bind it to a component
		cdesTable = new Table("", mcInfos);
		cdesTable.setStyleName("big strong");
		
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "nom", "prenom" ,"roles","etatUtilisateur" });
		cdesTable.setColumnHeader("nom","Nom");
		cdesTable.setColumnHeader("prenom","Prenom");
		cdesTable.setColumnHeader("roles","Role");
		cdesTable.setColumnHeader("etatUtilisateur","Etat");

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
		
		
		Label title2 = new Label("Liste des utilisateurs");
		title2.setSizeUndefined();
		title2.addStyleName("h1");	
		
		newButton = new Button("Créer un nouvel utilisateur");
		newButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAjouter();
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
		

		deleteButton = new Button("Supprimer");
		deleteButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSupprimer();

			}
		});
		
		changePasswordButton = new Button("Changer le mot de passe");
		changePasswordButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleChangerPassword();
			}
		});
		
		
		changeStateButton = new Button("Activer/Désactiver");
		changeStateButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleChangeState();
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
		toolbar.addComponent(editButton);
		toolbar.addComponent(deleteButton);
		toolbar.addComponent(changePasswordButton);
		toolbar.addComponent(changeStateButton);
		toolbar.addComponent(moreButton);
		
		toolbar.addComponent(searchField);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

		
	
		addComponent(title2);
		addComponent(toolbar);
		addComponent(LinkCreator.createLink(new EGListeAdherent(Type.AVEC_INACTIF)));
		addComponent(cdesTable);
		setExpandRatio(cdesTable, 1);
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		refreshTable();

	}
	
	
	private void handleChangerPassword()
	{
		UtilisateurDTO dto = (UtilisateurDTO) cdesTable.getValue();
		FormPopup.open(new PopupSaisiePassword(dto.id),this);		
	}
	


	private void handleChangeState()
	{
		UtilisateurDTO dto = (UtilisateurDTO) cdesTable.getValue();
		FormPopup.open(new PopupSaisieEtatUtilisateur(dto),this);
	}
	

	private void handleAjouter()
	{
		CreationUtilisateurEditorPart.open(new CreationUtilisateurEditorPart(), this);
	}
	
	private void handleMore()
	{
		ChoixActionUtilisateur.open(new ChoixActionUtilisateur(), this);
	}


	

	protected void handleEditer()
	{
		UtilisateurDTO dto = (UtilisateurDTO) cdesTable.getValue();
		ModificationUtilisateurEditorPart.open(new ModificationUtilisateurEditorPart(dto), this);
	}

	protected void handleSupprimer()
	{
		UtilisateurDTO dto = (UtilisateurDTO) cdesTable.getValue();
		String text = "Etes vous sûr de vouloir supprimer l'utilisateur "+dto.nom+" "+dto.prenom+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,dto.id);
		SuppressionPopup.open(confirmPopup, this);		
	}
	
	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new UtilisateurService().deleteUtilisateur(idItemToSuppress);
	}




	private void updateFilters()
	{
		mcInfos.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			Or or = new Or(new Like("nom", textFilter + "%", false), new Like("prenom", textFilter + "%", false));
			mcInfos.addContainerFilter(or);
		}
	}
	
	/**
	 * Permet de rafraichir la table
	 */
	public void refreshTable()
	{
		String[] sortColumns = new String[] { "nom" , "prenom" };
		boolean[] sortAscending = new boolean[] { true, true } ;
		
		List<UtilisateurDTO> res = new UtilisateurService().getAllUtilisateurs();
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
		deleteButton.setEnabled(enable);
		editButton.setEnabled(enable);
		changePasswordButton.setEnabled(enable);
		changeStateButton.setEnabled(enable);
	}
	
	
	
}
