package fr.amapj.service.services.utilisateur;

import java.util.List;

import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.view.engine.menu.RoleList;
import fr.amapj.view.engine.tools.TableItem;

/**
 * Permet la gestion des utilisateurs en masse
 * ou du changement de son état
 * 
 */
public class UtilisateurDTO implements TableItem
{
	public Long id;

	public String prenom;
	
	public String nom;
	
	public String roles;
	
	public String email;
	
	public EtatUtilisateur etatUtilisateur;
	
	public String numTel1;
	
	public String numTel2;
	
	public String libAdr1;
	
	public String codePostal;

	public String ville;

	public EtatUtilisateur cotisation;

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

	public String getRoles()
	{
		return roles;
	}

	public void setRoles(String roles)
	{
		this.roles = roles;
	}
	
	
	
	
}
