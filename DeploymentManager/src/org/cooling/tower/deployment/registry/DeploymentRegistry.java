package org.cooling.tower.deployment.registry;

import java.util.HashMap;
import java.util.List;

import org.cooling.tower.deployment.agents.Agent.DeploymentState;

public class DeploymentRegistry 
{
	private HashMap<String, Deployment> deployments;
	
	private static DeploymentRegistry deploymentRegistry = null;
	
	private DeploymentRegistry()
	{
		deployments = new HashMap<String, Deployment>();
	}
	
	public static DeploymentRegistry getInstance()
	{
		if(deploymentRegistry == null)
		{
			deploymentRegistry = new DeploymentRegistry();
		}
		return deploymentRegistry;
	}
	
	public void addDeployment(Deployment deployment)
	{
		deployments.put(deployment.getDeploymentId(), deployment);
	}
	
	public void removeDeployment(String deploymentId)
	{
		deployments.remove(deploymentId);
	}
	
	public Deployment getDeployment(String deploymentId)
	{
		return deployments.get(deploymentId);
	}
}
