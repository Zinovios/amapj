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
import javax.validation.constraints.Size;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.fichierbase.Utilisateur;

/**
 * Suivi des accès à l'application
 * 
 */
@Entity
public class LogAccess  implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String ip;
	
	private String browser;
	
	@NotNull
	@ManyToOne
	private Utilisateur utilisateur;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateIn;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOut;

	
	
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



	public String getIp()
	{
		return ip;
	}



	public void setIp(String ip)
	{
		this.ip = ip;
	}



	public String getBrowser()
	{
		return browser;
	}



	public void setBrowser(String browser)
	{
		this.browser = browser;
	}



	public Utilisateur getUtilisateur()
	{
		return utilisateur;
	}



	public void setUtilisateur(Utilisateur utilisateur)
	{
		this.utilisateur = utilisateur;
	}



	public Date getDateIn()
	{
		return dateIn;
	}



	public void setDateIn(Date dateIn)
	{
		this.dateIn = dateIn;
	}



	public Date getDateOut()
	{
		return dateOut;
	}



	public void setDateOut(Date dateOut)
	{
		this.dateOut = dateOut;
	} ;
	
	
	
	
	
	
	
}
