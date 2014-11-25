package fr.amapj.view.engine.widgets;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;


/**
 * Gestion des quantités
 * TODO interdire les nombres négatifs
 * 
 * 
 *  
 *
 */
public class QteTextFieldConverter implements Converter
{


	public QteTextFieldConverter()
	{
	}

	public Integer convertToModel(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value==null)
		{
			return null;
		}
		
		Integer i = convertToInteger((String) value);
		if (i==null)
		{
			throw new ConversionException("Valeur incorrecte");
		}
		return i;
	}

	public Object convertToPresentation(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value != null)
		{
			Integer l = (Integer) value;
			if (l.intValue()==0)
			{
				return "";
			}
			else
			{
				return l.toString();
			}
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
	
	
	/**
	 * Cette méthode convertit une chaine de caractères de la forme
	 * 
	 * 123   en un Integer 
	 * 
	 * Return null si invalide 
	 * 
	 * @param value ne doit pas être null 
	 * @return
	 */
	public Integer convertToInteger(String str)
	{
		if (str.length()==0)
		{
			return new Integer(0);
		}
		
		// Uniquement des chiffres
		if (str.matches("^[0-9]*"))
		{
			return new Integer(str);
		}
		
		return null;
			
	}

	

}
