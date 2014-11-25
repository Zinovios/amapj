package fr.amapj.model.models.stats;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.distribution.DatePermanenceUtilisateur;
import fr.amapj.model.models.fichierbase.Utilisateur;

/**
 * Suivi des notifications réalisées par e mail 
 * 
 */
@Entity
public class NotificationDone  implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	// Permet d'indiquer si cet utilisateur est actif ou inactif
    private TypNotificationDone typNotificationDone;
	
	@ManyToOne
	private ModeleContratDate modeleContratDate;
	
	@ManyToOne
	private DatePermanenceUtilisateur datePermanenceUtilisateur;
	
	@Temporal(TemporalType.DATE)
	private Date dateMailPeriodique;
	
	@NotNull
	@ManyToOne
	private Utilisateur utilisateur;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEnvoi;
	
	
	public enum P implements Mdm
	{
		ID("id");
		
		private String propertyId;   
		   
	    P(String propertyId) 
	    {
	        this.propertyId = propertyId;
	    }
	    public String prop() 
	    { 
	    	return propertyId; 
	    }
		
	}
	
	

	// Getters and setters

	
	


	public Long getId()
	{
		return id;
	}


	public void setId(Long id)
	{
		this.id = id;
	}


	public TypNotificationDone getTypNotificationDone()
	{
		return typNotificationDone;
	}


	public void setTypNotificationDone(TypNotificationDone typNotificationDone)
	{
		this.typNotificationDone = typNotificationDone;
	}


	public ModeleContratDate getModeleContratDate()
	{
		return modeleContratDate;
	}


	public void setModeleContratDate(ModeleContratDate modeleContratDate)
	{
		this.modeleContratDate = modeleContratDate;
	}


	public Utilisateur getUtilisateur()
	{
		return utilisateur;
	}


	public void setUtilisateur(Utilisateur utilisateur)
	{
		this.utilisateur = utilisateur;
	}


	public Date getDateEnvoi()
	{
		return dateEnvoi;
	}


	public void setDateEnvoi(Date dateEnvoi)
	{
		this.dateEnvoi = dateEnvoi;
	}


	public DatePermanenceUtilisateur getDatePermanenceUtilisateur()
	{
		return datePermanenceUtilisateur;
	}


	public void setDatePermanenceUtilisateur(DatePermanenceUtilisateur datePermanenceUtilisateur)
	{
		this.datePermanenceUtilisateur = datePermanenceUtilisateur;
	}


	public Date getDateMailPeriodique()
	{
		return dateMailPeriodique;
	}


	public void setDateMailPeriodique(Date dateMailPeriodique)
	{
		this.dateMailPeriodique = dateMailPeriodique;
	}


	
	
	
}
