package fr.amapj.model.models.fichierbase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;

/**
 * Liste des référents d'un producteur
 *
 */
@Entity
public class ProducteurReferent implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	
	
	@NotNull
	@ManyToOne
	private Producteur producteur;
	
	@NotNull
	@ManyToOne
	private Utilisateur referent;
	
	
	public enum P implements Mdm
	{ 
		ID("id") ,  REFERENT("referent") ,  PRODUCTEUR("producteur")  ;
	
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
	
	
	// Getters ans setters
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Producteur getProducteur()
	{
		return producteur;
	}

	public void setProducteur(Producteur producteur)
	{
		this.producteur = producteur;
	}

	public Utilisateur getReferent()
	{
		return referent;
	}

	public void setReferent(Utilisateur referent)
	{
		this.referent = referent;
	}
	
	
}
