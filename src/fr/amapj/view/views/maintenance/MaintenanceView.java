package fr.amapj.view.views.maintenance;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;

import sun.misc.Resource;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.backupdb.BackupDatabaseService;
import fr.amapj.service.services.maintenance.MaintenanceService;
import fr.amapj.view.engine.popup.corepopup.CorePopup;

public class MaintenanceView extends VerticalLayout implements View
{

	private final static Logger logger = Logger.getLogger(MaintenanceView.class.getName());

	@Override
	public void enter(ViewChangeEvent event)
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
		VerticalLayout layout = new VerticalLayout();

		addLabelH1(layout, "Maintenance du système");
		addLabel(layout, "");
		addLabel(layout, "Date et heure courante "+df.format(new Date()));
		addLabel(layout, "");
		
		addLabel(layout, "");
		addLabel(layout, "Version de l'application "+getVersion());
		addLabel(layout, "");
		
		
		
		
		Button b1 = new Button("Backup de la base et envoi par mail", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				new BackupDatabaseService().backupDatabase();
			}
		});
		
		addLabel(layout, "");
		
		Button b2 = new Button("Remise à zéro du cache (obligatoire après requete SQL)", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				new MaintenanceService().resetDatabaseCache();
			}
		});
		
		
		
		Button b3 = new Button("Suppression complète d'un contrat vierge et des contrats associés", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				PopupSuppressionTotaleContrat popup = new PopupSuppressionTotaleContrat();
				CorePopup.open(popup);
			}
		});
		
		
		Button b4 = new Button("Positionner les dates pour la base démo", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				PopupDateDemo popup = new PopupDateDemo();
				CorePopup.open(popup);
			}
		});
		
		Button b5 = new Button("Décalage dans le temps d'un contrat vierge et des contrats associés", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				PopupDecalageTemporelContrat popup = new PopupDecalageTemporelContrat();
				CorePopup.open(popup);
			}
		});
		
		
		
		
		
				
		
		layout.addComponent(b1);
		layout.addComponent(b2);
		layout.addComponent(b3);
		layout.addComponent(b4);
		layout.addComponent(b5);
		
		addComponent(layout);
		setSizeFull();
	}
	
	/**
	 * Lit lenuméro de la version
	 * @return
	 */
	private String getVersion()
	{
		try
		{
			InputStream in = this.getClass().getResourceAsStream("/amapj_version.txt");
			byte[] bs = IOUtils.toByteArray(in);
			return new String(bs);
		} 
		catch (IOException e)
		{
			return "error";
		}
	}

	private Label addLabelH1(VerticalLayout layout, String str)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_H1);
		layout.addComponent(tf);
		return tf;
		
	}
	
	private Label addLabel(VerticalLayout layout, String str)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_BIG);
		layout.addComponent(tf);
		return tf;
	}
	
	
	
}
