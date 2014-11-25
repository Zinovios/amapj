package fr.amapj.model.models.contrat.reel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.contrat.modele.ModeleContratProduit;

@Entity
public class ContratCell  implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	private Contrat contrat;
	
	@NotNull
	@ManyToOne
	private ModeleContratProduit modeleContratProduit;
	
	@NotNull
	@ManyToOne
	private ModeleContratDate modeleContratDate;

	@NotNull
	private int qte;
	
	
	
	public enum P implements Mdm
	{
		ID("id") , CONTRAT("contrat") , MODELECONTRATPRODUIT("modeleContratProduit") , MODELECONTRATDATE("modeleContratDate") , QTE("qte") ;
		
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

	public Contrat getContrat()
	{
		return contrat;
	}

	public void setContrat(Contrat contrat)
	{
		this.contrat = contrat;
	}

	public ModeleContratProduit getModeleContratProduit()
	{
		return modeleContratProduit;
	}

	public void setModeleContratProduit(ModeleContratProduit modeleContratProduit)
	{
		this.modeleContratProduit = modeleContratProduit;
	}

	public ModeleContratDate getModeleContratDate()
	{
		return modeleContratDate;
	}

	public void setModeleContratDate(ModeleContratDate modeleContratDate)
	{
		this.modeleContratDate = modeleContratDate;
	}

	public int getQte()
	{
		return qte;
	}

	public void setQte(int qte)
	{
		this.qte = qte;
	}
		

	

	
	
}
