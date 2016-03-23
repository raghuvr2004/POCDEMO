package com.newt.selenium.utlis;
/*package utils;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.verizon.cao.selenium.constants.ApplicationConstants;
import com.verizon.cao.selenium.constants.MyConstants;




public class StaffingUtil implements ApplicationConstants { 

	//Creating required variables
	
	public WebDriver driver;
	
	//protected ThreadLocal<WebDriver> threadLocalDriver = null;
	protected ThreadLocal<RemoteWebDriver> threadDriver = null;
	//protected static InheritableThreadLocal<WebDriver> threadDriver = new InheritableThreadLocal<WebDriver>();" 
	PropertyUtility putility = new PropertyUtility(OBJECT_REPO_FILEPATH);
	ExcelUtils excelUtils = new ExcelUtils();
	
	
	
	 * @date 10/14/2015
	 * @author Devbrath Singh
	 * @description Method to login into the application
	 
	public boolean Staffing_Login(ExtentReports extent,ExtentTest test,String usrname,String password, String sheetName,String testCaseId) throws Exception{
		try{				
			Log.info("Staffing :: App login invoked ()...");
			Reporter.log("Going to log in into the application");
			driver.findElement(By.linkText(putility.getProperty("english_link"))).click();
			driver.findElement(By.xpath(putility.getProperty("user_id"))).clear();
			driver.findElement(By.xpath(putility.getProperty("user_id"))).sendKeys(usrname);
			test.log(LogStatus.PASS, "Enter User ID","User id - '"+usrname+"' entered sucessfully");
			driver.findElement(By.xpath(putility.getProperty("pwd"))).sendKeys(password);
			test.log(LogStatus.PASS,"Enter Password", "Password - '"+password+"' entered sucessfully");
			
			clickElement(test,putility.getProperty("signin_btn"),"signin_btn",testCaseId);
			Processing();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().window().maximize();	
			//Validating if login is successful or not
			if(existsElement(putility.getProperty("login_sucess_elemnt"))){
				test.log(LogStatus.PASS, "User Should be able to Login","User logged in sucessfully");
				Log.info("Staffing :: App login () :: Login successful...");
				Reporter.log("Staffing :: App login () :: Login successful...");
			}
			return true;
			
		} catch (Exception e){
			Log.error("Staffing :: App Loging failed () :: Exception:"+e);
			Reporter.log("Staffing :: App Loging failed () :: Exception: "+e);
			test.log(LogStatus.FAIL,"User Should be able to Login", "User login failed - "+e);
			getScreenShot(driver,testCaseId, "App_Login_Failed",test);
			excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
			excelUtils.setCellData(sheetName, ""+e, testCaseId, "Result_Errors");
			throw new Exception("Error occured while trying to login to the application  -  " +e);
			
		}
		
	}


	
	 * @date 10/14/2015
	 * @author Devbrath Singh
	 * @description Method used to logout from the application
	 

	public boolean LaunchUrl(ExtentReports extent,ExtentTest test,String strUrl, String sheetName,String testCaseId)throws Exception {
		try{
			Log.info("Staffing : Launching url");
			System.out.println("Staffing : Launching url :: "+ MyConstants.browser);
			//String env_type =putility.getProperty("execution_type").trim();
			
			//Create a new instance of the Firefox driver
				if((MyConstants.browser).equalsIgnoreCase("FIREFOX")){
					System.out.println("IN FF LOOP");
					if((MyConstants.env).equalsIgnoreCase("LOCAL")){
					System.out.println("In LOCAL LOOP");
					FirefoxProfile profile = new FirefoxProfile();
					profile.setAcceptUntrustedCertificates(true);
					driver = new FirefoxDriver(profile);				
				}
				else{
					//Code for running from Selenium HUB
					System.out.println("In DEVOPS LOOP 0");
					String hub =putility.getProperty("SELENIUM_HUB_URL_"+MyConstants.env);
					threadDriver = new ThreadLocal<RemoteWebDriver>();
					DesiredCapabilities dc = new DesiredCapabilities();
			        FirefoxProfile fp = new FirefoxProfile();
			        dc.setCapability(FirefoxDriver.PROFILE, fp);
			        dc.setPlatform(Platform.WINDOWS);
			        dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
			        threadDriver.set(new RemoteWebDriver(new URL(hub), dc));			
			        driver = threadDriver.get();
				}	
			}
			
			if((MyConstants.browser).equalsIgnoreCase("IE")){
				//Create a new instance of the IE driver
				System.out.println("IN IE LOOP");
				if((MyConstants.env).equalsIgnoreCase("LOCAL")){
					System.out.println("In LOCAL LOOP");
					System.setProperty("webdriver.ie.driver", IE_DRIVER_PATH+"/IEDriverServer.exe");
			    	 driver=new InternetExplorerDriver(); 
				}
				else{
					//Code for running from Selenium HUB
					System.out.println("In DEVOPS LOOP");
					String hub =putility.getProperty("SELENIUM_HUB_URL_"+MyConstants.env);
					threadDriver = new ThreadLocal<RemoteWebDriver>();        
					DesiredCapabilities dc =  DesiredCapabilities.internetExplorer();
					dc.setPlatform(Platform.WINDOWS);
					dc.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
					dc.setVersion("11");
					dc.setCapability("ignoreProtectedModeSettings",true); 
					System.setProperty("webdriver.ie.driver", IE_DRIVER_PATH+"/IEDriverServer.exe");
					threadDriver.set(new RemoteWebDriver(new URL(hub), dc));			
			        driver = threadDriver.get(); 
				}
			}
			
				
			Log.info("New driver instantiated");

			//Deleting all browser cookies
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().deleteAllCookies();

			//Launch the Staffing application
			driver.get(strUrl);
			Log.info("Staffing :: URL launch successfull...");
			Reporter.log("Staffing :: URL launch successfull...");
			test.log(LogStatus.PASS, "URL should be launched","Staffing url  '"+strUrl+"' Launced sucessfully");
			return true;	
		} catch(Exception e){
			e.printStackTrace();
			Log.error("Staffing :: Url launch failed () :: Exception:"+e);
			Reporter.log("Staffing :: Url launch failed () :: Exception:"+e);
			getScreenShot(driver,testCaseId, "Url_Launch_Failed",test);
			test.log(LogStatus.FAIL, "Staffing url  "+strUrl+" could not be launched - " +e);
			excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
			excelUtils.setCellData(sheetName, ""+e, testCaseId, "Result_Errors");
			throw new Exception("Error occured while launching the url -   "+e);
			

		}
		
	}		


	
	 * @date 10/14/2015
	 * @author Devbrath Singh
	 * @description Method used to Logout from the application
	 

	public void AppLogout(ExtentTest test) throws Exception{
		Log.info("Staffing :: App Logout () method invoked...");
		try{
			//Close the driver
			driver.findElement(By.xpath(putility.getProperty("singout_btn"))).click();
			Log.info("Staffing :: App Logout () :: Logout Successfull...");
			Reporter.log("Staffing :: App Logout () ::  Logout Successfull...");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			test.log(LogStatus.INFO, "App Logout", "Usage: <span style='font-weight:bold;'>Loging out from the application</span>");
			test.log(LogStatus.PASS, "User should be able to Logout","User sucessfully logged out of the application");
		}catch (Exception e){
			Log.error("Staffing :: App Logout () :: Exception:"+e);
			test.log(LogStatus.FAIL, "User should be able to Logout", "Logout failed - "+e);
			Reporter.log("Staffing :: App Logout () :: Logout failed  - "+e);
			//driver.quit();
		}
	}


	
	 * @date 10/14/2015
	 * @author Devbrath Singh
	 * @description Method used to navigate to a menu by clicking on Text elements
	 
	public void Navigate_To_Menu(ExtentReports extent, ExtentTest test, String Navigate,String sheetName,String testCaseId) throws Exception{
		try{
			String navigation[] = Navigate.split(">");
			for (int i = 0; i < navigation.length; i++) 
			{	
				Log.info(" '" + navigation[i] +"' Clicked");
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				String trimNavigation = navigation[i].trim();
				driver.findElement(By.linkText(trimNavigation)).click();
				test.log(LogStatus.PASS, "Should be able to Navigate","sucessfully navigated to the - "+navigation[i]+" page");
			}
		}catch (Exception e){
			Log.error("Staffing :: App Navigation () :: Navigation failed:"+e);
			getScreenShot(driver,testCaseId, "Navigation_Failed",test);
			Reporter.log("Staffing :: App Navigation () :: Navigation failed "+e);
			test.log(LogStatus.FAIL, "Should be able to Navigate","Navigation failed to the desired page - "+e);
			excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
			excelUtils.setCellData(sheetName, ""+e, testCaseId, "Result_Errors");
			throw new Exception("Error occured while Navigating to the page   - "+e);
		}
	}


	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to switch between different frames using name of id
	 

	public void SwitchFrames(String FrameName){
		try{
			driver.switchTo().frame(FrameName);	
			Log.info("Successfully switched  to main frame");
		} catch (Exception e){
			Log.error("Frame could not be changed - "+e);
		}
	}
	
	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to switch between different frames using id
	 
	
	public void SwitchFramesbyID(String FrameID){
		try{
			WebElement frame= driver.findElement(By.xpath("//iframe[@id='"+FrameID+"']"));
			driver.switchTo().frame(frame);
			Log.info("Successfully switched  to main frame");
		} catch (Exception e){
			Log.error("Frame could not be changed - "+e);
		}
	}
	

	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to set data in app in edit box fields
	 

	public void SetData(ExtentTest test,String objPath, String setValue){
		if(driver.findElement(By.xpath(objPath)).getSize().height> 0){
			try{
				if (setValue != null && setValue.trim().length() >0 ){
					Processing();
					driver.findElement(By.xpath(objPath)).clear();
					Processing();
					driver.findElement(By.xpath(objPath)).sendKeys(setValue);
					Processing();
					driver.findElement(By.xpath(objPath)).sendKeys(Keys.TAB);
					Processing();
					Log.info("Data  '"+setValue+"' entered sucessfully in the edit box");
				}
			}catch (Exception e){
				Log.error("Staffing ::Set data () :: Set data failed while setting  '"+setValue+"'  - "+e);
				Reporter.log("Staffing ::Set data () :: Set data failed while setting  '"+setValue+"'  - "+e);
			}	 
		}
	}
	

	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to set Target opening date  in edit box fields in Job Opening page
	 

	public void setTarget(ExtentReports extent,ExtentTest test,String intTarget,String sheetName,String testCaseId) throws Exception{
		try{
			Processing();
			driver.findElement(By.xpath(putility.getProperty("target_openings"))).clear();
			Processing();
			driver.findElement(By.xpath(putility.getProperty("target_openings"))).sendKeys(intTarget);
			Processing();
			driver.findElement(By.xpath(putility.getProperty("target_openings"))).sendKeys(Keys.TAB);
			Processing();
			test.log(LogStatus.PASS, "Target opening must be set","Target openings sucessfully set to  '"+intTarget+"'");
			Log.info("Staffing :: Set Target Opening data () :: Target OPening set as '"+intTarget+"'  sucessfully ");
		}catch (Exception e){
			getScreenShot(driver,testCaseId, "Setting_targetOpening_Val",test);
			Log.error("Staffing :: Set Target Opening data () :: Data  '"+intTarget+"' could not be entered"+e);
			test.log(LogStatus.FAIL, "Target openings could not be set to   '"+intTarget+"' because - "+e);
			excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
			excelUtils.setCellData(sheetName, ""+e, testCaseId, "Result_Errors");
			throw new Exception("Error occured while Navigating to the page   - "+e);
			
		}
	}


	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to select values from the drop down
	 


	public boolean SelectDropDown(ExtentTest test,String objPath, String value,String sheetName,String testCaseId) throws Exception{
		try{
			if (value != null && value.trim().length() >0 ){
				Select oSelect = new Select(driver.findElement(By.xpath(objPath)));
				oSelect.selectByVisibleText(value);
				Processing();
				Log.info("Selected - '"+value+"' from the dropdown");
				test.log(LogStatus.PASS, "Drop down should be selected","Drop down value -'"+value+"'- selected sucessfully");
				Reporter.log("Selected - '"+value+"' from the dropdown");
			}
			return true;
		} catch (Exception e){
			getScreenShot(driver,testCaseId, "Selecting  "+value+"",test);
			Log.error("Staffing :: Dropdown selection failed..." +e);	
			test.log(LogStatus.FAIL,"Drop down should be selected", "Drop down value -'"+value+"'- could not be selected because -"+e);
			Reporter.log("Value - '"+value+"' from the dropdown could not be selected   - "+e);
			excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
			excelUtils.setCellData(sheetName, ""+e, testCaseId, "Result_Errors");
			return false;
		}
		
	}


	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to remove special character from Job header
	 

	public String TestRegex(ExtentTest test,String strText){
		try{
			if (strText != null && strText.trim().length() >0 ){
				Matcher m = Pattern.compile("[^\\s\\w]").matcher(strText);
				if (m.find()){
					Log.info("String  '"+strText+"'  has special characters");
					String str = strText.replaceAll("[^\\s\\w]"," ");
					test.log(LogStatus.PASS, "Special characters should be removed","Special characters from -'"+strText+"'- removed sucessfully and new String is - '"+str+"'");
					Log.info("Special characters from -'"+strText+"'- removed sucessfully and new String is - '"+str+"'");
					strText= str;
				} 

				else{
					test.log(LogStatus.PASS, "Job heading already ahs no special charater in it -"+strText);
					Log.info("String  '"+strText+"' is already without special characters");
				}
			}
		} catch (Exception e){
			Log.error("Special charaters could not be removed..." +e);
			test.log(LogStatus.FAIL, "Special characters should be removed","Job heading special charater could not be removed because -"+e);
			Reporter.log("Special charaters could not be removed..." +e);
		}
		return strText;

	}


	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to set value in Look up and get the desired result
	 

	public void SetLookUpValue(ExtentReports extent,ExtentTest test,String LookUp_path,String SearchedTxtBox,String strValue,String sheetName,String testCaseId) throws Exception{
		try{
			if (strValue != null && strValue.trim().length() >0 ){
				Processing();
				driver.findElement(By.xpath(LookUp_path)).click();
				Processing();
				driver.switchTo().defaultContent();
				WebElement frame= driver.findElement(By.xpath(putility.getProperty("rec_mod_frame")));
				driver.switchTo().frame(frame);
				driver.findElement(By.xpath(SearchedTxtBox)).sendKeys(strValue);
				driver.findElement(By.xpath(putility.getProperty("lookup_button"))).click();
				Processing();
				if (existsElement("//a[@id='SEARCH_RESULT1']")){
					driver.findElement(By.xpath("//a[@id='SEARCH_RESULT1']")).click();
					driver.switchTo().defaultContent();
					Log.info("Value '"+strValue+"'  entered sucessfully from the look up");
					test.log(LogStatus.PASS,"Value should be selected from the look up", "Value '"+strValue+"'  entered sucessfully from the look up");
				}else {
					getScreenShot(driver,testCaseId, "Value "+strValue+"  not found in the look up",test);
					driver.findElement(By.xpath("//input[@id='#ICCancel']")).click();
					test.log(LogStatus.INFO, "Look Up Value", "Usage: <span style='font-weight:bold;'> Value "+strValue+"  is not found in the look up</span>");
					test.log(LogStatus.FAIL,"Value should be selected from the look up", "Value '"+strValue+"'  is not found in the look up");
					Reporter.log("Look up value '"+strValue+"' could not be found");
					excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
					excelUtils.setCellData(sheetName, "Value '"+strValue+"' not found in the lookup", testCaseId, "Result_Errors");
					WindowHandles();
					throw new Exception("Value '"+strValue+"' not found in the lookup");
				} 
								
			}
		} catch(Exception e){
			getScreenShot(driver,testCaseId, "Value not found in the look up",test);
			Log.error("Look up value could not be entered..." +e);
			test.log(LogStatus.FAIL,"Value should be selected from the look up", "Value '"+strValue+"'  could not be entered from the look up because "+e);
			Reporter.log("Look up value could not be entered - " +e);
			excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
			excelUtils.setCellData(sheetName, ""+e, testCaseId, "Result_Errors");
			WindowHandles();
			throw new Exception("Value '"+strValue+"' not found in the lookup  - "+e);
		}
	}


	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to click on a particular element in page
	 

	public boolean clickElement(ExtentTest test,String clkObject,String Elementname,String testCaseId) throws Exception{
		try{
			driver.findElement(By.xpath(clkObject)).click();
			Log.info(Elementname+" - having xpath '"+clkObject+"' clicked sucessfully");
			test.log(LogStatus.PASS, "Element should be clicked sucessfully",Elementname+" - having xpath '"+clkObject+"' clicked sucessfully");
			return true;
		} catch (Exception e){
			getScreenShot(driver,testCaseId, "'"+Elementname+"'  could not be clicked",test);
			Log.error(Elementname+" - having xpath '"+clkObject+"'  click failed  - "+e);
			test.log(LogStatus.WARNING, "Element should be clicked sucessfully",Elementname+" - having xpath  '"+clkObject+"' could not be clicked because -" +e );
			Reporter.log(Elementname+" - having xpath '"+clkObject+"'  click failed  - "+e);
			return false;
		}
	}


	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to set the new text where special character has been rmoved
	 

	public void RemoveSpclChar(ExtentTest test,String objPath){
		try{
			Processing();
			String content = driver.findElement(By.xpath(objPath)).getAttribute("value");
			driver.findElement(By.xpath(objPath)).clear();
			driver.findElement(By.xpath(objPath)).sendKeys(TestRegex(test,content));
			Processing();
		} catch(Exception e){
			Log.error("Secial char could not be removed " +e);
			Reporter.log("Secial char could not be removed " +e);
		}

	}


	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to set dummy data in Job description fields
	 


	public void setJobDescription(ExtentTest test,String strJobDesc,String sheetName,String testCaseId,String Job_Type) throws Exception{
		try{
			if (Job_Type.equalsIgnoreCase("Create")){
				if(MyConstants.browser.equalsIgnoreCase("FIREFOX")){	                
					WebElement element = null;
					WebElement editable= null;
					if(existsElement("//iframe[@title='Rich Text Editor, HRS_JO_PST_DSCR_DESCRLONG$0']")){
						WebElement frame= driver.findElement(By.xpath("//iframe[@title='Rich Text Editor, HRS_JO_PST_DSCR_DESCRLONG$0']"));
		                driver.switchTo().frame(frame);
		                editable = driver.switchTo().activeElement();
		                if(editable.getSize().height >0)
		                	editable.sendKeys(strJobDesc);
		                driver.switchTo().defaultContent();
		                SwitchFramesbyID("ptifrmtgtframe");
					}
					
					List<WebElement> element1 = driver.findElements(By.xpath(putility.getProperty("job_desc_textbox_table_newff")));	
					int length = element1.size();
					System.out.println("Length is "+length);

	                for(int i = 1; i<=length;i++)
					{	System.out.println("Inside for loop");
						if(existsElement(("//iframe[@title='Rich Text Editor, HRS_JO_PST_DSCR_DESCRLONG$"+i+"']"))){
							element = driver.findElement(By.xpath("//iframe[@title='Rich Text Editor, HRS_JO_PST_DSCR_DESCRLONG$"+i+"']"));
							Processing();
			                driver.switchTo().frame(element);
			                editable = driver.switchTo().activeElement();
			                String textTest=editable.getText();
			                if(textTest.isEmpty())
			                	editable.sendKeys(strJobDesc);
			                
			                driver.switchTo().defaultContent(); 
			                SwitchFramesbyID("ptifrmtgtframe");
						}
					}

					for(int i = 1; i<=length;i++)
					{	
						List<WebElement> table = driver.findElements(By.xpath(putility.getProperty("job_desc_textbox")+i+"')]"));
						if(table.size() > 0 )
						{
							Processing();
							element = driver.findElement(By.xpath(putility.getProperty("job_desc_textbox")+i+"')]//iframe"));
							if(element.getText() == null && element.getText().trim().length() == 0){
								Log.info("description is there in "+element+" box");
								element.sendKeys(strJobDesc);
							}else {
								element.sendKeys(strJobDesc);
								Processing();
							}
							
						}
					}
					Log.info("Job Description set as '"+strJobDesc+"' sucessfully in all boxes" );
					test.log(LogStatus.PASS, "All Job Description should be set", "Job Description set as '"+strJobDesc+"' sucessfully in all boxes" );
					
				}
				else {
					
					WebElement frame= driver.findElement(By.xpath(putility.getProperty("job_desc_first_textbox_frame")));
	                driver.switchTo().frame(frame);
	                
	                WebElement editable = driver.switchTo().activeElement();
	                if(editable.getSize().height >0)
	                	editable.sendKeys(strJobDesc);
	                
	                driver.switchTo().defaultContent();
	                SwitchFramesbyID("ptifrmtgtframe");
	                List<WebElement> element1 = driver.findElements(By.xpath(putility.getProperty("job_desc_textbox_table")));
					int length = element1.size();
					WebElement element = null;
					for(int i = 1; i<=length;i++)
					{
						WebElement table = driver.findElement(By.xpath(putility.getProperty("job_desc_textbox_frame")+i+", press ALT 0 for help.']"));
						if(table.getSize().height >0 )
						{
							Processing();
							element = driver.findElement(By.xpath(putility.getProperty("job_desc_textbox_frame")+i+", press ALT 0 for help.']"));
			                driver.switchTo().frame(element);
			                editable = driver.switchTo().activeElement();
			                String textTest=editable.getText();
			                if(textTest.isEmpty())
			                	editable.sendKeys(strJobDesc);
			                
			                driver.switchTo().defaultContent(); 
			                SwitchFramesbyID("ptifrmtgtframe");
							
						}
					}
	                
					Log.info("Job Description set as '"+strJobDesc+"' sucessfully in all boxes" );
					test.log(LogStatus.PASS, "All Job Description should be set", "Job Description set as '"+strJobDesc+"' sucessfully in all boxes" );
					
				}   
				
			}

		} catch(Exception e){
			getScreenShot(driver,testCaseId, "Error_in_setting_job_Descriptoin",test);
			Reporter.log("*****Error in setting Job Desc in boxes******" +e);
			Log.error("*****Error in setting Job Desc in boxes******" +e);
			test.log(LogStatus.FAIL, "All Job Description should be set",  "Job Description could not be set as '"+strJobDesc+"'  in all boxes because - "+e );
			excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
			excelUtils.setCellData(sheetName, ""+e, testCaseId, "Result_Errors");

		}

	}


	
	 * @date 11/4/2015
	 * @author Devbrath Singh
	 * @description Method used for handling any error or pop up in the screen
	 
	
	public static void setAttribute(WebElement element, String attributeName, String value)
	{
		WrapsDriver wd = (WrapsDriver) element;
		JavascriptExecutor dr = (JavascriptExecutor)wd.getWrappedDriver();
		dr.executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", element, attributeName, value);
	}
	
	
	
	
	 * @date 11/4/2015
	 * @author Devbrath Singh
	 * @description Method used for handling any error or pop up in the screen
	 
	
	public void checkPopUp(ExtentReports extent,ExtentTest test, String sheetName,String testCaseId, String testStep) throws Exception{
		WindowHandles();
			if (existsElement(putility.getProperty("pop_up_confirmation_window"))){
				System.out.println("POP Up has appeared now" );
				getScreenShot(driver,testCaseId, testStep,test);
				WebElement element = driver.findElement(By.xpath(putility.getProperty("pop_up_confirmation_txt")));
				String PopUp_Name = element.getText();
				test.log(LogStatus.FAIL, "Action on "+testStep,"Pop up is being displayed and it is  - "+PopUp_Name );
				driver.findElement(By.xpath(putility.getProperty("error_popup_okbtn"))).click();
				excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
				excelUtils.setCellData(sheetName, PopUp_Name, testCaseId, "Result_Errors");
				Log.info("Error occured because  '"+testStep+"' actoin is not correct");
				throw new Exception("Error occured because  of POP Up - '"+PopUp_Name+"' at step - "+testStep); 
				
			}			
	}
	
	
	
	
	 * @date 11/4/2015
	 * @author Devbrath Singh
	 * @description Method used to check if element exist or not
	 
	
	public boolean existsElement(String strElement) {
	    try {
	    	WebElement element = driver.findElement(By.xpath(strElement));
	    	element.getTagName();
	    	Log.info(element + "Exist");
	        return true;
	
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	}
	
	

	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to set current date and post duration in Job opening page
	 


	public void setDate_PostDuration(ExtentTest test,String sysdate, String postDate,String sheetName,String testCaseId) throws Exception{
		try{
			Processing();
			List<WebElement> postingTable = driver.findElements(By.xpath(putility.getProperty("posting_date_table")));
			int length = postingTable.size();

			for(int i = 0; i<length;i++)
			{
				SetData(test,(putility.getProperty("sys_date_path")+i+"']"), GetCurrentSystemDate());
				Processing();
				if (postDate != null && postDate.trim().length() >0 ){
					SetData(test,(putility.getProperty("posting_date_duration")+i+"']"), postDate);
					Processing();
				}
			}
			test.log(LogStatus.PASS, "Post date and Duration should be entered","Posting date '"+GetCurrentSystemDate()+"' and posting Duration '"+postDate+"' set sucessfully");
			Log.info("Posting date '"+GetCurrentSystemDate()+"' and posting Duration '"+postDate+"' set sucessfully");

		} catch(Exception e){
			getScreenShot(driver,testCaseId, "Error_in_setDate_PostDuration_step",test);
			Log.error("Post Date or Duration could not be entered  :: " +e);
			Reporter.log("Post Date or Duration could not be entered  :: " +e);
			test.log(LogStatus.FAIL, "Posting date '"+GetCurrentSystemDate()+"' and posting Duration  : '"+postDate+"'  entry failed "+e);
			excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
			excelUtils.setCellData(sheetName, ""+e, testCaseId, "Result_Errors");

		}

	}


	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to get current system date
	 

	public String GetCurrentSystemDate(){
		int day, month, year;
		GregorianCalendar date = new GregorianCalendar();
		day = date.get(Calendar.DAY_OF_MONTH);
		month = date.get(Calendar.MONTH);
		year = date.get(Calendar.YEAR);
		String Sysdate = ""+(month+1)+"/"+day+"/"+year;
		return Sysdate;
	}
	
	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to get current system date
	 
	public String GetPreviousDate(){
		int day, month, year;
		GregorianCalendar date = new GregorianCalendar();

		day = date.get(Calendar.DAY_OF_MONTH);
		month = date.get(Calendar.MONTH);
		year = date.get(Calendar.YEAR);
		String Sysdate = ""+(month+1)+"/"+(day-1)+"/"+year;
		return Sysdate;
	}


	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to wait for the page to load until the processing img disappears
	 

	public void Processing(){
		try{
			WebDriverWait wait = new WebDriverWait(driver,300);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));
		}
		catch (Exception e){
			sleep(3000);
		}
		
	}


	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to handle new window
	 

	public void WindowHandles()
	{
		try{
			String handle= driver.getWindowHandle();
			sleep(3000);
			driver.switchTo().window(handle);
		} catch (Exception e){	
		}
		
	}

	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to switch to new handle window
	 
	
	public void switchToWindow(String givenWindow){
		sleep(5000);
		try{
			driver.switchTo().window(givenWindow);
		}catch(Exception e){
			
		}
		
	}

	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to get text from the element
	 

	public String getText(String path){
		String value = driver.findElement(By.xpath(path)).getText();
		return value;
	} 


	
	 * @date 10/27/2015
	 * @author Bupesh 
	 * @description Method used to click clone button in Clone Job Opening Page
	 


	public void Clone_Job(ExtentTest test,String Click_Job, String Create_Clone,String testCaseId) throws Exception{
		try{
			Processing();
			driver.findElement(By.xpath(Click_Job)).click();
			Log.info("Element  '"+Click_Job+"'  clicked sucessfully");

			Processing();
			driver.switchTo().defaultContent();

			WebElement frame= driver.findElement(By.xpath(putility.getProperty("rec_mod_frame")));
			driver.switchTo().frame(frame);

			driver.findElement(By.xpath(Create_Clone)).click();
			Log.info("Element  '"+Create_Clone+"'  clicked sucessfully");
			driver.switchTo().defaultContent();
			Processing();

		} catch(Exception e){
			getScreenShot(driver,testCaseId, "Error_in_CloneJob_step",test);
			Log.error("Look up value could not be entered..." +e);
			Reporter.log("Look up value could not be entered..." +e);

		}
	} 


	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to wait for the specified time interval in seconds
	 

	public void sleep(long millis) 
	{
		try 
		{
			Thread.sleep(millis);
		}catch (InterruptedException e){
			Log.error("Exception :" + e.getMessage());
		}
	}

	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to wait for the specified time interval in seconds
	 
	
	public String calcullateTime(Date startTime, Date endTime) {
		String time = "";
		Long timeTaken = endTime.getTime() - startTime.getTime();
		int intTimeTaken = timeTaken.intValue();
		int milliSecs = intTimeTaken % 1000;
		int secs = (intTimeTaken/1000) % 60;
		int hrs = ((intTimeTaken/1000)/60)/60;
		int mins = ((intTimeTaken/1000)/60) % 60;
		if (hrs > 0)
			time = hrs + " hours ";
		if (mins > 0)
			time = time + mins + " minutes ";
		
		time = time + secs +"."+milliSecs+ " seconds ";
		return time;
	}

	
	 * @date 10/19/2015
	 * @author Devbrath Singh
	 * @description Method used to wait for the specified time interval in seconds
	 
	
	 public void Driverquit(){
		 driver.quit();
	 }
	 
	 public void DriverClose(){
		 driver.close();
	 }



	 public void HandleAlert(String msg){
		 try{
			 driver.switchTo().alert().sendKeys(msg); 
		 }
		 catch(Exception e){
			 System.out.println("Exception is - "+e);
		 }
	 }


		public void ConfirmAlert(){
			 try{
				 driver.switchTo().alert().accept();
			 }
			 catch(Exception e){
				 System.out.println("Exception is - "+e);
			 }
			
		 }

	 
		
		 * @date 11/02/2015
		 * @author 
		 * @description Method used to verify the element  is selected or not
		 

		public String  ElementSelected(String objPath){
			WebElement option ;
			String value = null;
			try
			{
				Select select = new	Select(driver.findElement(By.xpath(objPath)));
				option = select.getFirstSelectedOption();
				value = option.getText();
			}catch (NoSuchElementException e) {
				Log.error("Exception :" + e.getMessage());
				System.out.println("Exception :" + e.getMessage());
			}
			return value;
		}

		
		 * @date 11/02/2015
		 * @author  
		 * @description Method used to verify the checkbox  is selected or not
		  

		public boolean selectCheckBox(String objPath) {
			try{
				if ( !driver.findElement(By.xpath(objPath)).isSelected() )
				{
					driver.findElement(By.xpath(objPath)).click();
					WindowHandles();
					driver.findElement(By.xpath("//div[@id = 'alertbutton']//a//input[@id = '#ICYes']")).click();
					Processing();
					return true;
					
				}
				else{
					return false;
				}
			}catch (Exception e){
				Log.error("Exception on Selecting Check Box :" + e.getMessage());
				Reporter.log("Exception on Selecting Check Box :" + e.getMessage());
				return false;
				
			}

			
		}

		
		  @date 11/02/2015
		  * @author  
		  * @description Method used to clear the text box
		  

		public void clearTextBox(String xpathObj){
			try{

				driver.findElement(By.xpath(xpathObj)).clear();

			}catch(Exception e)
			{
				Log.error("Exception on Clearing Text Box :" + e.getMessage());
				Reporter.log("Exception on Clearing Text Box :" + e.getMessage());
			}

		}


			
			 * @date 11/02/2015
			 * @author  
			 * @description Method used to do double click on the element
			 


		public void doubleClick(String objectPath){
			try{
				Actions action = new Actions(driver);
				WebElement element=driver.findElement(By.xpath(objectPath));
				//Double click
				action.doubleClick(element).perform();
			}catch(Exception e){
				Reporter.log("Double click failed  -"+e);
				Log.error("Double click failed on - "+objectPath);
				
			}
			
		}
		
		public void SetDate(ExtentTest test,String objPath, Date dlExpDate,String testCaseId) throws Exception{
			if(driver.findElement(By.xpath(objPath)).getSize().height> 0){
				try{
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
					String reportDate = df.format(dlExpDate);
					WebElement webElement = driver.findElement(By.xpath(objPath));
					webElement.clear();
					webElement.sendKeys(reportDate);
					Log.info("Date  '"+dlExpDate+"' entered successfully in the edit box");
				}catch (Exception e){
					getScreenShot(driver,testCaseId, "Error_in_CloneJob_step",test);
					Log.error("Staffing :: Set data () :: Set data failed:"+e);
					System.out.println("Data  '"+dlExpDate+"' could not be entered - " +e);
					Reporter.log("Staffing ::Set data () :: Set data failed:"+e);

				}	 
			}
		}
		
		
		public String getValue(String path){
			String value = driver.findElement(By.xpath(path)).getAttribute("value");
			return value;
		}

		
		
		public void default_content()
		{
			driver.switchTo().defaultContent();
		}
		
		public String getCurrWindowName(){
			try{
				String winHandleBefore = driver.getWindowHandle();
				return winHandleBefore;
			}
			catch(Exception e){
				return null;
			}
			
		}
		
		
		
		public String rec_switchFrame(String frameObj){
			Processing();
			driver.switchTo().defaultContent();
			WebElement frame= driver.findElement(By.xpath(frameObj));
			String frame_name = frame.getAttribute("name");
		    driver.switchTo().frame(frame);
			return frame_name;
		}
		
		
		public void rec_action1(ExtentTest test,String selectObj, String status, String jobObj, String Job_id, String submitObj,String sheetName,String testCaseId ){
			try{
				rec_switchFrame(putility.getProperty("rec_mod_frame"));
				SelectDropDown(test,selectObj, status,sheetName,testCaseId);
			    //SelectDropDown(selectObj,status);
			    //SetData(jobObj,Job_id);
			    driver.findElement(By.xpath(jobObj)).sendKeys(Job_id);
			    sleep(3000);
			    clickElement(test,submitObj,"submitObj", testCaseId);    
			    System.out.println("Processing   popup");
			     ProcessingRE2E();
			     System.out.println("Test Processlnk");
			    sleep(3000);
			    driver.switchTo().defaultContent();

			}catch (Exception e){
				Reporter.log("Error occured  --"+e);
				Log.error("Error occured  - " +e);

			}

		} 
		
		
		public String chk_popup(ExtentTest test,String popObj,String canObj,String testCaseId){
			String get_popup_text = null;
			WindowHandles();
			try {

				if (existsElement(popObj)){
			    	get_popup_text = driver.findElement(By.xpath(popObj)).getText();
			    	Log.info("Pop up displayed as  - "+get_popup_text);
			    	Reporter.log("Pop up displayed as  - "+get_popup_text);
			    	clickElement(test,canObj,"canObj", testCaseId);
			    	default_content();
				}

			}
			catch (Exception e){
				System.out.println("No Popup Found "+e);
			}
			return get_popup_text;
	} 
		
		public void rec_create_interview(ExtentTest test,String interObj,String interType,String selectObj,String applyObj,String apply_value,String clickObj,String clkgoObj,
				String overallObj,String overallValue,String recommObj,String recommValue,String submitObj,String sheetName,String testCaseId ) throws Exception{
		    String get_frame = rec_switchFrame(putility.getProperty("rec_mod_frame"));
		    SetData(test,putility.getProperty("rec_sys_date"), GetCurrentSystemDate());
		    SelectDropDown(test,interObj,interType,sheetName,testCaseId);
		    ProcessingRE2E();
		    clickElement(test,selectObj,"selectObj", testCaseId);  
		    ProcessingRE2E();
		    clickElement(test,applyObj,"applyObj", testCaseId);  
		    String out[] = get_frame.split("_");
		    int a = Integer.parseInt(out[1]);
			int b = 1;
			int res = a + b;
			String dynamic_frame = "//iframe[contains(@id,'ptModFrame_"+res+"')]";
			System.out.println("dynamic_frame"+dynamic_frame);
			rec_switchFrame(dynamic_frame);
			if(existsElement(clickObj+" '"+apply_value+"']")){
				clickElement(test,clickObj+" '"+apply_value+"']","clickObj+apply_value", testCaseId);  
			    ProcessingRE2E();
			}
			else{
				clickElement(test, putility.getProperty("lookup_button"), "lookup_button", testCaseId);
				clickElement(test,clickObj+" '"+apply_value+"']","clickObj+apply_value", testCaseId);  
			    ProcessingRE2E();
			}
		    
		    driver.switchTo().defaultContent();

		    rec_switchFrame(putility.getProperty("rec_mod_frame"));
		    clickElement(test,clkgoObj,"clkgoObj", testCaseId);
		    
		    if(existsElement(clkgoObj))
		    clickElement(test,clkgoObj,"clkgoObj", testCaseId); 
		    
		    ProcessingRE2E();
		    SelectDropDown(test,overallObj,overallValue,sheetName,testCaseId);
		    SelectDropDown(test,recommObj,recommValue,sheetName,testCaseId);
		    driver.findElement(By.xpath(putility.getProperty("rec_general_cmt"))).sendKeys(putility.getProperty("job_description_txt"));
		    //SetData(putility.getProperty("rec_general_cmt"), putility.getProperty("job_description_txt"));
		    sleep(3000);
		    clickElement(test,submitObj,"submitObj", testCaseId);  
		    System.out.println("clicked Submit evaluation");
		    sleep(3000);
		    //ProcessingRE2E();
		    System.out.println("Test Process frame");
		    driver.switchTo().defaultContent();
		    } 
		
		
		public void rec_check_reloc(ExtentTest test,String relObj,String relValue,String sheetName,String testCaseId){
			try{
			if (existsElement(relObj) == true){
			//if (driver.findElement(By.xpath(relObj)).isDisplayed()){
				System.out.println("Relocation Enabled");
				 SelectDropDown(test,relObj,relValue,sheetName,testCaseId);
			}
			else{
				System.out.println("Relocation field not exist");
			}
			}
			catch(Exception e){
				System.out.println("Could not check relocation element " +e);
			}

			} 
		
		
		
		public void rec_check_total_wage(ExtentTest test,String chkObj,String totalObj,String totalValue){
			try{
			if (existsElement(chkObj) == true){
				System.out.println("Total Wage Enabled");
				SetData(test,totalObj, totalValue);
			}
			else{
				System.out.println("Total Wage field not exist");
			}
			}
			catch(Exception e){
				System.out.println("Could not check total wage element " +e);
			}

			} 
		
		
		
		public void rec_check_status(ExtentTest test,String statusObj,String chkObj,String chkValue,String subObj,String newObj,String sheetName,String testCaseId){
			try {
				if (driver.findElement(By.xpath(statusObj)).getTagName().equals("span")){
					System.out.println("Job status not enabled");
					if (driver.findElement(By.xpath(newObj)).getTagName().equals("span")){
						SelectDropDown(test,chkObj,chkValue,sheetName,testCaseId);
						clickElement(test,subObj,"subObj", testCaseId);   
						ProcessingRE2E();	
					}
					else{
						clickElement(test,subObj,"subObj", testCaseId);   
						ProcessingRE2E();
						SelectDropDown(test,chkObj,chkValue,sheetName,testCaseId);
						clickElement(test,subObj,"subObj", testCaseId);    
						ProcessingRE2E();
					}
				}
			} catch (Exception e)
			{
				System.out.println("Exception in checking Job Status  :: " +e);
			}
			} 
		
		public void ProcessingRE2E(){
			try{
			WebDriverWait wait = new WebDriverWait(driver,3000);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(putility.getProperty("processing_property"))));
			
			} catch(Exception e) {
				//System.out.println("Processing time exceed");
				sleep(5000);
			}
		}
		
		
		
		 * @date 10/19/2015
		 * @author Devbrath Singh
		 * @description Method used to handle new window
		 
		public void alert(String job_id){
			try{
				Alert alert=driver.switchTo().alert();
				driver.switchTo().alert().sendKeys(job_id);
				System.out.println("Alert message == " +alert.getText());
				alert.accept();
				Processing();
			}catch(Exception e){
				
			}
			
		}
		
		
		
		public String get_sunday(int d)
		{
			String sun_date = null;
			// Get calendar set to current date and time
			Calendar cal = Calendar.getInstance();

			// Set the calendar to monday of the current week
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

			// Print dates of the current week starting on Monday
			DateFormat df = new SimpleDateFormat("EEE MM/dd/yyyy");
			for (int i = 0; i < d; i++) {
				String get_sun = df.format(cal.getTime());
				//if (get_sun.contains("Sun"))
				if (i == d - 1)	
				{
				String out[] = get_sun.split(" ");
				sun_date = out[1];
			    System.out.println(df.format(cal.getTime()));
			    System.out.println(out[1]);
				}
			    cal.add(Calendar.DATE, 7);
			}
			return sun_date;
		} 
		
		
		
		
		public boolean checkApplication() throws Exception {
            boolean flag = true;
            ExcelUtils.setExcelFile(DATA_FILEPATH, "Create_JO");
            String strUrl = ExcelUtils.getExcelData("URL", "CJO-1");
            if((MyConstants.browser).equalsIgnoreCase("FIREFOX"))
            {
                  if((MyConstants.env).equalsIgnoreCase("LOCAL"))
                         driver = new FirefoxDriver();
                  else
                  {
                         String hub =putility.getProperty("SELENIUM_HUB_URL_"+MyConstants.env);
                         threadDriver = new ThreadLocal<RemoteWebDriver>();
                         DesiredCapabilities dc = new DesiredCapabilities();
                    FirefoxProfile fp = new FirefoxProfile();
                    dc.setCapability(FirefoxDriver.PROFILE, fp);
                    dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
                    threadDriver.set(new RemoteWebDriver(new URL(hub), dc));                  
                    driver = threadDriver.get();
                  }      
            }
            
            if((MyConstants.browser).equalsIgnoreCase("IE"))
            {
                  if((MyConstants.env).equalsIgnoreCase("LOCAL"))
                  {
                         DesiredCapabilities dc =  DesiredCapabilities.internetExplorer();
                   dc.setPlatform(Platform.WINDOWS);
                   dc.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
                         dc.setVersion("11");  
                         System.setProperty("webdriver.ie.driver", "C:\\selenium\\workspace\\VzStaffingApp\\lib\\IEDriverServer.exe");
                         driver =new InternetExplorerDriver();                          
                  }
                  else
                  {
                         threadDriver = new ThreadLocal<RemoteWebDriver>(); 
                         DesiredCapabilities dc =  DesiredCapabilities.internetExplorer();
                         dc.setPlatform(Platform.WINDOWS);
                         dc.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
                         dc.setVersion("11");  
                         String hub =putility.getProperty("SELENIUM_HUB_URL_"+MyConstants.env);
                         threadDriver.set(new RemoteWebDriver(new URL(hub), dc));
                         driver = threadDriver.get(); 
                  }
            }
                  
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().deleteAllCookies();
            driver.get(strUrl);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            
            WebElement element = driver.findElement(By.linkText(putility.getProperty("english_link")));
            if (element.getTagName() == null || element.getTagName().length() == 0) {
                  System.out.println("APPLICATION IS NOT RESPONDING.");
                  flag = false;
            }
            driver.quit();
            return flag;
     }

		
		
		
		public void getScreenShot(WebDriver driver, String testCase, String testStep, ExtentTest test) throws Exception {
			try{
				WebDriver augmentedDriver = new Augmenter().augment(driver);
				File screenshot = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
				Calendar c = new GregorianCalendar();
				c.setTime(new Date());
				String file_name = "SS_"+testCase+"_"+testStep+"-"+ c.get(Calendar.DATE)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR)+"_"+c.get(Calendar.HOUR_OF_DAY)+"-"+c.get(Calendar.MINUTE)+"-"+c.get(Calendar.SECOND)+".png"; 
			    File file = new File(SCREENSHOT_PATH+""+file_name);
			    FileUtils.copyFile(screenshot, file);	
			    test.log(LogStatus.INFO, "Snapshot for  "+testStep+"  : " + test.addScreenCapture(REPORT_SCREENSHOT_PATH+""+file_name)); 
			}
			catch (Exception e){
				e.printStackTrace();
				System.out.println("ERROR IN SCREENSHOT.");
			}
		} 

		
		public boolean executeTestCase(String sheetname, String testCase) throws Exception {
			boolean flag = true;
			ExcelUtils.setExcelFile(DATA_FILEPATH,sheetname);
			String tcResult = ExcelUtils.getExcelData("Result_Errors",testCase);
			if (tcResult != null && tcResult.trim().length() > 0 && tcResult.equalsIgnoreCase(putility.getProperty("DB_NO_ROW_ERROR"))) 
				flag = false;
			return flag;
		} 
		
		public void selectClosestDate(ExtentTest test,String objPath,String sheetName,String testCaseId) throws Exception{
			try{
				Select oSelect = new Select(driver.findElement(By.xpath(objPath)));
				List<WebElement> options = oSelect.getOptions();

				int selectedDateIndex = 0;
				long totalMilliInDay = 24*60*60*1000;
				for (int i=0;i<options.size();i++){
					String optionValue = (options.get(i).getAttribute("value")).trim();	
					if(optionValue !=null && optionValue.length() > 0){
						long differenceDate = ((new SimpleDateFormat("yyyy-MM-dd")).parse(optionValue)).getTime() - (new Date()).getTime();
						if (differenceDate >= 0 || totalMilliInDay > Math.abs(differenceDate)){
							selectedDateIndex = i -1;
							break;
						}					
					}
				}
				oSelect.selectByIndex(selectedDateIndex);
			} catch (Exception e){
				Log.error("Staffing :: Dropdown selection failed..." +e);	
				test.log(LogStatus.FAIL,"Drop down should be selected", "Drop down for closest date could not be selected because -"+e);
				Reporter.log("Closest date from the dropdown could not be selected   - "+e);
				excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
				excelUtils.setCellData(sheetName, ""+e, testCaseId, "Result_Errors");
			}
		} 
		
		
		
		
		public void DeSelectDropDown(ExtentTest test,String objPath, String value,String sheetName,String testCaseId) throws Exception{
			try{
				if (value != null){
					Select oSelect = new Select(driver.findElement(By.xpath(objPath)));
					oSelect.selectByValue(value);
					Processing();
					Log.info("Selected - '"+value+"' from the dropdown");
					test.log(LogStatus.PASS, "Drop down should be selected","Drop down value -'"+value+"'- selected sucessfully");
					Reporter.log("Selected - '"+value+"' from the dropdown");
				}
			} catch (Exception e){
				Log.error("Staffing :: Dropdown selection failed..." +e);	
				test.log(LogStatus.WARNING,"Drop down should be selected", "Drop down value -'"+value+"'- could not be selected because -"+e);
				Reporter.log("Value - '"+value+"' from the dropdown could not be selected   - "+e);
				excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
				excelUtils.setCellData(sheetName, ""+e, testCaseId, "Result_Errors");
			}
		} 
		
		
		public void EnterData(String objPath, String value){
			try{
				WebElement webElement =driver.findElement(By.xpath(objPath));
				webElement.clear();
				webElement.sendKeys(value);
			}catch (Exception e){
				System.out.println("Data  '"+value+"' could not be entered - " +e);
				Reporter.log("Staffing ::Set data () :: Set data failed:"+e);

			}	 
		} 
		
		
        public boolean Register_Now_Page(ExtentReports extent,ExtentTest test,String Username, String uPWD,String ConfirmPWD,String SecurityQuestion,String SecurityAnswer,String EmailAddress,String sheetName,String testCaseId) throws Exception{
            
            //Enter details in Register Now page
            
            SetData(test,putility.getProperty("reg_username"), Username);
            test.log(LogStatus.PASS, "Username should be entered","Username - '"+Username+"' enterd successsfully" );
            
            SetData(test,putility.getProperty("reg_pasword"), uPWD);
            test.log(LogStatus.PASS, "uPWD should be entered","uPWD - '"+uPWD+"' enterd successsfully" );
            
            SetData(test,putility.getProperty("reg_confirm_pasword"), ConfirmPWD);
            test.log(LogStatus.PASS, "ConfirmPWD should be entered","ConfirmPWD - '"+ConfirmPWD+"' enterd successsfully" );
            
            SelectDropDown(test, putility.getProperty("reg_security_question"), SecurityQuestion, "Apply_to_JO", testCaseId);

            SetData(test,putility.getProperty("reg_security_answer"), SecurityAnswer);
            test.log(LogStatus.PASS, "SecurityAnswer should be entered","SecurityAnswer - '"+SecurityAnswer+"' enterd successsfully" );
            
            SetData(test,putility.getProperty("reg_email_id"), EmailAddress);
            test.log(LogStatus.PASS, "EmailAddress should be entered","EmailAddress - '"+EmailAddress+"' enterd successsfully" );
            
            clickElement(test, putility.getProperty("reg_register_btn"),"reg_register_btn",testCaseId);
            Processing();
            WindowHandles();
            if(existsElement(putility.getProperty("error_popup_okbtn"))){
            	 default_content();
                 checkPopUp(extent,test, "Apply_to_JO", testCaseId, "Error while registering the user");
                 default_content();
                 System.out.println("NEXT btn was not found and not clicked");
                 return false;	
            }
            else{
            	default_content();
                rec_switchFrame(putility.getProperty("rec_mod_frame"));
                if (existsElement(putility.getProperty("reg_apply_nxt_btn"))){
    	            clickElement(test, putility.getProperty("reg_apply_nxt_btn"),"reg_apply_nxt_btn",testCaseId);
    	            //Changing back the frame
    	            default_content();
                }
                return true;
            }
        }
            
        
        
        
            public boolean My_Profiles_Page(ExtentReports extent,ExtentTest test,String FirstName,String LastName,String TimeatCurrentAddress,String Country,String Address1,String Street_address,String ZipCode,String Postal,String City,String State,String PhoneType,String Telephone,String BestContact,String sheetName,String testCaseId) throws Exception{
                try{
                	WebDriverWait wait = new WebDriverWait(driver, 40);
            		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(putility.getProperty("myprofile_first_name"))));
                    SetData(test,putility.getProperty("myprofile_first_name"), FirstName);
                    test.log(LogStatus.PASS, "FirstName should be entered","FirstName - '"+FirstName+"' enterd successsfully" );
                    
                    SetData(test,putility.getProperty("myprofile_last_name"), LastName);
                    test.log(LogStatus.PASS, "LastName should be entered","LastName - '"+LastName+"' enterd successsfully" );
                    
                    SelectDropDown(test, putility.getProperty("myprofile_time_at_acrr_address"), TimeatCurrentAddress, "Apply_to_JO", testCaseId);
                    
                    SelectDropDown(test, putility.getProperty("myprofile_country"), Country, "Apply_to_JO", testCaseId);
                    
                    if(Address1 != null && Address1.trim().length() >0){
	                    SetData(test,putility.getProperty("myprofile_address1"), Address1);
	                    test.log(LogStatus.PASS, "Address1 should be entered","Address1 - '"+Address1+"' enterd successsfully" );
                    }
                    
                    if(Street_address != null && Street_address.trim().length() >0){
                    	SetData(test,putility.getProperty("myprofile_address1"), Street_address);
	                    SetData(test,putility.getProperty("myprofile_address2"), Street_address);
	                    test.log(LogStatus.PASS, "Street_address should be entered","Street_address - '"+Street_address+"' enterd successsfully" );
                    }
                    if(ZipCode != null && ZipCode.trim().length() >0){
	                    SetData(test,putility.getProperty("myprofile_zip_code"), ZipCode); 
	                    test.log(LogStatus.PASS, "ZipCode should be entered","ZipCode - '"+ZipCode+"' enterd successsfully" );
                    }
                    
                    if(Postal != null && Postal.trim().length() >0){
	                    SetData(test,putility.getProperty("myprofile_zip_code"), Postal);
	                    test.log(LogStatus.PASS, "Postalcode should be entered","Postalcode - '"+Postal+"' enterd successsfully" );
                    }
                    
                
                    
                    SelectDropDown(test, putility.getProperty("myprofile_state"), State, "Apply_to_JO", testCaseId);
                    
                    SelectDropDown(test, putility.getProperty("myprofile_phone_type"), PhoneType, "Apply_to_JO", testCaseId);
                    
                    SetData(test,putility.getProperty("myprofile_telephone"), Telephone);
                    test.log(LogStatus.PASS, "Telephone should be entered","Telephone - '"+Telephone+"' enterd successsfully" );
                    
                    ProcessingRE2E();
                    if(BestContact.equalsIgnoreCase("Y")){
                                    clickElement(test, putility.getProperty("myprofile_best_contact_chkbox"), "myprofile_best_contact_chkbox", testCaseId);
                    }
                    
                    if(City != null && City.trim().length() >0){
                    	SetData(test,putility.getProperty("myprofile_city"), City);
                        test.log(LogStatus.PASS, "City should be entered","City - '"+City+"' enterd successsfully" );
                    }
                    
                    ProcessingRE2E();
                    singleClick(test,putility.getProperty("myprofile_save_btn"),"myprofile_save_btn",testCaseId);
                    ProcessingRE2E();
                    WindowHandles();
                    if(existsElement(putility.getProperty("error_popup_okbtn"))){
                    	 default_content();
                         checkPopUp(extent,test, "Apply_to_JO", testCaseId, "Error while entering details in My profile page");
                         default_content();
                         return false;	
                    }
                    else{
                    	default_content();
                    	return true;
                    }                                
                }catch (Exception e){
	                Log.error("Error occured while entering data in My Proifile page  - "+e);
	                Reporter.log("Error occured while entering data in My Proifile page  - "+e);
	                excelUtils.setCellData("Apply_to_JO", "FAIL", testCaseId, "Result_Status");
	                excelUtils.setCellData("Apply_to_JO", ""+e, testCaseId, "Result_Errors");
	                test.log(LogStatus.FAIL,"Details enetring in My Profile Page", "Error occured while entering data in My Proifile page  - "+e );
	                return false;
                }				                                              
                                                                            
                            
            }
            
            
            public void singleClick(ExtentTest test,String objectPath,String Elementname,String testCaseId) throws Exception{
                try{
                    Actions action = new Actions(driver);
                    WebElement element=driver.findElement(By.xpath(objectPath));
                    //Double click
                    action.moveToElement(element).click().perform();
                    Log.info(Elementname+" - having xpath '"+objectPath+"' clicked sucessfully");
                    test.log(LogStatus.PASS, "Element should be clicked sucessfully",Elementname+" - having xpath '"+objectPath+"' clicked sucessfully");
                }catch(Exception e){
                    Log.error(Elementname+" - having xpath '"+objectPath+"' could not be clicked   -  "+e);
                    System.out.println(Elementname+" - having xpath '"+objectPath+"' clicked sucessfully");
                    Reporter.log(Elementname+" - having xpath '"+objectPath+"' could not be clicked ");
                    test.log(LogStatus.FAIL, "Element should be clicked sucessfully",Elementname+" - having xpath  '"+objectPath+"' could not be clicked because -" +e );
                    getScreenShot(driver,testCaseId, "'"+Elementname+"'  could not be clicked because of "+e,test);
                                
                }
                            
            }
            
            public boolean SearchJob(ExtentReports extent,ExtentTest test,String JO,String sheetName,String testCaseId ) throws Exception{
                try{
                	WebDriverWait wait = new WebDriverWait(driver, 40);
            		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(putility.getProperty("job_code_srch_box"))));
                    //Searching for a job to apply
            		if(existsElement(putility.getProperty("job_code_srch_box"))){
            			 SetData(test, putility.getProperty("job_code_srch_box"), JO);
                         test.log(LogStatus.PASS, "Job ID should be entered","Job ID - '"+JO+"' enterd successsfully" );
                         SelectDropDown(test, putility.getProperty("job_country"), "-- Select --", "Apply_to_JO", testCaseId);
                         Processing();
                         clickElement(test, putility.getProperty("job_srch_btn"), "job_srch_btn", testCaseId);
                         ProcessingRE2E();
                         //Checking for error message
                         if(existsElement(putility.getProperty("rec_mod_frame"))) {
                        	 rec_switchFrame(putility.getProperty("rec_mod_frame"));
                             if (existsElement(putility.getProperty("error_msg_ok_btn"))){
                            	 String PopUpContent = getText(putility.getProperty("error_content"));
                            	 test.log(LogStatus.FAIL, "Job ID should be entered","Job ID - '"+JO+"' enterd but not found in the page" );
                 	            clickElement(test, putility.getProperty("error_msg_ok_btn"),"error_msg_ok_btn",testCaseId);
                 	           excelUtils.setCellData("Apply_to_JO", "FAIL", testCaseId, "Result_Status");
                               excelUtils.setCellData("Apply_to_JO", PopUpContent, testCaseId,"Result_Errors");
                 	            //Changing back the frame
                 	            default_content();
                 	           throw new Exception("Error on searching for Job  - "+JO+" and the error is - "+PopUpContent);
                             }
                             return false;
                         }		
                         clickElement(test, putility.getProperty("searched_job_chkbox"), "searched_job_chkbox", testCaseId);
                         ProcessingRE2E();
                         clickElement(test, putility.getProperty("apply_now_btn"), "apply_now_btn", testCaseId);
                         ProcessingRE2E();
                         WindowHandles();
                         if(existsElement(putility.getProperty("error_popup_okbtn"))){
                         	  default_content();
                              checkPopUp(extent,test, "Apply_to_JO", testCaseId, "Error while searching for the job");
                              default_content();
                              return false;	
                         }
                         else{
                         	default_content();
                         	return true;
                         }		
            		}
            		else {
                		Log.error("Job Search page not found");
                		test.log(LogStatus.FAIL, "Job Search page", "Usage: <span style='font-weight:bold;'>Job Search page not found</span>");
                		excelUtils.setCellData("Apply_to_JO", "FAIL", testCaseId, "Result_Status");
                        excelUtils.setCellData("Apply_to_JO", "Work Details page was not found so the details could not be entered", testCaseId,"Result_Errors");
                		return false;
                	}
                   
                                
                }catch (Exception e){
                    Reporter.log("Error occured while searching for job : "+JO+"  for test case -"+ testCaseId+"  and Exception is : "+ e);
                    Log.info("Error occured while searching for job : "+JO+"  for test case -"+ testCaseId+"  and Exception is : "+ e);
                    test.log(LogStatus.FAIL,"Error occured while searching for job : "+ testCaseId+" Exceptions : "+ e);
                    excelUtils.setCellData("Apply_to_JO", "FAIL", testCaseId, "Result_Status");
                    excelUtils.setCellData("Apply_to_JO", "Error occured while searching for job : "+JO+"  for test case -"+ testCaseId+"  and Exception is : "+ e, testCaseId, "Result_Errors");
                    throw new Exception("Error occured while searching for job : "+JO+"  for test case -"+ testCaseId+"  and Exception is : "+ e);
                }
                            
            }
            
            
            public void chooseDate(ExtentTest test,String date,String monthPath,String yearPath,String sheetName,String testCaseId) throws Exception{
            	String[] splitted_date = date.split("/");
            	String month =splitted_date[0];
            	String year =splitted_date[2];
            	System.out.println("month is "+month+"  and year is "+year);
            	SelectDropDown(test, monthPath, month, sheetName, testCaseId);
            	ProcessingRE2E();
            	SelectDropDown(test, yearPath, year, sheetName, testCaseId);
            	ProcessingRE2E();

            }
            
            
            public boolean workDetailsPage(ExtentReports extent,ExtentTest test,String Current_Employer,String May_We_Contact,String Start_Date,String Supv_Name,String Supv_Tele,String Employer,String Job_Title,String Responsibilities,
            		String Last_Salary,String Sal_Per,String Sal_Currency,String Last_Commission,String Comm_Per,String Comm_Currency,String Work_Country,String Work_Address1,
            		String Work_Street_and,String Work_Zipcode,String Work_Postal,String Work_City,String Work_State,String sheetName,String testCaseId) throws Exception{
            	try{
            		ProcessingRE2E();
            		WebDriverWait wait = new WebDriverWait(driver, 40);
            		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(putility.getProperty("add_wrk_history_link"))));
            		if(existsElement(putility.getProperty("add_wrk_history_link"))){
            			 test.log(LogStatus.INFO, "Work History details page", "Usage: <span style='font-weight:bold;'>Work History details page found,details will be entered now</span>"); 
                		clickElement(test, putility.getProperty("add_wrk_history_link"), "add_wrk_history_link", testCaseId);
                        ProcessingRE2E();
                    	if(Current_Employer.equalsIgnoreCase("Y")){
                    		clickElement(test, putility.getProperty("current_employr_chkbox"), "current_employr_chkbox", testCaseId);
                    		ProcessingRE2E();
                    	}
                    	if(May_We_Contact.equalsIgnoreCase("Y"))
                    	clickElement(test, putility.getProperty("may_we_contact_rdiobtn"), "may_we_contact_rdiobtn", testCaseId);
                    	ProcessingRE2E();
                    	
                    	chooseDate(test, Start_Date, putility.getProperty("sel_start_date_month"), putility.getProperty("sel_start_year"), "Apply_to_JO", testCaseId);
                    	
                    	SetData(test, putility.getProperty("supervisor_name"), Supv_Name);
                    	ProcessingRE2E();
                    	SetData(test, putility.getProperty("supervisor_telephone"), Supv_Tele);
                    	ProcessingRE2E();
                    	
                    	SetData(test, putility.getProperty("employer_editbox"), Employer);
                    	ProcessingRE2E();
                    	SetData(test, putility.getProperty("job_title_editbox"), Job_Title);
                    	ProcessingRE2E();
                    	SetData(test, putility.getProperty("roles_and_respns"), Responsibilities);
                    	ProcessingRE2E();
                    	//ENterig country
                    	SelectDropDown(test, putility.getProperty("work_country"), Work_Country, "Apply_to_JO", testCaseId);
                    	ProcessingRE2E();
                    	
                    	SetData(test,putility.getProperty("work_zip_code"), Work_Zipcode); 
        				test.log(LogStatus.PASS, "Work_Zipcode should be entered","Work_Zipcode - '"+Work_Zipcode+"' enterd successsfully" );
        				ProcessingRE2E();
        				 
        				SetData(test,putility.getProperty("work_zip_code"), Work_Postal);
        				ProcessingRE2E();
        				//Just checking unpaid emplyer chk boc to refresh the page
        				clickElement(test, putility.getProperty("unpaid_position_chkbox"), "unpaid_position_chkbox", testCaseId);
                		ProcessingRE2E();
                		clickElement(test, putility.getProperty("unpaid_position_chkbox"), "unpaid_position_chkbox", testCaseId);
                		ProcessingRE2E();
                		
                		
                    	//Entering Salary
                    	SetData(test, putility.getProperty("last_salary"), Last_Salary);
                    	ProcessingRE2E();
                    	SelectDropDown(test, putility.getProperty("sal_per"), Sal_Per, "Apply_to_JO", testCaseId);
                    	ProcessingRE2E();
                    	SelectDropDown(test, putility.getProperty("sal_currency"), Sal_Currency, "Apply_to_JO", testCaseId);
                    	
                    	//Entering commision
                    	SetData(test, putility.getProperty("last_commision"), Last_Commission);
                    	ProcessingRE2E();
                    	SelectDropDown(test, putility.getProperty("commision_per"), Comm_Per, "Apply_to_JO", testCaseId);
                    	ProcessingRE2E();
                    	SelectDropDown(test, putility.getProperty("commision_curr"), Comm_Currency, "Apply_to_JO", testCaseId);
                    	ProcessingRE2E();
                    	
                    	//Entering Work country or state details
                    	SelectDropDown(test, putility.getProperty("work_country"), Work_Country, "Apply_to_JO", testCaseId);
                    	ProcessingRE2E();
                    	
                    	SetData(test,putility.getProperty("work_address1"), Work_Address1);
        				test.log(LogStatus.PASS, "Work_Address1 should be entered","Work_Address1 - '"+Work_Address1+"' enterd successsfully" );
        				ProcessingRE2E();
        				 
        				SetData(test,putility.getProperty("work_address2"), Work_Street_and);
        				test.log(LogStatus.PASS, "Work_Street_and should be entered","Work_Street_and - '"+Work_Street_and+"' enterd successsfully" );
        				ProcessingRE2E();
        				 
        				SetData(test,putility.getProperty("work_zip_code"), Work_Zipcode); 
        				test.log(LogStatus.PASS, "Work_Zipcode should be entered","Work_Zipcode - '"+Work_Zipcode+"' enterd successsfully" );
        				ProcessingRE2E();
        				 
        				SetData(test,putility.getProperty("work_zip_code"), Work_Postal);
        				ProcessingRE2E();
        				 
        				SetData(test,putility.getProperty("work_city"), Work_City);
        				ProcessingRE2E();
        				 
        				SelectDropDown(test, putility.getProperty("work_state"), Work_State, "Apply_to_JO", testCaseId);	
        				ProcessingRE2E();
        				clickElement(test, putility.getProperty("work_save_btn"), "work_save_btn", testCaseId);
        				ProcessingRE2E();
        				WindowHandles();
                        if(existsElement(putility.getProperty("error_popup_okbtn"))){
                        	 default_content();
                             checkPopUp(extent,test, "Apply_to_JO", testCaseId, "Error while searching for the job");
                             default_content();
                             return false;	
                        }
                        else{
                        	default_content();
                        	clickElement(test, putility.getProperty("continue_to_step3_btn"), "continue_to_step3_btn", testCaseId);
            	            ProcessingRE2E();
                    		return true;
                        }	
        	            
                	}
                	else {
                		Log.error("Work History page not found");
                		test.log(LogStatus.FAIL, "Work History details page", "Usage: <span style='font-weight:bold;'>Work History details page not found</span>");
                		excelUtils.setCellData("Apply_to_JO", "FAIL", testCaseId, "Result_Status");
                        excelUtils.setCellData("Apply_to_JO", "Work Details page was not found so the details could not be entered", testCaseId,"Result_Errors");
                		return false;
                	}
            		
            	}catch (Exception e){
            		Log.error("Work History page error -"+e);
            		Reporter.log("Work History page error -"+e);
            		test.log(LogStatus.FAIL, "Work History details page","Error occured in Work History details page  - "+e );
            		
            		return false;
            	}
            	
            	
            }
            
            public boolean educationDetailsPage(ExtentReports extent,ExtentTest test,String Edu_Country,String Edu_State,String Edu_City,String Edu_Degree,String Edu_Major,String Edu_School,String Edu_Graduated,String Edu_Grad_Dt,String sheetName,String testCaseId) throws Exception{
            	try{
            		ProcessingRE2E();
            		WebDriverWait wait = new WebDriverWait(driver, 40);
            		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(putility.getProperty("edu_add_uiv_colg_degree"))));
            		if (existsElement(putility.getProperty("edu_add_uiv_colg_degree"))){
            			 test.log(LogStatus.INFO, "Education details page", "Usage: <span style='font-weight:bold;'>Education details page found,details will be entered now</span>"); 
                        clickElement(test, putility.getProperty("edu_add_uiv_colg_degree"), "edu_add_uiv_colg_degree_link", testCaseId);
                        ProcessingRE2E();
                        SetData(test,putility.getProperty("edu_country"), Edu_Country);
                        ProcessingRE2E();
                        SetData(test,putility.getProperty("edu_state"), Edu_State);
                        ProcessingRE2E();
                        SetData(test,putility.getProperty("edu_city"), Edu_City);
                        ProcessingRE2E();
                        SetData(test,putility.getProperty("edu_degree"), Edu_Degree);
                        ProcessingRE2E();
                        SetData(test,putility.getProperty("edu_major"), Edu_Major);
                        ProcessingRE2E();
                        SetData(test,putility.getProperty("edu_school"), Edu_School);
                        ProcessingRE2E();
                        if (Edu_Graduated.equalsIgnoreCase("Yes"))
                        clickElement(test,putility.getProperty("edu_graduated_radiobtn"), "edu_graduated_radiobtn", testCaseId);
                        ProcessingRE2E();
                        chooseDate(test, Edu_Grad_Dt, putility.getProperty("edu_anticipated_graduation_month"), putility.getProperty("edu_anticipated_graduation_year"), "Apply_to_JO", testCaseId);
                        ProcessingRE2E();
                        clickElement(test, putility.getProperty("edu_save_btn"), "edu_save_btn", testCaseId);
                        ProcessingRE2E();
        				WindowHandles();
                        if(existsElement(putility.getProperty("error_popup_okbtn"))){
                        	 default_content();
                             checkPopUp(extent,test, "Apply_to_JO", testCaseId, "Error while searching for the job");
                             default_content();
                             return false;	
                        }
                        else{
                        	default_content();
                        	clickElement(test, putility.getProperty("continue_to_step4_btn"), "continue_to_step4_btn", testCaseId);  
                        	ProcessingRE2E();
                            return true;
                        }	
                        
                    } 
            		else {
                		Log.error("Education details page not found");
                		test.log(LogStatus.FAIL, "Education details page", "Usage: <span style='font-weight:bold;'>Education details page not  found</span>");
                		excelUtils.setCellData("Apply_to_JO", "FAIL", testCaseId, "Result_Status");
                        excelUtils.setCellData("Apply_to_JO", "Education details page was not found so the details could not be entered", testCaseId,"Result_Errors");
                		return false;
                	}
            		
            	}catch (Exception e){
            		Log.error("Education details page error -"+e);
            		Reporter.log("Education details page error -"+e);
            		test.log(LogStatus.FAIL, "Education details page","Error occured in Education details page  - "+e );
            		return false;
            	}
            	
            }
            
            
            public boolean ApplyForInternalCandidate(ExtentReports extent,ExtentTest test,String JO,String Take_Retail_Assessment,String sheetName,String testCaseId ) throws Exception{
            	try{
                	WebDriverWait wait = new WebDriverWait(driver, 40);
            		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(putility.getProperty("internal_jobcode_searcbox"))));
                    //Searching for a job to apply
            		if(existsElement(putility.getProperty("internal_jobcode_searcbox"))){
            			test.log(LogStatus.INFO, "Internal Candidate Job Search Details Page","Usage: <span style='font-weight:bold;'>Internal Candidate Job Search Details Page found and starting Job Search</span>");
            			SetData(test, putility.getProperty("internal_jobcode_searcbox"), JO);
                        test.log(LogStatus.PASS, "Job ID should be entered","Job ID - '"+JO+"' enterd successsfully" );
                        ProcessingRE2E();
                        clickElement(test, putility.getProperty("internal_job_search_btn"), "internal_job_search_btn", testCaseId);
                        ProcessingRE2E();
                        WindowHandles();
                        if(existsElement(putility.getProperty("error_popup_okbtn"))){
                        	 default_content();
	                         checkPopUp(extent,test, "Apply_to_JO", testCaseId, "Error while searching for the job for Internal Candidate");
	                         default_content();
	                         return false;	
                        }
                        default_content();
                        ProcessingRE2E();
                        //Checking if the job does not exist in the application
                        if(existsElement(putility.getProperty("internal_no_job_found_link"))){
                        	test.log(LogStatus.FAIL, "No result found on searching for Job  - "+JO );
                        	test.log(LogStatus.FAIL, "Internal Candidate Job Search Page","Usage: <span style='font-weight:bold;'>No result found on searching for Job </span>");
                        	getScreenShot(driver,testCaseId, "No result found on searching for Job",test);
             	          	excelUtils.setCellData("Apply_to_JO", "FAIL", testCaseId, "Result_Status");
             	          	excelUtils.setCellData("Apply_to_JO", "No result found on searching for Job  - "+JO, testCaseId,"Result_Errors");
             	            //Changing back the frame
             	           throw new Exception("No result found on searching for Job  - "+JO);
                       }
                        WebDriverWait wait1 = new WebDriverWait(driver, 40);
                		 wait1.until(ExpectedConditions.elementToBeClickable(By.xpath(putility.getProperty("internal_searched_job_chkbox"))));
                        clickElement(test, putility.getProperty("internal_searched_job_chkbox"), "internal_searched_job_chkbox", testCaseId);
                        ProcessingRE2E();
                        clickElement(test, putility.getProperty("internal_apply_now_btn"), "internal_apply_now_btn", testCaseId);
                        ProcessingRE2E();
                        WindowHandles();
                        if(existsElement(putility.getProperty("error_popup_okbtn"))){
                        	WebElement element = driver.findElement(By.xpath(putility.getProperty("pop_up_confirmation_txt")));
            				String PopUp_Name = element.getText();
                        	 default_content();
	                         checkPopUp(extent,test, "Apply_to_JO", testCaseId, "Error while searching for the job for Internal Candidate");
	                         default_content();
	                         return false;	
                        }
                        else{ 
                        	 default_content();
                            rec_switchFrame(putility.getProperty("rec_mod_frame"));
                            if (existsElement(putility.getProperty("internal_confirmation_ok_btn"))){
                	            clickElement(test, putility.getProperty("internal_confirmation_ok_btn"),"internal_confirmation_ok_btn",testCaseId);
                	            //Changing back the frame
                	            default_content();
                            }
                        }
                        ProcessingRE2E();
                        sleep(5000);
                        ProcessingRE2E();
                        clickElement(test, putility.getProperty("internal_submit_btn"),"internal_submit_btn",testCaseId);
                        ProcessingRE2E();
                        
                        //Adding code for Associate candidates where need to handle two pop up.
                        WindowHandles();
                        if(existsElement(putility.getProperty("error_popup_okbtn"))){
                        	WebElement element = driver.findElement(By.xpath(putility.getProperty("pop_up_confirmation_txt")));
            				String PopUp_Name = element.getText();
            				if(PopUp_Name.contains("Exempt Status Message")){
            					System.out.println("Exempt Status Message id displayed now");
            					driver.findElement(By.xpath(putility.getProperty("error_popup_okbtn"))).click();
            					 default_content();
                                 rec_switchFrame(putility.getProperty("rec_mod_frame"));
                                 if (existsElement(putility.getProperty("internal_confirmation_ok_btn"))){
                     	            clickElement(test, putility.getProperty("internal_confirmation_ok_btn"),"internal_confirmation_ok_btn",testCaseId);
                     	            //Processing();
                     	            //Changing back the frame
                     	            default_content();
                                 }
                                 default_content();
            				}else{
            					System.out.println("Exempt Status Message NOT displayed ");
            				}
                        	
                        }
                        default_content();
                        
                        if(Take_Retail_Assessment.equalsIgnoreCase("Y")){
                        	checkRetailAssessment(test, testCaseId);
                        }
                        
                        if(existsElement(putility.getProperty("internal_application_status")))
                       	 	return true;
                        else
                       	 	return false;
           		}
           		else {
               		Log.error("Internal Job Search page not found");
               		test.log(LogStatus.FAIL, "Internal Job Search page", "Usage: <span style='font-weight:bold;'>Internal Job Search page not found</span>");
               		excelUtils.setCellData("Apply_to_JO", "FAIL", testCaseId, "Result_Status");
                       excelUtils.setCellData("Apply_to_JO", "Internal Job search page was not found so the details could not be entered", testCaseId,"Result_Errors");
               		return false;
               	}
                  
                               
               }catch (Exception e){
                   Reporter.log("Error occured while searching for Internal job : "+JO+"  for test case -"+ testCaseId+"  and Exception is : "+ e);
                   Log.info("Error occured while searching for Internal job : "+JO+"  for test case -"+ testCaseId+"  and Exception is : "+ e);
                   test.log(LogStatus.FAIL,"Error occured while searching for Internal job : "+JO+"  for test case -"+ testCaseId+"  and Exception is : "+ e);
                   excelUtils.setCellData("Apply_to_JO", "FAIL", testCaseId, "Result_Status");
                   excelUtils.setCellData("Apply_to_JO", "Error occured while searching for Internal job : "+JO+"  for test case -"+ testCaseId+"  and Exception is : "+ e, testCaseId, "Result_Errors");
                   throw new Exception("Error occured while searching for Internal job : "+JO+"  for test case -"+ testCaseId+"  and Exception is : "+ e);
		 
               }
            	
            }

            public void answerQATab(ExtentTest test) throws Exception{
    			try{
    				WebDriverWait wait = new WebDriverWait(driver, 40);
            		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(putility.getProperty("question_element_table"))));
    				List<WebElement> allTD=driver.findElements(By.xpath(putility.getProperty("question_element_table")));
    				int length = allTD.size();
    				System.out.println("Total Number of questions in Q&A Page is : " +length);	
    		
    				for(int i = 0; i< length;i++){
    					WebElement question_element = driver.findElement(By.xpath(putility.getProperty("question_element")+i+"')]"));
    					String question = question_element.getText();
    					System.out.println("question is : "+question);
    					String excelanswer = excelUtils.getQuestionAnswer(question);
    					System.out.println("Answer for the question from excel is : "+excelanswer);
    					List<WebElement> allanswer=driver.findElements(By.xpath(putility.getProperty("all_answer")+i+"')]//table//tr//td//div//span)"));
    					int answer_length = allanswer.size();
        				System.out.println("Total Number of answers are  : " +answer_length);	
    					for(int j = 1; j<= answer_length;j++){
    						WebElement firstanswer_element = driver.findElement(By.xpath(putility.getProperty("all_answer")+i+"')]//table//tr//td//div//span)["+j+"]"));
    						String answer = firstanswer_element.getText().trim();
    						if(excelanswer.equalsIgnoreCase(answer)){
    							test.log(LogStatus.PASS, "Question - "+question,"Answer -"+answer );       
    							driver.findElement(By.xpath(putility.getProperty("all_answer")+i+"')]//table//tr//td//div//input)["+j+"]")).click();
    							Processing();
    						}
    					}
    					
    				}
    				
    			}catch(Exception e){
    				System.out.println("Exception -- "+e);
    			}
    			

    		}
		
		
		public boolean applyToJobForm3(ExtentReports extent,ExtentTest test,String Form3_Gender,String Form3_Hispanic,String Form3_Race,String Form3_Veteran,String Form3_Disability_Link,String Disability_Question,String sheetName,String testCaseId ) throws Exception{
			try{
				WebDriverWait wait = new WebDriverWait(driver, 40);
	    		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(putility.getProperty("form3_gender"))));
	    		if(existsElement(putility.getProperty("form3_gender"))){
	    			SelectDropDown(test, putility.getProperty("form3_gender"), Form3_Gender, "Apply_to_JO", testCaseId);	
	    			ProcessingRE2E();
	    			SelectDropDown(test, putility.getProperty("form3_hispanic"), Form3_Hispanic, "Apply_to_JO", testCaseId);
	    			ProcessingRE2E();
	    			SelectDropDown(test, putility.getProperty("form3_race"), Form3_Race, "Apply_to_JO", testCaseId);
	    			ProcessingRE2E();
	    			SelectDropDown(test, putility.getProperty("form3_veteran"), Form3_Veteran, "Apply_to_JO", testCaseId);
	    			ProcessingRE2E();
	    			if(Form3_Disability_Link.equalsIgnoreCase("Y")){
	    				clickElement(test, putility.getProperty("form3_disability_link"),"form3_disability_link",testCaseId);
	    				ProcessingRE2E();
	    				sleep(5000);
	    				SelectDropDown(test, putility.getProperty("form3_disability_ques"), Disability_Question, "Apply_to_JO", testCaseId);
	    				ProcessingRE2E();
	    				clickElement(test, putility.getProperty("form3_disability_page_save_btn"),"form3_disability_page_save_btn",testCaseId);
	    				ProcessingRE2E();
	    			}
	    			
	    			ProcessingRE2E();
	    			clickElement(test, putility.getProperty("form3_continue_btn"),"form3_continue_btn",testCaseId);
	    			ProcessingRE2E();
	    			WindowHandles();
	                if(existsElement(putility.getProperty("error_popup_okbtn"))){
	                 	 default_content();
	                     checkPopUp(extent,test, "Apply_to_JO", testCaseId, "Error while performing action of Form 3 page of Apply to JO");
	                     default_content();
	                     return false;	
	                }
	                default_content();
	                return true;
	    		}
	    		else{
	    			Log.error("Form 3 page of plly to JO not found");
               		test.log(LogStatus.FAIL, "Form 3 page of Aplly to JO", "Usage: <span style='font-weight:bold;'>Form 3 page of Aplly to JO not found</span>");
               		excelUtils.setCellData("Apply_to_JO", "FAIL", testCaseId, "Result_Status");
                    excelUtils.setCellData("Apply_to_JO", "Form 3 page of Aplly to JO not found in application", testCaseId,"Result_Errors");
               		return false;
	    		}
	    		
				
			}catch(Exception e){
				Reporter.log("Error occured in Form 3 Page of Apply to JO for Exxternal candidate  - "+ e);
                Log.info("Error occured in Form 3 Page of Apply to JO for Exxternal candidate  - "+ e);
                test.log(LogStatus.FAIL,"Error occured in Form 3 Page of Apply to JO for Exxternal candidate  - "+ e);
                excelUtils.setCellData("Apply_to_JO", "FAIL", testCaseId, "Result_Status");
                excelUtils.setCellData("Apply_to_JO", "Error occured in Form 3 Page of Apply to JO for Exxternal candidate  - "+ e, testCaseId, "Result_Errors");
                throw new Exception("Error occured in Form 3 Page of Apply to JO for Exxternal candidate  - "+ e);
			}
			
		}
		
		
		public void checkRetailAssessment(ExtentTest test,String testCaseId) throws Exception{
			try{
				WindowHandles();
	        	if(existsElement(putility.getProperty("error_popup_okbtn"))){
					//String popUp = getText("//div[@id='alertmsg']//span");
	           	 	clickElement(test, putility.getProperty(putility.getProperty("error_popup_okbtn")), "Retail Assessment Ok Button", testCaseId);
	                default_content();	
	                test.log(LogStatus.PASS, "Apply to JO- Take Reatil Assessment", "Usage: <span style='font-weight:bold;'>Reatils assessment done sucessfully</span>");
	           }
	        	else{
	        		default_content();
	        		test.log(LogStatus.FAIL, "Apply to JO- Take Reatil Assessment", "Usage: <span style='font-weight:bold;'>Reatils assessment could not be done</span>");
	        	}
				
			}catch(Exception e){
				System.out.println("Exceptio   -"+e);
			}
			
		}
		
		
		
		public void newWindowHandles()
		{	sleep(45000);
			try{

				for ( String handle : driver.getWindowHandles()) {
					driver.switchTo().window(handle);
				}

			} catch (Exception e){
				System.out.println("New Handle window error - "+e);

			}

		} 
		
		
		
		public boolean searchApplicant_ManageHire(ExtentReports extent,ExtentTest test,String APPLID,String sheetName,String testCaseId ) throws Exception{
			try{
				WebDriverWait wait = new WebDriverWait(driver, 40);
	    		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(putility.getProperty("applicant_id"))));
	    		//Entering Applicant ID
	    		if(existsElement(putility.getProperty("applicant_id"))){
	    			SetData(test,putility.getProperty("applicant_id"),APPLID);  
					ProcessingRE2E();
					
					//Selecting blank from drop down
					DeSelectDropDown(test,putility.getProperty("status_dropdown"),"","Manage_Hire",testCaseId);
					//Clearing cell value from Start and End date field
					driver.findElement(By.xpath(putility.getProperty("start_date_from"))).clear();
					ProcessingRE2E();
					driver.findElement(By.xpath(putility.getProperty("end_date"))).clear();
					ProcessingRE2E();
					
					clickElement(test, putility.getProperty("refresh_btn"), "refresh_btn", testCaseId);
					ProcessingRE2E();
					WindowHandles();
	                if(existsElement(putility.getProperty("error_popup_okbtn"))){
	                 	 default_content();
	                     checkPopUp(extent,test, "Apply_to_JO", testCaseId, "Error while performing action Applicant search Page of Manage Hire");
	                     default_content();
	                     return false;	
	                }
	                default_content();
	                SwitchFrames("ptifrmtgtframe");
					if(existsElement(putility.getProperty("applicant_name_link"))){
						clickElement(test, putility.getProperty("applicant_name_link"), "applicant_name_link", testCaseId);
						return true;
					}
					else
						return false;
					
	    		}
	    		else{
	    			Log.error("Applicant search Page of Manage Hire not found");
               		test.log(LogStatus.FAIL, "Applicant search Page of Manage Hire", "Usage: <span style='font-weight:bold;'>Applicant search Page of Manage Hire not found</span>");
               		excelUtils.setCellData("Manage_Hire", "FAIL", testCaseId, "Result_Status");
                    excelUtils.setCellData("Manage_Hire", "Applicant search Page of Manage Hire not found", testCaseId,"Result_Errors");
               		return false;
	    		}
				
			}catch(Exception e){
				Reporter.log("Error occured in Applicant search Page of Manage Hire  - "+ e);
                Log.info("Error occured in Applicant search Page of Manage Hire  - "+ e);
                test.log(LogStatus.FAIL,"Error occured in Applicant search Page of Manage Hire  - "+ e);
                excelUtils.setCellData("Manage_Hire", "FAIL", testCaseId, "Result_Status");
                excelUtils.setCellData("Manage_Hire", "Error occured in Applicant search Page of Manage Hire  - "+ e, testCaseId, "Result_Errors");
                throw new Exception("Error occured in Applicant search Page of Manage Hire  - "+ e);
			}
			
			
		}
		
		
		public boolean waitForElement(String objPath){
			try{
				WebDriverWait wait = new WebDriverWait(driver, 40);
	    		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(objPath)));
	    		return true;
			}
			catch(Exception e){
				//Reporter.log("Error occured on waiting for the element to appear  - "+ e);
                //Log.info("Error occured on waiting for the element to appear  - "+ e);
                return false;
			}
		}
		
		public void actionMHR(ExtentTest test,String testCaseId ) throws Exception{
			 SetData(test, putility.getProperty("desired_start_date"), GetCurrentSystemDate());
				//Clicking on Add Job btn
				 
				 clickElement(test, putility.getProperty("add_job_btn"), "add_job_btn", testCaseId);
				 sleep(20000);
				 for(String winHandle : driver.getWindowHandles()){
				  	    driver.switchTo().window(winHandle);
				  	  }
		}
		
		public WebElement getCurrSelectedDropDownVal(String objPath){
			 Select sel = new Select(driver.findElement(By.xpath(objPath)));
			  WebElement strCurrentValue = sel.getFirstSelectedOption();
			  //Print the Currently selected value
			  System.out.println(strCurrentValue);
			  return strCurrentValue;
		}
		
		public void SelectDropDownByIndex(ExtentTest test,String objPath, int ind,String sheetName,String testCaseId) throws Exception{
			try{
				if ( ind > 0 ){
					Select oSelect = new Select(driver.findElement(By.xpath(objPath)));				
					oSelect.selectByIndex(ind);
					Processing();
					Log.info("Selected - '"+ ind +"' value from the dropdown");
					test.log(LogStatus.PASS, "Drop down should be selected","Drop down value -'"+ind+"'- selected sucessfully");
					Reporter.log("Selected - '"+ind+"' from the dropdown");
				}
			} catch (Exception e){
				getScreenShot(driver,testCaseId, "Selecting - '"+ind +"' value",test);
				Log.error("Staffing :: Dropdown selection failed..." +e);	
				test.log(LogStatus.FAIL,"Drop down should be selected", "Drop down value -'"+ind+"'-  could not be selected because -"+e);
				Reporter.log("Value - '"+ind+"' from the dropdown could not be selected   - "+e);
				excelUtils.setCellData(sheetName, "FAIL", testCaseId, "Result_Status");
				excelUtils.setCellData(sheetName, ""+e, testCaseId, "Result_Errors");
			}
			
		}

		
		
		public void takeScreenShot(ExtentTest test, String sheetName,String testCaseId, String testStep) throws Exception{
			getScreenShot(driver,testCaseId, testStep,test);
			
		}		
		
}

*/