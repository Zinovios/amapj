package fr.amapj.view.engine.popup.suppressionpopup;



/**
 * Appel de la procédure de suppression d'un élement
 * 
 */
public class UnableToSuppressException extends RuntimeException
{
	public UnableToSuppressException(String message)
	{
		super(message);
	}
}
