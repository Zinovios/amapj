package fr.amapj.view.views.mespermanences;

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
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.excelgenerator.EGPlanningPermanence;
import fr.amapj.service.services.saisiepermanence.MesPermanencesDTO;
import fr.amapj.service.services.saisiepermanence.PermanenceDTO;
import fr.amapj.service.services.saisiepermanence.PermanenceService;
import fr.amapj.service.services.saisiepermanence.PermanenceUtilisateurDTO;
import fr.amapj.view.engine.excelgenerator.LinkCreator;


/**
 * Page permettant à l'utilisateur de visualiser ses distributions
 * et le planning global des distributions
 * 
 *  
 *
 */
public class PlanningPermanenceView extends Panel implements View
{

	SimpleDateFormat df = new SimpleDateFormat("EEEEE dd MMMMM yyyy");
	
	private Label mesDistributions;
	
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
		
		VerticalLayout vertical = new VerticalLayout();
		

		// Partie haute
		addLabel(vertical, "Les dates des permanences que je dois faire");
		mesDistributions = addLabel2(vertical, "");
		
		// Partie centrale
		addLabel(vertical, "Télécharger tout le planning");
		vertical.addComponent(LinkCreator.createLink(new EGPlanningPermanence(new Date())));
		
		
		// Partie basse
		addLabel(vertical, "Voir le planning semaine par semaine");
		addLabel2(vertical, "<br/>");
		
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
		
		titre = addLabel2(central, "");
		livraison = addLabel2(central, "");
		
		
		addButton(layout, ">>>> ",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				avancer();
			}
		});
		
		vertical.addComponent(layout);
		
		refresh();
		
		
		
		vertical.setMargin(true);
		vertical.setSpacing(true);
		
		setContent(vertical);
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



	private Label addLabel(Layout layout, String str)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_H1);
		layout.addComponent(tf);
		return tf;
		
	}
	
	private Label addLabel2(Layout layout, String str)
	{
		Label tf = new Label(str,ContentMode.HTML);
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
		MesPermanencesDTO res = new PermanenceService().getMesDistributions(date);
		
		// Partie haute
		StringBuffer buf = new StringBuffer();
		buf.append("<br/>Vous devez faire les permanences suivantes :<ul>");
		for (PermanenceDTO distribution : res.permanencesFutures)
		{
			buf.append("<li>"+df.format(distribution.datePermanence)+"</li>");	
		}
		buf.append("</ul><br/>");
		mesDistributions.setValue(buf.toString());
		
		// Partie basse
		titre.setValue("<h1><center>Semaine du "+df.format(res.dateDebut)+" au "+df.format(res.dateFin)+"</center></h1>");
		
		buf = new StringBuffer();
		
		for (PermanenceDTO distribution : res.permanencesSemaine)
		{
			buf.append("<br/><br/><h2><center>"+df.format(distribution.datePermanence)+"</center></h2>");		
			for (PermanenceUtilisateurDTO utilisateur : distribution.permanenceUtilisateurs)
			{
				buf.append("<br/><center>"+utilisateur.nom+" "+utilisateur.prenom+"</center>");
			}
		}
		
		livraison.setValue(buf.toString());
		
		
	}
	
	


}
