package fr.amapj.service.services.mailer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.ss.usermodel.Workbook;

import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorService;
import fr.amapj.service.engine.excelgenerator.FileInfoDTO;

/**
 * Permet de stocker une piece jointe 
 * 
 *
 */
public class MailerAttachement
{

	private DataSource dataSource;
	
	private String name;
	
	
	public MailerAttachement()
	{
		
	}
	
	/**
	 * Permet de construire une pièce jointe à partir d'un fichier local
	 */
	public MailerAttachement(File file)
	{
		dataSource = new FileDataSource(file);
		name = file.getName();	
	}
	
	/**
	 * Permet de construire une pièce jointe à partir d'un fichier excel 
	 */	
	public MailerAttachement(AbstractExcelGenerator generator)
	{
		ExcelGeneratorService excelGeneratorService = new ExcelGeneratorService();

		Workbook workbook = excelGeneratorService.getFichierExcel(generator);
		FileInfoDTO fileInfo = excelGeneratorService.getFileInfo(generator);

		ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();

		try
		{
			workbook.write(imagebuffer);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Erreur inattendue");
		}

		dataSource = new ByteArrayDataSource(imagebuffer.toByteArray(), "application/vnd.ms-excel");
		name = fileInfo.fileName;
	}
	

	
	
	
	
	
	public DataSource getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	
	
	
	
	
	
}
