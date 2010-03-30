package org.cooling.tower.deployment.util;

import java.util.ArrayList;
import java.util.List;

import org.cooling.tower.deployment.registry.DeploymentRegistry;

public class Utils 
{
	private Utils()
	{
	}
		
	public static List<String> getChildDeployments(String deploymentId, boolean rootDeployment)
	{
		List<String> deployments = new ArrayList<String>();
		if(rootDeployment) deployments.add(deploymentId);
		
		for(String deployment : DeploymentRegistry.getInstance().getDeployment(deploymentId).getChildren())
		{
			deployments.add(deployment);
			deployments.addAll(getChildDeployments(deployment, false));
		}
		return deployments;
	}
}
