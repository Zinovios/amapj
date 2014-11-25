package fr.amapj.service.services.demoservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import fr.amapj.common.DateUtils;
import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.model.engine.transaction.Call;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.engine.transaction.NewTransaction;
import fr.amapj.model.models.contrat.modele.EtatModeleContrat;
import fr.amapj.model.models.contrat.modele.GestionPaiement;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.LigneContratDTO;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.view.views.gestioncontrat.editorpart.FrequenceLivraison;

/**
 * Ce service permet de creer des données pour la base de démonstration (création de contrat)
 * 
 * 
 */
public class DemoService
{
	public DemoService()
	{

	}
	
	@DbWrite
	public void generateDemoData(Date dateDebut, Date dateFin, Date dateFinInscription)
	{
		EntityManager em = TransactionHelper.getEm();
		
		createContrat(em, "PRODUITS LAITIERS de VACHE", "lait, yaourts, faisselle, crème fraîche", 3011L, dateFinInscription, dateDebut, dateFin);

		createContrat(em, "PAIN", "pains complet, campagne ou seigle", 3029L, dateFinInscription, dateDebut, dateFin);

		createContrat(em, "PRODUITS LAITIERS de CHEVRE", "fromages, yaourts, faisselles, savons", 3002L, dateFinInscription, dateDebut, dateFin);

		createContrat(em, "PRODUITS LAITIERS de BREBIS", "lait, yaourts, et fromages de brebis", 3019L, dateFinInscription, dateDebut, dateFin);

		setAllContratActifs();
	}

	
	/**
	 * Positionne tous les contrats actifs
	 */
	private void setAllContratActifs()
	{
		GestionContratService service = new GestionContratService();
		List<ModeleContratSummaryDTO> modeles = service.getModeleContratInfo();
		for (ModeleContratSummaryDTO dto : modeles)
		{
			service.updateEtat(EtatModeleContrat.ACTIF, dto.id);
		}
		
	}

	private void createContrat(EntityManager em, String nom, String description, Long idProducteur, Date dateFinInscription, Date dateDebut, Date dateFin)
	{
		ModeleContratDTO modeleContrat = new ModeleContratDTO();
		modeleContrat.nom = nom;
		modeleContrat.description = description;
		modeleContrat.producteur = idProducteur;
		modeleContrat.dateFinInscription = dateFinInscription;
		modeleContrat.frequence = FrequenceLivraison.UNE_FOIS_PAR_SEMAINE;
		modeleContrat.gestionPaiement = GestionPaiement.GESTION_STANDARD;

		modeleContrat.dateDebut = dateDebut;
		modeleContrat.dateFin = dateFin;

		modeleContrat.produits = getProduits(idProducteur.intValue(), em);

		modeleContrat.libCheque = em.find(Producteur.class, idProducteur).getNom().toLowerCase();
		modeleContrat.dateRemiseCheque = dateFinInscription;
		modeleContrat.premierCheque = DateUtils.firstDayInMonth(modeleContrat.dateDebut);
		modeleContrat.dernierCheque = DateUtils.firstDayInMonth(modeleContrat.dateFin);
		
		new GestionContratService().saveNewModeleContrat(modeleContrat);

	}

	private List<LigneContratDTO> getProduits(int idProducteur, EntityManager em)
	{
		List<LigneContratDTO> res = new ArrayList<>();

		switch (idProducteur)
		{
		// VACHE
		case 3011:
			add(res, em, 120, 3012);
			add(res, em, 200, 3013);
			add(res, em, 390, 3014);
			add(res, em, 250, 3015);
			add(res, em, 340, 3016);
			add(res, em, 205, 3017);
			break;

		// PAIN
		case 3029:
			add(res, em, 400, 3030);
			add(res, em, 400, 3031);
			add(res, em, 400, 3032);
			add(res, em, 400, 3033);
			add(res, em, 400, 3034);
			add(res, em, 400, 3035);
			break;
		// CHEVRE
		case 3002:

			add(res, em, 130, 3003);
			add(res, em, 130, 3004);
			add(res, em, 130, 3005);
			add(res, em, 240, 3006);
			add(res, em, 70, 3007);
			add(res, em, 80, 3008);
			add(res, em, 350, 3009);
			add(res, em, 440, 3010);
			break;
		// BREBIS
		case 3019:
			add(res, em, 350, 3020);
			add(res, em, 250, 3023);
			add(res, em, 270, 3024);
			add(res, em, 160, 3025);
			add(res, em, 1500, 3026);
			add(res, em, 750, 3027);
			add(res, em, 375, 3028);
			add(res, em, 1000, 4340);
			add(res, em, 500, 4341);
			break;
			
		default:
			break;
		}

		return res;
	}

	private void add(List<LigneContratDTO> res, EntityManager em, int prix, int idProduit)
	{
		LigneContratDTO dto = new LigneContratDTO();

		dto.prix = prix;
		dto.produitId = new Long(idProduit);

		res.add(dto);

	}
	
	public static void main(String[] args) throws ParseException
	{
		TestTools.init();
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		
		Date dateDebut = df.parse("24/07/14");
		Date dateFin = df.parse("25/09/14");
		
		Date dateFinInscription = df.parse("17/09/14");
		
		
		
		System.out.println("Debut de la generation ...");
		new DemoService().generateDemoData(dateDebut, dateFin, dateFinInscription);
		System.out.println("Fin de la generation.");
	}

}
