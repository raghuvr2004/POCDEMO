package com.newt.selenium.utlis;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class PropertyUtility {
	

	Properties prop = new Properties();	
	
	public PropertyUtility(String fileName)
	{
		try{
			InputStream input =  null;
			input = new FileInputStream(fileName);
			prop.load(input);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	

	
	public String getProperty(String propertyKey){
		String propertyValue = "";
		propertyValue=prop.getProperty(propertyKey);
		return propertyValue;
	}
}