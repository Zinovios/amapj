package fr.amapj.service.services.saisiepermanence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import fr.amapj.view.engine.tools.TableItem;

/**
 * Permet la gestion des utilisateurs en masse
 * ou du changement de son état
 * 
 */
public class PermanenceDTO implements TableItem
{
	public Long id;

	public Date datePermanence;
	
	
	public List<PermanenceUtilisateurDTO> permanenceUtilisateurs = new ArrayList<>();
	
	/*
	 * Pour affichage dans le tableau
	 */

	
	
	
	public String getUtilisateurs()
	{
		return getUtilisateurs(", ");
	}
	
	public String getUtilisateurs(String sep)
	{
		if (permanenceUtilisateurs.size()==0)
		{
			return "";
		}
		
		String str ="";
		for (int i = 0; i < permanenceUtilisateurs.size()-1; i++)
		{
			PermanenceUtilisateurDTO dto = permanenceUtilisateurs.get(i);
			str = str+dto.nom+" "+dto.prenom+sep;
		}
		
		PermanenceUtilisateurDTO dto = permanenceUtilisateurs.get( permanenceUtilisateurs.size()-1);
		str = str+dto.nom+" "+dto.prenom;
		return str;
		
	}

	public void setUtilisateurs(String utilisateurs)
	{
		// DO NOTHING
	}
	
	
	public String getNumeroSession()
	{
		if (permanenceUtilisateurs.size()==0)
		{
			return "";
		}
		
		List<Integer> is = new ArrayList<>();
		
		for (PermanenceUtilisateurDTO dto : permanenceUtilisateurs)
		{
			if (is.contains(dto.numSession)==false)
			{
				is.add(dto.numSession);
			}
		}
		
		Collections.sort(is);
		
		String str ="";
		for (int i = 0; i < is.size()-1; i++)
		{
			Integer num = is.get(i);
			
			str = str+num+", ";
		}
		
		Integer num = is.get( is.size()-1);
		str = str+num;
		return str;
	}
	
	public void setNumeroSession(String numeroSession)
	{
		// DO NOTHING
	}
	
	
	
	/*
	 * 
	 */
	


	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Date getDatePermanence()
	{
		return datePermanence;
	}

	public void setDatePermanence(Date datePermanence)
	{
		this.datePermanence = datePermanence;
	}

	public List<PermanenceUtilisateurDTO> getPermanenceUtilisateurs()
	{
		return permanenceUtilisateurs;
	}

	public void setPermanenceUtilisateurs(List<PermanenceUtilisateurDTO> permanenceUtilisateurs)
	{
		this.permanenceUtilisateurs = permanenceUtilisateurs;
	}

	
	
	
	
	
}
