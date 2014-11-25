package fr.amapj.service.services.mailer;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.common.DebugUtil;
import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.ui.AmapUI;
import fr.amapj.view.engine.ui.AppConfiguration;

/**
 * Permet d'envoyer des mails
 * 
 *
 */
public class MailerService
{
	private final static Logger logger = Logger.getLogger(MailerService.class.getName());


	public MailerService()
	{

	}
	
	private Message initMail(String recipient, String subject) throws AddressException, MessagingException, UnsupportedEncodingException
	{
		ParametresDTO param = new ParametresService().getParametres();
		 
		final String username = param.getSendingMailUsername();
		final String password = param.getSendingMailPassword();
		
		if ( (username==null) || (username.length()==0))
		{
			throw new AmapjRuntimeException("Le service mail n'est pas paramétré : absence du nom de l'utilisateur");
		}
		
		if ( (password==null) || (password.length()==0))
		{
			throw new AmapjRuntimeException("Le service mail n'est pas paramétré : absence du mot de passe");
		}
		
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator()
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(username, password);
			}
		});
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(username,param.nomAmap));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
		message.setSubject(subject);
		
		return message;
	}
	
	
	
	/**
	 *  Envoi d'un mail simple en utilisant TLS
	 */
	public void sendHtmlMail(MailerMessage mailerMessage)
	{
		try
		{
			Message message = initMail(mailerMessage.getEmail(), mailerMessage.getTitle());
			
			Multipart mp = new MimeMultipart();

	        MimeBodyPart htmlPart = new MimeBodyPart();
	        htmlPart.setContent(mailerMessage.getContent(), "text/html");
	        mp.addBodyPart(htmlPart);

	        for (MailerAttachement attachement : mailerMessage.getAttachements())
			{
		        MimeBodyPart attachment = new MimeBodyPart();
		        attachment.setFileName(attachement.getName());
		        attachment.setDataHandler(new DataHandler(attachement.getDataSource()));
		        mp.addBodyPart(attachment);
	        }
			
			message.setContent(mp);
			Transport.send(message);
			logger.info("Envoi d'un message a : "+mailerMessage.getEmail());

		}
		catch (MessagingException | UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	public static void main(String[] args)
	{
		TestTools.init();
		
		MailerMessage message = new MailerMessage("essai@gmail.com", "essai", "<h1>This is actual message</h1>");
		
		new MailerService().sendHtmlMail(message);
	}
}
