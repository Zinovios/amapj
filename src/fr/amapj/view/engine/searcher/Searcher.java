package fr.amapj.view.engine.searcher;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.ComboBox;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.service.services.searcher.SearcherBean;



/**
 * Implementation du searcher 
 * 
 */
public class Searcher extends ComboBox
{
	private Object params;
	
	private BeanItemContainer<Identifiable> container;
	
	private BeanItemContainer<SearcherBean> containerBean;
	
	private SearcherDefinition iSearcher;
	
	private Searcher linkedSearcher;
	
	// 
	private List<? extends Identifiable> fixedValues = null;
	
	
	public Searcher(SearcherDefinition iSearcher)
	{
		this(iSearcher,iSearcher.getTitle(),null);
	}
	
	public Searcher(SearcherDefinition iSearcher,String title)
	{
		this(iSearcher,title,null);
	}
	
	public Searcher(SearcherDefinition iSearcher,String title,List<? extends Identifiable> fixedValues)
	{
		super(title);
		this.iSearcher = iSearcher;
		this.fixedValues = fixedValues;
		
		setImmediate(true);
		
		if (iSearcher.getPropertyId()!=null)
		{
			container = new BeanItemContainer(iSearcher.getClazz());
			setContainerDataSource(container);
			setItemCaptionMode(ItemCaptionMode.PROPERTY);
			setItemCaptionPropertyId(iSearcher.getPropertyId());
			setConverter(new SearcherConverterIdentifiable(container));
		}
		else
		{
			
			containerBean = new BeanItemContainer(SearcherBean.class);
			setContainerDataSource(containerBean);
			setItemCaptionMode(ItemCaptionMode.PROPERTY);
			setItemCaptionPropertyId("lib");
			setConverter(new SearcherConverterBean(containerBean));
		}
		
		// Si il n'y a pas de paramètres on peut tout de suite charger le contenu 
		if (iSearcher.needParams()==false)
		{
			refreshLines();
		}
		
	}
	
	
	public void setParams(Object params)
	{
		this.params = params;
		refreshLines();	
	}
	
	
	public void setLinkedSearcher(Searcher linkedSearcher)
	{
		this.linkedSearcher = linkedSearcher;
		
		//
		linkedSearcher.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				handleChangeLinkedSearcher();
			}
		});
		
		// On remplit immédiatement le searcher si le searcher lié est déjà actif 
		if (linkedSearcher.getConvertedValue()!=null)
		{
			handleChangeLinkedSearcher();
		}
		
	}
	
	
	
	
	private void handleChangeLinkedSearcher()
	{
		Long id = (Long) linkedSearcher.getConvertedValue();
		setParams(id);
	}


	private void refreshLines()
	{
		if (iSearcher.getPropertyId()!=null)
		{
			container.removeAllItems();
			if (canBeFill())
			{
				container.addAll(getValues());
			}
		}
		else
		{
			containerBean.removeAllItems();
			if (canBeFill())
			{
				List<? extends Identifiable> identifiables = getValues();
				List<SearcherBean> beans = new ArrayList<>();
				for (Identifiable identifiable : identifiables)
				{
					beans.add(new SearcherBean(identifiable, iSearcher.toString(identifiable)));
				}
				containerBean.addAll(beans);
			}
		}
	}
	
	/**
	 * Permet de retrouver la liste des valeurs à mettre dans le searcher
	 * @return
	 */
	private List<? extends Identifiable> getValues()
	{
		if (fixedValues!=null)
		{
			return fixedValues;
		}
		
		return iSearcher.getAllElements(params);
	}

	/**
	 * 
	 * @return
	 */
	private boolean canBeFill()
	{
		if ( (iSearcher.needParams()) && (params==null))
		{
			return false;
		}
		return true;
	}


	public void bind(FieldGroup binder,String propertyId)
	{
		binder.bind(this, propertyId);
	}
		
}
