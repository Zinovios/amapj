package fr.amapj.service.services.producteur;

import java.util.ArrayList;
import java.util.List;

import fr.amapj.view.engine.tools.TableItem;

/**
 * Permet la gestion des utilisateurs en masse
 * ou du changement de son état
 * 
 */
public class ProducteurDTO implements TableItem
{
	public Long id;
	
	public String nom;
	
	public String description;
	
	public int delaiModifContrat;
	
	public List<ProdUtilisateurDTO> referents = new ArrayList<>();
	
	public List<ProdUtilisateurDTO> utilisateurs = new ArrayList<>();
		

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

	public int getDelaiModifContrat()
	{
		return delaiModifContrat;
	}

	public void setDelaiModifContrat(int delaiModifContrat)
	{
		this.delaiModifContrat = delaiModifContrat;
	}

	public List<ProdUtilisateurDTO> getReferents()
	{
		return referents;
	}

	public void setReferents(List<ProdUtilisateurDTO> referents)
	{
		this.referents = referents;
	}

	public List<ProdUtilisateurDTO> getUtilisateurs()
	{
		return utilisateurs;
	}

	public void setUtilisateurs(List<ProdUtilisateurDTO> utilisateurs)
	{
		this.utilisateurs = utilisateurs;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	
}
