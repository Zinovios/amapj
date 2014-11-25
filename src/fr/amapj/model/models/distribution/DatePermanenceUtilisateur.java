package fr.amapj.model.models.distribution;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.fichierbase.Utilisateur;

@Entity
public class DatePermanenceUtilisateur implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	private DatePermanence datePermanence;
	
	@NotNull
	@ManyToOne
	private Utilisateur utilisateur;
	
	// Numéro de la session
	private int numSession=0;
	
	
	public enum P implements Mdm
	{
		ID("id") ;
		
		private String propertyId;   
		   
	    P(String propertyId) 
	    {
	        this.propertyId = propertyId;
	    }
	    public String prop() 
	    { 
	    	return propertyId; 
	    }
		
	} ;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public DatePermanence getDatePermanence()
	{
		return datePermanence;
	}

	public void setDatePermanence(DatePermanence datePermanence)
	{
		this.datePermanence = datePermanence;
	}

	public Utilisateur getUtilisateur()
	{
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur)
	{
		this.utilisateur = utilisateur;
	}

	public int getNumSession()
	{
		return numSession;
	}

	public void setNumSession(int numSession)
	{
		this.numSession = numSession;
	}

	
	
}
