package fr.amapj.view.views.sendmail;

import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.listeadherents.ListeAdherentsService;
import fr.amapj.service.services.mailer.MailerMessage;
import fr.amapj.service.services.mailer.MailerService;
import fr.amapj.service.services.moncompte.MonCompteService;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.popup.okcancelpopup.OKCancelPopup;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.ui.AppConfiguration;


/**
 * Page permettant à l'utilisateur de gérer son compte :
 * -> changement de l'adresse e mail 
 * -> changement du mot de passe 
 * 
 *  
 *
 */
public class SendMailView extends VerticalLayout implements View
{

	
	TextField titre;
	TextArea zoneTexte;
	

	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{
		setSizeFull();

		VerticalLayout layout = new VerticalLayout();
		
		titre = new TextField("Titre");
		titre.setWidth("100%");
		layout.addComponent(titre);
		
		
		zoneTexte = new TextArea("Message");
		zoneTexte.setWidth("100%");
		zoneTexte.setHeight("400px");
		
		layout.addComponent(zoneTexte);
		

		addButton(layout, "Envoi 1 mail",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				String email  = SessionManager.getSessionParameters().userEmail;
				handleEnvoi1Mail(email);
			}
		});
		
		
		addButton(layout, "Envoyer à tous",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleEnvoiAllMails();
			}
		});
		
	
		
		layout.setMargin(true);
		layout.setSpacing(true);
		addComponent(layout);
		
			
	}

	protected void handleEnvoi1Mail(String email)
	{
		String link = new ParametresService().getParametres().getUrl()+"?username="+email;
		String subject = titre.getValue();
		String htmlContent = zoneTexte.getValue();
		htmlContent = htmlContent.replaceAll("#LINK#", link);	
		new MailerService().sendHtmlMail( new MailerMessage(email, subject, htmlContent));
		Notification.show("Message envoyé à "+email);
		
	}

	protected void handleEnvoiAllMails()
	{
		List<Utilisateur> us = new ListeAdherentsService().getAllUtilisateurs(false);
		for (Utilisateur utilisateur : us)
		{
			String email = utilisateur.getEmail();
			if (email.indexOf('@')!=-1)
			{
				handleEnvoi1Mail(email);
			}
		}
	}
	
	
	
	private void addButton(Layout layout, String str,ClickListener listener)
	{
		Button b = new Button(str);
		b.addStyleName(ChameleonTheme.BUTTON_BIG);
		b.addClickListener(listener);
		layout.addComponent(b);
		
	}

}
