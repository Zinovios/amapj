package fr.amapj.service.services.utilisateur.envoimail;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 
 */
public class EnvoiMailDTO 
{
	
	public List<EnvoiMailUtilisateurDTO> utilisateurs = new ArrayList<>();
	
	public String texteMail;

	public List<EnvoiMailUtilisateurDTO> getUtilisateurs()
	{
		return utilisateurs;
	}

	public void setUtilisateurs(List<EnvoiMailUtilisateurDTO> utilisateurs)
	{
		this.utilisateurs = utilisateurs;
	}

	public String getTexteMail()
	{
		return texteMail;
	}

	public void setTexteMail(String texteMail)
	{
		this.texteMail = texteMail;
	}

	
	

}
