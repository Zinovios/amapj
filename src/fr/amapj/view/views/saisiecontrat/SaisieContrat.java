package fr.amapj.view.views.saisiecontrat;

import fr.amapj.model.models.contrat.modele.GestionPaiement;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.view.engine.grid.integergrid.IntegerGridParam;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.corepopup.CorePopup;

public class SaisieContrat
{
	private SaisieContratData data;
	private PopupListener listener;
	
	public SaisieContrat(SaisieContratData data,PopupListener listener)
	{
		super();
		this.data = data;
		this.listener = listener;
	}

	public void doSaisie()
	{
		// SI cheque seul : on passe directement à la suite
		if (data.modeSaisie==ModeSaisie.CHEQUE_SEUL)
		{
			// Calcul du montant total 
			IntegerGridParam param = new IntegerGridParam();
			data.contratDTO.fillParam(param);
			param.initialize();
			data.montantCible = param.montantTotal;
			
			//
			popupPaiement();
			return;
		}
		
		PopupSaisieQteContrat popup = new PopupSaisieQteContrat(data);
		CorePopup.open(popup, new PopupListener()
		{
			@Override
			public void onPopupClose()
			{
				// Si l'opérateur a appuyé sur cancel on arrete
				if (data.cancel==true)
				{
					listener.onPopupClose();
				}
				else if (data.modeSaisie==ModeSaisie.QTE_SEUL)
				{
					new MesContratsService().saveNewContrat(data.contratDTO,data.userId);
					listener.onPopupClose();
				}
				// Sinon on continue
				else
				{
					popupPaiement();
				}
			}
		});
	}

	

	protected void popupPaiement()
	{
		if (data.contratDTO.paiement.gestionPaiement==GestionPaiement.GESTION_STANDARD)
		{
			PopupSaisiePaiement paiement = new PopupSaisiePaiement(data);
			CorePopup.open(paiement, listener);
		}
		else
		{
			PopupInfoPaiement paiement = new PopupInfoPaiement(data);
			CorePopup.open(paiement, listener);
		}
	}
	

	/**
	 * Permet de lancer le cyle de saisie d'un contrat, avec les quantités
	 * et les reglements
	 * 
	 * @param contratDTO
	 * @param readOnly
	 * @param forTest
	 * @param userId
	 * @param messageSpecifique
	 */
	static public void saisieContrat(ContratDTO contratDTO, Long userId, String messageSpecifique,ModeSaisie modeSaisie,
			final PopupListener listener)
	{
		SaisieContratData data = new SaisieContratData(contratDTO, userId, messageSpecifique,modeSaisie);
		SaisieContrat saisieContrat = new SaisieContrat(data, listener);
		saisieContrat.doSaisie();
	}
	
	public enum ModeSaisie
	{
		// Saisie standard faite par l'utilisateur
		STANDARD,
		
		// Mode test, correspond à la saisie standard faite par l'utilisateur 
		FOR_TEST,
		
		// Visualisation des informations du contrat 
		READ_ONLY,
		
		// Saisie de la quantité seule, pour correction par le referent
		QTE_SEUL,
		
		// Saisie des chéques seuls, pour correction par le referent
		CHEQUE_SEUL
		
	}
	
	
	public static class SaisieContratData
	{
		public ContratDTO contratDTO;
		public Long userId;
		public String messageSpecifique;
		public ModeSaisie modeSaisie;
		
		// Variables échangés entre les différents popup de la saisie
		public int montantCible;
		public boolean cancel = false;
		
		
		public SaisieContratData(ContratDTO contratDTO, Long userId, String messageSpecifique,ModeSaisie modeSaisie)
		{
			
			this.contratDTO = contratDTO;
			this.userId = userId;
			this.messageSpecifique = messageSpecifique;
			this.modeSaisie = modeSaisie;
		}
		
		
	}

}
