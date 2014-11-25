package fr.amapj.service.services.historiquecontrats;

import java.util.Date;

import fr.amapj.view.engine.tools.TableItem;

/**
 * Represente un contrat signe
 *
 */
public class HistoriqueContratDTO implements TableItem
{
	public String nomContrat;

	public String nomProducteur;

	public Date dateDebut;

	public Date dateFin;

	public Date dateCreation;

	public Date dateModification;

	public int montant;

	public Long idUtilisateur;

	public Long idContrat;

	public Long idModeleContrat;

	public String getNomContrat()
	{
		return nomContrat;
	}

	public void setNomContrat(String nomContrat)
	{
		this.nomContrat = nomContrat;
	}

	public String getNomProducteur()
	{
		return nomProducteur;
	}

	public void setNomProducteur(String nomProducteur)
	{
		this.nomProducteur = nomProducteur;
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

	public Date getDateCreation()
	{
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation)
	{
		this.dateCreation = dateCreation;
	}

	public Date getDateModification()
	{
		return dateModification;
	}

	public void setDateModification(Date dateModification)
	{
		this.dateModification = dateModification;
	}

	public int getMontant()
	{
		return montant;
	}

	public void setMontant(int montant)
	{
		this.montant = montant;
	}

	/**
	 * Element permettant de distinguer les lignes
	 */
	public Long getId()
	{
		return idContrat;
	}

}
