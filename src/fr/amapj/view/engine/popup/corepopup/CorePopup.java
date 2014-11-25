package fr.amapj.view.engine.popup.corepopup;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.view.engine.popup.PopupListener;

/**
 * Popup primitive , etant la base de tous les autres popup
 * Contient les outils pour créer une barre de bouton facilement
 *  
 */
@SuppressWarnings("serial")
abstract public class CorePopup extends Window
{

	protected CloseListener popupCloseListener;
	
	protected HorizontalLayout popupButtonBarLayout;
	
	protected String popupTitle;
	
	protected String popupWidth="40%";
	protected String popupHeight=null;
	
	private boolean isFirstButton = true;
	
	
	
	public void createPopup()
	{
		
		// Déclaration des propriétés et construction des champs
		VerticalLayout contentLayout = new VerticalLayout();
		createContent(contentLayout);
		
		// Construction de la barre de boutons
		popupButtonBarLayout = new HorizontalLayout();
		popupButtonBarLayout.setMargin(true);
		popupButtonBarLayout.setSpacing(true);
		popupButtonBarLayout.setWidth("100%");
		
		createButtonBar();
				
		
		// Construction globale
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.addComponent(contentLayout);
		
		/*
		VerticalLayout empty = new VerticalLayout();
		empty.setSizeFull();
		mainLayout.addComponent(empty);
		*/
		
		mainLayout.addComponent(popupButtonBarLayout);
		
		//
		setSizeUndefined();
		setContent(mainLayout);
		setCaption(popupTitle);
		setStyleName("opaque");
		setClosable(true);
		setResizable(true);
		setModal(true);
		setWidth(popupWidth);
		if (popupHeight!=null)
		{
			setHeight(popupHeight);
		}
		center();
	}
	
	abstract protected void createContent(VerticalLayout contentLayout);

	abstract protected void createButtonBar();
	
	
	protected Button addButton(String title,Button.ClickListener listener)
	{
		Button saveButton = new Button(title, listener);
		saveButton.addStyleName(ChameleonTheme.BUTTON_BIG);
		popupButtonBarLayout.addComponent(saveButton);
		if (isFirstButton)
		{
			popupButtonBarLayout.setExpandRatio(saveButton, 1);
			popupButtonBarLayout.setComponentAlignment(saveButton, Alignment.TOP_RIGHT);
			isFirstButton = false;
		}
		return saveButton;
	}
	
	protected Button addDefaultButton(String title,Button.ClickListener listener)
	{
		Button saveButton = addButton(title, listener);
		saveButton.addStyleName("default");
		return saveButton;
	}
	
	
	
	static public void open(CorePopup popup)
	{
		open(popup,null);
	}
	
	static public void open(CorePopup popup,final PopupListener listener)
	{
		if (listener!=null)
		{
			popup.popupCloseListener = new CloseListener()
			{
				@Override
				public void windowClose(CloseEvent e)
				{
					listener.onPopupClose();	
				}
			};
			popup.addCloseListener(popup.popupCloseListener);
		}
		popup.createPopup();
		UI.getCurrent().addWindow(popup);
	}
}
