package fr.amapj.model.models.contrat.reel;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.fichierbase.Utilisateur;

@Entity
@Table( uniqueConstraints=
{
   @UniqueConstraint(columnNames={"modeleContrat_id" , "utilisateur_id"})
})
public class Contrat  implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	private ModeleContrat modeleContrat;
	
	@NotNull
	@ManyToOne
	private Utilisateur utilisateur;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreation;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateModification;
	
	// Montant de l'avoir en centimes
	@NotNull
	private int montantAvoir=0;
	
	
	
	public enum P implements Mdm
	{
		ID("id") , MODELECONTRAT("modeleContrat") , UTILISATEUR("utilisateur") , DATECREATION("dateCreation") , DATEMODIFICATION("dateModification") ;
		
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

	public Utilisateur getUtilisateur()
	{
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur)
	{
		this.utilisateur = utilisateur;
	}

	public Date getDateCreation()
	{
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation)
	{
		this.dateCreation = dateCreation;
	}

	public Date getDateModification()
	{
		return dateModification;
	}

	public void setDateModification(Date dateModification)
	{
		this.dateModification = dateModification;
	}

	public int getMontantAvoir()
	{
		return montantAvoir;
	}

	public void setMontantAvoir(int montantAvoir)
	{
		this.montantAvoir = montantAvoir;
	}

	
}
