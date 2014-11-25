package fr.amapj.model.models.contrat.modele;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;

@Entity
public class ModeleContratDate implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	private ModeleContrat modeleContrat;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dateLiv;
	
	public enum P implements Mdm
	{
		ID("id") ,  MODELECONTRAT("modeleContrat") , DATELIV("dateLiv") ;
		
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

	public Date getDateLiv()
	{
		return dateLiv;
	}

	public void setDateLiv(Date dateLiv)
	{
		this.dateLiv = dateLiv;
	}
	
}
