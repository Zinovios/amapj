package fr.amapj.view.views.mescontrats;

import java.text.SimpleDateFormat;
import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.excelgenerator.EGContratUtilisateur;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.MesContratsDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.excelgenerator.TelechargerPopup;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.views.gestioncontratsignes.GestionContratSignesListPart.Status;
import fr.amapj.view.views.saisiecontrat.SaisieContrat;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.ModeSaisie;


/**
 * Page permettant à l'utilisateur de gérer ses contrats
 * 
 *  
 *
 */
public class MesContratsView extends Panel implements View, PopupSuppressionListener
{
	
	SimpleDateFormat df = new SimpleDateFormat("EEEEE dd MMMMM yyyy");
	GridLayout layout = null;
	private MesContratsDTO mesContratsDTO;

	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{
		setSizeFull();
		refresh();
	}

	
	/**
	 * Ajoute un label sur toute la largeur à la ligne indiquée
	 */
	private Label addLabel(GridLayout layout, String str,int row1)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_H1);
		layout.addComponent(tf, 0, row1, 3, row1);
		return tf;
		
	}

	
	
	private Button addButtonInscription(String str,final ContratDTO c)
	{
		Button b = new Button(str);
		b.addStyleName(ChameleonTheme.BUTTON_BIG);
		b.addClickListener(new ClickListener()
		{
			
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleInscription(c,ModeSaisie.STANDARD);
			}
		});
		return b;
	}
	
	private Button addButtonVoir(String str,final ContratDTO c)
	{
		Button b = new Button(str);
		b.addStyleName(ChameleonTheme.BUTTON_BIG);
		b.addClickListener(new ClickListener()
		{
			
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleInscription(c,ModeSaisie.READ_ONLY);
			}
		});
		b.setWidth("100%");
		return b;
	}
	
	



	private Button addButtonSupprimer(String str,final ContratDTO c)
	{
		Button b = new Button(str);
		b.addStyleName(ChameleonTheme.BUTTON_BIG);
		b.addClickListener(new ClickListener()
		{
			
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSupprimer(c);
			}
		});
		return b;
	}

	protected void handleSupprimer(final ContratDTO c)
	{
		String text = "Etes vous sûr de vouloir supprimer le contrat de "+c.nom+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,c.contratId);
		SuppressionPopup.open(confirmPopup, this);		
	}
	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new MesContratsService().deleteContrat(idItemToSuppress);
	}
	

	protected void handleInscription(ContratDTO c,ModeSaisie modeSaisie)
	{
		// Dans le cas de l'inscription à un nouveau contrat
		ContratDTO contratDTO = new MesContratsService().loadContrat(c.modeleContratId,c.contratId);
		SaisieContrat.saisieContrat(contratDTO,SessionManager.getUserId(),null,modeSaisie,this);
				
	}



	private void refresh()
	{
		mesContratsDTO = new MesContratsService().getMesContrats(SessionManager.getUserId());
		
		int nbRow = mesContratsDTO.getNewContrats().size()+mesContratsDTO.getExistingContrats().size()+3;
		int index = 0;
		
		layout = new GridLayout(4, nbRow);
		
		// Le titre
		addLabel(layout,"Les nouveaux contrats disponibles",index);
		index++;
		
		// la liste des nouveaux contrats 
		List<ContratDTO> newContrats = mesContratsDTO.getNewContrats();
		for (ContratDTO c : newContrats)
		{
			
			String str = formatLibelleContrat(c,true);
			
			layout.addComponent(new Label(str, ContentMode.HTML),0,index);
			Button b = addButtonInscription("S'inscrire",c);
			layout.addComponent(b,1,index);
			layout.setComponentAlignment(b, Alignment.MIDDLE_CENTER);
			
			index++;
		}
		
		
		// Le titre
		addLabel(layout,"Mes contrats existants",index);
		index++;
		
		// la liste des contrats existants 
		List<ContratDTO> existingContrats = mesContratsDTO.getExistingContrats();
		for (ContratDTO c : existingContrats)
		{
			String str = formatLibelleContrat(c,false);
			
			layout.addComponent(new Label(str, ContentMode.HTML),0,index);
			
			Button v = addButtonVoir("Voir",c);
			layout.addComponent(v,1,index);
			layout.setComponentAlignment(v, Alignment.MIDDLE_CENTER);
			
			if (c.isModifiable())
			{
				Button b = addButtonInscription("Modifier",c);
				layout.addComponent(b,2,index);
				layout.setComponentAlignment(b, Alignment.MIDDLE_CENTER);
				
				b = addButtonSupprimer("Supprimer",c);
				layout.addComponent(b,3,index);
				layout.setComponentAlignment(b, Alignment.MIDDLE_CENTER);
			}
			index++;
		}
		
		// Le bouton pour télécharger les contrats
		if (existingContrats.size()>0)
		{
			Button telechargerButton = new Button("Télécharger mes contrats pour les imprimer ...");
			telechargerButton.addClickListener(new Button.ClickListener()
			{
	
				@Override
				public void buttonClick(ClickEvent event)
				{
					handleTelecharger();
				}
			});		
			telechargerButton.setWidth("100%");
			layout.addComponent(telechargerButton, 0, index, 0, index);
			index++;
		}
		
		
		layout.setMargin(true);
		layout.setSpacing(true);
		
		setContent(layout);
		addStyleName(ChameleonTheme.PANEL_BORDERLESS);
	}
	
	
	private void handleTelecharger()
	{
		TelechargerPopup popup = new TelechargerPopup();
		List<ContratDTO> existingContrats = mesContratsDTO.getExistingContrats();
		for (ContratDTO c : existingContrats)
		{
			popup.addGenerator(new EGContratUtilisateur(c.contratId));
		}
		CorePopup.open(popup,this);
		
	}


	private String formatLibelleContrat(ContratDTO c,boolean isInscription)
	{
		// Ligne 0
		String str = "<h4>"+c.nom+"</h4>";
		
		// Ligne 1
		str = str+c.description;
		str=str+"<br/>";
		
		// Ligne 2 - Les dates de livraisons
		if (c.nbLivraison==1)
		{
			str = str+"Une seule livraison le "+df.format(c.dateDebut);
		}
		else
		{
			str = str+c.nbLivraison+" livraisons à partir du "+df.format(c.dateDebut)+" jusqu'au "+df.format(c.dateFin);
		}
		str=str+"<br/>";
		
		// Ligne : modifiable ou non 
		if (c.isModifiable())
		{
			if (isInscription)
			{
				str = str+"Vous pouvez vous inscrire et modifier ce contrat jusqu'au "+df.format(c.dateFinInscription)+ " minuit.";
			}
			else
			{
				str = str+"Ce contrat est modifiable jusqu'au "+df.format(c.dateFinInscription)+ " minuit.";
			}
		}
		else
		{
			str = str+"Ce contrat n'est plus modifiable.";
		}
		
		str=str+"<br/>";
		str=str+"<br/>";
		
		return str;
	}



	@Override
	public void onPopupClose()
	{
		refresh();
		
	}


}
