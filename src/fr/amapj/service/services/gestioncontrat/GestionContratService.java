package fr.amapj.service.services.gestioncontrat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fr.amapj.common.BigDecimalUtils;
import fr.amapj.common.DateUtils;
import fr.amapj.common.LongUtils;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.contrat.modele.EtatModeleContrat;
import fr.amapj.model.models.contrat.modele.GestionPaiement;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.contrat.modele.ModeleContratDatePaiement;
import fr.amapj.model.models.contrat.modele.ModeleContratExclude;
import fr.amapj.model.models.contrat.modele.ModeleContratProduit;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.service.services.mescontrats.ContratColDTO;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.ContratLigDTO;
import fr.amapj.view.views.gestioncontrat.editorpart.FrequenceLivraison;

/**
 * Permet la gestion des modeles de contrat
 * 
 *  
 * 
 */
public class GestionContratService
{
	private final static Logger logger = Logger.getLogger(GestionContratService.class.getName());

	public GestionContratService()
	{

	}

	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES CONTRATS

	/**
	 * Permet de charger la liste de tous les modeles de contrats dans une
	 * transaction en lecture
	 */
	@DbRead
	public List<ModeleContratSummaryDTO> getModeleContratInfo()
	{
		EntityManager em = TransactionHelper.getEm();

		List<ModeleContratSummaryDTO> res = new ArrayList<ModeleContratSummaryDTO>();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ModeleContrat> cq = cb.createQuery(ModeleContrat.class);

		List<ModeleContrat> mcs = em.createQuery(cq).getResultList();
		for (ModeleContrat mc : mcs)
		{
			ModeleContratSummaryDTO mcInfo = createModeleContratInfo(em, mc);
			res.add(mcInfo);
		}

		return res;

	}

	public ModeleContratSummaryDTO createModeleContratInfo(EntityManager em, ModeleContrat mc)
	{
		ModeleContratSummaryDTO info = new ModeleContratSummaryDTO();

		
		info.id = mc.getId();
		info.nom = mc.getNom();
		info.nomProducteur = mc.getProducteur().getNom();
		info.producteurId = mc.getProducteur().getId();
		info.finInscription = mc.getDateFinInscription();
		info.etat = mc.getEtat().toString();

		// Avec une sous requete, on obtient la liste de toutes les dates de
		// livraison
		List<ModeleContratDate> dates = getAllDates(em, mc);

		info.nbLivraison = dates.size()-getNbDateAnnulees(em,mc);
		
		info.nbInscrits = getNbInscrits(em, mc);

		if (dates.size() >= 1)
		{
			info.dateDebut = dates.get(0).getDateLiv();
			info.dateFin = dates.get(dates.size() - 1).getDateLiv();
		}

		info.nbProduit = getNbProduit(em, mc);

		return info;
	}

	/**
	 * Retourne le nombre d'adherent ayant souscrit à ce modele de contrat
	 * @param em
	 * @param mc
	 * @return
	 */
	private int getNbInscrits(EntityManager em, ModeleContrat mc)
	{
		Query q = em.createQuery("select count(c.id) from Contrat c WHERE c.modeleContrat=:mc");
		q.setParameter("mc",mc);
		
		return ((Long) q.getSingleResult()).intValue();
	}

	/**
	 * Retourne le nombre de dates annulées pour un modele de contrat
	 */
	private int getNbDateAnnulees(EntityManager em, ModeleContrat mc)
	{
		Query q = em.createQuery("select count(p.id) from ModeleContratExclude p WHERE p.modeleContrat=:mc and p.produit is null");
		q.setParameter("mc",mc);
		
		return ((Long) q.getSingleResult()).intValue();
	}

	private int getNbProduit(EntityManager em, ModeleContrat mc)
	{
		return getAllProduit(em, mc).size();
	}

	/**
	 * Retrouve la liste des produits, triés suivant la valeur indx
	 * 
	 * @param em
	 * @param mc
	 * @return
	 */
	public List<ModeleContratProduit> getAllProduit(EntityManager em, ModeleContrat mc)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ModeleContratProduit> cq = cb.createQuery(ModeleContratProduit.class);
		Root<ModeleContratProduit> root = cq.from(ModeleContratProduit.class);

		// On ajoute la condition where
		cq.where(cb.equal(root.get(ModeleContratProduit.P.MODELECONTRAT.prop()), mc));

		// On trie par ordre croissant indx
		cq.orderBy(cb.asc(root.get(ModeleContratProduit.P.INDX.prop())));

		List<ModeleContratProduit> prods = em.createQuery(cq).getResultList();
		return prods;
	}

	public List<ModeleContratDate> getAllDates(EntityManager em, ModeleContrat mc)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ModeleContratDate> cq = cb.createQuery(ModeleContratDate.class);
		Root<ModeleContratDate> root = cq.from(ModeleContratDate.class);

		// On ajoute la condition where
		cq.where(cb.equal(root.get(ModeleContratDate.P.MODELECONTRAT.prop()), mc));

		// On trie par ordre croissant de date
		cq.orderBy(cb.asc(root.get(ModeleContratDate.P.DATELIV.prop())));

		List<ModeleContratDate> dates = em.createQuery(cq).getResultList();
		return dates;
	}

	public List<ModeleContratExclude> getAllExcludedDateProduit(EntityManager em, ModeleContrat mc)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ModeleContratExclude> cq = cb.createQuery(ModeleContratExclude.class);
		Root<ModeleContratExclude> root = cq.from(ModeleContratExclude.class);

		// On ajoute la condition where
		cq.where(cb.equal(root.get(ModeleContratExclude.P.MODELECONTRAT.prop()), mc));

		List<ModeleContratExclude> exclude = em.createQuery(cq).getResultList();
		return exclude;
	}
	
	
	public List<ModeleContratDatePaiement> getAllDatesPaiements(EntityManager em, ModeleContrat mc)
	{
		// On récupère ensuite la liste de tous les paiements de ce contrat
		Query q = em.createQuery("select p from ModeleContratDatePaiement p WHERE p.modeleContrat=:mc order by p.datePaiement");
		q.setParameter("mc",mc);
		List<ModeleContratDatePaiement> paiements = q.getResultList();
		return paiements;
	}
	
	
	// PARTIE CREATION D'UN MODELE DE CONTRAT
	/**
	 * Permet de pre charger les nouveaux modeles de contrat
	 */
	@DbRead
	public List<LigneContratDTO> getInfoProduitModeleContrat(Long idProducteur)
	{
		EntityManager em = TransactionHelper.getEm();
		
		List<LigneContratDTO> res = new ArrayList<LigneContratDTO>();
		
		Query q = em.createQuery("select p from Produit p " +
				"WHERE p.producteur=:producteur order by p.id");
		q.setParameter("producteur",em.find(Producteur.class, idProducteur));
		List<Produit> prods = q.getResultList();
		for (Produit prod : prods)
		{
			LigneContratDTO l =new LigneContratDTO();
			l.prix = new Integer(0);
			l.produitId = prod.getId();
			l.produitNom = prod.getNom();
			res.add(l);
		}
		return res;
	}
	

	// PARTIE CHARGEMENT D'UN MODELE DE CONTRAT

	/**
	 * Permet de charger les informations d'un modele contrat dans une
	 * transaction en lecture
	 */
	@DbRead
	public ModeleContratDTO loadModeleContrat(Long id)
	{
		EntityManager em = TransactionHelper.getEm();
		
		ModeleContrat mc = em.find(ModeleContrat.class, id);

		ModeleContratDTO info = new ModeleContratDTO();
		info.id = mc.getId();
		info.nom = mc.getNom();
		info.description = mc.getDescription();
		info.producteur = mc.getProducteur().getId();
		info.dateFinInscription = mc.getDateFinInscription();
		info.gestionPaiement = mc.getGestionPaiement();
		info.textPaiement = mc.getTextPaiement();
		info.libCheque = mc.getLibCheque();
		info.dateRemiseCheque = mc.getDateRemiseCheque();
	

		// Avec une sous requete, on obtient la liste de toutes les dates de
		// livraison
		List<ModeleContratDate> dates = getAllDates(em, mc);
		for (ModeleContratDate date : dates)
		{
			DateModeleContratDTO dto = new DateModeleContratDTO();
			dto.dateLiv = date.getDateLiv();
			info.dateLivs.add(dto);
		}

		if (dates.size() >= 1)
		{
			info.dateDebut = dates.get(0).getDateLiv();
			info.dateFin = dates.get(dates.size() - 1).getDateLiv();
		}

		// Avec une sous requete, on récupere la liste des produits
		List<ModeleContratProduit> prods = getAllProduit(em, mc);
		for (ModeleContratProduit prod : prods)
		{
			LigneContratDTO lig = new LigneContratDTO();
			lig.produitId = prod.getProduit().getId();
			lig.produitNom = prod.getProduit().getNom();
			lig.prix = prod.getPrix();

			info.produits.add(lig);
		}

		info.frequence = guessFrequence(dates);
		
		// Avec une sous requete, on récupere la liste des dates de paiements
		List<ModeleContratDatePaiement> datePaiements = getAllDatesPaiements(em, mc);
		if (datePaiements.size() >= 1)
		{
			info.premierCheque = datePaiements.get(0).getDatePaiement();
			info.dernierCheque = datePaiements.get(datePaiements.size()-1).getDatePaiement();
		}

		return info;
	}

	private FrequenceLivraison guessFrequence(List<ModeleContratDate> dates)
	{
		if ((dates.size() == 0) || dates.size() == 1)
		{
			return FrequenceLivraison.UNE_SEULE_LIVRAISON;
		}
		int delta = DateUtils.getDeltaDay(dates.get(0).getDateLiv(), dates.get(1).getDateLiv());
		if (delta == 7)
		{
			return FrequenceLivraison.UNE_FOIS_PAR_SEMAINE;
		} else if (delta == 14)
		{
			return FrequenceLivraison.QUINZE_JOURS;
		} else
		{
			return FrequenceLivraison.UNE_FOIS_PAR_MOIS;
		}

	}

	// PARTIE SAUVEGARDE D'UN NOUVEAU CONTRAT

	/**
	 * Permet de sauvegarder un nouveau contrat Ceci est fait dans une
	 * transaction en ecriture
	 */
	@DbWrite
	public void saveNewModeleContrat(final ModeleContratDTO modeleContrat)
	{
		EntityManager em = TransactionHelper.getEm();
		
		// On charge le producteur
		Producteur p = em.find(Producteur.class, modeleContrat.producteur);

		// Informations d'entete
		ModeleContrat mc = new ModeleContrat();
		mc.setProducteur(p);
		mc.setNom(modeleContrat.nom);
		mc.setDescription(modeleContrat.description);
		mc.setDateFinInscription(modeleContrat.dateFinInscription);
		
		
		// Informations sur le paiement
		mc.setGestionPaiement(modeleContrat.gestionPaiement);
		mc.setTextPaiement(modeleContrat.textPaiement);
		mc.setDateRemiseCheque(modeleContrat.dateRemiseCheque);
		mc.setLibCheque(modeleContrat.libCheque);
		
		em.persist(mc);

		// Création de toutes les lignes pour chacune des dates
		List<Date> dates = getAllDates(modeleContrat.dateDebut, modeleContrat.dateFin, modeleContrat.frequence,modeleContrat.dateLivs);
		for (Date date : dates)
		{
			ModeleContratDate md = new ModeleContratDate();
			md.setModeleContrat(mc);
			md.setDateLiv(date);
			em.persist(md);
		}

		// Création de loutes les lignes pour chacun des produits
		List<LigneContratDTO> produits = modeleContrat.getProduits();
		int index = 0;
		for (LigneContratDTO lig : produits)
		{
			ModeleContratProduit mcp = new ModeleContratProduit();
			mcp.setIndx(index);
			mcp.setModeleContrat(mc);
			mcp.setPrix(lig.getPrix().intValue());
			mcp.setProduit(em.find(Produit.class, lig.produitId));

			em.persist(mcp);

			index++;

		}
		
		// Informations de dates de paiement
		if (modeleContrat.gestionPaiement!=GestionPaiement.NON_GERE)
		{
			List<Date> datePaiements = getAllDatePaiements(modeleContrat.premierCheque, modeleContrat.dernierCheque, modeleContrat.frequence,modeleContrat.dateRemiseCheque);
			for (Date datePaiement : datePaiements)
			{
				ModeleContratDatePaiement md = new ModeleContratDatePaiement();
				md.setModeleContrat(mc);
				md.setDatePaiement(datePaiement);
				em.persist(md);
			}
		}
	}
	
	private List<Date> getAllDatePaiements(Date premierCheque, Date dernierCheque, FrequenceLivraison frequence, Date dateRemiseCheque)
	{
		List<Date> res = new ArrayList<Date>();

		// Si une seule livraison : c'est fini
		if (frequence.equals(FrequenceLivraison.UNE_SEULE_LIVRAISON))
		{
			int cpt = 0;
			res.add(dateRemiseCheque);
			return res;
		} 
		
		int cpt =0;
		
		while (premierCheque.before(dernierCheque) || premierCheque.equals(dernierCheque))
		{
			cpt++;
			res.add(premierCheque);
			premierCheque = DateUtils.addMonth(premierCheque, 1);

			if (cpt > 1000)
			{
				throw new RuntimeException("Erreur dans la saisie des dates");
			}
		}

		return res;
	}
	
	
	
	
	

	public List<Date> getAllDates(Date dateDebut, Date dateFin, FrequenceLivraison frequence, List<DateModeleContratDTO> dateLivs)
	{
		List<Date> res = new ArrayList<Date>();

		int cpt = 0;
		res.add(dateDebut);

		// Si une seule livraison : c'est fini
		if (frequence.equals(FrequenceLivraison.UNE_SEULE_LIVRAISON))
		{
			return res;
		} 
		// Si la liste a été définie complètement
		else if (frequence.equals(FrequenceLivraison.AUTRE))
		{
			return getAllDatesAutre(dateLivs);
		}
		else if (frequence.equals(FrequenceLivraison.UNE_FOIS_PAR_MOIS))
		{
			return getAllDatesUneFoisParMois(dateDebut, dateFin);
		}

		int delta = 0;
		if (frequence.equals(FrequenceLivraison.UNE_FOIS_PAR_SEMAINE))
		{
			delta = 7;
		} 
		else if (frequence.equals(FrequenceLivraison.QUINZE_JOURS))
		{
			delta = 14;
		}

		dateDebut = DateUtils.addDays(dateDebut, delta);

		while (dateDebut.before(dateFin) || dateDebut.equals(dateFin))
		{
			cpt++;
			res.add(dateDebut);
			dateDebut = DateUtils.addDays(dateDebut, delta);

			if (cpt > 1000)
			{
				throw new RuntimeException("Erreur dans la saisie des dates");
			}
		}

		return res;
	}

	private List<Date> getAllDatesAutre(List<DateModeleContratDTO> dateLivs)
	{
		List<Date> res = new ArrayList<>();
		for (DateModeleContratDTO dto : dateLivs)
		{
			res.add(dto.dateLiv);
		}
		return res;
	}

	/**
	 * Calcul permettant d'avoir par exemple tous les 1er jeudi du mois
	 */
	private List<Date> getAllDatesUneFoisParMois(Date dateDebut, Date dateFin)
	{
		List<Date> res = new ArrayList<Date>();

		int cpt = 0;
		res.add(dateDebut);

		int rank = DateUtils.getDayOfWeekInMonth(dateDebut);
		int delta = 7;

		dateDebut = DateUtils.addDays(dateDebut, delta);

		while (dateDebut.before(dateFin) || dateDebut.equals(dateFin))
		{
			cpt++;
			if (DateUtils.getDayOfWeekInMonth(dateDebut) == rank)
			{
				res.add(dateDebut);
			}
			dateDebut = DateUtils.addDays(dateDebut, delta);

			if (cpt > 1000)
			{
				throw new RuntimeException("Erreur dans la saisie des dates");
			}
		}

		return res;
	}

	// PARTIE SUPPRESSION

	/**
	 * Permet de supprimer un contrat Ceci est fait dans une transaction en
	 * ecriture
	 */
	@DbWrite
	public void deleteContrat(Long id)
	{
		EntityManager em = TransactionHelper.getEm();
		
		ModeleContrat mc = em.find(ModeleContrat.class, id);

		suppressAllDatesPaiement(em, mc);
		deleteAllDateBarreesModeleContrat(em, mc);
		suppressAllDates(em, mc);
		suppressAllProduits(em, mc);

		em.remove(mc);
	}

	private void suppressAllProduits(EntityManager em, ModeleContrat mc)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ModeleContratProduit> cq = cb.createQuery(ModeleContratProduit.class);
		Root<ModeleContratProduit> root = cq.from(ModeleContratProduit.class);

		// On ajoute la condition where
		cq.where(cb.equal(root.get(ModeleContratProduit.P.MODELECONTRAT.prop()), mc));

		List<ModeleContratProduit> prods = em.createQuery(cq).getResultList();
		for (ModeleContratProduit modeleContratProduit : prods)
		{
			em.remove(modeleContratProduit);
		}
	}

	private void suppressAllDates(EntityManager em, ModeleContrat mc)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ModeleContratDate> cq = cb.createQuery(ModeleContratDate.class);
		Root<ModeleContratDate> root = cq.from(ModeleContratDate.class);

		// On ajoute la condition where
		cq.where(cb.equal(root.get(ModeleContratDate.P.MODELECONTRAT.prop()), mc));

		List<ModeleContratDate> dates = em.createQuery(cq).getResultList();
		for (ModeleContratDate modeleContratDate : dates)
		{
			em.remove(modeleContratDate);
		}
	}
	
	private void suppressAllDatesPaiement(EntityManager em, ModeleContrat mc)
	{
		Query q = em.createQuery("select d from ModeleContratDatePaiement d WHERE d.modeleContrat=:mc");
		q.setParameter("mc",mc);
		List<ModeleContratDatePaiement> ds = q.getResultList();
		for (ModeleContratDatePaiement modeleContratDatePaiement : ds)
		{
			em.remove(modeleContratDatePaiement);
		}
	}

	// PARTIE MISE A JOUR

	/**
	 * Permet de mettre à jour l'etat d'un contrat
	 * 
	 */
	@DbWrite
	public void updateEtat(EtatModeleContrat newValue, Long idModeleContrat)
	{
		EntityManager em = TransactionHelper.getEm();
		
		ModeleContrat mc = em.find(ModeleContrat.class, idModeleContrat);
		mc.setEtat(newValue);
	}

	/**
	 * Permet de mettre à jour les elements d'entete d'un contrat
	 * 
	 * @param newValue
	 * @param idModeleContrat
	 */
	@DbWrite
	public void updateEnteteModeleContrat(ModeleContratDTO modeleContrat)
	{
		EntityManager em = TransactionHelper.getEm();
		
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContrat.id);
		mc.setDateFinInscription(modeleContrat.dateFinInscription);
		mc.setNom(modeleContrat.nom);
		mc.setDescription(modeleContrat.description);
	}

	/**
	 * Permet de mettre à jour les dates d'un contrat
	 * 
	 * TODO on pourrait ameliorer en ne supprimant pas les dates qui vont être
	 * recrees juste apres
	 *
	 */
	@DbWrite
	public void updateDateModeleContrat(ModeleContratDTO modeleContrat)
	{
		EntityManager em = TransactionHelper.getEm();
		

		ModeleContrat mc = em.find(ModeleContrat.class, modeleContrat.id);

		// Avec une sous requete, on obtient la liste de toutes les dates
		// exclues actuellement en base et on les efface
		deleteAllDateBarreesModeleContrat(em, mc);

		// Avec une sous requete, on obtient la liste de toutes les dates de
		// livraison
		// actuellement en base et on les efface
		List<ModeleContratDate> datesInBase = getAllDates(em, mc);
		for (ModeleContratDate modeleContratDate : datesInBase)
		{
			em.remove(modeleContratDate);
		}

		// Création de toutes les lignes pour chacune des dates
		List<Date> dates = getAllDates(modeleContrat.dateDebut, modeleContrat.dateFin, modeleContrat.frequence,modeleContrat.dateLivs);
		for (Date date : dates)
		{
			ModeleContratDate md = new ModeleContratDate();
			md.setModeleContrat(mc);
			md.setDateLiv(date);
			em.persist(md);
		}
	}

	/**
	 * Perlet la mise à jour des dates barrées d'un contrat dans une transaction
	 * en ecriture
	 */
	@DbWrite
	public void updateDateBarreesModeleContrat(ContratDTO contratDTO)
	{
		EntityManager em = TransactionHelper.getEm();
		 
		
		ModeleContrat mc = em.find(ModeleContrat.class, contratDTO.modeleContratId);

		// On commence par effacer toutes les dates exclues
		deleteAllDateBarreesModeleContrat(em, mc);

		// On recree ensuite toutes les exclusions
		boolean[][] excluded = contratDTO.excluded;
		for (int i = 0; i < contratDTO.contratLigs.size(); i++)
		{
			ContratLigDTO ligDto = contratDTO.contratLigs.get(i);
			if (isFullExcludedLine(excluded, i,contratDTO.contratColumns.size()) == true)
			{
				ModeleContratExclude exclude = new ModeleContratExclude();
				exclude.setModeleContrat(mc);
				exclude.setDate(em.find(ModeleContratDate.class, ligDto.modeleContratDateId));
				exclude.setProduit(null);
				em.persist(exclude);
			} else
			{
				for (int j = 0; j < contratDTO.contratColumns.size(); j++)
				{
					if (excluded[i][j] == true)
					{
						ContratColDTO colDto = contratDTO.contratColumns.get(j);

						ModeleContratExclude exclude = new ModeleContratExclude();
						exclude.setModeleContrat(mc);
						exclude.setDate(em.find(ModeleContratDate.class, ligDto.modeleContratDateId));
						exclude.setProduit(em.find(ModeleContratProduit.class, colDto.modeleContratProduitId));
						em.persist(exclude);
					}
				}
			}
		}
	}

	/**
	 * Return true si toute la ligne est exclue
	 * @param excluded
	 * @param lineNumber
	 * @param lineLength
	 * @return
	 */
	private boolean isFullExcludedLine(boolean[][] excluded, int lineNumber,int lineLength)
	{
		for (int j = 0; j < lineLength; j++)
		{
			if (excluded[lineNumber][j]==false)
			{
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Methode utilitaire permettant de supprimer toutes les dates barrées d'un modele de contrat
	 * @param em
	 * @param mc
	 */
	public void deleteAllDateBarreesModeleContrat(EntityManager em, ModeleContrat mc)
	{
		// On commence par effacer toutes les dates exclues
		List<ModeleContratExclude> excludes = getAllExcludedDateProduit(em, mc);
		for (ModeleContratExclude exclude : excludes)
		{
			em.remove(exclude);
		}
	}
	
	/**
	 * Perlet la mise à jour des produits d'un contrat dans une transaction
	 * en ecriture
	 * 
	 * TODO avoir un truc plus fin pour effacer uniquement ce qui est nécessaire 
	 */
	@DbWrite
	public void updateProduitModeleContrat(final ModeleContratDTO modeleContrat)
	{
		EntityManager em = TransactionHelper.getEm();
		
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContrat.id);
		
		// On supprime d'abord tous les produits
		suppressAllProduits(em, mc);
		
		// Création de toutes les lignes pour chacun des produits
		List<LigneContratDTO> produits = modeleContrat.getProduits();
		int index = 0;
		for (LigneContratDTO lig : produits)
		{
			ModeleContratProduit mcp = new ModeleContratProduit();
			mcp.setIndx(index);
			mcp.setModeleContrat(mc);
			mcp.setPrix(lig.getPrix().intValue());
			mcp.setProduit(em.find(Produit.class, lig.produitId));

			em.persist(mcp);

			index++;

		}
	}

	@DbWrite
	public void updateInfoPaiement(ModeleContratDTO modeleContrat)
	{
		EntityManager em = TransactionHelper.getEm();
		
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContrat.id);
		
		// Sauvegarde des info de paiements
		mc.setGestionPaiement(modeleContrat.gestionPaiement);
		mc.setTextPaiement(modeleContrat.textPaiement);
		mc.setDateRemiseCheque(modeleContrat.dateRemiseCheque);
		mc.setLibCheque(modeleContrat.libCheque);

		// Avec une sous requete, on obtient la liste de toutes les dates de
		// paiement  actuellement en base et on les efface
		List<ModeleContratDatePaiement> datesInBase = getAllDatesPaiements(em, mc);
		for (ModeleContratDatePaiement datePaiement : datesInBase)
		{
			em.remove(datePaiement);
		}

		// Création de toutes les lignes pour chacune des dates
		if (modeleContrat.gestionPaiement!=GestionPaiement.NON_GERE)
		{
			List<Date> datePaiements = getAllDatePaiements(modeleContrat.premierCheque, modeleContrat.dernierCheque, modeleContrat.frequence,modeleContrat.dateRemiseCheque);
			for (Date datePaiement : datePaiements)
			{
				ModeleContratDatePaiement md = new ModeleContratDatePaiement();
				md.setModeleContrat(mc);
				md.setDatePaiement(datePaiement);
				em.persist(md);
			}
		}
		
	}
	
	
	
	// MISE A JOUR POUR BASE DE DEMO
	@DbWrite
	public void updateForDemo(DemoDateDTO demoDateDTO)
	{
		EntityManager em = TransactionHelper.getEm();
		
		// Mise à jour des contrats
		Query q = em.createQuery("select mc from ModeleContrat mc");
		List<ModeleContrat> mcs = q.getResultList();
		
		for (ModeleContrat mc : mcs)
		{
			updateForDemo(em, mc,demoDateDTO);
		}
		
		// Mise à jour des mots de passe
		PasswordManager passwordManager = new PasswordManager();
		q = em.createQuery("select u from Utilisateur u order by u.id");
		List<Utilisateur> us = q.getResultList();
		for (Utilisateur u : us)
		{
			passwordManager.setUserPassword(u.getId(), demoDateDTO.password);
		}
	
	}
	
	
	
	private void updateForDemo(EntityManager em, ModeleContrat mc, DemoDateDTO demoDateDTO)
	{
		ModeleContratDTO dto = loadModeleContrat(mc.getId());
		
		dto.dateFinInscription = demoDateDTO.dateFinInscription;
		dto.dateRemiseCheque = demoDateDTO.dateRemiseCheque;
		dto.dateDebut = demoDateDTO.dateDebut;
		dto.dateFin = demoDateDTO.dateFin;
		dto.premierCheque = demoDateDTO.premierCheque;
		dto.dernierCheque = demoDateDTO.dernierCheque;
		
		updateEnteteModeleContrat(dto);
		updateInfoPaiement(dto);
		updateDateModeleContrat(dto);
		
	}

	// Obtenir le montant total commandé pour un contrat
	public int getMontantCommande(EntityManager em, ModeleContrat mc)
	{
		Query q = em.createQuery("select sum(c.qte*c.modeleContratProduit.prix) " +
				"from ContratCell c " +
				"WHERE c.contrat.modeleContrat=:mc");
		q.setParameter("mc", mc);
		return BigDecimalUtils.toInt(q.getSingleResult());
	}
	
	
	// Obtenir le montant total des avoirs initiaux pour un contrat
	public int getMontantAvoir(EntityManager em, ModeleContrat mc)
	{
		Query q = em.createQuery("select sum(c.montantAvoir) " +
				"from Contrat c " +
				"WHERE c.modeleContrat=:mc");
		q.setParameter("mc", mc);
		return LongUtils.toInt(q.getSingleResult());
	}
	
	
	
	

}
