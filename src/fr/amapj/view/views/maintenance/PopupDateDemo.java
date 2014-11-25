package fr.amapj.view.views.maintenance;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;

import fr.amapj.common.StringUtils;
import fr.amapj.service.services.gestioncontrat.DemoDateDTO;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Permet uniquement de creer des contrats
 * 
 *
 */
@SuppressWarnings("serial")
public class PopupDateDemo extends WizardFormPopup
{

	private DemoDateDTO demoDateDTO;



	public enum Step
	{
		INFO, SAISIE , AFFICHAGE;
	}

	/**
	 * 
	 */
	public PopupDateDemo()
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Mise à jour de la base de démo";

		demoDateDTO = new DemoDateDTO();
		item = new BeanItem<DemoDateDTO>(demoDateDTO);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case INFO:
			addFieldInfo();
			break;
			
		case SAISIE:
			addFieldSaisie();
			break;
			
		case AFFICHAGE:
			addFieldAffichage();
			break;

		default:
			break;
		}
	}

	private void addFieldInfo()
	{
		// Titre
		setStepTitle("les informations générales");
		
		//
		addLabel("Cet outil permet de positionner toutes les dates de tous les contrats et les mots de passe pour préparer facilement une base de démo", ContentMode.HTML);

	}

	private void addFieldSaisie()
	{
		// Titre
		setStepTitle("la saisie des dates");
		
		addDateField("Date de fin des inscriptions", "dateFinInscription");	
		
		addDateField("Date de remise du chèque", "dateRemiseCheque");
		
		addDateField("Date de la première livraison", "dateDebut");
		
		addDateField("Date de la dernière livraison", "dateFin");
		
		addDateField("Date du premier paiement", "premierCheque");
				
		addDateField("Date du dernier paiement", "dernierCheque");
		
		addTextField("Mot de passe", "password");
		 
	}
	
	private void addFieldAffichage()
	{
		// Titre
		setStepTitle("l'url d'aide à la visite");
		
		String str = "L'url est la suivante<br/>"+
					 "http://amapj.fr/docs_utilisateur_visite_guidee.html?"+
					  "d1="+getDate(demoDateDTO.dateFinInscription)+"&"+
					  "d2="+getDate(demoDateDTO.dateRemiseCheque)+"&"+
					  "d3="+getDate(demoDateDTO.dateDebut)+"&"+
					  "d4="+getDate(demoDateDTO.dateFin)+"&"+
					  "pass="+demoDateDTO.password;
					 
		//
		addLabel(str, ContentMode.HTML);

	}

	

	private String getDate(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat("EEEEE dd MMMMM yyyy");	
		String str = df.format(date);
		str = str.replaceAll(" ", "_");
		return StringUtils.sansAccent(str);
	}

	@Override
	protected void performSauvegarder()
	{
		new GestionContratService().updateForDemo(demoDateDTO);
		
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
