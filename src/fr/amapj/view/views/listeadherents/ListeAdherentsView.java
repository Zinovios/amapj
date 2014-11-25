package fr.amapj.view.views.listeadherents;

import java.util.List;

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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.excelgenerator.EGListeAdherent;
import fr.amapj.service.services.excelgenerator.EGListeAdherent.Type;
import fr.amapj.service.services.listeadherents.ListeAdherentsService;
import fr.amapj.view.engine.excelgenerator.LinkCreator;
import fr.amapj.view.engine.popup.corepopup.CorePopup;


/**
 * Page permettant de presenter la liste des utilisateurs
 * 
 *  
 *
 */
public class ListeAdherentsView extends VerticalLayout implements View
{

	private Table beanTable;
	
	private TextField searchField;

	private String textFilter;
	
	BeanItemContainer<Utilisateur> listPartContainer;
	
	private Button sendMailButton;
	
	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{
		listPartContainer = new BeanItemContainer<>(Utilisateur.class);
		List<Utilisateur> us = new ListeAdherentsService().getAllUtilisateurs(false);
		listPartContainer.addAll(us);
		
					
		// on trie par nom puis prenom
		listPartContainer.sort(new String[] { "nom" , "prenom" }, new boolean[] { true, true });
			
		// Bind it to a component
		beanTable = new Table("", listPartContainer);
		beanTable.setStyleName("big strong");
		
		// Gestion de la liste des colonnes visibles
		beanTable.setVisibleColumns("nom", "prenom", "email" , "numTel1" , "numTel2" );
		
		beanTable.setColumnHeader("nom","Nom");
		beanTable.setColumnHeader("prenom","Prénom");
		beanTable.setColumnHeader("email","E mail");
		beanTable.setColumnHeader("numTel1","Numéro Tel 1");
		beanTable.setColumnHeader("numTel2","Numéro Tel 2");
		
		
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

		HorizontalLayout toolbar = new HorizontalLayout();
		
	
		
		Label title = new Label("Liste des adhérents");
		title.setSizeUndefined();
		title.addStyleName("h1");
		
		
		sendMailButton = new Button("Envoyer un mail à tous ...");
		sendMailButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSendMail();
			}
		});
		

		searchField = new TextField();
		searchField.setInputPrompt("Rechercher par le nom ou le prénom");
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
		
		
		toolbar.addComponent(sendMailButton);
		toolbar.addComponent(LinkCreator.createLink(new EGListeAdherent(Type.STD)));
		toolbar.addComponent(searchField);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);
		toolbar.setSpacing(true);

		setMargin(true);
		setSpacing(true);
		
		addComponent(title);
		addComponent(toolbar);
		
		addComponent(beanTable);
		setExpandRatio(beanTable, 1);
		setSizeFull();
		
	}
	
	private void handleSendMail()
	{
		String mails = new ListeAdherentsService().getAllEmails();
		PopupCopyAllMail popup = new PopupCopyAllMail(mails);
		CorePopup.open(popup);
		
	}

	private void updateFilters()
	{
		
		listPartContainer.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			Or or = new Or(new Like(Utilisateur.P.NOM.prop(), textFilter + "%", false), new Like(Utilisateur.P.PRENOM.prop(), textFilter + "%", false));
			listPartContainer.addContainerFilter(or);
		}
	}
}
