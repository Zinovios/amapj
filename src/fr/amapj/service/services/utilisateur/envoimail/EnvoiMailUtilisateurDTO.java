package fr.amapj.service.services.utilisateur.envoimail;


/**
 * 
 * 
 */
public class EnvoiMailUtilisateurDTO 
{
	public Long idUtilisateur;

	public boolean sendMail;

	public Long getIdUtilisateur()
	{
		return idUtilisateur;
	}

	public void setIdUtilisateur(Long idUtilisateur)
	{
		this.idUtilisateur = idUtilisateur;
	}

	public boolean isSendMail()
	{
		return sendMail;
	}

	public void setSendMail(boolean sendMail)
	{
		this.sendMail = sendMail;
	}
	
	
	
		
	
	
}
