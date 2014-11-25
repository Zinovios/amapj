package fr.amapj.view.engine.basicform;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.view.engine.popup.corepopup.CorePopup;

/**
 * BasicForm est un outil générique permettant de gérer des formulaires de saisie pour les fichiers classiques
 * 
 * Fonctionnellement, une vue BasicForm est constituée de deux parties 
 * -> une partie avec la liste des enregistrements présents dans la base
 * -> une partie avec un aspect formulaire permettant de modifier ou de créer un enregistrement dans la base 
 * 
 * 
 * TODO : meilleure gestion des erreurs de validation (en particulier visualisation à l'écran)
 * 
 */
@SuppressWarnings("serial")
public class BasicFormEditorPart extends CorePopup
{

	private Object bean;
	private Item beanItem;
	private FieldGroup binder;
	private FormLayout form;
	

	// True si on ajoute un nouvel element, false si on edite un element
	// existant
	private boolean addMode;
		
	//
	private BasicFormListPart stdFormMainView;

	
	/**
	 * @param id : identifiable de l'objet à modifier, est null dans le cas de l'ajout d'un element
	 */
	public BasicFormEditorPart(Long id, BasicFormListPart stdFormMainView)
	{
		this.addMode = (id==null);
		
		this.stdFormMainView = stdFormMainView;
		
		// chargement de l'object depuis la base
		bean = stdFormMainView.loadDTO(id);
		
		beanItem = new BeanItem<Object>(bean);
		
		popupTitle = stdFormMainView.getEditorPartTitle(addMode);
	}
	
	protected void createContent(VerticalLayout contentLayout)
	{		
	
		// Construction de la forme
		form = new FormLayout();
		form.setWidth("100%");
		form.setImmediate(true);

		//
		binder = new FieldGroup();
		binder.setBuffered(true);
		binder.setItemDataSource(beanItem);
		
		FormInfo formInfo = new FormInfo(binder, form, addMode);
		stdFormMainView.createFormField(formInfo);
		
		contentLayout.addComponent(new Label(" "));
		contentLayout.addComponent(form);
		contentLayout.addComponent(new Label(" "));
		
		
	}
	
	protected void createButtonBar()
	{
		
		addDefaultButton("Sauvegarder", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSauvegarder();
			}
		});
		
		addButton("Annuler", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAnnuler();
			}
		});
	}

	


	protected void handleAnnuler()
	{
		close();
	}

	protected void handleSauvegarder()
	{
		try
		{
			binder.commit();
		}
		catch (CommitException e)
		{
			// Si une erreur apparait : on la notifie dans un popup window
			Notification.show("Une erreur est présente","Détails:"+e.getCause().getMessage(),
	                  Notification.Type.WARNING_MESSAGE);
			
			return;
		}
		
		// Sauvegarde dans la base
		stdFormMainView.saveDTO(bean, addMode);
		
		//
		close();
	}

}
