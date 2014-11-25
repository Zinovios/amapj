package fr.amapj.view.views.gestioncontrat.editorpart;

import fr.amapj.view.engine.popup.swicthpopup.SwitchPopup;

/**
 * Permet de choisir ce que l'on veut modifier
 * dans le contrat : l'entete, les dates ou les produits
 */
@SuppressWarnings("serial")
public class ChoixModifEditorPart extends SwitchPopup
{

	private Long id;

	/**
	 * 
	 */
	public ChoixModifEditorPart(Long id)
	{
		this.id = id;

		popupTitle = "Modification d'un contrat";
		popupWidth = "50%";

		if (id == null)
		{
			throw new RuntimeException("Le contrat a modifier ne peut pas etre null");
		}
	}

	@Override
	protected void loadFollowingPopups()
	{
		line1 = "Veuillez indiquer ce que vous souhaitez modifier :";

		addLine("Les informations d'entete (nom,description, date limite d'inscription)", new ModifEnteteContratEditorPart(id));

		addLine("Les dates de livraisons", new ModifDateContratEditorPart(id));

		addLine("Les produits disponibles et les prix", new ModifProduitContratEditorPart(id));

		addLine("Barrer certaines dates ou certains produits", new BarrerDateContratEditorPart(id));
		
		addLine("Les informations de paiement", new ModifPaiementContratEditorPart(id));

	}

}
