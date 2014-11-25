package fr.amapj.view.engine.popup.formpopup;


/**
 * équivalent à WizardFormPopup mais avec une seule page
 *  
 */
@SuppressWarnings("serial")
abstract public class FormPopup extends WizardFormPopup
{
	
	/**
	 * Utilisé dans le cas ou il y a une seule page
	 */
	private enum DefaultStep
	{
		STEP1;
	}
	
	protected Class getEnumClass()
	{
		return DefaultStep.class;
	}
	
	protected void addFields(Enum step)
	{
		addFields();
	}

	abstract protected void addFields();
	
	
	
	
	
	
}
