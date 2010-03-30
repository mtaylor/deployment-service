package org.cooling.tower.deployment.manager;

import java.io.File;
import java.util.List;

import org.cooling.tower.deployment.Job.CoolingTowerJob;
import org.cooling.tower.deployment.Job.JobQueue;
import org.cooling.tower.deployment.Job.CoolingTowerJob.DeploymentType;
import org.cooling.tower.deployment.registry.Deployment;
import org.cooling.tower.description.parser.DescriptionParser;

public class DeploymentManager implements DeploymentInterface
{
	private static DeploymentManager deploymentManager = null;
	
	private DeploymentManager()
	{
	}
	
	public static DeploymentManager getInstance()
	{
		if(deploymentManager == null)
		{
			deploymentManager = new DeploymentManager();
		}
		return deploymentManager;
	}
	
	/**
	 * This implementation does not allow single deployments, for example a deployment that does not deploy its children first
	 */
	@Override
	public String deploy(List<File> artifacts, File config) 
	{
		try
		{
			Deployment rootDeployment = DescriptionParser.generateDeployments(artifacts, config);
			CoolingTowerJob ctJob = new CoolingTowerJob(DeploymentType.DEPLOY, rootDeployment.getDeploymentId(), true);
			
			JobQueue.getInstance().addJob(ctJob);
			
			return rootDeployment.getDeploymentId();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This implementation does not allow single undeployments, all child deployments will be undeployed
	 */
	@Override
	public String undeploy(String deploymentId)
	{
		CoolingTowerJob ctJob = new CoolingTowerJob(DeploymentType.UNDEPLOY, deploymentId, true);
		
		return null;
	}
}
