package fr.amapj.model.models.fichierbase;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;

/**
 * Liste des utilisateurs d'un producteur
 *
 */
@Entity
public class ProducteurUtilisateur implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	
	
	@NotNull
	@ManyToOne
	private Producteur producteur;
	
	@NotNull
	@ManyToOne
	private Utilisateur utilisateur;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	// Permet d'indiquer si cet utilisateur souhaite être notifié
    private EtatNotification notification;
	
	
	public enum P implements Mdm
	{ 
		ID("id") ,  UTILISATEUR("utilisateur") ,  PRODUCTEUR("producteur")  ;
	
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

	public Utilisateur getUtilisateur()
	{
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur)
	{
		this.utilisateur = utilisateur;
	}

	public EtatNotification getNotification()
	{
		return notification;
	}

	public void setNotification(EtatNotification notification)
	{
		this.notification = notification;
	}


	
	
}
