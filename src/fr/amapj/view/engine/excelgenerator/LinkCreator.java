package fr.amapj.view.engine.excelgenerator;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Link;

import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorService;
import fr.amapj.service.engine.excelgenerator.FileInfoDTO;

/**
 * Outil pour créer facilement des liens 
 *  
 */
@SuppressWarnings("serial")
public class LinkCreator 
{
	
	static public Link createLink(AbstractExcelGenerator generator)
	{
		FileInfoDTO fileInfoDTO = new ExcelGeneratorService().getFileInfo(generator);
		
		String titre = fileInfoDTO.nameToDisplay;
		String fileName = fileInfoDTO.fileName;
		String extension = fileInfoDTO.generator.getFormat().name().toLowerCase();
		Link extractFile = new Link("Télécharger "+titre,new StreamResource(new ExcelResource(fileInfoDTO.generator), fileName+"."+extension));
		return extractFile;
	}
	
}
