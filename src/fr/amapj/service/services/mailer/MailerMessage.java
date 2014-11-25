package fr.amapj.service.services.mailer;

import java.util.ArrayList;
import java.util.List;


/**
 * Permet de stocker un message à envoyer par mail
 * 
 *
 */
public class MailerMessage
{

	// Destinataires du message
	private String email;
	
	// Titre du message
	private String title;
	
	// Contenu du message en html
	private String content;
	
	// Liste des pièces jointes
	private List<MailerAttachement> attachements = new ArrayList<>();

	public MailerMessage()
	{
		
	}
	
	public MailerMessage(String email, String title, String content)
	{
		super();
		this.email = email;
		this.title = title;
		this.content = content;
	}
	
	
	public void addAttachement(MailerAttachement attachement)
	{
		attachements.add(attachement);
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public List<MailerAttachement> getAttachements()
	{
		return attachements;
	}

	public void setAttachements(List<MailerAttachement> attachements)
	{
		this.attachements = attachements;
	}

	
	
}
