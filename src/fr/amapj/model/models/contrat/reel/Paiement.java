package fr.amapj.model.models.contrat.reel;

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
import fr.amapj.model.models.contrat.modele.ModeleContratDatePaiement;
import fr.amapj.model.models.remise.RemiseProducteur;

/**
 * Correspond à un chéque
 *
 */
@Entity
public class Paiement  implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	private Contrat contrat;
	
	@NotNull
	@ManyToOne
	private ModeleContratDatePaiement modeleContratDatePaiement;

	// Montant du paiement en centimes
	@NotNull
	private int montant;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	// Permet de savoir l'état du modele de contrat
    private EtatPaiement etat = EtatPaiement.A_FOURNIR;
	
	@ManyToOne
	private RemiseProducteur remise;
	
	
	public enum P implements Mdm
	{
		ID("id") , CONTRAT("contrat") ,  MODELECONTRATDATEPAIEMENT("modeleContratDatePaiement") , MONTANT("montant") ;
		
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
	

	public Paiement()
	{
		
	}
	

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

	public ModeleContratDatePaiement getModeleContratDatePaiement()
	{
		return modeleContratDatePaiement;
	}

	public void setModeleContratDatePaiement(ModeleContratDatePaiement modeleContratDatePaiement)
	{
		this.modeleContratDatePaiement = modeleContratDatePaiement;
	}

	public int getMontant()
	{
		return montant;
	}

	public void setMontant(int montant)
	{
		this.montant = montant;
	}

	public EtatPaiement getEtat()
	{
		return etat;
	}

	public void setEtat(EtatPaiement etat)
	{
		this.etat = etat;
	}

	public RemiseProducteur getRemise()
	{
		return remise;
	}

	public void setRemise(RemiseProducteur remise)
	{
		this.remise = remise;
	}
	
}
