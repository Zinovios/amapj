package fr.amapj.view.engine.tools;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Table.ColumnHeaderMode;

import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;
import fr.amapj.view.engine.widgets.IntegerTextFieldConverter;
import fr.amapj.view.engine.widgets.QteTextFieldConverter;

public class BaseUiTools
{
	
	static public PopupDateField createDateField(FieldGroup binder,String propertyId,String title)
	{
		PopupDateField sample = new PopupDateField(title);
	    sample.setValue(new Date());
	    sample.setImmediate(true);
	    sample.setLocale(Locale.FRANCE);
	    sample.setResolution(Resolution.DAY);
		binder.bind(sample, propertyId);
		return sample;
	}
	
	static public PopupDateField createDateField(String title)
	{
		PopupDateField sample = new PopupDateField(title);
	    sample.setValue(new Date());
	    sample.setImmediate(true);
	    sample.setLocale(Locale.FRANCE);
	    sample.setResolution(Resolution.DAY);
		return sample;
	}

	
	
	/**
	 * Permet la saisie d'une quantité, c'est à dire un 
	 * nombre entier compris entre 0 et l'infini 
	 * 
	 */
	static public TextField createQteField(String title)
	{
		TextField tf = new TextField(title);
		tf.setConverter(new QteTextFieldConverter());
		tf.setNullRepresentation("");
		tf.setImmediate(true);
		return tf;
	}
	
	
	/**
	 * Permet la saisie d'un Integer, c'est à dire un 
	 * nombre entier compris entre moins l'infi et plus l'infini 
	 * 
	 */
	static public TextField createIntegerField(String title)
	{
		TextField tf = new TextField(title);
		tf.setConverter(new IntegerTextFieldConverter());
		tf.setNullRepresentation("");
		tf.setImmediate(true);
		return tf;
	}
	
	
	

	/**
	 * Permet la saisie d'un prix, c'est à dire un 
	 * nombre allant de 0.00 à 999999999.99
	 * avec deux chiffres après la virgule
	 * 
	 */

	static public TextField createCurrencyField(String title,boolean allowNegativeNumber)
	{
		TextField tf = new TextField(title);
		tf.setConverter(new CurrencyTextFieldConverter(allowNegativeNumber));
		tf.setNullRepresentation("");
		tf.setImmediate(true);
		return tf;
	}
	
	
	/**
	 * Permet la saisied'une check box
	 * 
	 */

	static public CheckBox createCheckBoxField(String title)
	{
		CheckBox checkbox = new CheckBox();
		checkbox.addStyleName("align-center");
		checkbox.addStyleName("big");
		checkbox.setImmediate(true);
		
		return checkbox;
	}
	
	
	
	
	

	
	/**
	 * Permet de créer une table sans titre 
	 * 
	 * @param nbCol
	 * @return
	 */
	static public Table createStaticTable(int nbCol)
	{
		Table t = new Table();
		t.setStyleName("big");
		t.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
			
		for (int i = 0; i < nbCol; i++)
		{
			t.addContainerProperty("", String.class, null);
		}
		
		return t;
	}
	
	
	static public Table addLine(Table t,String ... line)
	{
		t.addItem(line,null);
		return t;
	}
	
}
