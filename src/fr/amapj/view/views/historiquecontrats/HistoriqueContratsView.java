package fr.amapj.view.views.historiquecontrats;

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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.historiquecontrats.HistoriqueContratDTO;
import fr.amapj.service.services.historiquecontrats.HistoriqueContratsService;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.tools.DateTimeToStringConverter;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;
import fr.amapj.view.views.saisiecontrat.SaisieContrat;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.ModeSaisie;


/**
 * Page permettant de presenter la liste des utilisateurs
 * 
 *  
 *
 */
public class HistoriqueContratsView extends VerticalLayout implements View
{

	private Table beanTable;
	
	private TextField searchField;

	private String textFilter;
	
	private Button voirButton;

	
	BeanItemContainer<HistoriqueContratDTO> listPartContainer;
	
	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{
		listPartContainer = new BeanItemContainer<>(HistoriqueContratDTO.class);
		List<HistoriqueContratDTO> contrats = new HistoriqueContratsService().getHistoriqueContrats(SessionManager.getUserId());
		listPartContainer.addAll(contrats);
		
					
		// on trie par nom puis prenom
		listPartContainer.sort(new String[] { "nomProducteur" , "dateFin"}, new boolean[] { true, false});
			
		// Bind it to a component
		beanTable = new Table("", listPartContainer);
		beanTable.setStyleName("big strong");
		
		// Gestion de la liste des colonnes visibles
		beanTable.setVisibleColumns("nomProducteur" , "nomContrat" ,"dateDebut" , "dateFin" , "dateCreation" , "dateModification" , "montant");
		
		beanTable.setColumnHeader("nomProducteur","Producteur");
		beanTable.setColumnHeader("nomContrat","Contrat");
		beanTable.setColumnHeader("dateDebut","Première livraison");
		beanTable.setColumnHeader("dateFin","Dernière livraison");
		beanTable.setColumnHeader("dateCreation","Date création");
		beanTable.setColumnHeader("dateModification","Date modification");
		beanTable.setColumnHeader("montant","Montant (en €)");
		beanTable.setColumnAlignment("montant",Align.RIGHT);
		
		//
		beanTable.setConverter("dateCreation", new DateTimeToStringConverter());
		beanTable.setConverter("dateModification", new DateTimeToStringConverter());
		beanTable.setConverter("dateDebut", new DateToStringConverter());
		beanTable.setConverter("dateFin", new DateToStringConverter());
		beanTable.setConverter("montant", new CurrencyTextFieldConverter());
		
		
		beanTable.setSelectable(true);
		beanTable.setImmediate(true);

		beanTable.setSizeFull();

		beanTable.addItemClickListener(new ItemClickListener()
		{
			@Override
			public void itemClick(ItemClickEvent event)
			{
				if (event.isDoubleClick())
				{
					beanTable.select(event.getItemId());
				}
			}
		});
		
		// Activation au desactivation des boutons delete et edit
		beanTable.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				setModificationsEnabled(event.getProperty().getValue() != null);
			}

			private void setModificationsEnabled(boolean b)
			{
				voirButton.setEnabled(b);
			}
		});
		

		HorizontalLayout toolbar = new HorizontalLayout();
		
	
		
		Label title = new Label("Liste de vos anciens contrats");
		title.setSizeUndefined();
		title.addStyleName("h1");
		
		voirButton = new Button("Voir le détail");
		voirButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleVoir();
			}
		});
		voirButton.setEnabled(false);
		voirButton.addStyleName(ChameleonTheme.BUTTON_BIG);
		

		searchField = new TextField();
		searchField.setInputPrompt("Rechercher par le producteur ou le nom du contrat");
		searchField.addTextChangeListener(new TextChangeListener()
		{

			@Override
			public void textChange(TextChangeEvent event)
			{
				textFilter = event.getText();
				updateFilters();
			}
		});
		searchField.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
		searchField.setWidth("50%");
		
		
		toolbar.addComponent(voirButton);
		toolbar.setComponentAlignment(voirButton, Alignment.TOP_LEFT);
		toolbar.addComponent(searchField);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		

		setMargin(true);
		setSpacing(true);
		
		addComponent(title);
		addComponent(toolbar);
		addComponent(beanTable);
		setExpandRatio(beanTable, 1);
		setSizeFull();
		
	}
	
	protected void handleVoir()
	{
		HistoriqueContratDTO c = (HistoriqueContratDTO) beanTable.getValue();
		
		ContratDTO contratDTO = new MesContratsService().loadContrat(c.idModeleContrat,c.idContrat);
		SaisieContrat.saisieContrat(contratDTO,c.idUtilisateur,null,ModeSaisie.READ_ONLY,null);

	}

	private void updateFilters()
	{
		
		listPartContainer.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			Or or = new Or(new Like("nomProducteur", textFilter + "%", false), new Like("nomContrat", textFilter + "%", false));
			listPartContainer.addContainerFilter(or);
		}
	}

}
