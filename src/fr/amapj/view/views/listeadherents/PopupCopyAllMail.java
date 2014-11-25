package fr.amapj.view.views.listeadherents;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.view.engine.popup.corepopup.CorePopup;

/**
 * Popup pour permettre la copie facile de tous les mails
 *  
 */
@SuppressWarnings("serial")
public class PopupCopyAllMail extends CorePopup
{
	
	private String mails;	
	
	public PopupCopyAllMail(String mails)
	{
		popupTitle = "Comment envoyer un mail à tous les amapiens ? ";
		popupWidth = "60%";
				
		this.mails = mails;
		
	}
	
	
	protected void createButtonBar()
	{		
		Button okButton = addDefaultButton("OK", new Button.ClickListener()
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
		
		// Construction de la zone de texte explicative
		String msg = "Pour envoyer un mail à tous les amapiens, vous devez :<br/><ul>"
					+ "<li>Faire un copier de toutes les adresses e-mail en faisant Ctrl+C ou en faisant clic droit + Copier sur la zone bleue ci dessous</li>"
					+ "<li>Ouvrir votre outil favori pour l'envoi des mails (Thunderbird, Gmail, Outlook, ...)</li>"
					+ "<li>Faire nouveau message</li>"
					+ "<li>Faire un coller de toutes les adresses e-mail en faisant Ctrl+C ou en faisant clic droit + Coller dans la liste des destinataires du message.</li></ul>";
		
		HorizontalLayout hlTexte = new HorizontalLayout();
		hlTexte.setMargin(true);
		hlTexte.setSpacing(true);
		hlTexte.setWidth("100%");
		
		
		Label textArea = new Label(msg,ContentMode.HTML);
		textArea.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
		textArea.setWidth("80%");
		
		
		hlTexte.addComponent(textArea);
		hlTexte.setExpandRatio(textArea, 1);
		hlTexte.setComponentAlignment(textArea, Alignment.MIDDLE_CENTER);
		
		contentLayout.addComponent(hlTexte);
		
		
		// Construction de la zone d'affichage des mails
		hlTexte = new HorizontalLayout();
		hlTexte.setMargin(true);
		hlTexte.setSpacing(true);
		hlTexte.setWidth("100%");
		
		
		TextArea listeMails = new TextArea("");
		listeMails.setValue(mails);
		listeMails.setReadOnly(true);
		listeMails.selectAll();
		listeMails.setWidth("80%");
		listeMails.setHeight(5, Unit.CM);
		
		
		hlTexte.addComponent(listeMails);
		hlTexte.setExpandRatio(listeMails, 1);
		hlTexte.setComponentAlignment(listeMails, Alignment.MIDDLE_CENTER);
		
		contentLayout.addComponent(hlTexte);
		
		
		
	}
	

	
}
