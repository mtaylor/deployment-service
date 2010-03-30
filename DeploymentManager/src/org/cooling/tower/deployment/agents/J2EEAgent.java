package org.cooling.tower.deployment.agents;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.nio.CharBuffer;
import java.security.KeyStore.Builder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.codehaus.cargo.container.InstalledLocalContainer;
import org.codehaus.cargo.container.configuration.LocalConfiguration;
import org.codehaus.cargo.container.deployable.Deployable;
import org.codehaus.cargo.container.deployable.WAR;
import org.codehaus.cargo.container.deployer.DeployableMonitor;
import org.codehaus.cargo.container.deployer.DeployableMonitorListener;
import org.codehaus.cargo.container.deployer.Deployer;
import org.codehaus.cargo.container.deployer.URLDeployableMonitor;
import org.codehaus.cargo.container.jboss.JBoss5xInstalledLocalContainer;
import org.codehaus.cargo.container.jboss.JBossExistingLocalConfiguration;
import org.codehaus.cargo.container.jboss.JBossInstalledLocalDeployer;
import org.codehaus.cargo.container.spi.deployer.DeployerWatchdog;
import org.cooling.tower.deployment.agents.messages.AgentRequest;
import org.cooling.tower.deployment.agents.messages.AgentResponse;

public class J2EEAgent implements Agent
{
	public static final long DEPLOY_TIMEOUT = 60000;
	
	public static final long UNDEPLOY_TIMEOUT = 60000;
	
	private static final String JBOSS_HOME="JBOSS_HOME";
	
	private static final String HOSTNAME = "cargo.hostname";
	
	private static final String PROTOCOL = "cargo.protocol";
	
	private static final String SERVLET_PORT = "cargo.servlet.port";
		
	private static final String WAR_DEPLOYMENT_PATH = "WAR_DEPLOYMENT_PATH";
	
	private static final String TEST_PATH = "TEST_PATH";
	
	private static final String ARTIFACT = "artifact";
		
	private static final String JBOSS_SERVER_CONFIG = File.separator + "server" + File.separator + "default";
	
	/**
	 * Method Deploys WAR File into a local JBoss Installation.  The war will be deployed to $JBOSS_HOME/server/default/deploy.
	 * It checks the deployment has been successfully deployed by ping the address http://<host>:<port>/<path> where <path> is 
	 * a resource that is part of the deployment and defined in the request properties as J2EEAgent.PATH.
	 * 
	 * e.g. http://localhost:8080/Calender/Calender.html
	 * 
	 * NOTE: Currently this method does not check to see if there is an existing deployment of this WAR.  
	 */
	@Override
	public AgentResponse deploy(AgentRequest request) 
	{
		try
		{
			File warFilePath = new File(request.getProperty(ARTIFACT));
			
			String jbossHome = System.getenv(JBOSS_HOME) + JBOSS_SERVER_CONFIG;
		
			LocalConfiguration jbossASConfig = new JBossExistingLocalConfiguration(jbossHome);		
			InstalledLocalContainer jbossAS = new JBoss5xInstalledLocalContainer(jbossASConfig);

			String hostname = jbossASConfig.getPropertyValue(HOSTNAME);
			int port = Integer.parseInt(jbossASConfig.getPropertyValue(SERVLET_PORT));
			String protocol = jbossASConfig.getPropertyValue(PROTOCOL);
			String path = request.getProperty(TEST_PATH);
			URL testURL = new URL(protocol, hostname, port, path); 
			
			System.out.println(warFilePath);
			Deployable war = new WAR(warFilePath.getAbsolutePath());
			Deployer deployer = new JBossInstalledLocalDeployer(jbossAS);
			
			DeployableMonitor monitor = new URLDeployableMonitor(testURL, DEPLOY_TIMEOUT);
			deployer.deploy(war, monitor);
			
			AgentResponse response = new AgentResponse(DeploymentState.SUCCESS);
			System.out.println("Deployed to " + war.getFile().toString());
			
			response.addProperty(WAR_DEPLOYMENT_PATH, jbossHome + File.separator + warFilePath.getName());
			response.addProperty(TEST_PATH, testURL.toString());
			return response;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new AgentResponse(DeploymentState.FAILED);
		}	
	}

	@Override
	public AgentResponse undeploy(AgentRequest request)
	{
		try
		{			
			String jbossHome = System.getenv(JBOSS_HOME) + JBOSS_SERVER_CONFIG;
		
			LocalConfiguration jbossASConfig = new JBossExistingLocalConfiguration(jbossHome);		
			InstalledLocalContainer jbossAS = new JBoss5xInstalledLocalContainer(jbossASConfig);
				
			Deployable war = new WAR(request.getProperty(WAR_DEPLOYMENT_PATH));
			Deployer deployer = new JBossInstalledLocalDeployer(jbossAS);
			
			DeployableMonitor monitor = new URLDeployableMonitor(new URL(request.getProperty(TEST_PATH)), UNDEPLOY_TIMEOUT);
			deployer.undeploy(war, monitor);
			
			return new AgentResponse(DeploymentState.SUCCESS);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new AgentResponse(DeploymentState.FAILED);
		}
	}
}
