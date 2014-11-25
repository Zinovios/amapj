package fr.amapj.view.engine.searcher;

import java.util.List;
import java.util.Locale;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;

import fr.amapj.service.services.searcher.SearcherBean;

public class SearcherConverterBean implements Converter
{

	private final BeanItemContainer<SearcherBean> container;

	public SearcherConverterBean(BeanItemContainer<SearcherBean> container)
	{
		this.container = container;
	}

	public Long convertToModel(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value!=null)
		{
			return ((SearcherBean) value).getId();
		}
		return null;
	}

	public SearcherBean convertToPresentation(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value != null)
		{
			Long l = (Long) value;
			List<SearcherBean> ls = container.getItemIds();
			for (SearcherBean identifiable : ls)
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

	public Class<SearcherBean> getPresentationType()
	{
		return SearcherBean.class;
	}

	

}
