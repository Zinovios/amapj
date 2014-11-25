package fr.amapj.view.engine.collectioneditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Action;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

import fr.amapj.view.engine.collectioneditor.columns.ColumnInfo;
import fr.amapj.view.engine.collectioneditor.columns.SearcherColumn;
import fr.amapj.view.engine.enumselector.EnumSearcher;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.engine.searcher.SearcherDefinition;
import fr.amapj.view.engine.tools.BaseUiTools;

/**
 * Permet la saisie de multi valeur  dans un tableau
 * en lien avec un BeanItem 
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CollectionEditor<BEANTYPE> extends CustomField implements Action.Handler
{

	private final static Logger logger = Logger.getLogger(CollectionEditor.class.getName());
	
	
	final private Action add = new Action(getMasterDetailAddItemCaption());
	final private Action remove = new Action(getMasterDetailRemoveItemCaption());
	final private Action up = new Action(getMasterDetailUpItemCaption());
	final private Action down = new Action(getMasterDetailDownItemCaption());
	
	final private Action[] actions = new Action[] { add, remove , up , down};
	

	private Table table;
	private BeanItem item;
	private Object propertyId;
	private Class<BEANTYPE> beanType;
	
	// La liste des objects graphiques permettant l'édition
	private RowList rows;
	
	// La liste des colonnes à afficher
	private List<ColumnInfo> columns;
	

	/**
	 * 
	 * 
	 */
	public CollectionEditor(String caption,BeanItem item, Object propertyId,Class<BEANTYPE> beanType)
	{
		this.item = item;
		this.beanType = beanType;
		this.propertyId = propertyId;
		
		rows = new RowList();
		columns = new ArrayList<ColumnInfo>();
		
		setCaption(caption);
	}
	
	
	
	public void addColumn(String propertyId, String title,FieldType fieldType,Object defaultValue)
	{
		columns.add(new ColumnInfo(propertyId, title, fieldType, defaultValue));
	}
	
	public void addSearcherColumn(String propertyId, String title, FieldType fieldType, Object defaultValue,SearcherDefinition searcher,Searcher linkedSearcher)
	{
		columns.add(new SearcherColumn(propertyId, title, fieldType, defaultValue,searcher,linkedSearcher));
	}
	
	
	

	private void buildTable()
	{
		table = new Table();
		table.addStyleName("big");
		
		
		/*
		 * Define the names and data types of columns. The "default value"
		 * parameter is meaningless here.
		 */
		table.addContainerProperty("A", Label.class, null);
		for (ColumnInfo col : columns)
		{
			Class clazz = getFieldAsClass(col.fieldType);
			table.addContainerProperty(col.title, clazz, null);
		}
		
		
		/* Add  items in the table. */
	
		List<BEANTYPE> beans = (List<BEANTYPE>) item.getItemProperty(propertyId).getValue();
		for (BEANTYPE bean : beans)
		{
			addRow(bean);		
		}
		
		getTable().setPageLength(10);
		getTable().addActionHandler(this);

		getTable().setEditable(true);
		getTable().setSelectable(true);
		table.setSortEnabled(false);
	}
	
	
	private Class getFieldAsClass(FieldType fieldType)
	{
		switch (fieldType)
		{
		case STRING:
			return TextField.class;
			
		case SEARCHER:
			return ComboBox.class;

		case CURRENCY:
			return TextField.class;
		
		case QTE:
			return TextField.class;

		case INTEGER:
			return TextField.class;

		case DATE:
			return PopupDateField.class;

			
		case CHECK_BOX:
			return CheckBox.class;
			
		case COMBO:
			return ComboBox.class;

			
		}
		
		throw new RuntimeException("Erreur inattendue");
	
	}
	
	
	private AbstractField getField(FieldType fieldType,ColumnInfo col)
	{
		switch (fieldType)
		{
		case STRING:
			return new TextField();
			 
		case SEARCHER:
			SearcherColumn s = (SearcherColumn) col;
			Searcher box =   new Searcher(s.searcher,null);
			if (s.params!=null)
			{
				box.setParams(s.params);
			}
			if (s.linkedSearcher!=null)
			{
				box.setLinkedSearcher(s.linkedSearcher);
			}
			return box;
			
		case CURRENCY:
			return BaseUiTools.createCurrencyField("",false);
			
		case QTE:
			return BaseUiTools.createQteField("");
			
		case INTEGER:
			return BaseUiTools.createIntegerField("");
			
		case DATE:
			return BaseUiTools.createDateField("");

			
		case CHECK_BOX:
			return BaseUiTools.createCheckBoxField("");
			
		case COMBO:
			return EnumSearcher.createEnumSearcher("", (Enum) col.defaultValue);


		}
		
		throw new RuntimeException("Erreur inattendue");
		
	}




	/**
	 * Permet l'ajout d'une ligne dans le tableau
	 * 
	 * Si bean est null, alors la ligne est chargé avec les valeurs par défaut 
	 * 
	 * @param bean
	 */
	private void addRow(BEANTYPE bean)
	{
		// Create the table row.
		Row row = new Row();
		BeanItem beanItem = null;
		if (bean !=null)
		{
			beanItem = new BeanItem(bean);
		}
		
		
				
		// Ajout de toutes les colonnes
		for (ColumnInfo col : columns)
		{
			Object val1 = col.defaultValue;
			
			// Récupération des données
			if (beanItem !=null)
			{
				val1 = beanItem.getItemProperty(col.propertyId).getValue();
			}
					
			AbstractField f = getField(col.fieldType,col);
			f.setConvertedValue(val1);
			
			row.addField(f);
		}
		
		// Ajout de la ligne et calcul de l'item id		 
		rows.add(row);
		
		//
		table.addItem(row.getColumnTable(),row.getItemId());
		
		
	}
	
	
	
	private void remove(Object itemId)
	{
		if (itemId==null)
		{
			return ;
		}
		
		//
		Object selectedRow = rows.remove(itemId);
		table.removeItem(itemId);
		if (selectedRow!=null)
		{
			table.select(selectedRow);
		}
	}
	
	
	protected Table getTable()
	{
		return table;
	}

	protected String getMasterDetailRemoveItemCaption()
	{
		return "Supprimer";
	}

	protected String getMasterDetailAddItemCaption()
	{
		return "Ajouter";
	}
	
	protected String getMasterDetailUpItemCaption()
	{
		return "Monter";
	}
	
	protected String getMasterDetailDownItemCaption()
	{
		return "Descendre";
	}
	

	public void handleAction(Action action, Object sender, Object target)
	{
		if (action == add)
		{
			addRow(null);
		} 
		else if (action == remove)
		{
			remove(target);
		}
		else if (action == up)
		{
			up(target);
		}
		else if (action == down)
		{
			down(target);
		}

	}

	private void down(Object target)
	{
		if (target==null)
		{
			return ;
		}
		
		int index = rows.getIndex(target);
		if (rows.canDown(index)==false)
		{
			return ;
		}
		Object itemId = rows.downRow(index);
		table.select(itemId);
		
	}



	private void up(Object target)
	{
		if (target==null)
		{
			return ;
		}
		
		int index = rows.getIndex(target);
		if (rows.canUp(index)==false)
		{
			return ;
		}
		Object itemId = rows.upRow(index);
		table.select(itemId);
		
	}



	public Action[] getActions(Object target, Object sender)
	{
		return actions;
	}

	

	@Override
	public void commit() throws SourceException, InvalidValueException
	{
		try
		{
			List<BEANTYPE> ls = new ArrayList<BEANTYPE>();
			
			for (Row row : rows.getRows())
			{
				BEANTYPE elt = beanType.newInstance();
				BeanItem beanItem = new BeanItem(elt);
				
				int i=0;
				for (ColumnInfo col : columns)
				{
					Object val = row.getFieldValue(i);
					beanItem.getItemProperty(col.propertyId).setValue(val);
					i++;
				}
				
				ls.add(elt);
			}
			item.getItemProperty(propertyId).setValue(ls);
		}
		catch (InstantiationException  | IllegalAccessException  | ReadOnlyException e)
		{
			logger.log(Level.WARNING, "Commit failed", e);
			throw new RuntimeException("Erreur inattendue",e);
		}

	}

	@Override
	public Class<?> getType()
	{
		return List.class;
	}

	public Collection getElements()
	{
		return (Collection) getPropertyDataSource().getValue();
	}

	
	@Override
	protected Component initContent()
	{
		CssLayout vl = new CssLayout();
		buildTable();
		vl.addComponent(getTable());

		CssLayout buttons = new CssLayout();
		buttons.addComponent(new Button(getMasterDetailAddItemCaption(), new ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				addRow(null);
			}
		}));
		// TODO replace with a (-) button in a generated column? Table currently
		// not selectable.
		buttons.addComponent(new Button(getMasterDetailRemoveItemCaption(), new ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				remove(getTable().getValue());
			}
		}));
		
		
		buttons.addComponent(new Button(getMasterDetailUpItemCaption(), new ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				up(getTable().getValue());
			}
		}));
		
		buttons.addComponent(new Button(getMasterDetailDownItemCaption(), new ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				down(getTable().getValue());
			}
		}));
		
		
		
		vl.addComponent(buttons);
		return vl;
	}

}
