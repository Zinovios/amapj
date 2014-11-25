package fr.amapj.common;

import java.util.logging.Logger;

public class DebugUtil
{
	private final static Logger logger = Logger.getLogger(DebugUtil.class.getName());
	
	
	static public void trace(String message)
	{
		logger.info(message);
	}
	
	static public void trace(Throwable t)
	{
		logger.info(StackUtils.asString(t));
	}
}
