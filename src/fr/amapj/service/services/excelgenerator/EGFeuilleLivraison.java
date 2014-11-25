package fr.amapj.service.services.excelgenerator;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;

import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelFormat;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorTool;


/**
 * Permet la generation des feuilles de livraisons
 * 
 *  
 *
 */
public class EGFeuilleLivraison extends AbstractExcelGenerator
{
	//
	Long modeleContratId;
	

	// Si ce champ est null, alors on prend en compte toutes les dates de livraisons
	Long modeleContratDateId; 
	
	public EGFeuilleLivraison(Long modeleContratId,Long modeleContratDateId)
	{
		this.modeleContratId = modeleContratId;
		this.modeleContratDateId = modeleContratDateId;
	}
	
	public EGFeuilleLivraison(Long modeleContratId)
	{
		this(modeleContratId,null);
	}
	
	
	@Override
	public void fillExcelFile(EntityManager em,ExcelGeneratorTool et)
	{
		new EGSyntheseContrat(modeleContratId).fillExcelFile(em, et, false,modeleContratDateId);
	}
	
	@Override
	public String getFileName(EntityManager em)
	{
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		if (modeleContratDateId==null)
		{
			return "distri-"+mc.getNom();
		}
		else
		{
			ModeleContratDate date = em.find(ModeleContratDate.class, modeleContratDateId);
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			return "distri-"+mc.getNom()+"-"+df.format(date.getDateLiv());
		}
	}


	@Override
	public String getNameToDisplay(EntityManager em)
	{
		if (modeleContratDateId==null)
		{
			return "toutes les feuilles de distribution";
		}
		else
		{
			ModeleContratDate date = em.find(ModeleContratDate.class, modeleContratDateId);
			SimpleDateFormat df = new SimpleDateFormat("dd MMMMM yyyy");
			return "la feuille de distribution du "+df.format(date.getDateLiv());
		}
	}
	
	@Override
	public ExcelFormat getFormat()
	{
		return ExcelFormat.XLS;
	}
	
	
	public static void main(String[] args) throws IOException
	{
		//new EGFeuilleLivraison(4342L,4409L).test();
		new EGFeuilleLivraison(4342L).test();
	}


}
