package fr.amapj.service.services.parametres;

import javax.persistence.EntityManager;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.param.Parametres;

/**
 * 
 * 
 */
public class ParametresService
{
	
	static private Long ID_PARAM = new Long(1);
	
	// PARTIE REQUETAGE 
	
	/**
	 * Permet de charger les paramètres
	 */
	@DbRead
	public ParametresDTO getParametres()
	{
		EntityManager em = TransactionHelper.getEm();
		
		Parametres param = em.find(Parametres.class, ID_PARAM);
		
		if (param==null)
		{
			throw new RuntimeException("Il faut insérer les paramètres généraux dans la base");
		}
		
		ParametresDTO dto = new ParametresDTO();
		dto.nomAmap = param.getNomAmap();
		dto.villeAmap = param.getVilleAmap();
		dto.sendingMailUsername = param.getSendingMailUsername();
		dto.sendingMailPassword = param.getSendingMailPassword();
		dto.url = param.getUrl();
		dto.backupReceiver = param.getBackupReceiver();
		
		dto.etatPlanningDistribution = param.getEtatPlanningDistribution();
		dto.delaiMailRappelPermanence = param.getDelaiMailRappelPermanence();
		dto.envoiMailRappelPermanence = param.getEnvoiMailRappelPermanence();
		dto.titreMailRappelPermanence = param.getTitreMailRappelPermanence();
		dto.contenuMailRappelPermanence = param.getContenuMailRappelPermanence();
		
		dto.envoiMailPeriodique = param.getEnvoiMailPeriodique();
		dto.numJourDansMois = param.getNumJourDansMois();
		dto.titreMailPeriodique = param.getTitreMailPeriodique();
		dto.contenuMailPeriodique = param.getContenuMailPeriodique();
		
		// Champs calculés
		dto.serviceMailActif = false;
		if ((param.getSendingMailUsername()!=null) && (param.getSendingMailUsername().length()>0))
		{
			dto.serviceMailActif = true;
		}
		
		return dto;
		
	}

	
	


	// PARTIE MISE A JOUR 

	
	@DbWrite
	public void update(final ParametresDTO dto)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Parametres param = em.find(Parametres.class, ID_PARAM);
		
		param.setNomAmap(dto.nomAmap);
		param.setVilleAmap(dto.villeAmap);
		param.setSendingMailUsername(dto.sendingMailUsername);
		param.setSendingMailPassword(dto.sendingMailPassword);
		param.setUrl(dto.url);
		param.setBackupReceiver(dto.backupReceiver);
		
		param.setEtatPlanningDistribution(dto.etatPlanningDistribution);
		param.setDelaiMailRappelPermanence(dto.delaiMailRappelPermanence);
		param.setEnvoiMailRappelPermanence(dto.envoiMailRappelPermanence);
		param.setTitreMailRappelPermanence(dto.titreMailRappelPermanence);
		param.setContenuMailRappelPermanence(dto.contenuMailRappelPermanence);
		
		param.setEnvoiMailPeriodique(dto.envoiMailPeriodique);
		param.setNumJourDansMois(dto.numJourDansMois);
		param.setTitreMailPeriodique(dto.titreMailPeriodique);
		param.setContenuMailPeriodique(dto.contenuMailPeriodique);
		
	}
}
