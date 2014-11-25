package fr.amapj.model.models.fichierbase;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;

@Entity
public class Produit  implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 1, max = 100)
	private String nom;
	
	@NotNull
	@Size(min = 1, max = 100)
	private String conditionnement;
	

	@NotNull
	@ManyToOne
	private Producteur producteur;
	
	@NotNull
	@Enumerated(EnumType.STRING)
    private TypFacturation typFacturation = TypFacturation.UNITE;
	
	
	
	public enum P implements Mdm
	{
		ID("id") , NOM("nom") , CONDITIONNEMENT("conditionnement") , PRODUCTEUR("producteur") , TYPFACTURATION("typFacturation")  ;
		
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

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public Producteur getProducteur()
	{
		return producteur;
	}

	public void setProducteur(Producteur producteur)
	{
		this.producteur = producteur;
	}

	public TypFacturation getTypFacturation()
	{
		return typFacturation;
	}

	public void setTypFacturation(TypFacturation typFacturation)
	{
		this.typFacturation = typFacturation;
	}

	public String getConditionnement()
	{
		return conditionnement;
	}

	public void setConditionnement(String conditionnement)
	{
		this.conditionnement = conditionnement;
	}
	
	
	
	
	
}
