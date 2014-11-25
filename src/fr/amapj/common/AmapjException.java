package fr.amapj.common;

@SuppressWarnings("serial")
public class AmapjException extends Exception
{

	public AmapjException()
	{
	}

	public AmapjException(String message)
	{
		super(message);
	}

	public AmapjException(Throwable cause)
	{
		super(cause);
	}

	public AmapjException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AmapjException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
