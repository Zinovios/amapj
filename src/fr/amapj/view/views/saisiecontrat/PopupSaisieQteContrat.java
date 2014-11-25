package fr.amapj.view.views.saisiecontrat;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import com.vaadin.ui.Notification;

import fr.amapj.service.services.mescontrats.ContratColDTO;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.ContratLigDTO;
import fr.amapj.view.engine.grid.GridHeaderLine;
import fr.amapj.view.engine.grid.GridSizeCalculator;
import fr.amapj.view.engine.grid.integergrid.PopupIntegerGrid;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.ModeSaisie;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.SaisieContratData;

/**
 * Popup pour la saisie des quantites pour un contrat
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisieQteContrat extends PopupIntegerGrid
{
	private final static Logger logger = Logger.getLogger(PopupSaisieQteContrat.class.getName());
	
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	private ContratDTO contratDTO;
	
	private SaisieContratData data;
	
	
	
	/**
	 * 
	 */
	public PopupSaisieQteContrat(SaisieContratData data)
	{
		super();
		this.data = data;
		this.contratDTO = data.contratDTO;
		
		//
		popupTitle = "Saisie des quantités pour le contrat "+contratDTO.nom;
		popupWidth ="90%";
		
		// 
		param.readOnly = (data.modeSaisie==ModeSaisie.READ_ONLY);
		param.messageSpecifique = data.messageSpecifique;
		
	}
	
	
	
	public void loadParam()
	{
		contratDTO.fillParam(param);
	
		param.largeurCol = 110;
		param.espaceInterCol = 3;
		
				
		// Construction du header 1
		GridHeaderLine line1  =new GridHeaderLine();
		line1.styleName = "tete";
		line1.cells.add("Produit");
				
		for (ContratColDTO col : contratDTO.contratColumns)
		{
			line1.cells.add(col.nomProduit);
		}
		GridSizeCalculator.autoSize(line1,param.largeurCol,"Arial",16);
		
	
		// Construction du header 2
		GridHeaderLine line2  =new GridHeaderLine();
		line2.styleName = "prix";
		line2.cells.add("prix unitaire");
				
		for (ContratColDTO col : contratDTO.contratColumns)
		{
			line2.cells.add(new CurrencyTextFieldConverter().convertToString(col.prix));
		}

		// Construction du header 3
		GridHeaderLine line3  =new GridHeaderLine();
		line3.styleName = "tete";
		line3.cells.add("Dates");
				
		for (ContratColDTO col : contratDTO.contratColumns)
		{
			line3.cells.add(col.condtionnementProduit);
		}
		GridSizeCalculator.autoSize(line3,param.largeurCol,"Arial",16);
		
		param.headerLines.add(line1);
		param.headerLines.add(line2);
		param.headerLines.add(line3);
		
		
		// Partie gauche de chaque ligne
		for (ContratLigDTO lig : contratDTO.contratLigs)
		{
			param.leftPartLine.add(df.format(lig.date));
		}	
	}
	
	
	


	@Override
	protected void handleAnnuler()
	{
		data.cancel = true;
		close();
	}
	
	@Override
	protected void handleContinuer()
	{
		data.montantCible = param.montantTotal;
		close();
	}

	
	public boolean performSauvegarder()
	{
		if (contratDTO.isEmpty())
		{
			Notification.show("Vous devez saisir une quantité avant de continuer");
			return false;
		}
		
		data.montantCible = param.montantTotal;
		
		return true;
	}
	
	
	
}
