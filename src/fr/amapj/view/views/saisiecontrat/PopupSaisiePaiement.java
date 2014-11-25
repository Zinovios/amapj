package fr.amapj.view.views.saisiecontrat;

import java.text.SimpleDateFormat;

import com.vaadin.ui.Notification;

import fr.amapj.service.services.mescontrats.DatePaiementDTO;
import fr.amapj.service.services.mescontrats.InfoPaiementDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.view.engine.grid.GridHeaderLine;
import fr.amapj.view.engine.grid.currencyvector.PopupCurrencyVector;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.ModeSaisie;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.SaisieContratData;

/**
 * Popup pour la saisie des paiements pour un contrat
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisiePaiement extends PopupCurrencyVector
{
	SimpleDateFormat df = new SimpleDateFormat("MMMMM yyyy");
	
	private InfoPaiementDTO paiementDTO;
	
	private SaisieContratData data;
	
	/**
	 * 
	 */
	public PopupSaisiePaiement(SaisieContratData data)
	{
		super();
		this.data = data;
		this.paiementDTO = data.contratDTO.paiement;
		
		
		//
		popupTitle = "Saisie des paiements pour le contrat "+data.contratDTO.nom;
		popupWidth ="50%";
		
		// 
		param.readOnly = (data.modeSaisie==ModeSaisie.READ_ONLY);
		param.messageSpecifique = data.messageSpecifique;
		param.montantCible = data.montantCible;
		param.computeLastLine = (data.modeSaisie!=ModeSaisie.CHEQUE_SEUL);
	}
	
	
	
	public void loadParam()
	{
		param.nbLig = paiementDTO.datePaiements.size();
		param.montant = new int[param.nbLig];
		param.avoirInitial = paiementDTO.avoirInitial;
		param.excluded =  new boolean[param.nbLig];
	
		for (int i = 0; i < param.nbLig; i++)
		{
			param.montant[i] = paiementDTO.datePaiements.get(i).montant;
			param.excluded[i] = paiementDTO.datePaiements.get(i).excluded;
			if (param.excluded[i])
			{
				param.nbExcluded++;
			}
		}
		
		param.largeurCol = 200;
		param.espaceInterCol = 3;
		
		// Calcul d'une proposition de paiement si necessaire
		computePropositionPaiement();
		
				
		// Construction du header 1
		GridHeaderLine line1  =new GridHeaderLine();
		line1.height = 40;
		line1.styleName = "tete";
		line1.cells.add("Date");
		line1.cells.add("Montant €");
		
		param.headerLines.add(line1);
		
		// Partie gauche de chaque ligne
		for (DatePaiementDTO datePaiement : paiementDTO.datePaiements)
		{
			param.leftPartLine.add(df.format(datePaiement.datePaiement));
		}	
	}
	
	
	/**
	 * Calcul d'une proposition de paiement, dans le cas d'un nouveau contrat
	 */
	private void computePropositionPaiement()
	{
		// Si visualisation seule : pas de recalcul , on pourra visualiser l'eventuel ecart
		if (data.modeSaisie==ModeSaisie.READ_ONLY)
		{
			return ;
		}
		
		// Si cheque seul : pas de recalcul, on fait tout à la main 
		if (data.modeSaisie==ModeSaisie.CHEQUE_SEUL)
		{
			return ;
		}
		
		// Si contrat existant et en modification : on recalcule uniquement la dernière ligne 
		if (data.contratDTO.contratId!=null) 
		{
			int cumul = data.contratDTO.paiement.avoirInitial;
			for (int i = 0; i < param.nbLig-1; i++)
			{
				cumul = cumul+param.montant[i];
			}
			param.montant[param.nbLig-1] = param.montantCible-cumul;
			
			return ;
		}

		// Sinon on fait un calcul complet
		for (int i = 0; i < param.nbLig; i++)
		{
			if (param.excluded[i]==false)
			{
				// Calcul du montant restant à affecter
				int montantRestant = param.montantCible;
				for (int j = 0; j < i; j++)
				{
					montantRestant = montantRestant-param.montant[j];
				}
				
				// Calcul du nombre de paiements disponibles
				int nbPaiement=0;
				for (int j = i; j < param.nbLig; j++)
				{
					if (param.excluded[j]==false)
					{
						nbPaiement++;
					}
				}
				
				// Calcul du montant
				param.montant[i] = round(montantRestant,nbPaiement);
			}
		}
		
	}



	/**
	 * 
	 * @param montantRestant
	 * @param nbPaiement
	 * @return
	 */
	private int round(int montantRestant, int nbPaiement)
	{
		// Si il reste un dernier paiement : on l'affecte en entier
		if (nbPaiement==1)
		{
			return montantRestant;
		}
		return (montantRestant/nbPaiement/100)*100;
	}



	public boolean performSauvegarder()
	{
		if (param.montant[param.nbLig-1]<0)
		{
			Notification.show("Les paiements saisis sont incorrects. Il y a un trop payé de "+
								new CurrencyTextFieldConverter().convertToString(-param.montant[param.nbLig-1])+" €");
			return false;
		}
		
		// Copie dans le DTO
		for (int i = 0; i < param.montant.length; i++)
		{
			paiementDTO.datePaiements.get(i).montant = param.montant[i];
		}
		
		
		if (data.modeSaisie!=ModeSaisie.FOR_TEST)
		{
			new MesContratsService().saveNewContrat(data.contratDTO,data.userId);
		}
		
		return true;
	}
	
}
