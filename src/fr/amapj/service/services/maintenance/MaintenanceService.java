package fr.amapj.service.services.maintenance;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.common.DateUtils;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.contrat.modele.ModeleContratDatePaiement;
import fr.amapj.model.models.contrat.reel.Contrat;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.mescontrats.MesContratsService;

/**
 * Permet la gestion des contrats
 * 
 *  
 *
 */
public class MaintenanceService
{
	public MaintenanceService()
	{

	}



	// PARTIE SUPPRESSION D'UN MODELE DE CONTRAT ET DE TOUS LES CONTRATS ASSOCIES

	/**
	 * Permet de supprimer un modele de contrat et TOUS les contrats associées
	 * Ceci est fait dans une transaction en ecriture  
	 */
	@DbWrite
	public void deleteModeleContratAndContrats(Long modeleContratId)
	{
		EntityManager em = TransactionHelper.getEm();
		
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		List<Contrat> cs = getAllContrats(em, mc);
		
		for (Contrat contrat : cs)
		{
			new MesContratsService().deleteContrat(contrat.getId());
		}
		
		new GestionContratService().deleteContrat(modeleContratId);
		
	}

	
	/**
	 * 
	 */
	private List<Contrat> getAllContrats(EntityManager em, ModeleContrat mc)
	{
		Query q = em.createQuery("select c from Contrat c WHERE c.modeleContrat=:mc");
		q.setParameter("mc",mc);
		List<Contrat> cs = q.getResultList();
		return cs;
	}
	
	
	

	// PARTIE DECALAGE TEMPOREL D'UN MODELE DE CONTRAT ET DE TOUS LES CONTRATS ASSOCIES

	/**
	 * Permet de supprimer un modele de contrat et TOUS les contrats associées
	 * Ceci est fait dans une transaction en ecriture  
	 */
	@DbWrite
	public void shiftDateModeleContratAndContrats(Long modeleContratId,int deltaInMonth)
	{
		EntityManager em = TransactionHelper.getEm();
		
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		
		mc.setDateFinInscription(add(deltaInMonth,mc.getDateFinInscription()));
		mc.setDateRemiseCheque(add(deltaInMonth,mc.getDateRemiseCheque()));
		
		
		Query q = em.createQuery("select d from ModeleContratDatePaiement d WHERE d.modeleContrat=:mc");
		q.setParameter("mc",mc);
		List<ModeleContratDatePaiement> ds = q.getResultList();
		for (ModeleContratDatePaiement modeleContratDatePaiement : ds)
		{
			modeleContratDatePaiement.setDatePaiement(add(deltaInMonth,modeleContratDatePaiement.getDatePaiement()));
		}
		
		q = em.createQuery("select d from ModeleContratDate d WHERE d.modeleContrat=:mc");
		q.setParameter("mc",mc);
		List<ModeleContratDate> mcds = q.getResultList();
		for (ModeleContratDate mcd : mcds)
		{
			mcd.setDateLiv(add(deltaInMonth,mcd.getDateLiv()));
		}
		
		List<Contrat> cs = getAllContrats(em, mc);
		
		for (Contrat contrat : cs)
		{
			contrat.setDateCreation(add(deltaInMonth,contrat.getDateCreation()));
			contrat.setDateModification(add(deltaInMonth,contrat.getDateModification()));
		}
	}


	private Date add(int deltaInMonth, Date date)
	{
		if (date==null)
		{
			return null;
		}
		return DateUtils.addMonth(date, deltaInMonth);
	}


	
	/**
	 * Permet de vider le cache de la base
	 * Ceci est fait dans une transaction en ecriture  
	 * obligatoire après requete SQL manuelle
	 */
	@DbWrite
	public void resetDatabaseCache()
	{
		EntityManager em = TransactionHelper.getEm();
		em.getEntityManagerFactory().getCache().evictAll();
	}

	
		
	
}
