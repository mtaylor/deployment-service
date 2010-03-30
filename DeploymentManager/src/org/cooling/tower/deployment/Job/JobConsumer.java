package org.cooling.tower.deployment.Job;

import org.cooling.tower.deployment.Job.CoolingTowerJob.DeploymentType;
import org.cooling.tower.deployment.agents.Agent;
import org.cooling.tower.deployment.agents.messages.AgentRequest;
import org.cooling.tower.deployment.agents.messages.AgentResponse;
import org.cooling.tower.deployment.agents.registry.AgentRegistry;
import org.cooling.tower.deployment.registry.Deployment;
import org.cooling.tower.deployment.registry.DeploymentRegistry;

public class JobConsumer implements Runnable
{	
	private AgentRegistry agentRegistry;
	
	private DeploymentRegistry deploymentRegistry;
	
	private JobQueue jobQueue;
	
	public JobConsumer()
	{
		agentRegistry = AgentRegistry.getInstance();
		
		deploymentRegistry = DeploymentRegistry.getInstance();
		
		jobQueue = JobQueue.getInstance();
	}
	
	@Override
	public void run() 
	{
		try
		{
			while(true)
			{
				CoolingTowerJob ctjob = jobQueue.getJob();
				if(ctjob != null)
				{
					JobRunner runner = new JobRunner(ctjob);
					Thread thread = new Thread(runner);
					thread.start();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}

