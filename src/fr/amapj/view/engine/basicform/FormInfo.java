package fr.amapj.view.engine.basicform;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.FormLayout;

public class FormInfo
{

	public FieldGroup binder;
	public FormLayout form;
	public boolean addMode;

	public FormInfo(FieldGroup binder, FormLayout form, boolean addMode)
	{
		this.binder = binder;
		this.form = form;
		
		this.addMode = addMode;
	}

}
