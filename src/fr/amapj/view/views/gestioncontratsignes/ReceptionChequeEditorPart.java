package fr.amapj.view.views.gestioncontratsignes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Button.ClickEvent;

import fr.amapj.model.models.contrat.reel.EtatPaiement;
import fr.amapj.service.services.gestioncontratsigne.ContratSigneDTO;
import fr.amapj.service.services.mescontrats.DatePaiementDTO;
import fr.amapj.service.services.mespaiements.MesPaiementsService;
import fr.amapj.view.engine.grid.GridHeaderLine;
import fr.amapj.view.engine.grid.booleangrid.PopupBooleanGrid;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;

/**
 * Popup pour la réception des chèques
 *  
 */
@SuppressWarnings("serial")
public class ReceptionChequeEditorPart extends PopupBooleanGrid
{
	
	SimpleDateFormat df = new SimpleDateFormat("MMMMM yyyy");
	
	private ContratSigneDTO c;
	
	private List<DatePaiementDTO> paiements;
		
	
	/**
	 * 
	 */
	public ReceptionChequeEditorPart(ContratSigneDTO c)
	{
		super();
		this.c = c;
	}
	
	
	
	public void loadParam()
	{
		//
		paiements = new MesPaiementsService().getPaiementAReceptionner(c.idContrat);
		
		//
		popupTitle = "Réception chèques";
		popupWidth ="60%";
		
		
		//
		param.messageSpecifique = "<h2> Réception des chèques de "+c.prenomUtilisateur+" "+c.nomUtilisateur+"</h2>";
		
		param.nbCol = 1;
		param.nbLig = paiements.size();
		param.box = new boolean[param.nbLig][param.nbCol];
	
		for (int i = 0; i < param.nbLig; i++)
		{
			param.box[i][0] = (paiements.get(i).etatPaiement==EtatPaiement.AMAP);
		}
		
		param.largeurCol = 200;
		param.espaceInterCol = 3;
		
		
		
		
		// Construction du header 1
		GridHeaderLine line1  =new GridHeaderLine();
		line1.height = 70;
		line1.styleName = "tete";
		line1.cells.add("Date");
		line1.cells.add("Montant €");
		line1.cells.add("Cocher la case si le chèque a été donné");
		
		param.headerLines.add(line1);
		
		// Partie gauche de chaque ligne
		param.leftPartLine2 = new ArrayList<>();
		for (DatePaiementDTO datePaiement : paiements)
		{
			param.leftPartLine.add(df.format(datePaiement.datePaiement));
			param.leftPartLine2.add(new CurrencyTextFieldConverter().convertToString(datePaiement.montant));
		}	
		
	}
	
	@Override
	protected void createButtonBar()
	{
		Button toutOK = addButton("J'ai bien reçu tous les chèques", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleToutSelectionner();
			}
		});
		popupButtonBarLayout.setComponentAlignment(toutOK, Alignment.TOP_LEFT);
		
		
		super.createButtonBar();
	}
	
	
	protected void handleToutSelectionner()
	{
		for (int i = 0; i < param.nbLig; i++)
		{
			Item item = table.getItem(new Integer(i));
			
			CheckBox tf = (CheckBox) item.getItemProperty(new Integer(0)).getValue();
			tf.setValue(Boolean.TRUE);
		}
		
	}



	public void performSauvegarder()
	{
		
		for (int i = 0; i < param.nbLig; i++)
		{
			if (param.box[i][0] == true)
			{
				paiements.get(i).etatPaiement=EtatPaiement.AMAP;
			}
			else
			{
				paiements.get(i).etatPaiement=EtatPaiement.A_FOURNIR;
			}
		}
		
		
		new MesPaiementsService().receptionCheque(paiements);
	}
	
}
