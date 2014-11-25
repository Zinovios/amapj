package fr.amapj.view.engine.excelgenerator;



import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorService;
import fr.amapj.service.engine.excelgenerator.FileInfoDTO;
import fr.amapj.view.engine.popup.corepopup.CorePopup;

/**
 * Popup de base , avec deux boutons Sauvegarder et Annuler
 *  
 */
@SuppressWarnings("serial")
public class TelechargerPopup extends CorePopup
{
	
	private List<AbstractExcelGenerator> generators = new ArrayList<>();
	
	
	public TelechargerPopup()
	{	
	}
	
	public void addGenerator(AbstractExcelGenerator generator)
	{
		generators.add(generator);
	}

	protected void createContent(VerticalLayout contentLayout)
	{
		contentLayout.addComponent(new Label("Veuillez cliquer sur le lien du fichier que vous souhaitez télécharger"));
		
		List<FileInfoDTO> fileInfoDTOs = new ExcelGeneratorService().getFileInfo(generators);
		for (FileInfoDTO fileInfoDTO : fileInfoDTOs)
		{
			addLink(contentLayout,fileInfoDTO);	
		}	
	}
	
	private void addLink(VerticalLayout contentLayout, FileInfoDTO fileInfoDTO)
	{
		String titre = fileInfoDTO.nameToDisplay;
		String fileName = fileInfoDTO.fileName;
		String extension = fileInfoDTO.generator.getFormat().name().toLowerCase();
		Link extractFile = new Link(titre,new StreamResource(new ExcelResource(fileInfoDTO.generator), fileName+"."+extension));
		contentLayout.addComponent(extractFile);
	}

	protected void createButtonBar()
	{		
		addButton("Quitter", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAnnuler();
			}
		});
	}
	

	protected void handleAnnuler()
	{
		close();
	}
	
}
