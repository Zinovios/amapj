package fr.amapj.service.services.gestioncontratsigne;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fr.amapj.common.LongUtils;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.contrat.modele.ModeleContratProduit;
import fr.amapj.model.models.contrat.reel.Contrat;
import fr.amapj.model.models.contrat.reel.ContratCell;
import fr.amapj.model.models.contrat.reel.EtatPaiement;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.mescontrats.MesContratsService;


/**
 * Permet la gestion des modeles de contrat
 * 
 *  
 *
 */
public class GestionContratSigneService
{
	
	public GestionContratSigneService()
	{
		
	}
	
	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES CONTRATS SIGNE
	
	
	/**
	 * Permet de charger la liste de tous les contrat signes
	 * dans une transaction en lecture
	 */
	@DbRead
	public List<ContratSigneDTO> getAllContratSigne(Long idModeleContrat)
	{
		EntityManager em = TransactionHelper.getEm();
		
		ModeleContrat mc = em.find(ModeleContrat.class, idModeleContrat);
		List<ContratSigneDTO> res = new ArrayList<ContratSigneDTO>();
		
		Query q = em.createQuery("select c from Contrat c "+
							"where c.modeleContrat=:mc "+
							"order by c.utilisateur.nom, c.utilisateur.prenom");
		q.setParameter("mc",mc);				
	
		List<Contrat> mcs = q.getResultList();
		
		for (Contrat contrat : mcs)
		{
			ContratSigneDTO mcInfo = createContratSigneInfo(em,contrat);
			res.add(mcInfo);
		}
		
		return res;
		
	}

	public ContratSigneDTO createContratSigneInfo(EntityManager em, Contrat contrat)
	{
		ContratSigneDTO info = new ContratSigneDTO();
		
		info.nomUtilisateur = contrat.getUtilisateur().getNom();
		info.prenomUtilisateur = contrat.getUtilisateur().getPrenom();
		info.idUtilisateur = contrat.getUtilisateur().getId();
		info.idContrat = contrat.getId();
		info.idModeleContrat = contrat.getModeleContrat().getId();
		info.dateCreation = contrat.getDateCreation();
		info.dateModification = contrat.getDateModification();
		info.mntCommande = getMontant(em,contrat);
		
		info.nbChequePromis = getNbCheque(em,contrat,EtatPaiement.A_FOURNIR);
		info.nbChequeRecus = getNbCheque(em,contrat,EtatPaiement.AMAP);
		info.nbChequeRemis = getNbCheque(em,contrat,EtatPaiement.PRODUCTEUR);
		
		int mntChequeRemis = getMontantChequeRemis(em,contrat);
		info.mntAvoirInitial = contrat.getMontantAvoir();
		info.mntSolde = info.mntAvoirInitial+mntChequeRemis-info.mntCommande;
		
		
				
		return info;
	}
	
	private int getMontantChequeRemis(EntityManager em, Contrat contrat)
	{
		Query q = em.createQuery("select sum(p.montant) from Paiement p WHERE p.etat=:etat and p.contrat=:c");
		q.setParameter("c", contrat);
		q.setParameter("etat", EtatPaiement.PRODUCTEUR);
		
		return LongUtils.toInt(q.getSingleResult());
	}

	private int getNbCheque(EntityManager em, Contrat contrat,EtatPaiement etatPaiement)
	{
		Query q = em.createQuery("select count(p) from Paiement p WHERE p.etat=:etat and p.contrat=:c");
		q.setParameter("etat", etatPaiement);
		q.setParameter("c", contrat);
		
		return ((Long) q.getSingleResult()).intValue();
	}


	/**
	 * TODO : récupérer tout ca en une seule requete 
	 * 
	 */
	public int getMontant(EntityManager em, Contrat contrat)
	{
		List<ContratCell> cells = new MesContratsService().getAllQte(em,contrat);
		
		int montant = 0;
		for (ContratCell cell : cells)
		{
			montant = montant+cell.getQte()*cell.getModeleContratProduit().getPrix();
		}
		return montant;
	}
	
	
	
	
	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES CONTRATS MODLEE D'UN PRODUCTEUR
	
	
	/**
	 * Permet de charger la liste de tous les modeles de contrats
	 * dans une transaction en lecture
	 */
	@DbRead
	public List<ModeleContrat> getModeleContrat(Long idProducteur)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Producteur prod = em.find(Producteur.class, idProducteur);
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ModeleContrat> cq = cb.createQuery(ModeleContrat.class);
		
		Root<ModeleContrat> root = cq.from(ModeleContrat.class);
		
		// On ajoute la condition where 
		cq.where(cb.equal(root.get(ModeleContrat.P.PRODUCTEUR.prop()),prod));	
		
				
		List<ModeleContrat> mcs = em.createQuery(cq).getResultList();
		
		return mcs;
	}
	
	
	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES UTILISATEURS QUI N'ONT PAS DE CONTRAT SUR UN MODELE 
	
	/**
	 * Permet de charger la liste des utilisateurs sans ce contrat
	 * dans une transaction en lecture
	 */
	@DbRead
	public List<Utilisateur> getUtilisateurSansContrat(Long idModeleContrat)
	{
		EntityManager em = TransactionHelper.getEm();

		ModeleContrat mc = em.find(ModeleContrat.class, idModeleContrat);
		
		Query q = em.createQuery("select u from Utilisateur u WHERE NOT EXISTS (select c from Contrat c where c.utilisateur = u and c.modeleContrat=:mc) ORDER BY u.nom,u.prenom");
		q.setParameter("mc",mc);
		List<Utilisateur> us = q.getResultList();
		return us;
	}
	
	
	// PARTIE REQUETAGE POUR AVOIR UNIQUEMENT LES INFORMATIONS SUR LES AVOIRS
	/**
	 * Retourne la liste des avoirs
	 * @return
	 */
	public List<ContratSigneDTO> getAvoirsInfo(EntityManager em,Long idModeleContrat)
	{
		ModeleContrat mc = em.find(ModeleContrat.class, idModeleContrat);
		List<ContratSigneDTO> res = new ArrayList<ContratSigneDTO>();
		
		Query q = em.createQuery("select c from Contrat c "+
							"where c.modeleContrat=:mc and "+
							" c.montantAvoir>0 "+
							"order by c.utilisateur.nom, c.utilisateur.prenom");
		q.setParameter("mc",mc);				
	
		List<Contrat> mcs = q.getResultList();
		
		for (Contrat contrat : mcs)
		{
			ContratSigneDTO mcInfo = createAvoirInfo(em,contrat);
			res.add(mcInfo);
		}
		
		return res;
		
	}

	public ContratSigneDTO createAvoirInfo(EntityManager em, Contrat contrat)
	{
		ContratSigneDTO info = new ContratSigneDTO();
		
		info.nomUtilisateur = contrat.getUtilisateur().getNom();
		info.prenomUtilisateur = contrat.getUtilisateur().getPrenom();
		info.mntAvoirInitial = contrat.getMontantAvoir();
		
				
		return info;
	}

	/*
	 * Gestion des annulations des dates de livraisons
	 */
	@DbRead
	public AnnulationDateLivraisonDTO getAnnulationDateLivraisonDTO(Long mcId)
	{
		EntityManager em = TransactionHelper.getEm();
		
		AnnulationDateLivraisonDTO dto = new AnnulationDateLivraisonDTO();
		dto.mcId = mcId;
		
		ModeleContrat mc = em.find(ModeleContrat.class, mcId);
		
		List<ModeleContratDate> dates = new GestionContratService().getAllDates(em, mc);

		
		// La premiere date proposée est la première date du contrat dans le futur
		for (ModeleContratDate modeleContratDate : dates)
		{
			if (modeleContratDate.getDateLiv().after(new Date()))
			{
				dto.dateDebut = modeleContratDate.getDateLiv();
				break;
			}
		}
		

		// La dernière date proposée est la dernière date du contrat , si on est arrivé à calculer une date de début
		if ( (dto.dateDebut!=null) && (dates.size() >= 1))
		{
			dto.dateFin = dates.get(dates.size() - 1).getDateLiv();
		}
		return dto;
	}
	
	@DbRead
	public String getAnnulationInfo(final AnnulationDateLivraisonDTO annulationDto)
	{
		EntityManager em = TransactionHelper.getEm();
		
		StringBuffer buf = new StringBuffer();
		
		ModeleContrat mc = em.find(ModeleContrat.class, annulationDto.mcId);
		
		// On selectionne toutes les dates de livraison
		Query q = em.createQuery("select d from ModeleContratDate d where "+
				" d.modeleContrat=:mc and "+
				" d.dateLiv >= :debut and "+
				" d.dateLiv <= :fin");
		
		
		q.setParameter("mc",mc);	
		q.setParameter("debut",annulationDto.dateDebut);
		q.setParameter("fin",annulationDto.dateFin);
		
		List<ModeleContratDate> mcds = q.getResultList();
		
		SimpleDateFormat df = new SimpleDateFormat("EEEEE dd MMMMM yyyy");
		buf.append("Les quantités des "+mcds.size()+" dates de livraisons suivantes vont être mises à zéro:<br/>");
		for (ModeleContratDate modeleContratDate : mcds)
		{
			buf.append(" - "+df.format(modeleContratDate.getDateLiv())+"<br/>");	
		}
		buf.append("<br/>");
		
		q = em.createQuery("select sum(c.qte),c.modeleContratProduit from ContratCell c where "+
				" c.contrat.modeleContrat=:mc and "+
				" c.modeleContratDate.dateLiv >= :debut and "+
				" c.modeleContratDate.dateLiv <= :fin "+
				" group by c.modeleContratProduit "+
				" order by c.modeleContratProduit.indx");
		
		
		q.setParameter("mc",mc);	
		q.setParameter("debut",annulationDto.dateDebut);
		q.setParameter("fin",annulationDto.dateFin);
		
		List<Object[]> qtes = q.getResultList();
		buf.append("Les quantités suivantes vont être mises à zéro: ( "+qtes.size()+" produits)<br/>");
		for (Object[] qte : qtes)
		{
			Produit prod = ((ModeleContratProduit) qte[1]).getProduit();
			buf.append(" - "+qte[0]+" "+prod.getNom()+" , "+prod.getConditionnement()+"<br/>");
		}
		buf.append("<br/>");
		
		
		
		q = em.createQuery("select distinct(c.contrat.utilisateur) from ContratCell c where "+
				" c.contrat.modeleContrat=:mc and "+
				" c.modeleContratDate.dateLiv >= :debut and "+
				" c.modeleContratDate.dateLiv <= :fin "+
				" order by c.contrat.utilisateur.nom, c.contrat.utilisateur.prenom");
		
		
		q.setParameter("mc",mc);	
		q.setParameter("debut",annulationDto.dateDebut);
		q.setParameter("fin",annulationDto.dateFin);
		
		List<Utilisateur> utilisateurs = q.getResultList();
		buf.append("Les "+utilisateurs.size()+" utilisateurs suivants sont impactés par cette suppression:<br/>");
		for (Utilisateur utilisateur : utilisateurs)
		{
			buf.append(" - "+utilisateur.getNom()+" "+utilisateur.getPrenom()+"<br/>");	
		}
		buf.append("<br/>");
		
		buf.append("Liste des adresses e-mail :<br/>");
		for (Utilisateur utilisateur : utilisateurs)
		{
			buf.append(utilisateur.getEmail()+";");	
		}
		buf.append("<br/><br/>");
		
		
		
		
		return buf.toString();
	}
	
	
	
	@DbWrite
	public void performAnnulationDateLivraison(final AnnulationDateLivraisonDTO annulationDto)
	{
		EntityManager em = TransactionHelper.getEm();
		
		ModeleContrat mc = em.find(ModeleContrat.class, annulationDto.mcId);
		
		// On selectionne toutes les cellules, puis on les supprime
		Query q = em.createQuery("select c from ContratCell c where "+
				" c.contrat.modeleContrat=:mc and "+
				" c.modeleContratDate.dateLiv >= :debut and "+
				" c.modeleContratDate.dateLiv <= :fin");
		
		
		q.setParameter("mc",mc);		
		q.setParameter("debut",annulationDto.dateDebut);
		q.setParameter("fin",annulationDto.dateFin);
		
		
		List<ContratCell> mcs = q.getResultList();
		for (ContratCell contratCell : mcs)
		{
			// On supprime la cellule dans la base devenue inutile
			em.remove(contratCell);
		}
	}
	
	
	
	
}
