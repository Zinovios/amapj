package fr.amapj.view.samples.test006;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.view.engine.popup.errorpopup.ErrorPopup;
import fr.amapj.view.samples.VaadinTest;

/**
 * Test de la saisie d'un nombre
 * 
 *  
 *
 */
public class Test006 implements VaadinTest
{

	public class MyBean
	{
		Integer value;

		public Integer getValue()
		{
			return value;
		}

		public void setValue(Integer value)
		{
			this.value = value;
		}

	}

	public void buildView(VaadinRequest request, UI ui)
	{
		VerticalLayout layout = new VerticalLayout();

		final MyBean myBean = new MyBean();
		BeanItem<MyBean> beanItem = new BeanItem<MyBean>(myBean);

		final Property<Integer> integerProperty = (Property<Integer>) beanItem.getItemProperty("value");
		final TextField textField = new TextField("Text field", integerProperty);

		Button submitButton = new Button("Submit value", new ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				String uiValue = textField.getValue();
				Integer propertyValue = integerProperty.getValue();
				int dataModelValue = myBean.getValue();

				ErrorPopup.open("UI value (String): " + uiValue + "\nProperty value (Integer): " + 
				propertyValue + "\nData model value (int): "+ dataModelValue);
			}
		});

		layout.addComponent(new Label("Text field type: " + textField.getType()));
		layout.addComponent(new Label("Text field type: " + integerProperty.getType()));
		layout.addComponent(textField);
		layout.addComponent(submitButton);

		ui.setContent(layout);

	}
}
