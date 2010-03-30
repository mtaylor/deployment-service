package org.cooling.tower.deployment.Job;

public class DeploymentException extends Exception
{
	private static final long serialVersionUID = 1L;

	public DeploymentException()
	{
		super();
	}
	
	public DeploymentException(String message)
	{
		super(message);
	}
	
	public DeploymentException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public DeploymentException(Throwable cause)
	{
		super(cause);
	}
	
}
