package fr.amapj.view.engine.enumselector;

import java.util.List;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.service.services.searcher.SearcherService;

public class EnumBean 
{

	private Enum value;

	public EnumBean(Enum value)
	{
		this.value = value;
	}
	
	public Enum getValue()
	{
		return value;
	}
	
	
	public String getLib()
	{
		if (value instanceof Mdm)
		{
			return  ( (Mdm)value).prop();
		}
			
			
		return value.toString();
	}
	
	public void setLib(String lib)
	{
		//
	}
	
	
	
}
