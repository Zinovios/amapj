package fr.amapj.model.models.contrat.modele;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.fichierbase.Produit;

@Entity
public class ModeleContratProduit implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	private ModeleContrat modeleContrat;
	
	@NotNull
	@ManyToOne
	private Produit produit;
	
	@NotNull
	// Numéro d'ordre 
	private int indx;
	
	@NotNull
	// Prix en centimes d'euros, dans l'unité du produit
	private int prix;
	
	
	public enum P implements Mdm
	{
		ID("id") , MODELECONTRAT("modeleContrat") , PRODUIT("produit") , INDX("indx") ;
		
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


	public ModeleContrat getModeleContrat()
	{
		return modeleContrat;
	}


	public void setModeleContrat(ModeleContrat modeleContrat)
	{
		this.modeleContrat = modeleContrat;
	}


	public Produit getProduit()
	{
		return produit;
	}


	public void setProduit(Produit produit)
	{
		this.produit = produit;
	}


	public int getPrix()
	{
		return prix;
	}


	public void setPrix(int prix)
	{
		this.prix = prix;
	}


	public int getIndx()
	{
		return indx;
	}


	public void setIndx(int indx)
	{
		this.indx = indx;
	}
}
