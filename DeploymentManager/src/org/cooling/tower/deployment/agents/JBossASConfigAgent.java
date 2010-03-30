package org.cooling.tower.deployment.agents;

import java.io.File;
import java.util.List;

import org.apache.xerces.impl.validation.ConfigurableValidationState;
import org.codehaus.cargo.container.ContainerType;
import org.codehaus.cargo.container.InstalledLocalContainer;
import org.codehaus.cargo.container.configuration.Configuration;
import org.codehaus.cargo.container.configuration.ConfigurationCapability;
import org.codehaus.cargo.container.configuration.ConfigurationType;
import org.codehaus.cargo.container.configuration.entry.DataSource;
import org.codehaus.cargo.container.jboss.JBoss5xInstalledLocalContainer;
import org.codehaus.cargo.container.jboss.JBossExistingLocalConfiguration;
import org.codehaus.cargo.container.jboss.JBossRuntimeConfiguration;
import org.codehaus.cargo.container.jboss.internal.JBossRuntimeConfigurationCapability;
import org.codehaus.cargo.generic.configuration.ConfigurationFactory;
import org.codehaus.cargo.generic.configuration.DefaultConfigurationFactory;
import org.cooling.tower.deployment.agents.messages.AgentRequest;
import org.cooling.tower.deployment.agents.messages.AgentResponse;

public class JBossASConfigAgent implements Agent
{

	public static final String JNDI_NAME = "JNDI_NAME";
	
	public static final String DRIVER_CLASS = "DRIVER_CLASS";
	
	public static final String CONNECTION_URL = "CONNECTION_URL";
	
	public static final String USERNAME = "USERNAME";
	
	public static final String PASSWORD = "PASSWORD";
	
	private static final String SERVER_CONFIG = File.separator + "server"  + File.separator + "default";
	
	@Override
	public AgentResponse deploy(AgentRequest request)
	{
		System.out.println(System.getenv("JBOSS_HOME") + SERVER_CONFIG);
		
		
		ConfigurationFactory factory = new DefaultConfigurationFactory();
		Configuration configuration = factory.createConfiguration("jboss42x",  ContainerType.REMOTE, ConfigurationType.RUNTIME);
		
		ConfigurationCapability capability = configuration.getCapability();
		
		for(Object key : capability.getProperties().keySet())
		{
			System.out.println(key + ":" + capability.getProperties().get(key));
		}
		
//		List datasources = jbossASConfig.getDataSources();
//		for(Object datasource : datasources)
//		{
//			System.out.println("Datasource");
//			DataSource d = (DataSource)  datasource;
//			for(Object key : d.getConnectionProperties().keySet())
//			{
//			System.out.println(key + ":" + d.getConnectionProperties().getProperty(key.toString()));
//			}
//		}
				
		DataSource dataSource = new DataSource();
		dataSource.setConnectionType("javax.sql.DataSource");
		dataSource.setTransactionSupport(null);
		dataSource.setJndiLocation(request.getProperty(JNDI_NAME));
		dataSource.setDriverClass(request.getProperty(DRIVER_CLASS));
		dataSource.setUrl(request.getProperty(CONNECTION_URL));
		dataSource.setUsername(request.getProperty(USERNAME));
		dataSource.setPassword(request.getProperty(PASSWORD));
		
//		jbossASConfig.addDataSource(dataSource);
//		jbossAS.setConfiguration(jbossASConfig);
//		
//
//		datasources = jbossASConfig.getDataSources();
//		
//		jbossASConfig.collectUnsupportedDataSourcesAndThrowException();
//		for(Object datasource : datasources)
//		{
//			System.out.println("Datasource");
//			DataSource d = (DataSource)  datasource;
//			for(Object key : d.getConnectionProperties().keySet())
//			{
//				System.out.println(key + ":" + d.getConnectionProperties().getProperty(key.toString()));
//			}
//		}
		return new AgentResponse(DeploymentState.SUCCESS);
	}

	@Override
	public AgentResponse undeploy(AgentRequest request) 
	{
		// TODO Auto-generated method stub
		return null;
	}



}
