package fr.amapj.common;


public class LongUtils
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
		return ( (Long) o).intValue();
	}

	public static boolean equals(Long id1, Long id2)
	{
		if ( (id1==null) && (id2==null))
		{
			return true;
		}
		
		if ( (id1==null) || (id2==null))
		{
			return false;
		}
		
		return id1.equals(id2);
	}
}
