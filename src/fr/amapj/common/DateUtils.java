package fr.amapj.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils
{

	static public Date suppressTime(Date d)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR, 0);

		return c.getTime();
	}

	/**
	 * Retrouve le premier lundi avant cette date, et supprime la notion de
	 * temps
	 */
	static public Date firstMonday(Date d)
	{
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);

		c.setTime(d);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR, 0);

		// Retourne 1 pour dimanche, 2 pour lundi, ...
		int delta = c.get(Calendar.DAY_OF_WEEK);

		if (delta == 1)
		{
			c.add(Calendar.DAY_OF_MONTH, -6);
		} 
		else
		{
			c.add(Calendar.DAY_OF_MONTH, 2 - delta);
		}

		return c.getTime();
	}
	
	/**
	 * Retrouve le premier jour du mois indiqué par d
	 */
	public static Date firstDayInMonth(Date d)
	{
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);

		c.setTime(d);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR, 0);

		// Retourne 1 pour dimanche, 2 pour lundi, ...
		int delta = c.get(Calendar.DAY_OF_MONTH);

		c.add(Calendar.DAY_OF_MONTH, 1-delta);

		return c.getTime();
	}
	
	
	/**
	 * Retrouve le numero du jour dans le mois, 
	 * 
	 * c'est a dire 12 pour le 12/03/2014
	 */
	static public int getDayInMonth(Date d)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(d);

		return c.get(Calendar.DAY_OF_MONTH);

	}
	
	
	

	/**
	 * Retrouve le numero du jour dans le mois, c'est a dire le 1er jeudi du
	 * mois ou le 2eme ou le 3eme ou le ...
	 */
	static public int getDayOfWeekInMonth(Date d)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(d);

		return c.get(Calendar.DAY_OF_WEEK_IN_MONTH);

	}

	public static Date addDays(Date date, int amount)
	{

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		c.add(Calendar.DAY_OF_MONTH, amount);
		return c.getTime();
	}
	
	public static Date addHour(Date date, int amount)
	{

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		c.add(Calendar.HOUR_OF_DAY, amount);
		return c.getTime();
	}
	
	
	/**
	 * Permet d'ajouter x mois à la date courante 
	 * 
	 */
	public static Date addMonth(Date date, int amount)
	{

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		c.add(Calendar.MONTH, amount);
		return c.getTime();
	}
	
	
	
	

	public static void main(String[] args) throws ParseException
	{
		Date d = new SimpleDateFormat("dd/MM/yyyy").parse("17/09/2013");

		System.out.println("d=" + addMonth(d,1));
	}

	public static int getDeltaDay(Date d1, Date d2)
	{
		// TODO ce n'est pas bon aux changements d'heure ete hiver
		return (int) ((d2.getTime()-d1.getTime())/(1000L*3600L*24L));
	}



}
