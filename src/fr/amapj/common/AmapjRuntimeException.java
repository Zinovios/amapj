package fr.amapj.common;

@SuppressWarnings("serial")
public class AmapjRuntimeException extends RuntimeException
{

	public AmapjRuntimeException()
	{
	}

	public AmapjRuntimeException(String message)
	{
		super(message);
	}

	public AmapjRuntimeException(Throwable cause)
	{
		super(cause);
	}

	public AmapjRuntimeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AmapjRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
