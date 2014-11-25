package fr.amapj.service.services.meslivraisons;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.time.DateUtils;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.contrat.reel.ContratCell;
import fr.amapj.model.models.distribution.DatePermanence;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.saisiepermanence.PermanenceService;
import fr.amapj.service.services.session.SessionManager;

/**
 * Permet la gestion des modeles de contrat
 * 
 *  
 *
 */
public class MesLivraisonsService
{
	private final static Logger logger = Logger.getLogger(MesLivraisonsService.class.getName());

	public MesLivraisonsService()
	{

	}

	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES LIVRAISONS POUR UN UTILISATEUR

	/**
	 * Permet de charger la liste de tous les modeles de contrats
	 * dans une transaction en lecture
	 */
	@DbRead
	public MesLivraisonsDTO getMesLivraisons(Date d)
	{
		EntityManager em = TransactionHelper.getEm();

		MesLivraisonsDTO res = new MesLivraisonsDTO();

		Utilisateur user = em.find(Utilisateur.class, SessionManager.getUserId());

		res.dateDebut = fr.amapj.common.DateUtils.firstMonday(d);
		res.dateFin = DateUtils.addDays(res.dateDebut,6);
		
		// On récupère ensuite la liste de tous les cellules de contrats de cet utilisateur dans cet intervalle
		List<ContratCell> cells = getAllQte(em, res.dateDebut,res.dateFin,user);
		
		//
		for (ContratCell cell : cells)
		{
			addCell(cell,res);
		}
		
		// On récupère ensuite la liste de toutes les distributions de cet utilisateur dans cet intervalle
		List<DatePermanence> dds = getAllDistributionsForUtilisateur(em, res.dateDebut,res.dateFin,user);
		for (DatePermanence dd : dds)
		{
			addDistribution(em,dd,res);
		}
		
		
		return res;

	}
	
	
	private List<DatePermanence> getAllDistributionsForUtilisateur(EntityManager em, Date dateDebut, Date dateFin,Utilisateur utilisateur)
	{
		Query q = em.createQuery("select distinct(du.datePermanence) from DatePermanenceUtilisateur du WHERE " +
				"du.datePermanence.datePermanence>=:deb and " +
				"du.datePermanence.datePermanence<=:fin and " +
				"du.utilisateur=:user " +
				"order by du.datePermanence.datePermanence");
		q.setParameter("deb", dateDebut, TemporalType.DATE);
		q.setParameter("fin", dateFin, TemporalType.DATE);
		q.setParameter("user", utilisateur);
		
		List<DatePermanence> dds = q.getResultList();
		
		return dds;
	}
	
	private void addDistribution(EntityManager em,DatePermanence dd, MesLivraisonsDTO res)
	{
		JourLivraisonsDTO jour = findJour(dd.getDatePermanence(),res);
		jour.distribution = new PermanenceService().createDistributionDTO(em, dd);
	}
	

	
	
	private void addCell(ContratCell cell, MesLivraisonsDTO res)
	{
		JourLivraisonsDTO jour = findJour(cell.getModeleContratDate().getDateLiv(),res);
		ProducteurLivraisonsDTO producteurs = findProducteurLivraison(cell.getModeleContratDate(),cell.getModeleContratDate().getModeleContrat(),jour);
		
		QteProdDTO qteProdDTO = findQteProdDTO(producteurs.produits,cell);
		qteProdDTO.qte = qteProdDTO.qte+cell.getQte();
	}

	private QteProdDTO findQteProdDTO(List<QteProdDTO> produits, ContratCell cell)
	{
		for (QteProdDTO qteProdDTO : produits)
		{
			if (qteProdDTO.idProduit.equals(cell.getModeleContratProduit().getProduit().getId()))
			{
				return qteProdDTO;
			}
		}
		QteProdDTO qteProdDTO = new QteProdDTO();
		qteProdDTO.conditionnementProduit = cell.getModeleContratProduit().getProduit().getConditionnement();
		qteProdDTO.nomProduit = cell.getModeleContratProduit().getProduit().getNom();
		qteProdDTO.idProduit = cell.getModeleContratProduit().getProduit().getId();
		
		produits.add(qteProdDTO);
		
		return qteProdDTO;
	}

	private JourLivraisonsDTO findJour(Date dateLiv, MesLivraisonsDTO res)
	{
		for (JourLivraisonsDTO jour : res.jours)
		{
			if (jour.date.equals(dateLiv))
			{
				return jour;
			}
		}
		
		JourLivraisonsDTO jour = new JourLivraisonsDTO();
		jour.date = dateLiv;
		res.jours.add(jour);
		
		return jour;
		
	}
	
	
	

	private ProducteurLivraisonsDTO findProducteurLivraison(ModeleContratDate modeleContratDate, ModeleContrat modeleContrat, JourLivraisonsDTO jour)
	{
		for (ProducteurLivraisonsDTO producteur : jour.producteurs)
		{
			if (producteur.idModeleContrat.equals(modeleContrat.getId()))
			{
				return producteur;
			}
		}
		ProducteurLivraisonsDTO producteur = new ProducteurLivraisonsDTO();
		producteur.producteur = modeleContrat.getProducteur().getNom();
		producteur.modeleContrat = modeleContrat.getNom();
		producteur.idModeleContrat = modeleContrat.getId();
		producteur.idModeleContratDate = modeleContratDate.getId();
		jour.producteurs.add(producteur);
		
		return producteur;
	}

	/**
	 * 
	 */
	private List<ContratCell> getAllQte(EntityManager em, Date dateDebut, Date dateFin, Utilisateur user)
	{
		Query q = em.createQuery("select c from ContratCell c WHERE " +
				"c.modeleContratDate.dateLiv>=:deb AND " +
				"c.modeleContratDate.dateLiv<=:fin and " +
				"c.contrat.utilisateur =:user " +
				"order by c.modeleContratDate.dateLiv, c.contrat.modeleContrat.producteur.id, c.contrat.modeleContrat.id , c.modeleContratProduit.indx");
		q.setParameter("deb", dateDebut, TemporalType.DATE);
		q.setParameter("fin", dateFin, TemporalType.DATE);
		q.setParameter("user", user);
		
		List<ContratCell> prods = q.getResultList();
		return prods;
	}
	
	
	
	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES LIVRAISONS POUR UN PRODUCTEUR

	/**
	 * Retourne la liste des livraisons pour le producteur spécifié 
	 */
	@DbRead
	public MesLivraisonsDTO getLivraisonProducteur(Date d,Long idProducteur)
	{
		EntityManager em = TransactionHelper.getEm();
		
		MesLivraisonsDTO res = new MesLivraisonsDTO();

		Producteur producteur = em.find(Producteur.class, idProducteur);

		res.dateDebut = fr.amapj.common.DateUtils.firstMonday(d);
		res.dateFin = DateUtils.addDays(res.dateDebut,6);
		
		// On récupère ensuite la liste de tous les cellules de contrats de cet utilisateur dans cet intervalle
		List<ContratCell> cells = getAllQte(em, res.dateDebut,res.dateFin,producteur);
		
		//
		for (ContratCell cell : cells)
		{
			addCell(cell,res);
		}
		
		return res;

	}
	
	
	/**
	 * 
	 */
	private List<ContratCell> getAllQte(EntityManager em, Date dateDebut, Date dateFin, Producteur producteur)
	{
		Query q = em.createQuery("select c from ContratCell c " +
				"WHERE c.modeleContratDate.dateLiv>=:deb AND " +
				"c.modeleContratDate.dateLiv<=:fin and " +
				"c.contrat.modeleContrat.producteur =:prod " +
				"order by c.modeleContratDate.dateLiv, c.contrat.modeleContrat.producteur.id, c.contrat.modeleContrat.id , c.modeleContratProduit.indx");
		q.setParameter("deb", dateDebut, TemporalType.DATE);
		q.setParameter("fin", dateFin, TemporalType.DATE);
		q.setParameter("prod", producteur);
		
		List<ContratCell> prods = q.getResultList();
		return prods;
	}
	
	

	
}
