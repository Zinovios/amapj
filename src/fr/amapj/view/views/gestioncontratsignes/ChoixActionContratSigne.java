package fr.amapj.view.views.gestioncontratsignes;

import fr.amapj.view.engine.popup.swicthpopup.SwitchPopup;

/**
 * Permet de choisir ce que l'on veut modifier
 * dans le contrat : l'entete, les dates ou les produits
 */
@SuppressWarnings("serial")
public class ChoixActionContratSigne extends SwitchPopup
{
	
	private Long mcId;

	/**
	 * 
	 */
	public ChoixActionContratSigne(Long mcId)
	{
		popupTitle = "Autres actions sur les contrats signés";
		popupWidth = "50%";
		
		this.mcId = mcId;

	}

	@Override
	protected void loadFollowingPopups()
	{
		line1 = "Veuillez indiquer ce que vous souhaitez faire :";

		addLine("Mettre à zéro les quantités commandées sur une ou plusieurs dates de livraison", new PopupAnnulationDateLivraison(mcId));

	}

}
