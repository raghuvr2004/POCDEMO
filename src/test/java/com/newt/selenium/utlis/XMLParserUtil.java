package com.newt.selenium.utlis;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XMLParserUtil {
	
	public ArrayList <String> getAllTestCases(String xmlFilePath){
		ArrayList <String> testCases = new ArrayList<String>() ;
		try{
			File xmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();	
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("test");
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String testCase = eElement.getAttribute("name");
					testCases.add(testCase);
				}
			}
			return testCases;
		}
		catch (Exception e) {
			System.out.println("Error while reading the testng.xml "+ e);
	         return testCases; 
		}
	}
		
	public String getAttributeValue(String xmlFilePath, String attributeName){
		String paramValue = "";
		if(attributeName.equalsIgnoreCase("env"))
			paramValue = "Devops";
		else
			paramValue = "Firefox";
		try{
			File xmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();	
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("parameter");
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElementParam = (Element) nNode;
					if (eElementParam.getAttribute("name").equalsIgnoreCase(attributeName))
						paramValue = eElementParam.getAttribute("value");
				}
			}
			return paramValue;
		}
		catch (Exception e) {
			System.out.println("Error while reading the testng.xml in getEnviorenment "+ e);
	         return paramValue; 
		}
	}
	
	public String newXMLFile(String xmlFilePath, String nodeName){
		
		try{
			File xmlFile = new File(xmlFilePath);
			System.out.println("Inside newXML file method");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();	
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			NodeList list = doc.getElementsByTagName("*");
			 for (int i = 0; i <list.getLength(); i++) {
				 
				   Node node = (Node) list.item(i);
				   System.out.println("NODE   "+node);
				   //Look through entire settings file
					if (node.getNodeName().contains("!DOCTYPE suite SYSTEM ")) {
						 doc.getNodeName().replaceAll("!DOCTYPE suite SYSTEM 'http://testng.org/testng-1.0.dtd'", "");
						 System.out.println("Node found and deleted");
					
				    }
			}

			 return nodeName;
		}
		catch (Exception e) {
			System.out.println("Error while READING the testng.xml in getEnviorenment "+ e);
			return nodeName;
	
		}
		
	}
	
}
