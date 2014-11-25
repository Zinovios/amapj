package fr.amapj.service.services.excelgenerator;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.remise.RemiseProducteur;
import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelFormat;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorTool;
import fr.amapj.service.services.remiseproducteur.PaiementRemiseDTO;
import fr.amapj.service.services.remiseproducteur.RemiseDTO;
import fr.amapj.service.services.remiseproducteur.RemiseProducteurService;


/**
 * Permet la generation de la feuille d'une remise
 * 
 *  
 *
 */
public class EGRemise extends AbstractExcelGenerator
{
	
	Long remiseId;
	
	public EGRemise(Long remiseId)
	{
		this.remiseId = remiseId;
	}
	
	@Override
	public void fillExcelFile(EntityManager em,ExcelGeneratorTool et)
	{
		RemiseProducteur remise = em.find(RemiseProducteur.class, remiseId);
		ModeleContrat mc = remise.getDatePaiement().getModeleContrat();
		RemiseDTO dto = new RemiseProducteurService().loadRemise(remiseId);
		
		// Calcul du nombre de colonnes :  Nom + prénom + 1 montant du chéque
		et.addSheet(dto.moisRemise, 3, 20);
				
		et.addRow("Remise de chèques du mois de "+dto.moisRemise,et.grasGaucheNonWrappe);
		et.addRow("",et.grasGaucheNonWrappe);
		
		et.addRow("Nom du contrat : "+mc.getNom(),et.grasGaucheNonWrappe);
		et.addRow("Nom du producteur : "+mc.getProducteur().getNom(),et.grasGaucheNonWrappe);
		et.addRow("Ordre des chèques : "+mc.getLibCheque(),et.grasGaucheNonWrappe);
	
		
		et.addRow(dto.paiements.size()+" chèques dans cette remise",et.grasGaucheNonWrappe);
		et.addRow("",et.grasGaucheNonWrappe);
		

		// Création de la ligne titre des colonnes
		et.addRow();
		et.setCell(0,"Nom",et.grasCentreBordure);
		et.setCell(1,"Prénom",et.grasCentreBordure);
		et.setCell(2,"Montant chèques",et.grasCentreBordure);
		
		
		// Une ligne pour chaque chèque 
		for (PaiementRemiseDTO paiementRemiseDTO : dto.paiements)
		{
			addRow(paiementRemiseDTO,et);
		}
		
		// Une ligne vide
		et.addRow("",et.grasGaucheNonWrappe);
		
		addRowCumul(et, dto.paiements.size());
		

	}

	private void addRow(PaiementRemiseDTO paiementRemiseDTO, ExcelGeneratorTool et)
	{
		et.addRow();
		et.setCell(0,paiementRemiseDTO.nomUtilisateur,et.grasGaucheNonWrappeBordure);
		et.setCell(1,paiementRemiseDTO.prenomUtilisateur,et.nonGrasGaucheBordure);
		et.setCellPrix(2,paiementRemiseDTO.montant,et.prixCentreBordure);
	}


	
	
	private void addRowCumul(ExcelGeneratorTool et, int nbPaiements)
	{
		et.addRow();
		
		et.setCell(0,"Total",et.grasGaucheNonWrappeBordure);
		et.setCell(1,"",et.nonGrasGaucheBordure);
		et.setCellSumInColUp(2, 2, nbPaiements, et.prixCentreBordure);
	}

	

	@Override
	public String getFileName(EntityManager em)
	{
		RemiseProducteur remise = em.find(RemiseProducteur.class, remiseId);
		ModeleContrat mc = remise.getDatePaiement().getModeleContrat();
		return "remise-"+mc.getNom();
	}

	@Override
	public String getNameToDisplay(EntityManager em)
	{
		return "la feuille de remise des chèques au producteur";
	}
	
	@Override
	public ExcelFormat getFormat()
	{
		return ExcelFormat.XLS;
	}

	public static void main(String[] args) throws IOException
	{
		new EGRemise(12652L).test();
	}

}
