package com.newt.selenium.driver;


import java.io.*;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.IExecutionListener;

import com.newt.selenium.constants.ApplicationConstants;
import com.newt.selenium.constants.MyConstants;
import com.newt.selenium.utlis.XMLParserUtil;


public class Driver implements IExecutionListener, ApplicationConstants {
	
	//@Override
	public void onExecutionStart() {
		
		DOMConfigurator.configure("log4j.xml");
		XMLParserUtil xmlUtil = new XMLParserUtil();
		
		String env = xmlUtil.getAttributeValue(TESTNGPATH,"env");
		String browser = xmlUtil.getAttributeValue(TESTNGPATH,"browser");
		
		//Setting the env to run
		if (env.equalsIgnoreCase("Local")){
			MyConstants.env = "LOCAL";
		}
		else{
			MyConstants.env = "DEVOPS";
		}	
		
		//Setting the browserto run
		if (browser.equalsIgnoreCase("firefox")){
			MyConstants.browser = "FIREFOX";
		}
		else if (browser.equalsIgnoreCase("internetexplorer")) {
			MyConstants.browser = "IE";
		}
		else if (browser.equalsIgnoreCase("chrome")) {
			MyConstants.browser = "CHROME";
		}	
		
		//Emptying the log file before every run
		try {
			   BufferedWriter out = new BufferedWriter
		         (new FileWriter(LOG_FILEPATH));
		         out.write("");
		         out.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while deleting the log file before execution of test cases: " + e);
		}		
	}

	//@Override
	public void onExecutionFinish() {
		System.out.println("Inside onExecutionFinish method...");
		// TODO Auto-generated method stub
	}

}
