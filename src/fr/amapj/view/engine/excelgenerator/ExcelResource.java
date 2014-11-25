package fr.amapj.view.engine.excelgenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;

import com.vaadin.server.StreamResource;

import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorService;


public class ExcelResource implements StreamResource.StreamSource
{

	
	AbstractExcelGenerator generator;

	public ExcelResource(AbstractExcelGenerator generator)
	{
		this.generator = generator;
	}

	@Override
	public InputStream getStream()
	{
		
		Workbook workbook = new ExcelGeneratorService().getFichierExcel(generator);
		
		ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
	
		try
		{
			workbook.write(imagebuffer);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Erreur inattendue");
		}
		return new ByteArrayInputStream(imagebuffer.toByteArray());
	}

}
