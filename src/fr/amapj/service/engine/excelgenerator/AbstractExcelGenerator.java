package fr.amapj.service.engine.excelgenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.amapj.model.engine.db.DbManager;
import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.model.engine.transaction.Call;
import fr.amapj.model.engine.transaction.NewTransaction;
import fr.amapj.view.engine.ui.AppConfiguration;


/**
 * Permet la gestion des extractions excels
 * 
 *  
 *
 */
abstract public class AbstractExcelGenerator
{
	
	
	/**
	 * Permet de générer le fichier Excel pour un modele de contrat
	 * @return
	 */
	abstract public void fillExcelFile(EntityManager em,ExcelGeneratorTool et);
	
	abstract public String getFileName(EntityManager em);
	
	abstract public String getNameToDisplay(EntityManager em);
	
	abstract public ExcelFormat getFormat();
	
	
	public void test() throws IOException
	{
		TestTools.init();
		
		String filename = "test."+this.getFormat().name().toLowerCase();
		Workbook workbook = new ExcelGeneratorService().getFichierExcel(this); 
		
		FileOutputStream fileOut = new FileOutputStream(filename);
		workbook.write(fileOut);
		fileOut.close();
		System.out.println("Your excel file has been generated!");
	}
	
	
}
