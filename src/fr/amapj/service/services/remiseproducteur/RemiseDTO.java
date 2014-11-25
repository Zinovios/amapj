package fr.amapj.service.services.remiseproducteur;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.amapj.service.services.mespaiements.PaiementFourniDTO;
import fr.amapj.view.engine.tools.TableItem;

/**
 * Represente une remise
 *
 */
public class RemiseDTO implements TableItem
{

	public Long idModeleContrat;
	
	public Long idModeleContratDatePaiement;
	
	// Id de la remise
	public Long id;
	
	public Date dateTheoRemise;
	
	public String moisRemise;
		
	public Date dateCreation;
	
	public Date dateReelleRemise;
	
	public int mnt;
	
	public int nbCheque;
	
	public List<PaiementRemiseDTO> paiements = new ArrayList<>();

	// Indique si la preparation de la remise s'est passé correctement ou non
	public boolean preparationRemiseFailed;
	
	// Indique pourquoi la preparation de la remise a échouée 
	public String messageRemiseFailed;


	public Date getDateTheoRemise()
	{
		return dateTheoRemise;
	}

	public void setDateTheoRemise(Date dateTheoRemise)
	{
		this.dateTheoRemise = dateTheoRemise;
	}

	public String getMoisRemise()
	{
		return moisRemise;
	}

	public void setMoisRemise(String moisRemise)
	{
		this.moisRemise = moisRemise;
	}

	public Date getDateCreation()
	{
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation)
	{
		this.dateCreation = dateCreation;
	}

	public Date getDateReelleRemise()
	{
		return dateReelleRemise;
	}

	public void setDateReelleRemise(Date dateReelleRemise)
	{
		this.dateReelleRemise = dateReelleRemise;
	}

	public int getMnt()
	{
		return mnt;
	}

	public void setMnt(int mnt)
	{
		this.mnt = mnt;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public int getNbCheque()
	{
		return nbCheque;
	}

	public void setNbCheque(int nbCheque)
	{
		this.nbCheque = nbCheque;
	}
	
	
	
	
	
	
	
	
	

}
