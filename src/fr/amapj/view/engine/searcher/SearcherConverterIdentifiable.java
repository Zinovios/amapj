package fr.amapj.view.engine.searcher;

import java.util.List;
import java.util.Locale;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;

import fr.amapj.model.engine.Identifiable;

public class SearcherConverterIdentifiable implements Converter
{

	private final BeanItemContainer<Identifiable> container;

	public SearcherConverterIdentifiable(BeanItemContainer<Identifiable> container)
	{
		this.container = container;
	}

	public Long convertToModel(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value!=null)
		{
			return ((Identifiable) value).getId();
		}
		return null;
	}

	public Identifiable convertToPresentation(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value != null)
		{
			Long l = (Long) value;
			List<Identifiable> ls = container.getItemIds();
			for (Identifiable identifiable : ls)
			{
				if (identifiable.getId().equals(l))
				{
					return identifiable;
				}
			}
		}
		return null;
	}

	public Class<Long> getModelType()
	{
		return Long.class;
	}

	public Class<Identifiable> getPresentationType()
	{
		return Identifiable.class;
	}

	

}
