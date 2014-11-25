package fr.amapj.view.engine.popup.formpopup;

import java.util.EnumSet;
import java.util.List;

import javax.validation.ConstraintViolationException;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.view.engine.enumselector.EnumSearcher;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.errorpopup.ErrorPopup;
import fr.amapj.view.engine.tools.BaseUiTools;
import fr.amapj.view.engine.widgets.IntegerTextFieldConverter;

/**
 * Popup contenant un formulaire basé sur un PropertysetItem ou sur un BeanItem
 * avec la gestion couplée d'un wizard
 *  
 */
@SuppressWarnings("serial")
abstract public class WizardFormPopup extends CorePopup
{
	protected PropertysetItem item = new PropertysetItem();
	
	protected String nextButtonTitle = "Etape suivante ...";
	protected Button nextButton;
	
	protected String previousButtonTitle = "Etape précédente ...";
	protected Button previousButton;
	
	protected String saveButtonTitle = "Sauvegarder";
	
	protected FieldGroup binder;
	protected FormLayout form;
	
	private VerticalLayout contentLayout;
	
	protected Label hTitre;
	
	private int pageNumber;
	private Object[] enumArray;

	private boolean errorOnSave = false ;

	
	
	
	protected void createContent(VerticalLayout contentLayout)
	{
		//
		this.contentLayout = contentLayout;
		EnumSet enums = EnumSet.allOf(getEnumClass());
		enumArray = enums.toArray();
		
		
		// Mise en place du titre
		hTitre = new Label("",ContentMode.HTML);
		contentLayout.addComponent(hTitre);
		
		//
		updateForm();
		
	}
	
	private void updateForm()
	{
		if (form!=null)
		{
			contentLayout.removeComponent(form);
		}
		
		// Construction de la forme
		form = new FormLayout();
		form.setWidth("100%");
		form.setImmediate(true);


		//
		binder = new FieldGroup();
		binder.setBuffered(true);
		binder.setItemDataSource(item);
		
		// Déclaration des propriétés et construction des champs
		Enum current = (Enum) enumArray[pageNumber];
		addFields(current);
		
		contentLayout.addComponent(form);
		contentLayout.setComponentAlignment(form, Alignment.MIDDLE_LEFT);
		
	}

	protected void createButtonBar()
	{
		addButton("Annuler", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAnnuler();
			}
		});
		
		previousButton = addButton(previousButtonTitle, new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handlePrevious();
			}
		});
		previousButton.setEnabled(false);
		
		nextButton = addDefaultButton(nextButtonTitle, new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleNext();
			}
		});
		
		// On rend invisible le bouton précédent dans le cas ou il y a une seule page
		if (enumArray.length==1)
		{
			previousButton.setVisible(false);		
			nextButton.setCaption(saveButtonTitle);
		}
	}
	

	private void handleAnnuler()
	{
		binder.discard();
		close();
	}
	
	private void handlePrevious()
	{
		if (errorOnSave)
		{
			handleEndOfErrorOnSave();
			return ;
		}
		
		doCommit();
		pageNumber--;
		updateButtonStatus();
		updateForm();
	}

	private void updateButtonStatus()
	{
		// Gestion de l'état et du libellé du bouton suivant
		if (pageNumber==enumArray.length-1)
		{
			nextButton.setCaption(saveButtonTitle);		
		}
		else	
		{
			nextButton.setCaption(nextButtonTitle);
		}
		nextButton.setEnabled(true);
		
		// Gestion de l'état et du libellé du bouton précédent
		if (pageNumber==0)
		{
			previousButton.setEnabled(false);		
			
		}
		else	
		{
			previousButton.setEnabled(true);
		}
		
	}

	private void handleNext()
	{
		if (errorOnSave)
		{
			handleEndOfErrorOnSave();
			return ;
		}
		
		if (pageNumber==enumArray.length-1)
		{
			handleSauvegarder();
		}
		else
		{
			doCommit();
			pageNumber++;
			updateButtonStatus();
			updateForm();
		}
	}
	
	
	/**
	 * On a cliqué sur le bouton "Revenir à la saisie" après une erreur lors de la sauvegarde
	 */
	private void handleEndOfErrorOnSave()
	{
		errorOnSave = false;
		if (enumArray.length==1)
		{		
			nextButton.setCaption(saveButtonTitle);
		}
		setEmptyTitle();
		handlePrevious();
	}

	protected void handleSauvegarder()
	{
		doCommit();
		
		try
		{
			// Sauvegarde 
			performSauvegarder();
		}
		catch(OnSaveException e)
		{
			displayErrorOnSave(e.getAllMessages());
			return ;
		}
		catch(ConstraintViolationException e)
		{
			// TODO afficher plus clair 
			ErrorPopup.open(e);
			return;
		}
		catch(Exception e)
		{
			ErrorPopup.open(e);
			return;
		}

		close();
	}
	
	private void displayErrorOnSave(List<String> strs)
	{
		pageNumber++;
		errorOnSave  = true;
		if (enumArray.length==1)
		{		
			nextButton.setCaption("Retourner à la saisie ...");
		}
		else
		{
			nextButton.setEnabled(false);
		}
		
		if (form!=null)
		{
			contentLayout.removeComponent(form);
		}
		
		// Construction de la forme
		form = new FormLayout();
		form.setWidth("100%");
		form.setImmediate(true);


		//
		binder = new FieldGroup();
		binder.setBuffered(true);
		binder.setItemDataSource(item);
		
		// Déclaration des propriétés et construction des champs
		setErrorTitle("Une erreur est survenue durant la sauvegarde.");
		for (String string : strs)
		{
			addLabel(string, ContentMode.HTML);
		}
		
		contentLayout.addComponent(form);
		contentLayout.setComponentAlignment(form, Alignment.MIDDLE_LEFT);
		
	}

	private void doCommit()
	{
		try
		{
			binder.commit();
		}
		catch (CommitException e)
		{
			// Si une erreur apparait : on la notifie dans un popup window
			ErrorPopup.open(e);
			return;
		}
	}
	
	
	/**
	 * Dans ce mode, on ne que revenir à la page précédente
	 */
	protected void setBackOnlyMode()
	{
		nextButton.setEnabled(false);
	}
	
	
	/**
	 * Retourne null si tout est ok, sinon retourne une liste de messages d'erreur
	 * @return
	 */ 
	abstract protected void  performSauvegarder() throws OnSaveException;
	
	/**
	 * Retourne la liste des étapes
	 * 
	 * ne doit pas retourner null
	 */
	abstract protected Class getEnumClass();

	abstract protected void addFields(Enum step);
	
	
	protected void setEmptyTitle()
	{
		hTitre.setValue("");
		hTitre.setStyleName("");
	}
	
	
	protected void setStepTitle(String message)
	{
		hTitre.setValue("<h2>Etape "+(pageNumber+1)+" : "+message+"</h2>");
		hTitre.setStyleName(ChameleonTheme.LABEL_BIG);
	}
	
	/**
	 * Permet de positionner un titre en jaune, utile lors de la détection d'une erreur dans le processus de saisie 
	 * 
	 * @param message
	 */
	protected void setErrorTitle(String message)
	{
		hTitre.setValue("<h2>"+message+"</h2>");
		hTitre.setStyleName(ChameleonTheme.LABEL_WARNING);
	}
	
	
	protected Label addLabel(String content,ContentMode mode)
	{
		Label l = new Label(content,mode);
		l.setStyleName(ChameleonTheme.LABEL_BIG);
		l.setWidth("80%");
		form.addComponent(l);
		
		return l;
	}
	
	
	protected Field addTextField(String title, Object propertyId)
	{
		Field f = binder.buildAndBind(title, propertyId);
		// f.addValidator(new BeanValidator(getClazz(), propertyId));
		((TextField) f).setNullRepresentation("");
		((TextField) f).setStyleName(ChameleonTheme.TEXTFIELD_BIG);
		((TextField) f).setWidth("80%");
		form.addComponent(f);	
		
		return f;
	}
	
	
	protected Field addIntegerField(String title, Object propertyId)
	{
		Field f = addTextField(title, propertyId);
		( (TextField) f).setConverter(new IntegerTextFieldConverter());	
		return f;
	}
	
	
	protected TextArea addTextAeraField(String title, Object propertyId)
	{
		 TextArea f = new TextArea(title);
		 binder.bind(f, propertyId);
		
		f.setNullRepresentation("");
		f.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
		f.setWidth("80%");
		form.addComponent(f);	
		
		return f;
	}
	
	protected RichTextArea addRichTextAeraField(String title, Object propertyId)
	{
		 RichTextArea f = new RichTextArea(title);
		 binder.bind(f, propertyId);
		
		f.setNullRepresentation("");
		f.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
		f.setWidth("80%");
		form.addComponent(f);	
		
		return f;
	}
	

	protected ComboBox addComboEnumField(String title, Enum enumeration,String propertyId)
	{
		ComboBox box = EnumSearcher.createEnumSearcher(binder, title, enumeration, propertyId);
		form.addComponent(box);
		return box;
		
	}
	
	protected PopupDateField addDateField(String title, String propertyId)
	{
		PopupDateField f = BaseUiTools.createDateField(binder, propertyId,title);
		form.addComponent(f);
		return f;
	}
	
	protected Field addPasswordTextField(String title, Object propertyId)
	{
		PasswordField f = new PasswordField(title);
		binder.bind(f, propertyId);
		// 
		f.setNullRepresentation("");
		f.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
		f.setWidth("80%");
		form.addComponent(f);	
		
		return f;
	}

	
	protected TextField addIntegerField(String title, String propertyId)
	{
		TextField f = BaseUiTools.createIntegerField(title);
		binder.bind(f, propertyId);
		form.addComponent(f);
		return f;
	}
}
