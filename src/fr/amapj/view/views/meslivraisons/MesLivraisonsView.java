package fr.amapj.view.views.meslivraisons;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.meslivraisons.JourLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.MesLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.MesLivraisonsService;
import fr.amapj.service.services.meslivraisons.ProducteurLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.QteProdDTO;
import fr.amapj.service.services.saisiepermanence.PermanenceDTO;


/**
 * Page permettant à l'utilisateur de gérer son compte :
 * -> changement de l'adresse e mail 
 * -> changement du mot de passe 
 * 
 *  
 *
 */
public class MesLivraisonsView extends Panel implements View
{

	SimpleDateFormat df = new SimpleDateFormat("EEEEE dd MMMMM yyyy");
	
	private Label titre;
	
	private Label livraison;
	
	private Date date;

	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{
		date = new Date();
		setSizeFull();

		
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		
		addButton(layout, "<<<< ",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				reculer();
			}
		});
		
		VerticalLayout central = new VerticalLayout();
		central.setWidth("100%");
		layout.addComponent(central);
		layout.setExpandRatio(central, 8.0f);
		
		titre = addLabel(central, "");
		livraison = addLabel2(central, "");
		
		
		addButton(layout, ">>>> ",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				avancer();
			}
		});
		
		
		
		
		
		refresh();
		
		layout.setMargin(true);
		layout.setSpacing(true);
		
		setContent(layout);
		addStyleName(ChameleonTheme.PANEL_BORDERLESS);
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
		MesLivraisonsDTO res = new MesLivraisonsService().getMesLivraisons(date);
		
		titre.setValue("Semaine du "+df.format(res.dateDebut)+" au "+df.format(res.dateFin));
		
		StringBuffer buf = new StringBuffer();
		
		for (JourLivraisonsDTO jour : res.jours)
		{
			buf.append("<br/><br/><h2>"+df.format(jour.date)+"</h2>");		
			
			if (jour.distribution!=null)
			{
				buf.append("<br/><h2><i><b>"+
							"!! Attention, vous devez réaliser la permanence ce "+df.format(jour.date)+"!!</br>"+
							"Liste des personnes de permanence : "+jour.distribution.getUtilisateurs()+
							"</i></b></h2>");
			}
			
			
			for (ProducteurLivraisonsDTO producteurLiv : jour.producteurs)
			{
				buf.append("<br/><br/><h3> Contrat : "+producteurLiv.modeleContrat+ " - Producteur : "+producteurLiv.producteur+"</h3>");
				
				for (QteProdDTO cell : producteurLiv.produits)
				{
					buf.append("<br/>"+cell.qte+" "+cell.nomProduit+" , "+
							cell.conditionnementProduit+"</h3>");
				}
				
			}
		}
		
		livraison.setValue(buf.toString());
		
		
	}
	
	


}
