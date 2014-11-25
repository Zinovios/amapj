package fr.amapj.service.services.excelgenerator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

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
import fr.amapj.service.services.mescontrats.MesContratsService;


/**
 * Permet l'impression d'un contrat pour un utilisateur
 * 
 *  
 *
 */
public class EGContratUtilisateur  extends AbstractExcelGenerator
{
	
	Long contratId;
	
	public EGContratUtilisateur(Long contratId)
	{
		this.contratId = contratId;
	}
	
	@Override
	public void fillExcelFile(EntityManager em,ExcelGeneratorTool et)
	{	
		Contrat c = em.find(Contrat.class, contratId);
		
		ModeleContrat mc = c.getModeleContrat();
		
		// Avec une sous requete, on récupere la liste des produits
		List<ModeleContratProduit> prods = new GestionContratService().getAllProduit(em, mc);
		
		// Avec une sous requete, on obtient la liste de toutes les dates de livraison, trièes par ordre croissant 
		List<ModeleContratDate> dates = new GestionContratService().getAllDates(em, mc);

		// On charge ensuite le contrat
		ContratDTO contratDTO = new MesContratsService().loadContrat(mc.getId(), c.getId());
		
		// Nombre de colonnes fixe à gauche
		int nbColGauche = 3;
			
		// Calcul du nombre de colonnes 
		int nbColTotal = nbColGauche+prods.size();
		
		
		// Construction de la feuille et largeur des colonnes
		et.addSheet("AMAP", nbColTotal, 10);
	    et.setColumnWidth(0, 20);
	    et.setColumnWidth(1, 2);
	    
	    // Génération du titre 
	    List<String> titres = new ArrayList<>();
	    titres.add("");
	    
	    String firstLine = "Contrat de "+c.getUtilisateur().getPrenom()+" "+c.getUtilisateur().getNom();
	    int nbLine = dates.size();

		// Construction de l'entete
		new EGSyntheseContrat(null).contructEntete(et,mc,firstLine,titres,prods,nbLine,nbColGauche,"Dates","");
		
		SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
		
		// Contruction d'une ligne pour chaque date
		for (int i = 0; i < dates.size(); i++)
		{			 
			// Construction de la ligne
			contructRow(et,contratDTO,nbColGauche,i,dates.get(i),df2,prods.size());
		}	
	}

		


	/**
	 * Construction des lignes 
	 * 
	 */
	private void contructRow(ExcelGeneratorTool et, ContratDTO contratDto, int nbColGauche, int dateIndex,ModeleContratDate date, SimpleDateFormat df2,int nbProd)
	{
		et.addRow();
		
		// Colonne 0  : la date
		et.setCell(0,df2.format(date.getDateLiv()),et.grasCentreBordure);
		
		// Colonne 1 - Vide
		et.setCell(1,"",et.grasGaucheNonWrappeBordure);

		// Colonne 2 - cumul pour cet utilisateur 
		et.setCellSumProdInRow(2, 3, nbProd, 7, et.prixCentreBordure);
		
		// Affectation des quantités
		int index =nbColGauche;
		
		// On itere sur les produits
		for (int j = 0; j < contratDto.contratColumns.size(); j++)
		{	
			et.setCellQte(index, contratDto.qte[dateIndex][j], et.nonGrasCentreBordure);
			index++;
		}
	}


	@Override
	public String getFileName(EntityManager em)
	{
		Contrat c = em.find(Contrat.class, contratId);
		Utilisateur u = c.getUtilisateur();
		return "contrat-"+c.getModeleContrat().getNom()+"-"+u.getNom()+" "+u.getPrenom();
	}


	@Override
	public String getNameToDisplay(EntityManager em)
	{
		Contrat c = em.find(Contrat.class, contratId);
		Utilisateur u = c.getUtilisateur();
		return "le contrat "+c.getModeleContrat().getNom()+" pour "+u.getNom()+" "+u.getPrenom();
	}
	
	@Override
	public ExcelFormat getFormat()
	{
		return ExcelFormat.XLS;
	}
	
	

	public static void main(String[] args) throws IOException
	{
		new EGContratUtilisateur(8342L).test(); 
	}

}
