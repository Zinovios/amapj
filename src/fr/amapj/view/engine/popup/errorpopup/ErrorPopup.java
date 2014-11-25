package fr.amapj.view.engine.popup.errorpopup;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolationException;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.common.StackUtils;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.service.services.session.SessionParameters;
import fr.amapj.view.engine.popup.corepopup.CorePopup;


/**
 * Popup pour la confirmation de la suppression d'un élément
 *  
 */
@SuppressWarnings("serial")
public class ErrorPopup extends CorePopup
{

	private final static Logger logger = Logger.getLogger(ErrorPopup.class.getName());
	
	private Button okButton;
	private String okButtonTitle = "OK";
	
	private Button copyStackButton;
	private String copyStackButtonTitle = "Copier toute la trace";
	
	private String message;
	private Throwable throwable;
	
	public ErrorPopup(String message)
	{
		this(message,null);
	}
	
	public ErrorPopup(String message,Throwable throwable)
	{
		this.message = message;
		this.throwable = throwable;
		popupTitle = "Erreur";
	}
	
	public ErrorPopup(Throwable throwable)
	{
		this(null,throwable);
	}
	
	
	

	protected void createContent(VerticalLayout contentLayout)
	{

		// Message loggé 
		SessionParameters p = SessionManager.getSessionParameters();
		String debugMessage = p.userNom +" "+p.userPrenom+" voit l'erreur suivante dans un popup :"+message;
		logger.log(Level.INFO, debugMessage, throwable);
		
		if (throwable instanceof ConstraintViolationException)
		{
			ConstraintViolationException e = (ConstraintViolationException) throwable;
			logger.log(Level.INFO, StackUtils.getConstraints(e));
		}
		

		// Message utilisateur
		String msg = "Une erreur est survenue :";
		if (message!=null)
		{
			msg = msg+message+" : ";
		}
		if (throwable!=null)
		{
			msg = msg + throwable.getMessage();
		}
		

		// Construction de la zone de texte
		HorizontalLayout hlTexte = new HorizontalLayout();
		hlTexte.setMargin(true);
		hlTexte.setSpacing(true);
		hlTexte.setWidth("100%");
		
		
		Label textArea = new Label(msg);
		textArea.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
		textArea.setWidth("80%");
		
		hlTexte.addComponent(textArea);
		hlTexte.setExpandRatio(textArea, 1);
		hlTexte.setComponentAlignment(textArea, Alignment.MIDDLE_CENTER);
		
		contentLayout.addComponent(hlTexte);
	}
	
	protected void createButtonBar()
	{
	
		copyStackButton = addButton(copyStackButtonTitle, new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleCopyStack();
			}
		});
		

		okButton = addDefaultButton(okButtonTitle, new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleOK();
			}
		});	
	}

	protected void handleOK()
	{
		close();
	}
	
	protected void handleCopyStack()
	{
		// TODO 
	}
	


	/**
	 * Attention : cette méthode rend la main tout de suite, ca n'attend pas 
	 * le clic de l'opérateur 
	 */	
	static public void open(Throwable throwable)
	{
		open(new ErrorPopup(throwable));
	}
	
	/**
	 * Attention : cette méthode rend la main tout de suite, ca n'attend pas 
	 * le clic de l'opérateur 
	 */	
	static public void open(String message,Throwable throwable)
	{
		open(new ErrorPopup(message,throwable));
	}

	/**
	 * Attention : cette méthode rend la main tout de suite, ca n'attend pas 
	 * le clic de l'opérateur 
	 */	
	static public void open(String message)
	{
		open(new ErrorPopup(message));
	}
}
