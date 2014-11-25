package fr.amapj.view.views.parametres;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.formpopup.FormPopup;


/**
 * Page permettant à l'administrateur de modifier les paramètres généraux
 *  
 *
 */
public class ParametresView extends VerticalLayout implements View, PopupListener
{

	ParametresDTO dto;
	
	TextField nomAmap;
	TextField villeAmap;
	TextField sendingMailUsername;
	TextField sendingMailPassword;
	TextField url;
	
	TextField backupReceiver;
	TextField modulePlanningDistribution;

	

	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{

		GridLayout layout = new GridLayout(3, 8);
		layout.setWidth("80%");
		layout.setColumnExpandRatio(0, 0);
		
		
		// Une ligne vide
		addLabel(layout," ");
		addLabel(layout," ");
		addLabel(layout," ");	
		
		// 
		nomAmap = addLine(layout,"Nom de l'AMAP:");
		villeAmap = addLine(layout,"Ville de l'AMAP:");
		sendingMailUsername = addLine(layout,"Adresse mail qui enverra les messages");
		sendingMailPassword = addLine(layout,"Password de l'adresse mail qui enverra les messages");
		url = addLine(layout,"URL de l'application vue par les utilisateurs");

		backupReceiver = addLine(layout,"Adresse mail du destinataire des sauvegardes quotidiennes");
		
		modulePlanningDistribution = addLine(layout, "Activation du module Planning de distribution");
		
		addLabel(layout, "...");
		addLabel(layout," ");
		addLabel(layout," ");	
		
		
		addButton(layout, "Changer les paramètres",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleChangerParam();
			}
		});
		
		
		
		refresh();
		
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();
		addComponent(layout);
		
		
		
	}



	private TextField addLine(GridLayout layout, String label)
	{
		addLabel(layout,label);
		TextField tf = addTextField(layout);
		addLabel(layout," ");
		return tf;
	}



	
	private void handleChangerParam()
	{
		FormPopup.open(new PopupSaisieParametres(dto),this);
		
	}
	

	
	
	private Label addLabel(GridLayout layout, String str)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_BIG);
		layout.addComponent(tf);
		return tf;
		
	}

	private TextField addTextField(GridLayout layout)
	{
		TextField tf = new TextField();
		tf.setWidth("100%");
		tf.setNullRepresentation("");
		tf.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
		tf.setEnabled(false);
		layout.addComponent(tf);
		return tf;
		
	}
	
	private void addButton(GridLayout layout, String str,ClickListener listener)
	{
		Button b = new Button(str);
		b.addStyleName(ChameleonTheme.BUTTON_BIG);
		b.addClickListener(listener);
		layout.addComponent(b);
		
	}

	@Override
	public void onPopupClose()
	{
		refresh();
	}

	private void refresh()
	{
		dto = new ParametresService().getParametres();
		
		nomAmap.setValue(dto.nomAmap);
		villeAmap.setValue(dto.villeAmap);
		sendingMailUsername.setValue(dto.sendingMailUsername);
		sendingMailPassword.setValue("**********");
		url.setValue(dto.url);
		backupReceiver.setValue(dto.backupReceiver);
		modulePlanningDistribution.setValue(dto.etatPlanningDistribution.toString());
					
	}

}
