package fr.amapj.view.samples.test001;

import java.util.Iterator;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.NativeButton;

@SuppressWarnings("serial")
public class Test001SidebarMenu extends CssLayout
{

	public Test001SidebarMenu()
	{
		addStyleName("sidebar-menu");
	}

	public Test001SidebarMenu addButton(NativeButton b)
	{
		addComponent(b);
		b.addListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				updateButtonStyles();
				event.getButton().addStyleName("selected");
			}
		});
		return this;
	}

	private void updateButtonStyles()
	{
		for (Iterator<Component> iterator = getComponentIterator(); iterator.hasNext();)
		{
			Component c = iterator.next();
			c.removeStyleName("selected");
		}
	}

	public void setSelected(NativeButton b)
	{
		updateButtonStyles();
		b.addStyleName("selected");
	}
}