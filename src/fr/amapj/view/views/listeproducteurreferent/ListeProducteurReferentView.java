package fr.amapj.view.views.listeproducteurreferent;

import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.listeproducteurreferent.DetailProducteurDTO;
import fr.amapj.service.services.listeproducteurreferent.ListeProducteurReferentService;
import fr.amapj.service.services.producteur.ProdUtilisateurDTO;
import fr.amapj.service.services.utilisateur.util.UtilisateurUtil;


/**
 * Page permettant à l'utilisateur de visualiser tous les producteurs et les référents
 * 
 *  
 *
 */
public class ListeProducteurReferentView extends Panel implements View
{
	VerticalLayout layout = null;

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
		List<DetailProducteurDTO> dtos = new ListeProducteurReferentService().getAllProducteurs();
		
		
		layout = new VerticalLayout();
		
		
		for (DetailProducteurDTO detailProducteurDTO : dtos)
		{
			// Le titre
			addLabel(layout,"Producteur : "+detailProducteurDTO.nom);
			
			String str = detailProducteurDTO.description;
			if (str!=null)
			{
				Label tf =new Label(str, ContentMode.HTML);
				tf.addStyleName(ChameleonTheme.LABEL_H4);
				layout.addComponent(tf);
			}
			
			str = formatUtilisateur(detailProducteurDTO.utilisateurs);
			layout.addComponent(new Label(str, ContentMode.HTML));
			
			str = formatReferent(detailProducteurDTO.referents);
			layout.addComponent(new Label(str, ContentMode.HTML));
		}
		
		// 
		
		layout.setMargin(true);
		layout.setSpacing(true);
		
		setContent(layout);
		addStyleName(ChameleonTheme.PANEL_BORDERLESS);
	}
	
	
	
	
	
	
	private String formatUtilisateur(List<ProdUtilisateurDTO> utilisateurs)
	{
		if (utilisateurs.size()==0)
		{
			return "";
		}
		
		String str = UtilisateurUtil.asStringPrenomFirst(utilisateurs, " et ");
		
		if (utilisateurs.size()==1)
		{
			return "Le producteur est "+str+"<br/>";
		}
		else
		{
			return "Les producteurs sont  "+str+"<br/>";
		}
	}
	
	
	private String formatReferent(List<ProdUtilisateurDTO> utilisateurs)
	{
		if (utilisateurs.size()==0)
		{
			return "";
		}
		
		String str = UtilisateurUtil.asStringPrenomFirst(utilisateurs, " et ");
		
		if (utilisateurs.size()==1)
		{
			return "Le référent est "+str+"<br/>";
		}
		else
		{
			return "Les référents sont "+str+"<br/>";
		}
	}
}
