package com.newt.selenium.utlis;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
@Test

public class Userdefined {
RemoteWebDriver driver;
	
	
	public  void  Authentication () throws IOException {
		
		Runtime.getRuntime().exec("C:/Users/Raghu VR/Desktop/Authentication.exe");

	}

	public void UploadFile() throws IOException {
		
		Runtime.getRuntime().exec("C:/Users/Raghu VR/Desktop/UploadFile.exe");
		
	}
	
	public void DownloadFile() throws IOException {
			
		Runtime.getRuntime().exec("");
		
	}
	
	public void SelectByValue() {
		
		Select select = new Select(driver.findElement(By.id("")));
		select.selectByValue("your value");
	}
	
	public void SelectByVisibleTxtUsingId(String id, String txt ){
			  try {
			   Select dd = new Select(driver.findElementById(id));
			   dd.selectByVisibleText(txt);
			  }
			  catch (NoSuchElementException e) {
			   System.out.println("Element with id as "+id+" not found");
			   System.exit(0);
			  }
			 }	
	
	public void SelectByIndex( String id, String index ) {
		try{
		Select obj = new Select(driver.findElementById(id));
		obj.selectByIndex(0);
		}
		catch (NoSuchElementException e) {
			   System.out.println("Element with id as "+id+" not found");
			   System.exit(0);
		}
	}
	
	public void AlertSendkeys() {
		
		Alert a = driver.switchTo().alert();
		a.sendKeys(null);
		
	}
	
	public void AlertGettext() {
		
		Alert a = driver.switchTo().alert();
		a.getText();
		
	}
	
	public void acceptAlert() {
		  	Alert a = driver.switchTo().alert();
		  	a.accept();
		 }
		 
	public void dismissAlert(){
		  		Alert a = driver.switchTo().alert();
		  	a.dismiss();
		 }
	
	public void takeScreensshot() throws Exception {
		      int i =1;
			  File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			  FileUtils.copyFile(src, new File("Location"+i+".jpg"));
			  
			 }
	
	public void MouseClick() {
		
	Actions obj = new Actions(driver);
		
	obj.click(driver.findElement(By.linkText("text"))).build().perform();
		
	}

	public void MovetoElement() {

	Actions obj = new Actions(driver);
    
    obj.moveToElement(driver.findElement(By.xpath("path"))).build().perform();
	
	}

	public void RightClick() {
		
	Actions obj = new Actions (driver);	
	driver.findElement(By.id(""));
	obj.contextClick().build().perform();	
		
	}
	
	public void Dropdown() {
		
	Select dropdown = new Select(driver.findElement(By.id("name")));	
	dropdown.selectByVisibleText("");
	dropdown.selectByIndex(0);
		
	}
	
	public void JavascriptSubmenu() {
		
		JavascriptExecutor js = (JavascriptExecutor)driver;
	    //Hover on Automation Menu on the MenuBar
	    js.executeScript("$('ul.menus.menu-secondary.sf-js-enabled.sub-menu li').hover()");	
		
	}		
	
	public void JavascriptClick() {
		
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();", "");
		
	}
		

}


	
	
	

