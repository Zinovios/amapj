package fr.amapj.service.services.saisiepermanence.planif;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.model.engine.db.DbManager;
import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.distribution.DatePermanence;
import fr.amapj.model.models.distribution.DatePermanenceUtilisateur;
import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.gestioncontrat.DateModeleContratDTO;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.view.engine.ui.AppConfiguration;
import fr.amapj.view.views.gestioncontrat.editorpart.FrequenceLivraison;

/**
 * Permet la planifications des permanences 
 * 
 */
public class PlanifPermanenceService
{
	
	
	// PARTIE REQUETAGE POUR CHARGER LES PERMANENCES
	
	/**
	 * On fait un premier calcul pour charger les utilisateurs et les dates
	 */
	@DbRead
	public void fillPlanifInfo(PlanifDTO dto)
	{
		EntityManager em = TransactionHelper.getEm();
		
		dto.dates.clear();
		// On charge les dates
		List<Date> dates = new GestionContratService().getAllDates(dto.dateDebut, dto.dateFin, dto.frequence, new ArrayList<DateModeleContratDTO>());
		for (Date date : dates)
		{
			PlanifDateDTO planifDateDTO = new PlanifDateDTO();
			planifDateDTO.datePermanence = date;
			dto.dates.add(planifDateDTO);
		}
		
		
		// On charge les utilisateurs
		dto.utilisateurs.clear();
		List<Utilisateur> utilisateurs = getAllUtilisateursCotisants(em);
		for (Utilisateur utilisateur : utilisateurs)
		{
			PlanifUtilisateurDTO e = new PlanifUtilisateurDTO();
			e.actif = true;
			e.bonus = 0;
			e.idUtilisateur = utilisateur.getId();
			
			dto.utilisateurs.add(e);
		}
		
	}
	
	/**
	 * Retourne la liste des utilisateurs cotisants
	 * @return
	 */
	public List<Utilisateur> getAllUtilisateursCotisants(EntityManager em)
	{
		Query q = em.createQuery("select u from Utilisateur u where u.cotisation=:coti and u.etatUtilisateur=:etat order by u.nom,u.prenom");
		q.setParameter("coti", EtatUtilisateur.ACTIF);
		q.setParameter("etat", EtatUtilisateur.ACTIF);
		List<Utilisateur> us = q.getResultList();
		return us;
	}

	
	


	// PARTIE CREATION OU MISE A JOUR D'UNE DISTRIBUTION
	
	@DbWrite
	public void savePlanification(PlanifDTO planifDTO)
	{
		EntityManager em = TransactionHelper.getEm();
		
		// On calcule la liste des creneaus
		List<Creneau> creneaus = computeCreneau(planifDTO.dates,planifDTO.nbPersonne);
		
		// 
		populateCreneau(creneaus,em,planifDTO);
		
		// On sauvegarde ensuite tout ca en base
		for (Creneau creneau : creneaus)
		{
			DatePermanence datePermanence = findOrCreateDatePermanence(em,creneau.date);
			
			DatePermanenceUtilisateur du = new DatePermanenceUtilisateur();
			du.setDatePermanence(datePermanence);
			du.setUtilisateur(em.find(Utilisateur.class, creneau.idUtilisateur));
			du.setNumSession(creneau.numSession);
			em.persist(du);
		}
		
	}
	
	
	private DatePermanence findOrCreateDatePermanence(EntityManager em, Date date)
	{
		Query q = em.createQuery("select d from DatePermanence d WHERE d.datePermanence=:d");
		q.setParameter("d", date, TemporalType.DATE);
		
		List<DatePermanence> ds = q.getResultList();
		if (ds.size()==0)
		{
		
			DatePermanence dp = new DatePermanence();
			dp.setDatePermanence(date);
			em.persist(dp);
			return dp;
		}
		else
		{
			return ds.get(0);
		}
	}


	private void populateCreneau(List<Creneau> creneaus, EntityManager em, PlanifDTO planifDTO)
	{
		int numSession = 1;
		while(numSession<500)
		{
			// On recherche la liste des utilisateurs pour cette session
			List<UtilisateurInfo> utilisateurs = getUtilisateurs(planifDTO,em,numSession);
			
			// On recherche la liste des creneaux pour cette session
			List<Creneau> creneauSession = getCreneau(creneaus,utilisateurs.size());
			
			// Si la liste des creneaux est vide : tout a été fait, on arrête
			if (creneauSession.size()==0 && utilisateurs.size()!=0)
			{
				return;
			}
			else
			{
				// On place les utilisateurs de cette session
				placeUtilisateur(utilisateurs,creneauSession,em,numSession);
			}
			
			numSession++;
		}
		throw new AmapjRuntimeException("Boucle infinie");
	}
	
	
	/**
	 * Recherche de tous les utilisateurs pouvant être affectés à cette session 
	 * @param planifDTO
	 * @param em
	 * @param numSession
	 * @return
	 */
	private List<UtilisateurInfo> getUtilisateurs(PlanifDTO planifDTO, EntityManager em, int numSession)
	{
		List<UtilisateurInfo> res  = new ArrayList<>();
		
		for (PlanifUtilisateurDTO dto : planifDTO.utilisateurs)
		{
			if ( (dto.actif) && (dto.bonus<numSession) )
			{
				UtilisateurInfo u = new UtilisateurInfo();
				u.idUtilisateur = dto.idUtilisateur;
				u.dateLivraison = new ArrayList<>();
				res.add(u);
			}
		}
		
		return res;
	}


	/**
	 * On prend dans la listes creneaus les x premiers elements non affectés
	 *  
	 * @param creneaus
	 * @param x
	 * @return
	 */
	private List<Creneau> getCreneau(List<Creneau> creneaus, int x)
	{
		List<Creneau> res = new ArrayList<Creneau>();
		
		for (Creneau creneau : creneaus)
		{
			if (res.size()>=x)
			{
				return res;
			}
			
			if (creneau.idUtilisateur==null)
			{
				res.add(creneau);
			}
			
		}
		return res;
	}

	/**
	 * Placement des utilisateurs sur les creneaux
	 */
	private void placeUtilisateur(List<UtilisateurInfo> utilisateurs, List<Creneau> creneauSession, EntityManager em, int numSession)
	{
		// On randomize les utilisateurs
		Collections.shuffle(utilisateurs);
		
		// On extrait la liste des dates
		List<Date> dates = extractDate(creneauSession);
		
		// Pour chaque utilisateur, on calcule ses dates de livraison
		for (UtilisateurInfo utilisateurInfo : utilisateurs)
		{
			utilisateurInfo.dateLivraison = geDateLivraison(utilisateurInfo.idUtilisateur,dates,em);
		}
		
		// On trie ensuite suivant cette information
		Collections.sort(utilisateurs);
		
		System.out.println("Numero de session"+numSession);
		for (UtilisateurInfo u : utilisateurs)
		{
			Utilisateur ut = em.find(Utilisateur.class, u.idUtilisateur);
			System.out.println(ut.getNom()+" "+ut.getPrenom()+" "+u.dateLivraison.size());
		}
		
		// On itere ensuite sur chaque utilisateur et on le place
		for (UtilisateurInfo utilisateurInfo : utilisateurs)
		{
			placeOneUtilisateur(utilisateurInfo,creneauSession,numSession);
		}
		
	}
	
	
	private List<Date> geDateLivraison(Long idUtilisateur, List<Date> dates, EntityManager em)
	{
		Query q = em.createQuery("select distinct(c.modeleContratDate.dateLiv) from ContratCell c WHERE " +
				"c.contrat.utilisateur=:u and " +
				"c.modeleContratDate.dateLiv in :dates " +
				"order by c.modeleContratDate.dateLiv");
		q.setParameter("u", em.find(Utilisateur.class, idUtilisateur));
		q.setParameter("dates", dates);
		
		List<Date> ds = q.getResultList();
		return ds;
	}


	private List<Date> extractDate(List<Creneau> creneauSession)
	{
		List<Date> res  = new ArrayList<>();
		for (Creneau creneau : creneauSession)
		{
			if (res.contains(creneau.date)==false)
			{
				res.add(creneau.date);
			}
		}
		return res;
	}


	private void placeOneUtilisateur(UtilisateurInfo utilisateurInfo, List<Creneau> creneauSession, int numSession)
	{
		// On le place sur un créneau à une date ou l'utilisateur vient
		for (Creneau creneau : creneauSession)
		{
			if ( (creneau.idUtilisateur==null) && (utilisateurInfo.dateLivraison.contains(creneau.date)) )
			{
				creneau.idUtilisateur = utilisateurInfo.idUtilisateur;
				creneau.numSession = numSession;
				return ;
			}
		}
		
		// Si on n'y arrive pas : on le place sur le premier creneau qui est libre
		for (Creneau creneau : creneauSession)
		{
			if (creneau.idUtilisateur==null)
			{
				creneau.idUtilisateur = utilisateurInfo.idUtilisateur;
				creneau.numSession = numSession;
				return ;
			}
		}
		
		// Si on n'y arrive toujours pas : c'est que la session est finie, on ne fait rien
	}





	private List<Creneau> computeCreneau(List<PlanifDateDTO> dates, int nbPersonne)
	{
		List<Creneau> res  = new ArrayList<>();
		
		for (PlanifDateDTO date : dates)
		{
			for (int i = 0; i < nbPersonne; i++)
			{
				Creneau c = new Creneau();
				c.date = date.datePermanence;
				res.add(c);
			}
		}
		return res;
	}
	
	
	
	


	// Un creneau correspond à une personne de permanence un jour donné 
	private static class Creneau
	{
		Date date;
		Long idUtilisateur;
		int numSession;
	}
	
	//  
	private static class UtilisateurInfo implements Comparable<UtilisateurInfo>
	{
		Long idUtilisateur;
		List<Date> dateLivraison;
		
		@Override
		public int compareTo(UtilisateurInfo o)
		{
			// L'utilisateur qui n'a pas de livraisons est toujours placé en dernier
			int nbLivraison1 = dateLivraison.size()==0 ? 1000 :  dateLivraison.size();
			int nbLivraison2 = o.dateLivraison.size()==0 ? 1000 :  o.dateLivraison.size();
			
					
			// Sinon on compare simplement le nombre de livraison
			return nbLivraison1-nbLivraison2;
			
		}
	}
	
	public static void main(String[] args) throws ParseException
	{
		TestTools.init();
				
		PlanifPermanenceService service = new PlanifPermanenceService();
		System.out.println("Debut ...");

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		
		// Phase 1
		PlanifDTO dto = new PlanifDTO();
		dto.setDateDebut(df.parse("12/06/14"));
		dto.setDateFin(df.parse("04/06/15"));
		dto.frequence = FrequenceLivraison.UNE_FOIS_PAR_SEMAINE;
		dto.nbPersonne = 3;
		service.fillPlanifInfo(dto);
		
		
		// Phase 2
		for (PlanifUtilisateurDTO pu : dto.utilisateurs)
		{
			if (pu.idUtilisateur.intValue()==1052)
			{
				pu.bonus = 1;
			}
			
		}
		service.savePlanification(dto);
		
		
		System.out.println("Fin.");

	}



	
}
