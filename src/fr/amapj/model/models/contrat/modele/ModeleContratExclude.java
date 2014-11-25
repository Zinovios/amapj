package fr.amapj.model.models.contrat.modele;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;


/**
 * Cette classe permet de mémoriser les elements qui sont 
 * exclus de ce contrat.
 * Il est possible d'exclure une date complète, ou bien un produit à une date donnée
 *
 */
@Entity
public class ModeleContratExclude implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	private ModeleContrat modeleContrat;
	
	@ManyToOne
	private ModeleContratProduit produit;
	
	@NotNull
	@ManyToOne
	private ModeleContratDate date;
	
	
	
	public enum P implements Mdm
	{
		ID("id") , MODELECONTRAT("modeleContrat") , PRODUIT("produit") , DATE("date") ;
		
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



	public ModeleContratProduit getProduit()
	{
		return produit;
	}



	public void setProduit(ModeleContratProduit produit)
	{
		this.produit = produit;
	}



	public ModeleContratDate getDate()
	{
		return date;
	}



	public void setDate(ModeleContratDate date)
	{
		this.date = date;
	} ;
}
