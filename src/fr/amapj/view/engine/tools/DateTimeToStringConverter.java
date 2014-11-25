package fr.amapj.view.engine.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

/**
 * 
 */
public class DateTimeToStringConverter implements Converter<String, Date>
{
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

	@Override
	public Date convertToModel(String value, Class<? extends Date> targetType, Locale locale) throws ConversionException
	{
		// TODO
		return null;
	}

	@Override
	public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale) throws ConversionException
	{
		if (value==null)
		{
			return "";
		}
		return df.format(value);
	}

	@Override
	public Class<Date> getModelType()
	{
		return Date.class;
	}

	@Override
	public Class<String> getPresentationType()
	{
		return String.class;
	}

}
