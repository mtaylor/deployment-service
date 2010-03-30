package org.cooling.tower.description.parser;

public class CoolingTowerDescriptionParserException extends Exception
{
	private static final long serialVersionUID = 1L;

	public CoolingTowerDescriptionParserException()
	{
		super();
	}
	
	public CoolingTowerDescriptionParserException(String message)
	{
		super(message);
	}
	
	public CoolingTowerDescriptionParserException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public CoolingTowerDescriptionParserException(Throwable cause)
	{
		super(cause);
	}
}
