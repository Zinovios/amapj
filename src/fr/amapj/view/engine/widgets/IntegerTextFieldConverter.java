package fr.amapj.view.engine.widgets;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class IntegerTextFieldConverter implements Converter
{


	public IntegerTextFieldConverter()
	{
	}

	public Integer convertToModel(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value!=null)
		{
			String str = (String) value;
			if (str.length()==0)
			{
				return new Integer(0);
			}
			
			return new Integer((String) str);
		}
		return null;
	}

	public Object convertToPresentation(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value != null)
		{
			Integer l = (Integer) value;
			return l.toString();
		}
		return null;
	}

	public Class<Integer> getModelType()
	{
		return Integer.class;
	}

	public Class<String> getPresentationType()
	{
		return String.class;
	}

	

}
