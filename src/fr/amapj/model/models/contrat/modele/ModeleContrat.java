package fr.amapj.model.models.contrat.modele;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.fichierbase.Producteur;

@Entity
@Table( uniqueConstraints=
{
   @UniqueConstraint(columnNames={"nom"})
})
public class ModeleContrat implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 1, max = 100)
	private String nom;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String description;
	

	@NotNull
	@ManyToOne
	private Producteur producteur;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	// Permet de savoir l'état du modele de contrat
    private EtatModeleContrat etat = EtatModeleContrat.CREATION;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dateFinInscription;
	
	
	// Paiement
	
	@NotNull
	@Enumerated(EnumType.STRING)
	// Permet de savoir l'état du modele de contrat
    private GestionPaiement gestionPaiement = GestionPaiement.NON_GERE;
	
	@Size(min = 0, max = 2048)
	// Texte qui sera affiché dans le cas ou il n'y a pas de paiement
	private String textPaiement;
	
	// Libellé du chéque 
	@Size(min = 0, max = 100)
	private String libCheque;
	
	// Date de remise des chéques
	@Temporal(TemporalType.DATE)
	private Date dateRemiseCheque;
	
	
	
	
	public enum P implements Mdm
	{
		ID("id") , NOM("nom") , DESCRIPTION("description") , PRODUCTEUR("producteur") , ETAT("etat") , DATEFININSCRIPTION("dateFinInscription"),
		LIBCHEQUE("libCheque") , DATEREMISECHEQUE("dateRemiseCheque");
		
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

	public EtatModeleContrat getEtat()
	{
		return etat;
	}

	public void setEtat(EtatModeleContrat etat)
	{
		this.etat = etat;
	}

	public Date getDateFinInscription()
	{
		return dateFinInscription;
	}

	public void setDateFinInscription(Date dateFinInscription)
	{
		this.dateFinInscription = dateFinInscription;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getLibCheque()
	{
		return libCheque;
	}

	public void setLibCheque(String libCheque)
	{
		this.libCheque = libCheque;
	}

	public Date getDateRemiseCheque()
	{
		return dateRemiseCheque;
	}

	public void setDateRemiseCheque(Date dateRemiseCheque)
	{
		this.dateRemiseCheque = dateRemiseCheque;
	}

	public GestionPaiement getGestionPaiement()
	{
		return gestionPaiement;
	}

	public void setGestionPaiement(GestionPaiement gestionPaiement)
	{
		this.gestionPaiement = gestionPaiement;
	}

	public String getTextPaiement()
	{
		return textPaiement;
	}

	public void setTextPaiement(String textPaiement)
	{
		this.textPaiement = textPaiement;
	}

	
	
	
	
	

}
