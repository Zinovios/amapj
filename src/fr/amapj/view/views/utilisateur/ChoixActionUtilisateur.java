package fr.amapj.view.views.utilisateur;

import fr.amapj.view.engine.popup.swicthpopup.SwitchPopup;

/**
 * Permet de choisir ce que l'on veut modifier
 * dans le contrat : l'entete, les dates ou les produits
 */
@SuppressWarnings("serial")
public class ChoixActionUtilisateur extends SwitchPopup
{

	/**
	 * 
	 */
	public ChoixActionUtilisateur()
	{
		popupTitle = "Autres actions sur les utilisateurs";
		popupWidth = "50%";

	}

	@Override
	protected void loadFollowingPopups()
	{
		line1 = "Veuillez indiquer ce que vous souhaitez faire :";

		addLine("Envoyer un e mail de bienvenue avec un mot de passe pour tous les utilisateurs sans mot de passe ", new PopupEnvoiPasswordMasse());

	}

}
