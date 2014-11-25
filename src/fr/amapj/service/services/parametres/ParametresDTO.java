package fr.amapj.service.services.parametres;

import fr.amapj.model.models.param.ChoixOuiNon;
import fr.amapj.model.models.param.EtatModule;


/**
 * Permet la gestion des utilisateurs en masse
 * ou du changement de son état
 * 
 */
public class ParametresDTO 
{
	
	public String nomAmap;
	
	public String villeAmap;
	
	public String sendingMailUsername;
	
	public String sendingMailPassword;
	
	public String url;
	
	public String backupReceiver;
	
	public EtatModule etatPlanningDistribution;
	
	public ChoixOuiNon envoiMailRappelPermanence;
	
	public int delaiMailRappelPermanence;
	
	public String titreMailRappelPermanence;
	
	public String contenuMailRappelPermanence;
	
	public ChoixOuiNon envoiMailPeriodique;
	
	public int numJourDansMois;
	
	public String titreMailPeriodique;
	
	public String contenuMailPeriodique;
	
	
	
	// Champs calculés
	public boolean serviceMailActif;
	

	public String getNomAmap()
	{
		return nomAmap;
	}

	public void setNomAmap(String nomAmap)
	{
		this.nomAmap = nomAmap;
	}

	public String getSendingMailUsername()
	{
		return sendingMailUsername;
	}

	public void setSendingMailUsername(String sendingMailUsername)
	{
		this.sendingMailUsername = sendingMailUsername;
	}

	public String getSendingMailPassword()
	{
		return sendingMailPassword;
	}

	public void setSendingMailPassword(String sendingMailPassword)
	{
		this.sendingMailPassword = sendingMailPassword;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getBackupReceiver()
	{
		return backupReceiver;
	}

	public void setBackupReceiver(String backupReceiver)
	{
		this.backupReceiver = backupReceiver;
	}

	public String getVilleAmap()
	{
		return villeAmap;
	}

	public void setVilleAmap(String villeAmap)
	{
		this.villeAmap = villeAmap;
	}

	public EtatModule getEtatPlanningDistribution()
	{
		return etatPlanningDistribution;
	}

	public void setEtatPlanningDistribution(EtatModule etatPlanningDistribution)
	{
		this.etatPlanningDistribution = etatPlanningDistribution;
	}

	public ChoixOuiNon getEnvoiMailRappelPermanence()
	{
		return envoiMailRappelPermanence;
	}

	public void setEnvoiMailRappelPermanence(ChoixOuiNon envoiMailRappelPermanence)
	{
		this.envoiMailRappelPermanence = envoiMailRappelPermanence;
	}

	public String getContenuMailRappelPermanence()
	{
		return contenuMailRappelPermanence;
	}

	public void setContenuMailRappelPermanence(String contenuMailRappelPermanence)
	{
		this.contenuMailRappelPermanence = contenuMailRappelPermanence;
	}

	public String getTitreMailRappelPermanence()
	{
		return titreMailRappelPermanence;
	}

	public void setTitreMailRappelPermanence(String titreMailRappelPermanence)
	{
		this.titreMailRappelPermanence = titreMailRappelPermanence;
	}

	public int getDelaiMailRappelPermanence()
	{
		return delaiMailRappelPermanence;
	}

	public void setDelaiMailRappelPermanence(int delaiMailRappelPermanence)
	{
		this.delaiMailRappelPermanence = delaiMailRappelPermanence;
	}

	public ChoixOuiNon getEnvoiMailPeriodique()
	{
		return envoiMailPeriodique;
	}

	public void setEnvoiMailPeriodique(ChoixOuiNon envoiMailPeriodique)
	{
		this.envoiMailPeriodique = envoiMailPeriodique;
	}

	public int getNumJourDansMois()
	{
		return numJourDansMois;
	}

	public void setNumJourDansMois(int numJourDansMois)
	{
		this.numJourDansMois = numJourDansMois;
	}

	public String getTitreMailPeriodique()
	{
		return titreMailPeriodique;
	}

	public void setTitreMailPeriodique(String titreMailPeriodique)
	{
		this.titreMailPeriodique = titreMailPeriodique;
	}

	public String getContenuMailPeriodique()
	{
		return contenuMailPeriodique;
	}

	public void setContenuMailPeriodique(String contenuMailPeriodique)
	{
		this.contenuMailPeriodique = contenuMailPeriodique;
	}

	
	
	
}
