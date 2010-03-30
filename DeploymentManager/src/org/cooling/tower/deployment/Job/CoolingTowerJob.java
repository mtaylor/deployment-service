package org.cooling.tower.deployment.Job;

public class CoolingTowerJob 
{
	public static enum DeploymentType { DEPLOY, UNDEPLOY, REDEPLOY };
	
	private DeploymentType deploymentType;
	
	private String deploymentId;
	
	private boolean iterateChildren;
	
	public CoolingTowerJob(DeploymentType deploymentType, String deploymentId, boolean iterateChildren)
	{
		this.deploymentId = deploymentId;
		
		this.iterateChildren = iterateChildren;
		
		this.deploymentType = deploymentType;
	}

	public DeploymentType getDeploymentType() 
	{
		return deploymentType;
	}

	public void setDeploymentType(DeploymentType deploymentType) 
	{
		this.deploymentType = deploymentType;
	}

	public String getDeploymentId() 
	{
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) 
	{
		this.deploymentId = deploymentId;
	}

	public boolean isIterateChildren() 
	{
		return iterateChildren;
	}

	public void setIterateChildren(boolean iterateChildren) 
	{
		this.iterateChildren = iterateChildren;
	}	
}
