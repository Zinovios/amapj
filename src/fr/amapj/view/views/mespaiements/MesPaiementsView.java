package fr.amapj.view.views.mespaiements;

import java.text.SimpleDateFormat;
import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.model.models.contrat.reel.EtatPaiement;
import fr.amapj.service.services.mespaiements.DetailPaiementAFournirDTO;
import fr.amapj.service.services.mespaiements.DetailPaiementFourniDTO;
import fr.amapj.service.services.mespaiements.MesPaiementsDTO;
import fr.amapj.service.services.mespaiements.MesPaiementsService;
import fr.amapj.service.services.mespaiements.PaiementAFournirDTO;
import fr.amapj.service.services.mespaiements.PaiementFourniDTO;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;


/**
 * Page permettant à l'utilisateur de visualiser tous ses paiements
 * 
 *  
 *
 */
public class MesPaiementsView extends Panel implements View
{
	VerticalLayout layout = null;
	
	SimpleDateFormat df = new SimpleDateFormat("EEEEE dd MMMMM yyyy");

	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{
		setSizeFull();
		refresh();
	}

	
	/**
	 * Ajoute un label sur toute la largeur à la ligne indiquée
	 */
	private Label addLabel(VerticalLayout layout, String str)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_H1);
		layout.addComponent(tf);
		return tf;
		
	}


	private void refresh()
	{
		MesPaiementsDTO mesPaiementsDTO = new MesPaiementsService().getMesPaiements(SessionManager.getUserId());
		
		
		layout = new VerticalLayout();
		
		// Le titre
		addLabel(layout,"Les chèques que je dois donner à l'AMAP");
	
		
		// la liste des chéques à donner
		List<PaiementAFournirDTO> paiementAFournirs = mesPaiementsDTO.paiementAFournir;
		
		if (paiementAFournirs.size()==0)
		{
			String str = "Vous êtes à jour de vos paiements, vous n'avez pas de chèques à fournir à l'AMAP <br/>";
			layout.addComponent(new Label(str, ContentMode.HTML));
		}
		
		for (PaiementAFournirDTO paiementAFournir : paiementAFournirs)
		{
			String str = formatContrat(paiementAFournir);
			layout.addComponent(new Label(str, ContentMode.HTML));
		
			
			for (DetailPaiementAFournirDTO detail : paiementAFournir.paiements)
			{
				str = detail.formatPaiement(); 
				layout.addComponent(new Label(str, ContentMode.HTML));
			
			}
			
			// Une ligne vide
			layout.addComponent(new Label("<br/>", ContentMode.HTML));
		}
		
		
		// Le titre
		addLabel(layout,"Le planning de mes paiements à venir mois par mois");
	
		
		// la liste des chéques qui seront bientot encaissés		
		for (PaiementFourniDTO paiementFourni : mesPaiementsDTO.paiementFourni)
		{
			String str = formatMois(paiementFourni);
			layout.addComponent(new Label(str, ContentMode.HTML));
		
			
			for (DetailPaiementFourniDTO detail : paiementFourni.paiements)
			{
				str = formatPaiement(detail); 
				layout.addComponent(new Label(str, ContentMode.HTML));
			}
			
			// Une ligne vide
			layout.addComponent(new Label("<br/>", ContentMode.HTML));
		}
		
		layout.setMargin(true);
		layout.setSpacing(true);
		
		setContent(layout);
		addStyleName(ChameleonTheme.PANEL_BORDERLESS);
	}
	
	
	
	
	
	
	private String formatContrat(PaiementAFournirDTO paiementAFournir)
	{
		// Ligne 0
		String str = "<h4>"+
					"Nom du contrat : "+paiementAFournir.nomContrat+
					" - Date limite de remise des chèques: "+df.format(paiementAFournir.dateRemise)+
					"<br/>"+
					" Ordre des chèques : "+paiementAFournir.libCheque+
					"</h4>";
						
		return str;
	}
	
	
	


	private String formatMois(PaiementFourniDTO paiementFourni)
	{
		String montant = new CurrencyTextFieldConverter().convertToString(paiementFourni.totalMois)+" €";
		String str = "<h4>"+paiementFourni.moisPaiement+" - Total du mois : "+montant+"</h4>";
		return str;
	}




	private String formatPaiement(DetailPaiementFourniDTO detail)
	{
		String montant = new CurrencyTextFieldConverter().convertToString(detail.montant)+" €";
		String str = "Montant : "+montant+" - Contrat :"+detail.nomContrat+" - Ordre du chèque :"+detail.libCheque;
		
		if (detail.etatPaiement==EtatPaiement.A_FOURNIR)
		{
			str = str+" (Chèque à fournir à l'AMAP)";
		}
		
		return str;
	}


}
