package fr.amapj.model.models.remise;

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
import fr.amapj.model.models.contrat.modele.ModeleContratDatePaiement;

@Entity
@Table( uniqueConstraints=
{
   @UniqueConstraint(columnNames={"datePaiement_id"})
})
public class RemiseProducteur  implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreation;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateRemise;
	
	@NotNull
	@ManyToOne
	private ModeleContratDatePaiement datePaiement;
	
	
	// Montant de la remise en centimes
	@NotNull
	private int montant=0;


	public Long getId()
	{
		return id;
	}


	public void setId(Long id)
	{
		this.id = id;
	}


	public Date getDateCreation()
	{
		return dateCreation;
	}


	public void setDateCreation(Date dateCreation)
	{
		this.dateCreation = dateCreation;
	}


	public Date getDateRemise()
	{
		return dateRemise;
	}


	public void setDateRemise(Date dateRemise)
	{
		this.dateRemise = dateRemise;
	}


	public ModeleContratDatePaiement getDatePaiement()
	{
		return datePaiement;
	}


	public void setDatePaiement(ModeleContratDatePaiement datePaiement)
	{
		this.datePaiement = datePaiement;
	}


	public int getMontant()
	{
		return montant;
	}


	public void setMontant(int montant)
	{
		this.montant = montant;
	}
	

	

	
}
