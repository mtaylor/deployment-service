package org.cooling.tower.deployment.Job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.cooling.tower.deployment.Job.CoolingTowerJob.DeploymentType;
import org.cooling.tower.deployment.agents.Agent;
import org.cooling.tower.deployment.agents.Agent.DeploymentState;
import org.cooling.tower.deployment.agents.messages.AgentRequest;
import org.cooling.tower.deployment.agents.messages.AgentResponse;
import org.cooling.tower.deployment.agents.registry.AgentRegistry;
import org.cooling.tower.deployment.registry.Deployment;
import org.cooling.tower.deployment.registry.DeploymentRegistry;

public class JobRunner implements Runnable 
{

	CoolingTowerJob coolingTowerJob;
	
	public JobRunner(CoolingTowerJob coolingTowerJob)
	{
		this.coolingTowerJob = coolingTowerJob;
	}
	
	@Override
	public void run() 
	{
		switch(coolingTowerJob.getDeploymentType())
		{
			case DEPLOY: deploy(); break;
			case UNDEPLOY: undeploy(); break;
			default: break;
		}
	}
	
	/**
	 * Deploys the Deployment as specified in the CoolingTowerJob, If the iterateChildren is set to true in the Deployment, this method will iterate through all 
	 * children of the deployment, and their children and so on...  The deployments are executed in reverse order, the grandest of children are deployed first and the
	 * initial deployment is deployed last.  
	 * 
	 * If any deployment fails in the tree of deployments, all successful deployments are rolled back and this method return AgentResponse as Failed.  On successful 
	 * deployment this method returns a deployment response with Deployment Success
	 * 
	 * @return
	 */
	private AgentResponse deploy()
	{
		List<ArrayList<Deployment>> deploymentDependencies = DeploymentRegistry.getInstance().getDeployment(coolingTowerJob.getDeploymentId()).createParrellelExecutionList();
		
		HashMap<String, AgentResponse> responses = new HashMap<String, AgentResponse>();
		for(int i = deploymentDependencies.size() - 1; i >= 0; i--)
		{
			for(Deployment deployment : deploymentDependencies.get(i))
			{
				AgentResponse response = deploy(new CoolingTowerJob(DeploymentType.DEPLOY, deployment.getDeploymentId(), false));
				responses.put(deployment.getDeploymentId(), response);
				
				if(response.getState().equals(DeploymentState.FAILED))
				{
					rollback(responses);
					return new AgentResponse(DeploymentState.FAILED);
				}
			}
		}
		return new AgentResponse(DeploymentState.SUCCESS);
	}
	
	private AgentResponse deploy(CoolingTowerJob job)
	{
		try
		{
			Deployment deployment = DeploymentRegistry.getInstance().getDeployment(job.getDeploymentId());
			Agent agent = AgentRegistry.getInstance().getAgentInstance(deployment.getAgentType());	
			
			return agent.deploy(new AgentRequest(deployment.getProperties()));	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			AgentResponse response = new AgentResponse(DeploymentState.FAILED);
			response.setException(e);
			return response;
		}
	}
	
	private AgentResponse undeploy(CoolingTowerJob job)
	{
		try
		{
			Deployment deployment = DeploymentRegistry.getInstance().getDeployment(job.getDeploymentId());
			Agent agent = AgentRegistry.getInstance().getAgentInstance(deployment.getAgentType());	
			
			return agent.undeploy(new AgentRequest(deployment.getProperties()));	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			AgentResponse response = new AgentResponse(DeploymentState.FAILED);
			response.setException(e);
			return response;
		}
	}

	private AgentResponse undeploy()
	{
		List<ArrayList<Deployment>> deploymentDependencies = DeploymentRegistry.getInstance().getDeployment(coolingTowerJob.getDeploymentId()).createParrellelExecutionList();
		
		HashMap<String, AgentResponse> responses = new HashMap<String, AgentResponse>();
		for(int i = 0; i < deploymentDependencies.size(); i++)
		{
			for(Deployment deployment : deploymentDependencies.get(i))
			{
				AgentResponse response = undeploy(new CoolingTowerJob(DeploymentType.UNDEPLOY, deployment.getDeploymentId(), false));
				responses.put(deployment.getDeploymentId(), response);
				
				if(response.getState().equals(DeploymentState.FAILED))
				{
					rollback(responses);
					return new AgentResponse(DeploymentState.FAILED);
				}
			}
		}
		return new AgentResponse(DeploymentState.SUCCESS);
	}
	
	private void rollback(HashMap<String, AgentResponse> reponses)
	{
		//Roll Back Deployments
		//Mark any Child Jobs as Failed/Cancelled.
	}
}
