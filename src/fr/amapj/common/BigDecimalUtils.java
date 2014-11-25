package fr.amapj.common;

import java.math.BigDecimal;



public class BigDecimalUtils
{
	
	/**
	 * L'objet en parametre doit être un Long ou null
	 * 
	 */
	public static int toInt(Object o)
	{
		if (o==null)
		{
			return 0;
		}
		return ( (BigDecimal) o).intValue();
	}
}
