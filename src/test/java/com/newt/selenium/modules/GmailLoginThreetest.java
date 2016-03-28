package com.newt.selenium.modules;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.newt.selenium.constants.ApplicationConstants;
import com.newt.selenium.utlis.ExcelUtils;
import com.newt.selenium.utlis.HelperUtility;
import com.newt.selenium.utlis.Log;
import com.newt.selenium.utlis.PropertyUtility;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;




public class GmailLoginThreetest implements ApplicationConstants {
	
	PropertyUtility putility = new PropertyUtility(OBJECT_REPO_FILEPATH);
	HelperUtility hu = new HelperUtility();
	ExcelUtils excelUtils = new ExcelUtils();
	
	public static ExtentReports extent = new ExtentReports(REPORT_PATH+"/GmailLogin.html", true,DisplayOrder.OLDEST_FIRST);
	 private ExtentTest test ;
	 String testCaseId="TC_03";
	 
	 
		
	@Test
	
	public void DO () throws Exception {
		String testCaseId="TC_03";
		System.out.println(testCaseId);
		//Starting the extent report
		test = extent.startTest("Execution triggered for  - '"+testCaseId);
		
		
		String URL,Username,Pwd,browser;
		
		try{
			ExcelUtils.setExcelFile(DATA_FILEPATH,"Gmail");
			URL = ExcelUtils.getExcelData("URL",testCaseId);
			browser = ExcelUtils.getExcelData("Browser",testCaseId);
			Username = ExcelUtils.getExcelData("Username",testCaseId);
			Pwd = ExcelUtils.getExcelData("Password",testCaseId);	
				
		}
		catch (Exception e){
			System.out.println("Exception while reading data from EXCEL file for test case : "+ testCaseId+" Exceptions : "+ e);
			Reporter.log("Exception while reading data from EXCEL file for test case : "+ testCaseId+" Exception : "+ e);
			test.log(LogStatus.FAIL,"Exception while reading data from EXCEL file for test case : "+ testCaseId+" Exceptions : "+ e);
			excelUtils.setCellData("Gmail", "FAIL", testCaseId, "Result_Status");
			excelUtils.setCellData("Gmail", "Exception while reading data from EXCEL file for test case : "+ testCaseId+" Exceptions : "+ e, testCaseId, "Result_Errors");
			throw new Exception("Error occured while trying to login to the application  -  " +e);
		}		
		System.out.println("url is -"+URL);
		Log.info("Executing Test Case "+testCaseId );
		System.out.println("Executing Test Case "+testCaseId);
		
		//Launching the application
		test.log(LogStatus.INFO, "Launch App", "Usage: <span style='font-weight:bold;'>Going to Launch App</span>");
		hu.LaunchUrl(extent, test,URL, "Gmail", testCaseId,browser);

		//Going to Login
		hu.Gmail_Login(extent, test, Username, Pwd, "Gmail", testCaseId);
		
		hu.sleep(10000);
		//Verify your login
		//Assert.assertEquals(hu.getText(putility.getProperty("verify_element")), "Newt");
		
		
		if(hu.existsElement(putility.getProperty("verify_element"))){
			test.log(LogStatus.PASS,"Element should be verified","Element verified sucessfully");
			excelUtils.setCellData("GMAIL", "PASS", testCaseId, "Result_Status");
			hu.getScreenShot(hu.driver, testCaseId, "Verifying User name successfull", test);
		}
		else{
			test.log(LogStatus.FAIL,"Element should be veruified","Element could not be verified and test case failed");
			excelUtils.setCellData("GMAIL", "FAIL", testCaseId, "Result_Status");
			hu.getScreenShot(hu.driver, testCaseId, "Verifying User name failed", test);
		}
		
		
		
	}
	
	
	
	  @BeforeMethod
	
	  public void beforeMethod () throws Exception {
		  String testCaseId="TC_01";
		  DOMConfigurator.configure("log4j.xml");
		  Log.startTestCase("Start Execution");
		  Log.startTestCase(testCaseId);
		  Log.info("GMAIL_LOGIN :: beforeClass() method invoked...");
		/*
		  extent.config().documentTitle("Gmail Login");
		  extent.config().reportName("GMAIL");
		  extent.config().reportHeadline("LOGIN");			
	  */
	  
	  }	
	  
	
	  @AfterMethod
	 
	  public void afterMethod (){
		  Log.info("GMAIL_LOGIN :: afterClass() method invoked...");
		  try{
			  //Logging out from the applcation
			  hu.AppLogout( test,testCaseId);
			  Log.endTestCase(testCaseId);
			 hu.Driverquit();
			  //Ending the Extent test
			  extent.endTest(test);
			  //Writing the report to HTML format
			  extent.flush();    
		  } catch(Exception e){
			  Log.error("GMAIL_LOGIN :: App Logout failed () :: Exception:"+e);
		     
			  Log.endTestCase(testCaseId);
			  extent.endTest(test);
			  extent.flush();  
		
		  }
	  }	 
	
}
