package fr.amapj.view.views.gestioncontratsignes;

import fr.amapj.view.engine.popup.swicthpopup.SwitchPopup;

/**
 * 
 */
@SuppressWarnings("serial")
public class ChoixActionReceptionCheque extends SwitchPopup
{
	
	private Long mcId;

	/**
	 * 
	 */
	public ChoixActionReceptionCheque(Long mcId)
	{
		popupTitle = "Autres actions sur les paiements";
		popupWidth = "50%";
		
		this.mcId = mcId;

	}

	@Override
	protected void loadFollowingPopups()
	{
		line1 = "Veuillez indiquer ce que vous souhaitez faire :";

		addLine("Chercher les chèques à rendre aux amapiens", new PopupChercherChequeARendre(mcId));

	}

}
