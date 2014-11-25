package fr.amapj.view.engine.popup.suppressionpopup;

import fr.amapj.view.engine.popup.PopupListener;


/**
 * Appel de la procédure de suppression d'un élement
 * 
 */
public interface PopupSuppressionListener extends PopupListener
{
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException;
}
