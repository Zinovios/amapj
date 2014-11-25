package fr.amapj.view.samples.test002;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class Test002LoginView extends VerticalLayout implements View
{
	Navigator navigator;

	public Test002LoginView(Navigator navigator)
	{
		this.navigator = navigator;
		createView();
	}

	private void createView()
	{
		setSizeFull();
		
		@SuppressWarnings("unused")
		LoginForm loginForm = new LoginForm();
		
		
		addComponent(loginForm);
		setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

		Button button = new Button("Go to Main View",
				new Button.ClickListener()
				{
					@Override
					public void buttonClick(ClickEvent event)
					{
						navigator.navigateTo(Test002.MAINVIEW);
					}
				});
		addComponent(button);
		setComponentAlignment(button, Alignment.MIDDLE_CENTER);
		
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		

	}
}
