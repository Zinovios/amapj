package fr.amapj.service.services.gestioncontratsigne;


import java.util.Date;

import fr.amapj.view.engine.tools.TableItem;

/**
 * Permet l'annulation d'une ou plusieurs dates de livraison pour un contrat signé
 *
 */
public class AnnulationDateLivraisonDTO 
{
	public Long mcId;
	
	public Date dateDebut;
	
	public Date dateFin;

	
	
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
	
	
	
	
	
	
	

}
