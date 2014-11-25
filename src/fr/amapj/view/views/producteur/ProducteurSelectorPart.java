package fr.amapj.view.views.producteur;

import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.views.searcher.SearcherList;


/**
 * Outil permettant le choix du producteur 
 * sous la forme d'un bandeau en haut de l'écran
 *  
 *
 */
public class ProducteurSelectorPart
{

	
	private Searcher producteurBox;
	
	private Long idProducteur;
	
	private Button reinitButton;
	
	private List<Producteur> allowedProducteurs;	
	
	private PopupListener listener;

	/**
	 * 
	 */
	public ProducteurSelectorPart(PopupListener listener)
	{
		this.listener = listener;
		allowedProducteurs = new AccessManagementService().getAccessLivraisonProducteur(SessionManager.getUserRoles(),SessionManager.getUserId());
	}


	public HorizontalLayout getChoixProducteurComponent()
	{
		// Partie choix du producteur
		HorizontalLayout toolbar1 = new HorizontalLayout();	
	
		
		Label pLabel = new Label("Producteur");
		pLabel.addStyleName(ChameleonTheme.LABEL_BIG);
		pLabel.setSizeUndefined();
		
		toolbar1.addComponent(pLabel);
		
		if (allowedProducteurs.size()>1)
		{
			constructMultipleProducteur(toolbar1);
		}
		else
		{
			constructOneProducteur(toolbar1);
		}
		
		toolbar1.setSpacing(true);
		toolbar1.setMargin(true);
		toolbar1.setWidth("100%");
	
		return toolbar1;
	}
	
	
	
	
	private void constructOneProducteur(HorizontalLayout toolbar1)
	{
		Producteur p = allowedProducteurs.get(0);
		idProducteur = p.getId();
		
		
		Label pLabel = new Label(""+p.getNom());
		pLabel.addStyleName(ChameleonTheme.LABEL_H1);
		pLabel.setSizeUndefined();
		
		toolbar1.addComponent(pLabel);
		
	}


	private void constructMultipleProducteur(HorizontalLayout toolbar1)
	{
		producteurBox = new Searcher(SearcherList.PRODUCTEUR,null,allowedProducteurs);
		producteurBox.setImmediate(true);
		producteurBox.setSizeUndefined();
		producteurBox.addStyleName(ChameleonTheme.LABEL_BIG);
		
		producteurBox.addValueChangeListener(new Property.ValueChangeListener()
		{
			
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				handleProducteurChange();
			}
		});
		
		reinitButton = new Button("Changer de producteur");
		reinitButton.addClickListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleReinit();
			}
		});
		reinitButton.addStyleName(ChameleonTheme.BUTTON_BIG);
	
		
		
		
		toolbar1.addComponent(producteurBox);
		toolbar1.addComponent(reinitButton);
		toolbar1.setExpandRatio(reinitButton, 1);
		toolbar1.setComponentAlignment(reinitButton, Alignment.TOP_RIGHT);
		
	}


	/**
	 * 
	 */
	private void handleProducteurChange()
	{
		idProducteur = (Long) producteurBox.getConvertedValue();
		if (idProducteur!=null)
		{
			producteurBox.setEnabled(false);
		}
		listener.onPopupClose();
	}
	
	
	protected void handleReinit()
	{
		producteurBox.setValue(null);
		producteurBox.setEnabled(true);
		idProducteur = null;
		listener.onPopupClose();
	}
	
	
	public Long getProducteurId()
	{
		return idProducteur;
	}
	

}
