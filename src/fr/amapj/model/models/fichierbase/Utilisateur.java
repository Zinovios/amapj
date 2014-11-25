package fr.amapj.model.models.fichierbase;

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

@Entity
@Table( uniqueConstraints=
{
   @UniqueConstraint(columnNames={"nom" , "prenom"}),
   @UniqueConstraint(columnNames={"email"})
})
public class Utilisateur  implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 1, max = 100)
	private String prenom;
	
	@NotNull
	@Size(min = 1, max = 100)
	private String nom;
	
	@Size(min = 0, max = 150)
	// Contient l'adresse e mail
	private String email;
	
	@Size(min = 0, max = 150)
	// Contient le password encrypté
	private String password;
	
	@Size(min = 0, max = 150)
	// Contient le salt permettant d'encrypter le password 
	private String salt;
	
	@Size(min = 0, max = 150)
	// Contient le slat calculé à la demande du reset du password 
	private String resetPasswordSalt;
	
	@Temporal(TemporalType.TIMESTAMP)
	// Contient le slat calculé à la demande du reset du password 
	private Date resetPasswordDate;
	
	
	@NotNull
	@Enumerated(EnumType.STRING)
	// Permet d'indiquer si cet utilisateur est actif ou inactif
    private EtatUtilisateur etatUtilisateur = EtatUtilisateur.ACTIF;
	
	
	// Liste des élements d'informations générales
	
	
	@Size(min = 0, max = 25)
	// numéro de  téléphone 1
	private String numTel1;
	
	@Size(min = 0, max = 25)
	// numéro de  téléphone 2
	private String numTel2;
	
	@Size(min = 0, max = 150)
	private String libAdr1;
	
	@Size(min = 0, max = 150)
	private String codePostal;

	@Size(min = 0, max = 150)
	private String ville;
	
	
	// TEMPORAIRE 
	@NotNull
	@Enumerated(EnumType.STRING)
	// Permet d'indiquer si cet utilisateur costise ou non  
    private EtatUtilisateur cotisation = EtatUtilisateur.ACTIF;


	
	public enum P implements Mdm
	{
		ID("id") , PRENOM("prenom") , NOM("nom") , EMAIL("email") , PASSWORD("password") , RESETPASSWORDSALT("resetPasswordSalt") ;
		
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

	public String getPrenom()
	{
		return prenom;
	}

	public void setPrenom(String prenom)
	{
		this.prenom = prenom;
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	

	public String getSalt()
	{
		return salt;
	}

	public void setSalt(String salt)
	{
		this.salt = salt;
	}

	public String getResetPasswordSalt()
	{
		return resetPasswordSalt;
	}

	public void setResetPasswordSalt(String resetPasswordSalt)
	{
		this.resetPasswordSalt = resetPasswordSalt;
	}

	public Date getResetPasswordDate()
	{
		return resetPasswordDate;
	}

	public void setResetPasswordDate(Date resetPasswordDate)
	{
		this.resetPasswordDate = resetPasswordDate;
	}

	public EtatUtilisateur getEtatUtilisateur()
	{
		return etatUtilisateur;
	}

	public void setEtatUtilisateur(EtatUtilisateur etatUtilisateur)
	{
		this.etatUtilisateur = etatUtilisateur;
	}

	public String getNumTel1()
	{
		return numTel1;
	}

	public void setNumTel1(String numTel1)
	{
		this.numTel1 = numTel1;
	}

	public String getNumTel2()
	{
		return numTel2;
	}

	public void setNumTel2(String numTel2)
	{
		this.numTel2 = numTel2;
	}

	public String getLibAdr1()
	{
		return libAdr1;
	}

	public void setLibAdr1(String libAdr1)
	{
		this.libAdr1 = libAdr1;
	}

	public String getCodePostal()
	{
		return codePostal;
	}

	public void setCodePostal(String codePostal)
	{
		this.codePostal = codePostal;
	}

	public String getVille()
	{
		return ville;
	}

	public void setVille(String ville)
	{
		this.ville = ville;
	}

	public EtatUtilisateur getCotisation()
	{
		return cotisation;
	}

	public void setCotisation(EtatUtilisateur cotisation)
	{
		this.cotisation = cotisation;
	}
	
	

}
