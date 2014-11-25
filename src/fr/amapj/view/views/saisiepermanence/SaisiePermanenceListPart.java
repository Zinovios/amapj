package fr.amapj.view.views.saisiepermanence;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import fr.amapj.service.services.excelgenerator.EGListeAdherent;
import fr.amapj.service.services.excelgenerator.EGPlanningPermanence;
import fr.amapj.service.services.saisiepermanence.PermanenceDTO;
import fr.amapj.service.services.saisiepermanence.PermanenceService;
import fr.amapj.view.engine.excelgenerator.LinkCreator;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.tools.TableTools;


/**
 * Saisie des distributions
 *
 */
@SuppressWarnings("serial")
public class SaisiePermanenceListPart extends VerticalLayout implements ComponentContainer , View ,  PopupSuppressionListener
{

	private TextField searchField;

	private Button newButton;
	private Button deleteButton;
	private Button editButton;
	private Button planifButton;
	private Button cotisationButton;
	private Button rappelButton;
	

	private String textFilter;

	private BeanItemContainer<PermanenceDTO> mcInfos;

	private Table cdesTable;

	public SaisiePermanenceListPart()
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
		mcInfos = new BeanItemContainer<PermanenceDTO>(PermanenceDTO.class);
			
		// Bind it to a component
		cdesTable = new Table("", mcInfos);
		cdesTable.setStyleName("big strong");
		
				
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "datePermanence", "utilisateurs" , "numeroSession"});
		cdesTable.setColumnHeader("datePermanence","Date");
		cdesTable.setColumnHeader("utilisateurs","Personnes de permanence");
		cdesTable.setColumnHeader("numeroSession","Numéro de la session");
		
		cdesTable.setConverter("datePermanence", new DateToStringConverter());
		

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
		
		
		Label title2 = new Label("Planning des permanences");
		title2.setSizeUndefined();
		title2.addStyleName("h1");	
		
		newButton = new Button("Créer une nouvelle permanence");
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
		
		planifButton = new Button("Planifier");
		planifButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handlePlanification();
			}
		});
		
		cotisationButton = new Button("Cotisation");
		cotisationButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleCotisation();
			}
		});
		
		rappelButton = new Button("Envoyer un rappel");
		rappelButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleRappel();
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

		
		toolbar.addComponent(newButton);
		toolbar.addComponent(editButton);
		toolbar.addComponent(deleteButton);
		toolbar.addComponent(planifButton);
		toolbar.addComponent(cotisationButton);
		toolbar.addComponent(rappelButton);
		
		
		toolbar.addComponent(searchField);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

		
	
		addComponent(title2);
		addComponent(toolbar);
		addComponent(LinkCreator.createLink(new EGPlanningPermanence(new Date())));
		addComponent(cdesTable);
		setExpandRatio(cdesTable, 1);
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		refreshTable();

	}
	
	
	private void handlePlanification()
	{
		PopupPlanificationPermanence.open(new PopupPlanificationPermanence(),this);		
	}
	
	
	private void handleCotisation()
	{
		PopupCotisation.open(new PopupCotisation(),this);		
	}
	
	private void handleRappel()
	{
		PopupRappelPermanence.open(new PopupRappelPermanence(),this);		
	}
	
	

	private void handleAjouter()
	{
		PopupSaisiePermanence.open(new PopupSaisiePermanence(null), this);
	}

	

	protected void handleEditer()
	{
		PermanenceDTO dto = (PermanenceDTO) cdesTable.getValue();
		PopupSaisiePermanence.open(new PopupSaisiePermanence(dto), this);
	}

	protected void handleSupprimer()
	{
		PermanenceDTO dto = (PermanenceDTO) cdesTable.getValue();
		SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
		String text = "Etes vous sûr de vouloir supprimer la permanence du "+df1.format(dto.datePermanence)+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,dto.id);
		SuppressionPopup.open(confirmPopup, this);		
	}
	
	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new PermanenceService().deleteDistribution(idItemToSuppress);
	}




	private void updateFilters()
	{
		mcInfos.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			Like like = new Like("utilisateurs", "%" + textFilter + "%", false);
			mcInfos.addContainerFilter(like);
		}
	}
	
	/**
	 * Permet de rafraichir la table
	 */
	public void refreshTable()
	{
		String[] sortColumns = new String[] { "datePermanence" };
		boolean[] sortAscending = new boolean[] { true } ;
		
		List<PermanenceDTO> res = new PermanenceService().getAllDistributions();
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
	}
	
	
	
}
