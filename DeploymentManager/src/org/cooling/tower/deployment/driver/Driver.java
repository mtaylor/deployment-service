package org.cooling.tower.deployment.driver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.tools.ant.util.FileUtils;
import org.cooling.tower.deployment.Job.JobConsumer;
import org.cooling.tower.deployment.Job.JobQueue;
import org.cooling.tower.deployment.agents.J2EEAgent;
import org.cooling.tower.deployment.agents.JBossASConfigAgent;
import org.cooling.tower.deployment.agents.MockAgent;
import org.cooling.tower.deployment.agents.PostgresDDLAgent;
import org.cooling.tower.deployment.agents.messages.AgentRequest;
import org.cooling.tower.deployment.agents.messages.AgentResponse;
import org.cooling.tower.deployment.agents.registry.AgentRegistry;
import org.cooling.tower.deployment.manager.DeploymentManager;

public class Driver 
{
	public static void main(String[] args)
	{
		try
		{
			testJobCreation();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void registerAgents()
	{
		AgentRegistry registry = AgentRegistry.getInstance();
		
		registry.registerAgent("J2EE", J2EEAgent.class);
		registry.registerAgent("DDL", PostgresDDLAgent.class);
		registry.registerAgent("Datasource", JBossASConfigAgent.class);
	}
	
	private static void registerMockAgents()
	{
		AgentRegistry registry = AgentRegistry.getInstance();
		
		registry.registerAgent("ROOT", MockAgent.class);
		registry.registerAgent("J2EE", MockAgent.class);
		registry.registerAgent("DDL", MockAgent.class);
		registry.registerAgent("Datasource", MockAgent.class);
	}
	
	private static void testDataSource() throws Exception
	{
		registerAgents();
		AgentRegistry registry = AgentRegistry.getInstance();
		
		AgentRequest request = new AgentRequest();
		request.addProperty(JBossASConfigAgent.CONNECTION_URL, "jdbc:postgresql://localhost:5432/testdb");
		request.addProperty(JBossASConfigAgent.DRIVER_CLASS, "org.postgresql.Driver");
		request.addProperty(JBossASConfigAgent.JNDI_NAME, "jdbc/testdb");
		request.addProperty(JBossASConfigAgent.PASSWORD, "test");
		request.addProperty(JBossASConfigAgent.USERNAME, "test");
		
		AgentResponse dresponse = registry.getAgentInstance("Datasource").deploy(request);
		System.out.println(dresponse.getState());
	}
	
	private static void testWarDeploy() throws Exception
	{
		registerAgents();
		
		AgentRegistry registry = AgentRegistry.getInstance();
		
		AgentRequest request = new AgentRequest();
		request.addProperty("TEST_PATH", "/Calendar/Calendar.html");
		request.addProperty("FILE_PATH", "/home/martyn/dev/test/Calendar.war");
		
		AgentResponse dresponse = registry.getAgentInstance("J2EE").deploy(request);
		for(String key : dresponse.getProperties().keySet())
		{
			System.out.println(key + ":" + dresponse.getProperty(key));
		}
		
		AgentResponse uresponse = registry.getAgentInstance("J2EE").undeploy(new AgentRequest(dresponse.getProperties()));
		for(String key : uresponse.getProperties().keySet())
		{
			System.out.println(key + ":" + uresponse.getProperty(key));
		}
	}

	private static void testJobCreation() throws Exception
	{
		registerMockAgents();
		
		String testResourcesHome = "test" + File.separator + "testJobCreation";
		
		File workingDir = new File(testResourcesHome + File.separator + "tmpDir");
		workingDir.mkdir();
		
		JobConsumer consumer = new JobConsumer();
		Thread thread = new Thread(consumer);
		thread.start();
		
		File repository = new File("repository");
				
		File deploymentXML = copyFile(testResourcesHome + File.separator + "deployment.xml", workingDir + File.separator + "deployment.xml");
		File artifact1 = copyFile(testResourcesHome + File.separator + "application.ear", workingDir + File.separator + "application.ear");
		File artifact2 = copyFile(testResourcesHome + File.separator + "database.ddl", workingDir + File.separator + "database.ddl");
		
		ArrayList<File> artifacts = new ArrayList<File>();
		artifacts.add(artifact1);
		artifacts.add(artifact2);
		
		String deploymentId = DeploymentManager.getInstance().deploy(artifacts, deploymentXML);
		
		Thread.sleep(2000);
		//DeploymentManager.getInstance().undeploy(deploymentId);
		
//		DeploymentJob job1 = (DeploymentJob) JobQueue.getInstance().getJob();
//		DeploymentJob job2 = (DeploymentJob) JobQueue.getInstance().getJob();
//		DeploymentJob job3 = (DeploymentJob) JobQueue.getInstance().getJob();
//		DeploymentJob job4 = (DeploymentJob) JobQueue.getInstance().getJob();
//		
//		printJob(job1);
//		printJob(job2);
//		printJob(job3);
//		printJob(job4);
		
		
		
		
//		System.out.println(job1.getUserId() + " " + job1.getDeploymentId() + " " + job1.getProperties().get("type"));
//		System.out.println(job2.getUserId() + " " + job2.getDeploymentId() + " " + job2.getProperties().get("type"));
//		System.out.println(job3.getUserId() + " " + job3.getDeploymentId() + " " + job3.getProperties().get("type"));
		
		FileUtils.delete(workingDir);
		FileUtils.delete(repository);
		//FileUtils.
	}
		
	private static File copyFile(String from, String to) throws Exception
	{
		File toFile = new File(to);
	    FileReader in = new FileReader(new File(from));
	    FileWriter out = new FileWriter(toFile);
	    int c;
	    while ((c = in.read()) != -1)
	      out.write(c);

	    in.close();
	    out.close();
	    
	    return toFile;
	  }
}
