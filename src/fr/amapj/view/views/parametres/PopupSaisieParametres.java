package fr.amapj.view.views.parametres;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.RichTextArea;

import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Permet à un utilisateur de mettre à jour ses coordonnées
 * 
 *
 */
@SuppressWarnings("serial")
public class PopupSaisieParametres extends WizardFormPopup
{

	private ParametresDTO dto;

	public enum Step
	{
		AMAP_INFO , MAIL_INFO , PERMANENCE_INFO , MAIL_PERIODIQUE;
	}

	/**
	 * 
	 */
	public PopupSaisieParametres(ParametresDTO dto)
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Modification des paramètres";

		this.dto = dto;
		item = new BeanItem<ParametresDTO>(dto);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case AMAP_INFO:
			addFieldAmapInfo();
			break;

		case MAIL_INFO:
			addFieldMailInfo();
			break;
			
		case PERMANENCE_INFO:
			addFieldPermanenceInfo();
			break;
			
		case MAIL_PERIODIQUE:
			addFieldMailPeriodique();
			break;
		
		}
	}
	
	private void addFieldAmapInfo()
	{
		// Titre
		setStepTitle("identification de l'AMAP");
		
		// 
		addTextField("Nom de l'AMAP", "nomAmap");
		
		addTextField("Ville de l'AMAP", "villeAmap");
		
	}
	
	private void addFieldMailInfo()
	{
		// Titre
		setStepTitle("information sur l'envoi des mails");
		
		// Champ 5
		addTextField("Adresse mail qui enverra les messages", "sendingMailUsername");

		// Champ 6
		addPasswordTextField("Password de l'adresse mail qui enverra les messages", "sendingMailPassword");
		
		// Champ 7
		addTextField("URL de l'application vue par les utilisateurs", "url");
		
		// Champ 8
		addTextField("Adresse mail du destinataire des sauvegardes quotidiennes", "backupReceiver");
		
	}
	
	private void addFieldPermanenceInfo()
	{
		// Titre
		setStepTitle("module \"Gestion des permanences\"");
				
		// Champ 9
		addComboEnumField("Activation du module \"Gestion des permanences\"", dto.etatPlanningDistribution, "etatPlanningDistribution");
		
		addComboEnumField("Envoi d'un mail de rappel pour les permanences", dto.envoiMailRappelPermanence, "envoiMailRappelPermanence");
		
		addIntegerField("Délai en jours entre la permanence et l'envoi du mail", "delaiMailRappelPermanence");
		
		addTextField("Titre du mail de rappel pour les permanences", "titreMailRappelPermanence");
		
		RichTextArea f =  addRichTextAeraField("Contenu du mail de rappel pour les permanences", "contenuMailRappelPermanence");
		f.setHeight(8, Unit.CM);

	}
	
	private void addFieldMailPeriodique()
	{
		// Titre
		setStepTitle("Envoi d'un mail périodique (tous les mois)");
				
		// Champ 9
		addComboEnumField("Activation de l'envoi d'un mail périodique (tous les mois)", dto.envoiMailPeriodique, "envoiMailPeriodique");
		
		addIntegerField("Numéro du jour dans le mois où le mail sera envoyé", "numJourDansMois");
		
		addTextField("Titre du mail périodique", "titreMailPeriodique");
		
		RichTextArea f =  addRichTextAeraField("Contenu du mail périodique", "contenuMailPeriodique");
		f.setHeight(8, Unit.CM);

	}

	

	@Override
	protected void performSauvegarder()
	{
		new ParametresService().update(dto);
	}


	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
	
}
