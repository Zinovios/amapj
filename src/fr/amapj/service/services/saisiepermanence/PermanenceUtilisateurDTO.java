package fr.amapj.service.services.saisiepermanence;


/**
 * 
 * 
 */
public class PermanenceUtilisateurDTO 
{
	public Long idUtilisateur;
	
	public String nom;
	
	public String prenom;
	
	public int numSession;

	public Long getIdUtilisateur()
	{
		return idUtilisateur;
	}

	public void setIdUtilisateur(Long idUtilisateur)
	{
		this.idUtilisateur = idUtilisateur;
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public String getPrenom()
	{
		return prenom;
	}

	public void setPrenom(String prenom)
	{
		this.prenom = prenom;
	}

	public int getNumSession()
	{
		return numSession;
	}

	public void setNumSession(int numSession)
	{
		this.numSession = numSession;
	}

	

	
	
	
}
