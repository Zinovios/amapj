package fr.amapj.view.views.remiseproducteur;

import java.text.SimpleDateFormat;
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
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.services.excelgenerator.EGRemise;
import fr.amapj.service.services.remiseproducteur.PaiementRemiseDTO;
import fr.amapj.service.services.remiseproducteur.RemiseDTO;
import fr.amapj.service.services.remiseproducteur.RemiseProducteurService;
import fr.amapj.view.engine.excelgenerator.TelechargerPopup;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.messagepopup.MessagePopup;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.tools.DateTimeToStringConverter;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.tools.TableTools;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;
import fr.amapj.view.views.common.contratselector.ContratSelectorPart;
import fr.amapj.view.views.common.contratselector.IContratSelectorPart;


/**
 * Gestion des remises 
 */
public class RemiseProducteurListPart extends VerticalLayout implements ComponentContainer , View ,  PopupSuppressionListener , IContratSelectorPart
{
	
	private TextField searchField;
	private String textFilter;
	
	private ContratSelectorPart contratSelectorPart;
	
	private Button newButton;
	private Button deleteButton;
	private Button voirButton;
	private Button telechargerButton;
	

	private BeanItemContainer<RemiseDTO> mcInfos;

	private Table cdesTable;

	public RemiseProducteurListPart()
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
		mcInfos = new BeanItemContainer<RemiseDTO>(RemiseDTO.class);
			
		// Bind it to a component
		cdesTable = new Table("", mcInfos);
		
		// Titre des colonnes
		cdesTable.setVisibleColumns(new Object[] { "moisRemise", "dateCreation" , "dateReelleRemise" , "mnt" });
		
		
		
		cdesTable.setColumnHeader("moisRemise","Mois remise");
		cdesTable.setColumnHeader("dateCreation","Date création");
		cdesTable.setColumnHeader("dateReelleRemise","Date réelle de la remise");
		cdesTable.setColumnHeader("mnt","Montant (en €)");
		cdesTable.setColumnAlignment("mnt",Align.RIGHT);
		
		
		
		//
		cdesTable.setConverter("dateCreation", new DateTimeToStringConverter());
		cdesTable.setConverter("dateReelleRemise", new DateToStringConverter());
		cdesTable.setConverter("mnt", new CurrencyTextFieldConverter());
				

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
		String str = "Liste des remises aux producteurs";
		
		Label title2 = new Label(str);
		title2.setSizeUndefined();
		title2.addStyleName("h1");
		
		// Partie choix du contrat
		contratSelectorPart = new ContratSelectorPart(this);
		HorizontalLayout toolbar1 = contratSelectorPart.getChoixContratComponent();
		
		// Partie bouton
		HorizontalLayout toolbar2 = new HorizontalLayout();
		
		
		newButton = new Button("Faire une remise");
		newButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAjouter();
			}
		});
		
		voirButton = new Button("Visualiser une remise");
		voirButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleVoir();
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
		
		
		
	

		deleteButton = new Button("Supprimer une remise");
		deleteButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSupprimer();
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

		
		toolbar2.addComponent(newButton);
		toolbar2.addComponent(voirButton);
		toolbar2.addComponent(deleteButton);
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


	private void handleTelecharger()
	{
		RemiseDTO remiseDTO = (RemiseDTO) cdesTable.getValue();
		TelechargerPopup popup = new TelechargerPopup();
		popup.addGenerator(new EGRemise(remiseDTO.id));
		CorePopup.open(popup,this);
	}



	private void handleVoir()
	{
		RemiseDTO remiseDTO = (RemiseDTO) cdesTable.getValue();
		String str = formatRemise(remiseDTO.id);
		MessagePopup.open(new MessagePopup("Visualisation d'une remise", ContentMode.HTML, str));	
	}
	
	

	private void handleSupprimer()
	{
		
		RemiseDTO remiseDTO = (RemiseDTO) cdesTable.getValue();
		String text = "Etes vous sûr de vouloir supprimer la remise de "+remiseDTO.moisRemise+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,remiseDTO.id,true);
		SuppressionPopup.open(confirmPopup, this);	
		
	}
	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new RemiseProducteurService().deleteRemise(idItemToSuppress);
	}


	private void handleAjouter()
	{
		Long idModeleContrat = contratSelectorPart.getModeleContratId();
		RemiseDTO remiseDTO = new RemiseProducteurService().prepareRemise(idModeleContrat);
		if (remiseDTO.preparationRemiseFailed==false)
		{
			CorePopup.open(new RemiseEditorPart(remiseDTO),this);
		}
		else
		{
			MessagePopup.open(new MessagePopup("Impossible de faire la remise.", ContentMode.HTML,"Il n'est pas possible de faire la remise à cause de :",remiseDTO.messageRemiseFailed));
		}
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
		List<RemiseDTO> res = new ArrayList<>();
		if (idModeleContrat!=null)
		{
			res = new RemiseProducteurService().getAllRemise(idModeleContrat);
		}
		
		// Tris par date théorique de remise
		String[] sortColumns = new String[] { "dateTheoRemise" };
		boolean[] sortAscending = new boolean[] { true } ;
		
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
		telechargerButton.setEnabled(enable);
	}
	
	
	/**
	 * Formatage de la remise pour affichage dans un popup
	 * @param remiseDTO
	 * @return
	 */
	private String formatRemise(Long remiseId)
	{
		RemiseDTO remiseDTO = new RemiseProducteurService().loadRemise(remiseId);
				
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		StringBuffer buf = new StringBuffer();
		
		buf.append("Remise de chèques du mois de "+remiseDTO.moisRemise+"<br/>");
		buf.append(remiseDTO.nbCheque+" chèques dans cette remise<br/>");
		buf.append("Montant total :  "+new CurrencyTextFieldConverter().convertToString(remiseDTO.mnt)+" € <br/><br/>");
		buf.append("Date de création :  "+df.format(remiseDTO.dateCreation)+"<br/>");
		buf.append("Date réelle de remise :  "+df.format(remiseDTO.dateReelleRemise)+"<br/><br/>");
		
		for (PaiementRemiseDTO paiement : remiseDTO.paiements)
		{
			buf.append(paiement.nomUtilisateur+" "+paiement.prenomUtilisateur+" - "+new CurrencyTextFieldConverter().convertToString(paiement.montant)+" € <br/>");
		}
		
		
		return buf.toString();
	}

	
}
