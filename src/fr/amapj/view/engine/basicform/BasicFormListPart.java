package fr.amapj.view.engine.basicform;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.IdentifiableUtil;
import fr.amapj.model.engine.Mdm;
import fr.amapj.service.services.dbservice.DbService;
import fr.amapj.service.services.searcher.SearcherService;
import fr.amapj.view.engine.enumselector.EnumSearcher;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.engine.widgets.IntegerTextFieldConverter;

abstract public class BasicFormListPart extends VerticalLayout implements ComponentContainer , View , PopupSuppressionListener
{

	private TextField searchField;

	private Button newButton;
	private Button deleteButton;
	private Button editButton;

	private String textFilter;

	private BeanItemContainer listPartContainer;

	protected Table beanTable;
	
	private List<ColumnInfo> colInfos = new ArrayList<ColumnInfo>();
	
	private String[] orderByInfo;

	public BasicFormListPart()
	{
	}
	
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		setSizeFull();
		buildMainArea();
	}
	
	/*
	 * Bloc des fonctions abstraites à implémenter par la vue de base  
	 */
	
	abstract protected void setFilter(BeanItemContainer container,String textFilter);

	abstract protected String getListPartInputPrompt();
	
	abstract protected String getListPartTitle();

	abstract protected void createColumn();
	
	abstract protected Class<? extends Identifiable> getClazz();

	abstract protected void createFormField(FormInfo formInfo);
	
	abstract protected String getEditorPartTitle(boolean addMode);
	
	/**
	 * Correspond au chargement de l'objet depuis la base
	 * 
	 * id peut être null dans le cas d'une création
	 */ 
	abstract protected Object loadDTO(Long id);
	
	/**
	 * Correspond à la sauvegarde de l'objet dans la base
	 * 
	 */ 
	abstract protected void saveDTO(Object dto,boolean isNew);
	
	
	
	/*
	 * Bloc d'helpers qui seront appelées par la vue de base, pour la partie Liste  
	 */
	
	protected void addColumn(String propertyId)
	{
		colInfos.add(new ColumnInfo(propertyId, StringUtils.capitalize(propertyId)));
	}
	
	protected void addColumn(Mdm e)
	{
		addColumn(e.prop());
	}
	
	
	protected void addColumn(String propertyId, String title)
	{
		colInfos.add(new ColumnInfo(propertyId, title));
		
	}
	
	protected void orderBy(String propertyId1)
	{
		orderByInfo = new String[] { propertyId1  };	
	}
	
	protected void orderBy(Mdm e1)
	{
		orderBy(e1.prop());	
	}
	
	protected void orderBy(String propertyId1, String propertyId2)
	{
		orderByInfo = new String[] { propertyId1 , propertyId2 };	
	}
	
	protected void orderBy(Mdm e1, Mdm e2)
	{
		orderBy(e1.prop(), e2.prop());	
	}

	/*
	 * Bloc d'helpers qui seront appelées par la vue de base, pour la partie Form  
	 */

	
	protected Field addFieldText(FormInfo formInfo,String propertyId,String title)
	{
		Field f = formInfo.binder.buildAndBind(title, propertyId);
		f.addValidator(new BeanValidator(getClazz(), propertyId));
		((TextField) f).setNullRepresentation("");
		((TextField) f).setStyleName(ChameleonTheme.TEXTFIELD_BIG);
		((TextField) f).setWidth("80%");
		formInfo.form.addComponent(f);
		return f;
	}
	
	protected Field addFieldText(FormInfo formInfo,String propertyId)
	{
		return addFieldText(formInfo, propertyId,StringUtils.capitalize(propertyId));
	}
	
	protected Field addFieldText(FormInfo formInfo,Mdm e)
	{
		return addFieldText(formInfo, e.prop());
	}
	
	
	/**
	 * Permet la saisie d'un Integer, c'est à dire un 
	 * nombre entier compris entre moins l'infi et plus l'infini 
	 * 
	 */
	protected Field addFieldInteger(FormInfo formInfo,String propertyId,String title)
	{
		Field tf = addFieldText(formInfo, propertyId, title);
		((TextField) tf).setConverter(new IntegerTextFieldConverter());
		return tf;
	}
	
	
	
	protected ComboBox addFieldEnum(FormInfo formInfo,String propertyId,String title,Enum enumeration)
	{
		ComboBox box = EnumSearcher.createEnumSearcher(formInfo.binder, title, enumeration, propertyId);
		formInfo.form.addComponent(box);
		return box;
		
	}
	
	
	/**
	 * 
	 * 
	 */
	protected Field addFieldSearcher(FormInfo formInfo,String propertyId,String title,Searcher searcher)
	{
		searcher.bind(formInfo.binder, propertyId);
		formInfo.form.addComponent(searcher);
		return searcher;
	}
	
	
	
	
	
	/*
	 * Bloc de création réelle de la vue
	 */
	

	private void buildMainArea()
	{

		createColumn();
		
		// Create a persistent person container
		listPartContainer = new BeanItemContainer(getClazz());
		onPopupClose();
		
		
		// Add a nested property to a many-to-one property
		for (ColumnInfo colInfo : colInfos)
		{
			if (colInfo.isNested())
			{
				listPartContainer.addNestedContainerProperty(colInfo.propertyId);				
			}
		}

		// Set up sorting if the natural order is not appropriate
		listPartContainer.sort(orderByInfo, new boolean[] { true, true });
			
		// Bind it to a component
		beanTable = new Table("", listPartContainer);
		beanTable.setStyleName("big strong");
		
		// Gestion de la liste des colonnes visibles
		List<String> colNames = new ArrayList<String>();
		for (ColumnInfo colInfo : colInfos)
		{
			colNames.add(colInfo.propertyId);
		}
		beanTable.setVisibleColumns(colNames.toArray());
		
		// Gestion des titres de colonnes
		for (ColumnInfo colInfo : colInfos)
		{
			beanTable.setColumnHeader(colInfo.propertyId,colInfo.title);
		}
		
		beanTable.setSelectable(true);
		beanTable.setImmediate(true);

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
				Long id = IdentifiableUtil.getIdOfSelectedItem(beanTable);
				if ( (id!=null) &&  (b==true) && (isAccessAllowed(id)==false) )
				{
					b = false;
				}
				
				deleteButton.setEnabled(b);
				editButton.setEnabled(b);
				enableSpecificButton(b);
			}
		});

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
		
	
		
		Label title = new Label(getListPartTitle());
		title.setSizeUndefined();
		title.addStyleName("h1");
		
		
		
		
		newButton = new Button("Ajouter");
		newButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAjouter();
			}
		});
		newButton.addStyleName(ChameleonTheme.BUTTON_BIG);

		deleteButton = new Button("Supprimer");
		deleteButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSupprimer();

			}
		});
		deleteButton.setEnabled(false);
		deleteButton.addStyleName(ChameleonTheme.BUTTON_BIG);

		editButton = new Button("Editer");
		editButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleEditer();

			}
		});
		editButton.setEnabled(false);
		editButton.addStyleName(ChameleonTheme.BUTTON_BIG);
		

		searchField = new TextField();
		searchField.setInputPrompt(getListPartInputPrompt());
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
		
		
		
		
		
		toolbar.addComponent(newButton);
		toolbar.addComponent(deleteButton);
		toolbar.addComponent(editButton);
		addSpecificButton(toolbar);
		toolbar.addComponent(searchField);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

		setMargin(true);
		setSpacing(true);
		
		addComponent(title);
		addComponent(toolbar);
		Component c = getExtraComponent();
		if (c!=null)
		{
			addComponent(c);
		}
		
		
		addComponent(beanTable);
		setExpandRatio(beanTable, 1);
		setSizeFull();

	}
	
	
	/**
	 * Indique si l'acces à cet élement est autorisé
	 * Dans le cas contraire, les boutons "Editer", "Supprimer" ne sont pas accessibles
	 * 
	 */
	protected boolean isAccessAllowed(Long id)
	{
		// // DO nothing - To overwrite
		return true;
	}


	protected Component getExtraComponent()
	{
		// DO nothing - To overwrite
		return null;
	}


	protected void enableSpecificButton(boolean b)
	{
		// DO nothing - To overwrite
	}


	protected void addSpecificButton(HorizontalLayout toolbar)
	{
		// DO nothing - To overwrite
	}


	protected void handleEditer()
	{
		Long id = IdentifiableUtil.getIdOfSelectedItem(beanTable);
		CorePopup.open(new BasicFormEditorPart(id,this), this);
	}

	protected void handleSupprimer()
	{
		Long id = IdentifiableUtil.getIdOfSelectedItem(beanTable);
		String text = "Etes vous sûr de vouloir supprimer cet élément ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,id);
		SuppressionPopup.open(confirmPopup, this);		
		
	}
	
	
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new DbService().deleteOneElement(getClazz(), idItemToSuppress);
	}
	
	
	public void onPopupClose()
	{
		listPartContainer.removeAllItems();
		List<Identifiable> ls = new SearcherService().getAllElements(getClazz());
		listPartContainer.addAll(ls);
	}
	

	protected void handleAjouter()
	{
		BasicFormEditorPart personEditor = new BasicFormEditorPart(null,this);
		CorePopup.open(personEditor, this);
		
	}

	private void updateFilters()
	{
		listPartContainer.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			setFilter(listPartContainer,textFilter);
		}
	}
}
