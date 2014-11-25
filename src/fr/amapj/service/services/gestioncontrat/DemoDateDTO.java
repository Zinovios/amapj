package fr.amapj.service.services.gestioncontrat;


import java.util.Date;

/**
 * Mise à jour de la base de démo
 *
 */
public class DemoDateDTO
{
	
	public Date dateFinInscription;
	
	public Date dateRemiseCheque;
	
	public Date dateDebut;
	
	public Date dateFin;
	
	public Date premierCheque;
	
	public Date dernierCheque;
	
	public String password;

	public Date getDateFinInscription()
	{
		return dateFinInscription;
	}

	public void setDateFinInscription(Date dateFinInscription)
	{
		this.dateFinInscription = dateFinInscription;
	}

	public Date getDateRemiseCheque()
	{
		return dateRemiseCheque;
	}

	public void setDateRemiseCheque(Date dateRemiseCheque)
	{
		this.dateRemiseCheque = dateRemiseCheque;
	}

	public Date getDateDebut()
	{
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut)
	{
		this.dateDebut = dateDebut;
	}

	public Date getDateFin()
	{
		return dateFin;
	}

	public void setDateFin(Date dateFin)
	{
		this.dateFin = dateFin;
	}

	public Date getPremierCheque()
	{
		return premierCheque;
	}

	public void setPremierCheque(Date premierCheque)
	{
		this.premierCheque = premierCheque;
	}

	public Date getDernierCheque()
	{
		return dernierCheque;
	}

	public void setDernierCheque(Date dernierCheque)
	{
		this.dernierCheque = dernierCheque;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
	
	
	
}
