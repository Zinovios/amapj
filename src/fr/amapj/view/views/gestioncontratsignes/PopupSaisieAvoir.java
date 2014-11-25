package fr.amapj.view.views.gestioncontratsignes;

import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.services.gestioncontratsigne.ContratSigneDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.view.engine.popup.okcancelpopup.OKCancelPopup;
import fr.amapj.view.engine.tools.BaseUiTools;

/**
 * Popup pour la saisie des avoirs d'un utilisateur
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisieAvoir extends OKCancelPopup
{
	
	private TextField textField;
	
	private ContratSigneDTO contratSigneDTO;
	
	/**
	 * 
	 */
	public PopupSaisieAvoir(ContratSigneDTO contratSigneDTO)
	{
		this.contratSigneDTO = contratSigneDTO;
		popupTitle = "Saisie de l'avoir";
	}
	
	
	@Override
	protected void createContent(VerticalLayout contentLayout)
	{
		FormLayout f = new FormLayout();
		
		
		textField = BaseUiTools.createCurrencyField("Montant de l'avoir",false);
		
		textField.setConvertedValue(new Integer(contratSigneDTO.mntAvoirInitial));
		

		textField.addStyleName("align-center");
		textField.addStyleName("big");
		
		
		String message = "<h3> Saisie d'un avoir  pour "+contratSigneDTO.prenomUtilisateur+" "+contratSigneDTO.nomUtilisateur+"</h3>";
		
		f.addComponent(new Label(message, ContentMode.HTML));
		f.addComponent(textField);
		contentLayout.addComponent(f);
		
		
		
	}

	protected boolean performSauvegarder()
	{
		int qte = 0;
		try
		{
			Integer val = (Integer) textField.getConvertedValue();
			
			if (val != null)
			{
				qte = val.intValue();
			}
		}
		catch (ConversionException e)
		{
			Notification.show("Erreur de saisie");
			return false;
		}
				
		new MesContratsService().saveAvoirInitial(contratSigneDTO.idContrat,qte);
		return true;
	}

	
}
