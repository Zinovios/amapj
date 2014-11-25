package fr.amapj.service.services.excelgenerator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.contrat.modele.ModeleContratProduit;
import fr.amapj.model.models.contrat.reel.Contrat;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelFormat;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorTool;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.ContratLigDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;


/**
 * Permet la gestion des modeles de contrat
 * 
 *  
 *
 */
public class EGSyntheseContrat  extends AbstractExcelGenerator
{
	
	Long modeleContratId;
	
	public EGSyntheseContrat(Long modeleContratId)
	{
		this.modeleContratId = modeleContratId;
	}
	
	@Override
	public void fillExcelFile(EntityManager em,ExcelGeneratorTool et)
	{	
		fillExcelFile(em,et,true,null);
	}
	
	/**
	 * 
	 * @param em
	 * @param et
	 * @param oneSheet true si on veut tout sur une seule feuille, false si il y a une feuille par date
	 */
	public void fillExcelFile(EntityManager em,ExcelGeneratorTool et,boolean isSynthese,Long modeleContratDateId)
	{	
		
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		
		// Avec une sous requete, on récupere la liste des produits
		List<ModeleContratProduit> prods = new GestionContratService().getAllProduit(em, mc);
		
		// Avec une sous requete, on obtient la liste de toutes les dates de livraison, trièes par ordre croissant 
		List<ModeleContratDate> dates = new GestionContratService().getAllDates(em, mc);

		// Avec une sous requete, on obtient la liste de tous les utilisateur ayant commandé au moins un produit
		List<Utilisateur> utilisateurs = getUtilisateur(em, mc);
		
		// On charge ensuite la liste de tous les contrats pour chaque utilisateur
		Map<Utilisateur, ContratDTO> contrats = loadContrat(em,utilisateurs,mc);

		// Nombre de colonnes fixe à gauche
		int nbColGauche = 3;

		
		if (isSynthese)
		{
			performSheet(et,"SYNTHESE DU CONTRAT","Amap",mc,prods,dates,utilisateurs,nbColGauche,contrats);
		}
		else
		{
			SimpleDateFormat df = new SimpleDateFormat("dd MMMMM");
			SimpleDateFormat df3 = new SimpleDateFormat("dd MMMMM yyyy");
			
			// On itère sur chaque date, avec une feuille par date
			for (ModeleContratDate date : dates)
			{
				if (isAccordingDate(date,modeleContratDateId))
				{
					List<ModeleContratDate> ds = new ArrayList<>();
					ds.add(date);
					
					String firstLine = "FEUILLE DE LIVRAISON DU "+df3.format(date.getDateLiv());
					String sheetName = df.format(date.getDateLiv());
					
					performSheet(et,firstLine,sheetName,mc,prods,ds,utilisateurs,nbColGauche,contrats);
					
					// Suppression de la colonne avec les prix 
					et.setColHidden(2, true);
					
					// Ajustement pour tenir sur une seule page
					et.adjustSheetForOnePage();
				}
			}
			
			// Génération d'une page de cumul si on fait le classeur avec toutes les feuilles de distribution
			if (modeleContratDateId==null)
			{
				new EGTotalLivraison().fillExcelFile(em, et, prods, dates, mc);
			}
			
			
			
		}
	}

	/**
	 * Retourne si cette date fait parties des dates qui doivent être présentes dans le document 
	 * @param date
	 * @param modeleContratDateId
	 * @return
	 */
	private boolean isAccordingDate(ModeleContratDate date, Long modeleContratDateId)
	{
		if (modeleContratDateId==null)
		{
			return true;
		}
		return date.getId().equals(modeleContratDateId);
	}

	private void performSheet(ExcelGeneratorTool et, String firstLine,String nomFeuille, ModeleContrat mc, List<ModeleContratProduit> prods, List<ModeleContratDate> dates, List<Utilisateur> utilisateurs,
			int nbColGauche, Map<Utilisateur, ContratDTO> contrats)
	{
			
		// Calcul du nombre de colonnes :  (date / produit)
		int nbColDateProd = dates.size()*prods.size();
		
		int nbColTotal = nbColGauche+nbColDateProd;
		
		
		// Construction de la feuille et largeur des colonnes
		et.addSheet(nomFeuille, nbColTotal, 10);
	    et.setColumnWidth(0, 16);
	    et.setColumnWidth(1, 16);
	    
	    
	    // Génération du titre de chaque bloc (un bloc = une date)
	    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
	    List<String> titres = new ArrayList<>();
	    for (ModeleContratDate date : dates)
		{
			titres.add(df2.format(date.getDateLiv()));
		}
	    

		// Construction de l'entete
		contructEntete(et,mc,firstLine,titres,prods,utilisateurs.size(),nbColGauche,"Nom","Prénom");
		
		// Contruction d'une ligne pour chaque Utilisateur
		for (int i = 0; i < utilisateurs.size(); i++)
		{
			Utilisateur utilisateur = utilisateurs.get(i);
			
			// 
			ContratDTO contratDto = contrats.get(utilisateur); 
			
			// Construction de la ligne
			contructRow(et,contratDto,utilisateur,nbColDateProd,nbColGauche,dates);
		}	
	}



	public void contructEntete(ExcelGeneratorTool et, ModeleContrat mc, String firstLine,List<String> titres, List<ModeleContratProduit> prods, int nbClient, int nbColGauche,String c1,String c2)
	{
		SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		
		// Ligne 1 à 5
		et.addRow(firstLine,et.grasGaucheNonWrappe);
		et.addRow(mc.getNom(),et.grasGaucheNonWrappe);
		et.addRow(mc.getDescription(),et.grasGaucheNonWrappe);
		et.addRow("Extrait le "+df1.format(new Date()),et.grasGaucheNonWrappe);
		et.addRow("",et.grasGaucheNonWrappe);

				
		// Ligne 6 avec les dates
		et.addRow();
		et.setCell(0,c1,et.grasCentreBordure);
		et.setCell(1,c2,et.grasCentreBordure);
		et.setCell(2,"Montant total",et.grasCentreBordure);
		
		for (int i = 0; i < titres.size(); i++)
		{
			String titre = titres.get(i);
			int index = nbColGauche+i*prods.size(); 
			et.setCell(index,titre,et.switchColor(et.grasCentreBordure,i));
			et.mergeCellsRight(index, prods.size());
		}
		
		// Ligne 7 avec le nom du produit
		et.addRow();
		et.setRowHeigth(4);
		int i =0;
		for (int k = 0; k < titres.size(); k++)
		{
			for (ModeleContratProduit prod : prods)
			{
				int index = nbColGauche+i;
				et.setCell(index,prod.getProduit().getNom(),et.switchColor(et.grasCentreBordure,k));
				i++;
			}
		}
		
		// Ligne 8 avec le prix du produit
		et.addRow();
		i =0;
		for (int k = 0; k < titres.size(); k++)
		{
			for (ModeleContratProduit prod : prods)
			{
				int index = nbColGauche+i;
				et.setCellPrix(index,prod.getPrix(),et.switchColor(et.prixCentreBordure,k));
				i++;
			}
		}
		
		// Ligne 9 avec le conditionnement du produit
		et.addRow();
		et.setRowHeigth(6);
		et.mergeCellsUp(0, 4);
		et.mergeCellsUp(1, 4);
		et.mergeCellsUp(2, 4);
		i =0;
		for (int k = 0; k < titres.size(); k++)
		{
			for (ModeleContratProduit prod : prods)
			{
				int index = nbColGauche+i;
				et.setCell(index,prod.getProduit().getConditionnement(),et.switchColor(et.grasCentreBordure,k));
				i++;
			}
		}
			
		// Ligne 10 vide
		et.addRow();
		
		// Ligne 11 avec le cumul 
		et.addRow();
		et.setCell(0, "Cumul", et.grasGaucheNonWrappeBordure);
		et.setCell(1, "", et.grasGaucheNonWrappeBordure);
		et.setCellSumInColDown(2, 2, nbClient, et.prixCentreBordure);
				
		//
		i=0;
		for (int k = 0; k < titres.size(); k++)
		{
			for (ModeleContratProduit prod : prods)
			{
				int index=nbColGauche+i;
				et.setCellSumInColDown(index, 2, nbClient, et.switchColor(et.grasCentreBordure,k));
				i++;
			}
		}
		
		// Ligne 12 vide
		et.addRow();
	}

		


	/**
	 * Construction des lignes utilisateurs 
	 * 
	 * @param et
	 * @param contratDto
	 * @param utilisateur
	 * @param nbColDateProd
	 * @param nbColGauche
	 */
	private void contructRow(ExcelGeneratorTool et, ContratDTO contratDto, Utilisateur utilisateur,int nbColDateProd,int nbColGauche, List<ModeleContratDate> dates)
	{
		et.addRow();
		
		// Colonne 0 et 1 : le nom et prenom 
		et.setCell(0,utilisateur.getNom(),et.grasGaucheNonWrappeBordure);
		et.setCell(1,utilisateur.getPrenom(),et.nonGrasGaucheBordure);
		
		// Colonne 3 - cumul pour cet utilisateur 
		et.setCellSumProdInRow(2, 3, nbColDateProd, 7, et.prixCentreBordure);
		
		// Affectation des quantités
		int index =nbColGauche;
		
		// On itere sur les dates
		for (int k = 0; k < dates.size(); k++)
		{
			ModeleContratDate date = dates.get(k);
			int i = findIndex(date,contratDto.contratLigs);
			
			// On itere sur les produits
			for (int j = 0; j < contratDto.contratColumns.size(); j++)
			{	
				et.setCellQte(index, contratDto.qte[i][j], et.switchColor(et.nonGrasCentreBordure, k));
				index++;
			}
		}
	}

	private int findIndex(ModeleContratDate date, List<ContratLigDTO> contratLigs)
	{
		for (int i = 0; i < contratLigs.size(); i++)
		{
			ContratLigDTO contratLigDTO = contratLigs.get(i);
			if (contratLigDTO.modeleContratDateId.equals(date.getId()))
			{
				return i;
			}
		}
		return -1;
	}

	private List<Utilisateur> getUtilisateur(EntityManager em, ModeleContrat mc)
	{
		Query q = em.createQuery("select u from Utilisateur u WHERE EXISTS (select c from Contrat c where c.utilisateur = u and c.modeleContrat=:mc) ORDER BY u.nom,u.prenom");
		q.setParameter("mc",mc);
		List<Utilisateur> us = q.getResultList();
		return us;
	}

	
	
	private ContratDTO findContrat(EntityManager em,Utilisateur utilisateur,ModeleContrat mc)
	{
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Contrat> cq = cb.createQuery(Contrat.class);
		Root<Contrat> root = cq.from(Contrat.class);

		// On ajoute la condition where
		cq.where(cb.and(cb.equal(root.get(Contrat.P.UTILISATEUR.prop()), utilisateur),cb.equal(root.get(Contrat.P.MODELECONTRAT.prop()), mc)));
		
		List<Contrat> contrats = em.createQuery(cq).getResultList();
		if (contrats.size()==0)
		{
			throw new RuntimeException("Erreur inattendue");
		}
		if (contrats.size()>1)
		{
			throw new RuntimeException("L'utilisateur "+utilisateur.getNom()+" posséde plusieurs contrats !!");
		}
		
		Contrat contrat = contrats.get(0);
		
		return new MesContratsService().loadContrat(contrat.getModeleContrat().getId(), contrat.getId());
		
	}
	
	
	private Map<Utilisateur, ContratDTO> loadContrat(EntityManager em, List<Utilisateur> utilisateurs, ModeleContrat mc)
	{
		Map<Utilisateur, ContratDTO> res = new HashMap<>();
		for (Utilisateur utilisateur : utilisateurs)
		{
			ContratDTO dto = findContrat(em, utilisateur, mc);
			res.put(utilisateur, dto);
		}
		return res;
	}
	
	
	
	

	@Override
	public String getFileName(EntityManager em)
	{
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		return "synthese-"+mc.getNom();
	}


	@Override
	public String getNameToDisplay(EntityManager em)
	{
		return "la synthese du contrat en une page";
	}
	
	@Override
	public ExcelFormat getFormat()
	{
		return ExcelFormat.XLSX;
	}
	
	

	public static void main(String[] args) throws IOException
	{
		new EGSyntheseContrat(11252L).test(); 
	}

}
