package org.cooling.tower.description.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.cooling.tower.deployment.artifact.ArtifactManager;
import org.cooling.tower.deployment.registry.Deployment;
import org.cooling.tower.deployment.registry.DeploymentRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DescriptionParser 
{
	public static enum DeploymentType
	{ 
		DEPLOYMENT, UNDEPLOYMENT, REDEPLOY
	}
	
	public static enum Element 
	{ 
		DEPENDENCIES, APPLICATION, CONFIG, TYPE, USERNAME, PASSWORD, ARTIFACT
	}
	
	public static enum DefaultAgentTypes
	{
		ROOT
	}
	
	private DescriptionParser()
	{
	}
	
	public static Map<String, String> getPropertiesFromNode(Node node)
	{
		HashMap<String, String> properties = new HashMap<String, String>();
		NodeList children = node.getChildNodes();
		for(int i = 0; i < children.getLength(); i ++)
		{
			Node child = children.item(i);
			if(child.getNodeType() == 1 && !child.getNodeName().equalsIgnoreCase(Element.DEPENDENCIES.toString()))
			{
				System.out.println(child.getNodeName());
				properties.put(child.getNodeName(), child.getTextContent());
			}
		}
		return properties;
	}
	
	public static void generateChildDeployments(Node node, String userId, Deployment parentDeployment)
	{
		if(node.hasChildNodes())
		{
			NodeList children = node.getChildNodes();						
			for(int i = 0; i < children.getLength(); i ++)
			{
				Node childNode = children.item(i);
				String nodeName = childNode.getNodeName();	
								
				if(childNode.getNodeType() == 1)
				{
					if(nodeName.equalsIgnoreCase(Element.APPLICATION.toString()) || nodeName.equalsIgnoreCase(Element.CONFIG.toString()))
					{
						String deploymentId = UUID.randomUUID().toString();
						
						Map<String, String> properties = getPropertiesFromNode(childNode);
						
						Deployment childDeployment = new Deployment(userId, deploymentId, properties.get(Element.TYPE.toString().toLowerCase()), new ArrayList<String>());
						childDeployment.setProperties(properties);	
						DeploymentRegistry.getInstance().addDeployment(childDeployment);

						parentDeployment.addChild(childDeployment.getDeploymentId());	

						generateChildDeployments(childNode, userId, childDeployment);
					}
					else if(nodeName.equalsIgnoreCase(Element.DEPENDENCIES.toString()))
					{
						generateChildDeployments(childNode, userId, parentDeployment);
					}
				}
			}
		}
	}
	
	
	public static Deployment generateDeployments(List<File> artifacts, File config) throws CoolingTowerDescriptionParserException
	{		
		Document doc = getDocumentFromConfig(config);
		
		String userId = doc.getElementsByTagName(Element.USERNAME.toString().toLowerCase()).item(0).getTextContent();
		
		String deploymentId = UUID.randomUUID().toString();
		
		registerArtifacts(userId, deploymentId, config, artifacts, doc);
		
		Node node = doc.getFirstChild();
		
		Deployment rootDeployment = new Deployment(userId, deploymentId, DefaultAgentTypes.ROOT.toString(), new ArrayList<String>());
		DeploymentRegistry.getInstance().addDeployment(rootDeployment);
		
		generateChildDeployments(node, userId, rootDeployment);
		
		return rootDeployment;
	}
	
	/**
	 * Registers the Artifacts and the config file into the Artifact Registry, it then replaces the path to the artifacts Document Object to the new path of
	 * the Artifacts.
	 * @param userId
	 * @param deploymentId
	 * @param config
	 * @param artifacts
	 * @param doc
	 * @return
	 */
	private static Document registerArtifacts(String userId, String deploymentId, File config, List<File> artifacts, Document doc)
	{
		String configId = ArtifactManager.getInstance().storeArtifact(userId, deploymentId, config);
		config = ArtifactManager.getInstance().getArtifact(userId, deploymentId, configId);
		
		List<String> artifactIds = ArtifactManager.getInstance().storeArtifacts(userId, deploymentId, artifacts);
		NodeList nodeList = doc.getElementsByTagName(Element.ARTIFACT.toString().toLowerCase());
		
		for(int i = 0; i < nodeList.getLength(); i ++)
		{
			Node node = nodeList.item(i);
			int position = Integer.parseInt(node.getTextContent());
			String artifactId = artifactIds.get(position);
			node.setTextContent(artifactId);
		}
		
		return doc;
	}
	
	private static Document getDocumentFromConfig(File config) throws CoolingTowerDescriptionParserException
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(config);
		}
		catch(Exception e)
		{
			throw new CoolingTowerDescriptionParserException("Could not parse configuration file", e);
		}
	}
}
