package fr.amapj.view.engine.enumselector;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;

public class EnumSearcher 
{

	/**
	 * Permet de créer un searcher sur un type énuméré à mapper avec un Enum
	 */
	static public ComboBox createEnumSearcher(FieldGroup binder,String title,Enum enumeration,String propertyId)
	{
		List<EnumBean> beans = new ArrayList<>();
		EnumSet enums = EnumSet.allOf(enumeration.getDeclaringClass());
		for (Object enum1 : enums)
		{
			beans.add(new EnumBean( (Enum) enum1));
		}
			
		
		BeanItemContainer<EnumBean> container = new BeanItemContainer(EnumBean.class, beans);
	
		
		
		
		ComboBox comboBox = new ComboBox(title,container);
		
		comboBox.setConverter(new ComboBoxEnumConverter(container));
		
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("lib");
		
		// TODO ca ne marche pas 
		comboBox.setConvertedValue(enumeration);
		
		
		binder.bind(comboBox, propertyId);
		
		return comboBox;
	}
	
	
	/**
	 * Permet de créer un searcher sur un type énuméré à mapper avec un Enum
	 */
	static public ComboBox createEnumSearcher(String title,Enum enumeration)
	{
		List<EnumBean> beans = new ArrayList<>();
		EnumSet enums = EnumSet.allOf(enumeration.getDeclaringClass());
		for (Object enum1 : enums)
		{
			beans.add(new EnumBean( (Enum) enum1));
		}
			
		
		BeanItemContainer<EnumBean> container = new BeanItemContainer(EnumBean.class, beans);
	
		
		
		
		ComboBox comboBox = new ComboBox(title,container);
		
		comboBox.setConverter(new ComboBoxEnumConverter(container));
		
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("lib");
		
		// TODO ca ne marche pas 
		comboBox.setConvertedValue(enumeration);
		
		
		return comboBox;
	}
	
}
