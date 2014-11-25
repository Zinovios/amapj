package fr.amapj.view.engine.popup.formpopup;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class OnSaveException extends Exception
{
	
	private List<String> errorMessage = new ArrayList<>();

	public OnSaveException()
	{
	}

	public OnSaveException(String message)
	{
		super(message);
		errorMessage.add(message);
	}
	
	public OnSaveException(List<String> messages)
	{
		super();
		errorMessage.addAll(messages);
	}
	

		
	public List<String> getAllMessages()
	{
		return errorMessage;
	}

}
