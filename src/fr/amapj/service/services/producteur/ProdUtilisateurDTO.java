package fr.amapj.service.services.producteur;

import fr.amapj.service.services.utilisateur.util.IUtilisateur;


/**
 * 
 * 
 */
public class ProdUtilisateurDTO implements IUtilisateur
{
	public Long idUtilisateur;
	
	public String nom;
	
	public String prenom;
	
	public boolean etatNotification;

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

	public boolean isEtatNotification()
	{
		return etatNotification;
	}

	public void setEtatNotification(boolean etatNotification)
	{
		this.etatNotification = etatNotification;
	}

	
	

	
	
	
}
