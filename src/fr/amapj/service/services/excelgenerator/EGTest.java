package fr.amapj.service.services.excelgenerator;

import java.io.IOException;

import javax.persistence.EntityManager;

import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelFormat;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorTool;


/**
 * Test pour le calcul des hauteurs de ligne
 * 
 *  
 *
 */
public class EGTest extends AbstractExcelGenerator
{
	
	
	public EGTest()
	{
	}
	
	@Override
	public void fillExcelFile(EntityManager em,ExcelGeneratorTool et)
	{
		
		// Calcul du nombre de colonnes :  Nom + prénom + 1 montant de l'avoir
		et.addSheet("Avoirs", 3, 20);
				

		// Création de la ligne titre des colonnes
		et.addRow();
		et.setCell(0,"Nom",et.grasCentreBordure);
		et.setCell(1,"Prénom",et.grasCentreBordure);
		et.setCell(2,"Montant avoir",et.grasCentreBordure);
		

		addRow(et);

		
		

		

	}

	private void addRow( ExcelGeneratorTool et)
	{
		et.addRow();
		
		String cellValue = "court"; 
		cellValue = "un tres long message qui a besoin de plusieurs lignes pour s'afficher";
		
		et.setCell(0,cellValue,et.grasCentreBordure);
		et.setCell(1,"yyy",et.grasCentreBordure);
		et.setCellPrix(2,35,et.prixCentreBordure);
		
		/*int width = et.currentRow.getSheet().getColumnWidth(0)/64;
		
		CalculateSize cal = new CalculateSize();
		String fontName = "Arial";
		int fontSize = 10;
		
		System.out.println("width = "+width);
		System.out.println("height = "+cal.getHeight(cellValue, width, fontName, fontSize));
		*/
	}


	
	
	
	

	@Override
	public String getFileName(EntityManager em)
	{
		return "essai";
	}

	@Override
	public String getNameToDisplay(EntityManager em)
	{
		return "essai";
	}
	
	@Override
	public ExcelFormat getFormat()
	{
		return ExcelFormat.XLS;
	}

	public static void main(String[] args) throws IOException
	{
		new EGTest().test();
	}

}
