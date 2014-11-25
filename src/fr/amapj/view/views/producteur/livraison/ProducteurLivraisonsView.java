package fr.amapj.view.views.producteur.livraison;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.excelgenerator.EGFeuilleLivraison;
import fr.amapj.service.services.meslivraisons.JourLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.MesLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.MesLivraisonsService;
import fr.amapj.service.services.meslivraisons.ProducteurLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.QteProdDTO;
import fr.amapj.view.engine.excelgenerator.LinkCreator;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.views.producteur.ProducteurSelectorPart;


/**
 * Page permettant à l'utilisateur de gérer son compte :
 * -> changement de l'adresse e mail 
 * -> changement du mot de passe 
 * 
 *  
 *
 */
public class ProducteurLivraisonsView extends Panel implements View , PopupListener
{

	SimpleDateFormat df = new SimpleDateFormat("EEEEE dd MMMMM yyyy");
	
	private Date date;
	
	private ProducteurSelectorPart producteurSelector;
	
	private VerticalLayout central;
	

	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{
		date = new Date();
		producteurSelector = new ProducteurSelectorPart(this);
		
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponent(producteurSelector.getChoixProducteurComponent());
		vLayout.addComponent(getLivraisonDisplay());
		
		setSizeFull();
		setContent(vLayout);
		addStyleName(ChameleonTheme.PANEL_BORDERLESS);
		
		refresh();
	}



	private Component getLivraisonDisplay()
	{
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		
		addButton(layout, "<<<<<",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				reculer();
			}
		});
		
		central = new VerticalLayout();
		central.setWidth("100%");
		layout.addComponent(central);
		layout.setExpandRatio(central, 8.0f);	
		
		addButton(layout, ">>>>>",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				avancer();
			}
		});
		
		layout.setMargin(true);
		layout.setSpacing(true);
		
		return layout;
	}
	
	
	
	protected void avancer()
	{
		date = DateUtils.addDays(date, 7);
		refresh();
		
	}



	protected void reculer()
	{
		date = DateUtils.addDays(date, -7);
		refresh();
		
	}



	private Label addLabel(VerticalLayout layout, String str)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_H1);
		layout.addComponent(tf);
		return tf;
		
	}
	
	private Label addLabel2(VerticalLayout layout, String str)
	{
		Label tf = new Label(str,ContentMode.HTML);
		tf.addStyleName(ChameleonTheme.LABEL_BIG);
		layout.addComponent(tf);
		return tf;
		
	}

	
	
	private void addButton(HorizontalLayout layout, String str,ClickListener listener)
	{
		Button b = new Button(str);
		b.addStyleName(ChameleonTheme.BUTTON_BIG);
		b.addClickListener(listener);
		b.setWidth("100%");
		layout.addComponent(b);
		layout.setExpandRatio(b, 1.0f);
		
	}


	private void refresh()
	{
		Long idProducteur = producteurSelector.getProducteurId();
		
		if (idProducteur==null)
		{
			central.removeAllComponents();
			return ;
		}
		
		
		MesLivraisonsDTO res = new MesLivraisonsService().getLivraisonProducteur(date, idProducteur);
		central.removeAllComponents();

		// Titre
		addLabel(central, "Semaine du "+df.format(res.dateDebut)+" au "+df.format(res.dateFin));
	
		
		for (JourLivraisonsDTO jour : res.jours)
		{
			addLabel2(central,"<br/><br/><h2>"+df.format(jour.date)+"</h2>");		
			for (ProducteurLivraisonsDTO producteurLiv : jour.producteurs)
			{
				StringBuffer buf = new StringBuffer();
				buf.append("<br/><br/><h3> Contrat : "+producteurLiv.modeleContrat+"</h3>");
				
				for (QteProdDTO cell : producteurLiv.produits)
				{
					buf.append("<br/>"+cell.qte+" "+cell.nomProduit+" , "+cell.conditionnementProduit+"</h3>");
				}
				addLabel2(central,buf.toString());	
				
				Link extractFile = LinkCreator.createLink(new EGFeuilleLivraison(producteurLiv.idModeleContrat,producteurLiv.idModeleContratDate));
				central.addComponent(extractFile);
			}
		}
		
		
		
		
	}


	@Override
	public void onPopupClose()
	{
		refresh();
	}

}
