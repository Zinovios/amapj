package fr.amapj.service.services.saisiepermanence.planif;


/**
 * Permet la gestion des utilisateurs en masse
 * ou du changement de son état
 * 
 */
public class PlanifUtilisateurDTO 
{
	public Long idUtilisateur;

	public boolean actif;
	
	public int bonus;

	public Long getIdUtilisateur()
	{
		return idUtilisateur;
	}

	public void setIdUtilisateur(Long idUtilisateur)
	{
		this.idUtilisateur = idUtilisateur;
	}

	public boolean isActif()
	{
		return actif;
	}

	public void setActif(boolean actif)
	{
		this.actif = actif;
	}

	public int getBonus()
	{
		return bonus;
	}

	public void setBonus(int bonus)
	{
		this.bonus = bonus;
	}
	
		
	
	
}
