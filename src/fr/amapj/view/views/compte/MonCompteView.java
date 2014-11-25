package fr.amapj.view.views.compte;

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

import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.moncompte.MonCompteService;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.formpopup.FormPopup;


/**
 * Page permettant à l'utilisateur de gérer son compte :
 * -> changement de l'adresse e mail 
 * -> changement du mot de passe 
 * -> changement des coordonnées
 *  
 *
 */
public class MonCompteView extends VerticalLayout implements View, PopupListener
{

	UtilisateurDTO u;
	
	TextField nom;
	TextField prenom;
	TextField mail;
	TextField pwd;
	
	TextField numTel1;
	TextField numTel2;
	TextField adresse;
	TextField codePostal;
	TextField ville;	
	

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
		
		// le nom 
		addLabel(layout,"Votre nom: ");
		nom = addTextField(layout);
		addLabel(layout," ");
		
		// le prenom 
		addLabel(layout,"Votre prénom: ");
		prenom = addTextField(layout);
		addLabel(layout," ");
		
		// le mail 
		addLabel(layout,"Votre mail: ");
		mail = addTextField(layout);
		addButton(layout, "Changer votre adresse e-mail",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleChangerEmail();
			}
		});
		
		// le mot de passe
		addLabel(layout,"Votre mot de passe: ");
		pwd = addTextField(layout);
		addButton(layout, "Changer votre mot de passe",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleChangerPassword();
			}
		});
		
		// les coordonnées
		numTel1 = addLine(layout,"Numéro de tel 1:");
		numTel2 = addLine(layout,"Numéro de tel 2:");
		adresse = addLine(layout,"Adresse:");
		codePostal = addLine(layout,"Code Postal:");

		addLabel(layout,"Ville");
		ville = addTextField(layout);
		addButton(layout, "Changer vos coordonnées",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleChangerCoordonnees();
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



	protected void handleChangerPassword()
	{
		FormPopup.open(new PopupSaisiePassword(u));
	}
	
	private void handleChangerCoordonnees()
	{
		FormPopup.open(new PopupSaisieCoordonnees(u),this);
		
	}
	

	protected void handleChangerEmail()
	{
		FormPopup.open(new PopupSaisieEmail(u),this);
		
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
		u = new MonCompteService().getUtilisateurInfo();
		
		nom.setValue(u.getNom());
		prenom.setValue(u.getPrenom());
		mail.setValue(u.getEmail());
		pwd.setValue("***********");
		numTel1.setValue(u.getNumTel1());
		numTel2.setValue(u.getNumTel2());
		adresse.setValue(u.getLibAdr1());
		codePostal.setValue(u.getCodePostal());
		ville.setValue(u.getVille());			
	}
	
	


}
