package fr.amapj.view.engine.popup.messagepopup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ChameleonTheme;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.view.engine.popup.corepopup.CorePopup;

/**
 * Popup permettant d'afficher un message , avec 1 bouton OK
 *  
 */
@SuppressWarnings("serial")
public class MessagePopup extends CorePopup
{
	protected Button okButton;
	
	List<String> messages = new ArrayList<>();
	
	private ContentMode contentMode = ContentMode.TEXT;
	
	
	public MessagePopup(String title, List<String> strs)
	{
		popupTitle = title;
		messages.addAll(strs);
	}
	
	public MessagePopup(String title, String ... msgs)
	{
		this(title,ContentMode.TEXT,msgs);
	}
	
	public MessagePopup(String title, ContentMode contentMode,String ... msgs)
	{
		this.contentMode = contentMode;
		popupTitle = title;
		for (int i = 0; i < msgs.length; i++)
		{
			messages.add(msgs[i]);
		}
	}
	
	
	protected void createButtonBar()
	{		
		okButton = addDefaultButton("OK", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				close();
			}
		});
				
	
	}
	

	protected void createContent(VerticalLayout contentLayout)
	{
		for (String message : messages)
		{
			// Construction de la zone de texte
			HorizontalLayout hlTexte = new HorizontalLayout();
			hlTexte.setMargin(true);
			hlTexte.setSpacing(true);
			hlTexte.setWidth("100%");
			
			
			Label textArea = new Label(message,contentMode);
			textArea.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
			textArea.setWidth("80%");
			
			hlTexte.addComponent(textArea);
			hlTexte.setExpandRatio(textArea, 1);
			hlTexte.setComponentAlignment(textArea, Alignment.MIDDLE_CENTER);
			
			contentLayout.addComponent(hlTexte);
		}
	}
	
}
