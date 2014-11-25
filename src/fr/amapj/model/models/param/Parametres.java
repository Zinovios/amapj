package fr.amapj.model.models.param;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.amapj.model.engine.Identifiable;

/**
 * Paramètres généraux de l'application 
 * 
 *
 */
@Entity
public class Parametres implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Nom de l'AMAP
	 */
	@Size(min = 0, max = 100)
	private String nomAmap;
	
	/**
	 * Ville de l'AMAP
	 */
	@Size(min = 0, max = 200)
	private String villeAmap;
	
	/**
	 * Envoi de mail
	 */
	@Size(min = 0, max = 255)
	private String sendingMailUsername;
	
	@Size(min = 0, max = 255)
	private String sendingMailPassword;
	
	/**
	 * Url de l'application visible dans les mails
	 */
	@Size(min = 0, max = 255)
	private String url;
	

	/**
	 * Destinataire de la sauvegarde
	 */
	@Size(min = 0, max = 255)
	private String backupReceiver;

	
	// Partie gestion des permanences
	
	/**
	 * Activation ou désactivation du module "Planning de distribution"
	 */
	@NotNull
	@Enumerated(EnumType.STRING)
    private EtatModule etatPlanningDistribution;
	
	/**
	 * Envoi des mails pour le rappel de permanence
	 */
	@NotNull
	@Enumerated(EnumType.STRING)
    private ChoixOuiNon envoiMailRappelPermanence;
	
	
	private int delaiMailRappelPermanence;
	
	/**
	 * Titre du mail pour le rappel de permanence
	 */
	@Size(min = 0, max = 2048)
	private String titreMailRappelPermanence;
	
	/**
	 * Contenu du mail pour le rappel de permanence
	 */
	@Size(min = 0, max = 20480)
	private String contenuMailRappelPermanence;
	
	
	// Partie envoi des mails périodiques
	
	@NotNull
	@Enumerated(EnumType.STRING)
    private ChoixOuiNon envoiMailPeriodique;
	
	
	/**
	 * Numéro du jour dans le mois 
	 */
	private int numJourDansMois;
	
	/**
	 * Titre du mail pour le mail periodique
	 */
	@Size(min = 0, max = 2048)
	private String titreMailPeriodique;
	
	/**
	 * Contenu du mail pour le mail periodique
	 */
	@Size(min = 0, max = 20480)
	private String contenuMailPeriodique;
	
	
	
	public Long getId()
	{
		return id;
	}


	public void setId(Long id)
	{
		this.id = id;
	}


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
