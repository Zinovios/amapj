package fr.amapj.view.engine.enumselector;

import java.util.List;
import java.util.Locale;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;

import fr.amapj.model.engine.Identifiable;

public class ComboBoxEnumConverter implements Converter
{

	private final BeanItemContainer<EnumBean> container;

	public ComboBoxEnumConverter(BeanItemContainer<EnumBean> container)
	{
		this.container = container;
	}

	public Enum convertToModel(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value!=null)
		{
			return ((EnumBean) value).getValue();
		}
		return null;
	}

	public EnumBean convertToPresentation(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value != null)
		{
			Enum l = (Enum) value;
			List<EnumBean> ls = container.getItemIds();
			for (EnumBean enumBean : ls)
			{
				if (enumBean.getValue().equals(l))
				{
					return enumBean;
				}
			}
		}
		return null;
	}

	public Class<Enum> getModelType()
	{
		return Enum.class;
	}

	public Class<EnumBean> getPresentationType()
	{
		return EnumBean.class;
	}

	

}
