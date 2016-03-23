package com.newt.selenium.utlis;
/*package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import constants.ApplicationConstants;
import constants.MyConstants;

public class DataBaseConnect implements ApplicationConstants {

	PropertyUtility putility = new PropertyUtility(OBJECT_REPO_FILEPATH);
	// Connection object
	static Connection con = null;
	// Statement object
	private static Statement stmt;

	// Constant for Database URL
	public String DB_URL = putility.getProperty("DB_URL");

	// Constant for Database Username	
	public String DB_USER = putility.getProperty("DB_USER");

	// Constant for Database Password
	public String DB_PASSWORD = putility.getProperty("DB_PSD");

	OldUtils utils = new OldUtils();

	public void DBConnect() {
		try {
			String dbClass = "oracle.jdbc.driver.OracleDriver";
			Class.forName(dbClass).newInstance();
			Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			stmt = con.createStatement();
		} catch (Exception e) {
			System.out.println("Staffing :: App DB connection failed () :: Exception:" + e);
		}
	}
	
	public void clearData() throws Exception {
		ExcelUtils excelUtil = new ExcelUtils();	
		excelUtil.clearResultData("Create_JO","Result_Status","Result_Errors","Result_JO");
		excelUtil.clearResultData("Prepare_for_Hire","Result_Status","Result_Errors" , "");
		excelUtil.clearResultData("Recruiter_e2e_Action","Result_Status","Result_Errors","");
		excelUtil.clearResultData("Apply_to_JO","Result_Status","Result_Errors","");
		excelUtil.clearResultData("Manage_Hire","Result_Status","Result_Errors","");
		//excelUtil.clearResultData("Manage_Hire","","" , "");
	}
	
	public void loadDataCreateJO() throws Exception {
		String currentTestCase = "";
		XMLParserUtil xmlUtil = new XMLParserUtil();
		ArrayList<String> testCases = xmlUtil.getAllTestCases(TESTNGPATH);
		boolean flag = false;
		try {
			Date startTime = new Date();
			DBConnect();
			ExcelUtils excelUtil = new ExcelUtils();					
			InputStream inp = new FileInputStream(DATA_FILEPATH);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheet("Create_JO");
			for (int i = 0; i < testCases.size(); i++) {
				currentTestCase = testCases.get(i);
				String query = excelUtil.getExcelData(sheet, testCases.get(i), "SQL");
				if (query != null && query.trim().length() > 0) {
					flag = true;
					ResultSet res = stmt.executeQuery(query);
					if (res.next()) {
						if (hasColumn(res, "SUPERVISOR_ID"))
							excelUtil.setCellData(sheet, res.getString("SUPERVISOR_ID"), testCases.get(i),
									"Hiring_Manager");
						if (hasColumn(res, "B_SCOPE"))
							excelUtil.setCellData(sheet, res.getString("B_SCOPE"), testCases.get(i), "Scope");
						if (hasColumn(res, "BUSINESS_UNIT"))
							excelUtil.setCellData(sheet, res.getString("BUSINESS_UNIT"), testCases.get(i),
									"Business_Unit");
						if (hasColumn(res, "LOCATION"))
							excelUtil.setCellData(sheet, res.getString("LOCATION"), testCases.get(i), "Location");
						if (hasColumn(res, "UNION_CD"))
							excelUtil.setCellData(sheet, res.getString("UNION_CD"), testCases.get(i), "Union_Code");
						if (hasColumn(res, "LABOR_AGREEMENT"))
							excelUtil.setCellData(sheet, res.getString("LABOR_AGREEMENT"), testCases.get(i), "CBA");
						if (hasColumn(res, "JOBCODE"))
							excelUtil.setCellData(sheet, res.getString("JOBCODE"), testCases.get(i), "Job_Code");
						if (hasColumn(res, "COMPANY"))
							excelUtil.setCellData(sheet, res.getString("COMPANY"), testCases.get(i), "Company");
						if (hasColumn(res, "DEPTID"))
							excelUtil.setCellData(sheet, res.getString("DEPTID"), testCases.get(i), "Department");
						if (hasColumn(res, "EMPLID"))
							excelUtil.setCellData(sheet, res.getString("EMPLID"), testCases.get(i),
									"Employees_Being_Replaced");
						if (hasColumn(res, "HRS_JOB_OPENING_ID"))
							excelUtil.setCellData(sheet, res.getString("HRS_JOB_OPENING_ID"), testCases.get(i), "JO");
						if (hasColumn(res, "MANAGER_ID"))
							excelUtil.setCellData(sheet, res.getString("MANAGER_ID"), testCases.get(i),
									"Hiring_Manager");
						if (hasColumn(res, "HRS_PRM_JOBCODE"))
							excelUtil.setCellData(sheet, res.getString("HRS_PRM_JOBCODE"), testCases.get(i),
									"Job_Code");
						if (hasColumn(res, "RECRUITER_ID"))
							excelUtil.setCellData(sheet, res.getString("RECRUITER_ID"), testCases.get(i),
									"Add_Recruiter");
					}
					else{
						excelUtil.setCellData(sheet, "FAIL", testCases.get(i), "Result_Status");
						excelUtil.setCellData(sheet, putility.getProperty("DB_NO_ROW_ERROR"), testCases.get(i), "Result_Errors");
					}					
					res.close();
				}
			}
			if (flag){
				FileOutputStream fileOut = new FileOutputStream(DATA_FILEPATH);
				wb.write(fileOut);
				fileOut.close();
				Date endTime = new Date();
				System.out.println(
					"Total time taken in secs for loadDataCreateJO : " + utils.calcullateTime(startTime, endTime));
			}
		} catch (Exception e) {
			MyConstants.executeTestCase = false;
			System.out.println("loadDataCreateJO:: Error occured in Create JO query on test case: "
					+ currentTestCase + " :: Exception:" + e);
		} finally {
			if (con != null)
				con.close();
			if (stmt != null)
				stmt.close();
		}
	}

	public void loadDataPrepareForHire() throws Exception {
		String currentTestCase = "";
		XMLParserUtil xmlUtil = new XMLParserUtil();
		ArrayList<String> testCases = xmlUtil.getAllTestCases(TESTNGPATH);
		boolean flag = false;
		try {
			Date startTime = new Date();
			DBConnect();
			ExcelUtils excelUtil = new ExcelUtils();
			InputStream inp = new FileInputStream(DATA_FILEPATH);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheet("Prepare_for_Hire");
			for (int i = 0; i < testCases.size(); i++) {
				currentTestCase = testCases.get(i);
				String query = excelUtil.getExcelData(sheet, testCases.get(i), "SQL 1");
				if (query != null && query.trim().length() > 0) {
					flag = true;
					ResultSet res = stmt.executeQuery(query);
					if (res.next()) {
						if (hasColumn(res, "HRS_JOB_OPENING_ID"))
							excelUtil.setCellData(sheet, res.getString("HRS_JOB_OPENING_ID"), testCases.get(i),
									"JobOpening_ID");
						if (hasColumn(res, "HRS_PERSON_ID"))
							excelUtil.setCellData(sheet, res.getString("HRS_PERSON_ID"), testCases.get(i),
									"ApplicantID");
						if (hasColumn(res, "COUNTRY"))
							excelUtil.setCellData(sheet, res.getString("COUNTRY"), testCases.get(i),
									"National_ID_Country");
					}
					else{
						excelUtil.setCellData(sheet, "FAIL", testCases.get(i), "Result_Status");
						excelUtil.setCellData(sheet, putility.getProperty("DB_NO_ROW_ERROR"), testCases.get(i), "Result_Errors"); 
					}					
					res.close();
				}
			}
			if (flag){
				FileOutputStream fileOut = new FileOutputStream(DATA_FILEPATH);
				wb.write(fileOut);
				fileOut.close();
				Date endTime = new Date();
				System.out.println("Total time taken in secs for loadDataPrepareForHire : "
					+ utils.calcullateTime(startTime, endTime));
			}
		} catch (Exception e) {
			MyConstants.executeTestCase = false;
			System.out
					.println("Staffing loadDataPrepareForHire:: Error occured in Prepare for Hire query on test case: "
							+ currentTestCase + " :: Exception:" + e);
		} finally {
			if (con != null)
				con.close();
			if (stmt != null)
				stmt.close();
		}
	}

	public void loadDataRecruiterE2EAction() throws Exception {
		String currentTestCase = "";
		XMLParserUtil xmlUtil = new XMLParserUtil();
		ArrayList<String> testCases = xmlUtil.getAllTestCases(TESTNGPATH);
		boolean flag = false;
		try {
			Date startTime = new Date();
			DBConnect();
			ExcelUtils excelUtil = new ExcelUtils();
			InputStream inp = new FileInputStream(DATA_FILEPATH);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheet("Recruiter_e2e_Action");
			for (int i = 0; i < testCases.size(); i++) {
				currentTestCase = testCases.get(i);
				String query = excelUtil.getExcelData(sheet, testCases.get(i), "SQL 1");
				if (query != null && query.trim().length() > 0) {
					flag = true;
					ResultSet res = stmt.executeQuery(query);
					if (res.next()) {
						if (hasColumn(res, "HRS_PERSON_ID"))
							excelUtil.setCellData(sheet, res.getString("HRS_PERSON_ID"), testCases.get(i),
									"ApplicantID");
						if (hasColumn(res, "HRS_JOB_OPENING_ID"))
							excelUtil.setCellData(sheet, res.getString("HRS_JOB_OPENING_ID"), testCases.get(i), "JobOpening_ID");						
						if (hasColumn(res, "EMPLID"))
							excelUtil.setCellData(sheet, res.getString("EMPLID"), testCases.get(i), "Employee_ID");
					}
					else{
						excelUtil.setCellData(sheet, "FAIL", testCases.get(i), "Result_Status");
						excelUtil.setCellData(sheet, putility.getProperty("DB_NO_ROW_ERROR"), testCases.get(i), "Result_Errors"); 
					}
					res.close();
				}
			}
			if (flag){
				FileOutputStream fileOut = new FileOutputStream(DATA_FILEPATH);
				wb.write(fileOut);
				fileOut.close();
				Date endTime = new Date();
				System.out.println("Total time taken in secs for loadDataRecruiterE2EAction : "
					+ utils.calcullateTime(startTime, endTime));
			}
		} catch (Exception e) {
			MyConstants.executeTestCase = false;
			System.out.println(
					"Staffing loadDataRecruiterE2EAction:: Error occured in Recruiter E2E action query on test case: "
							+ currentTestCase + " :: Exception:" + e);
		} finally {
			if (con != null)
				con.close();
			if (stmt != null)
				stmt.close();
		}
	}

	public void loadDataApplyJobOpening() throws Exception {
		String currentTestCase = "";
		XMLParserUtil xmlUtil = new XMLParserUtil();
		ArrayList<String> testCases = xmlUtil.getAllTestCases(TESTNGPATH);
		boolean flag = false;
		try {
			Date startTime = new Date();
			DBConnect();
			ExcelUtils excelUtil = new ExcelUtils();
			InputStream inp = new FileInputStream(DATA_FILEPATH);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheet("Apply_to_JO");
			for (int i = 0; i < testCases.size(); i++) {
				currentTestCase = testCases.get(i);
				String query = excelUtil.getExcelData(sheet, testCases.get(i), "SQL");
				if (query != null && query.trim().length() > 0) {
					flag = true;
					ResultSet res = stmt.executeQuery(query);
					if (res.next()) {
						if (hasColumn(res, "LOGIN/PWD")){
							excelUtil.setCellData(sheet, res.getString("LOGIN/PWD"), testCases.get(i),"Login");
							excelUtil.setCellData(sheet, res.getString("LOGIN/PWD"), testCases.get(i),"Pwd");
						}
					}
					else{
						excelUtil.setCellData(sheet, "FAIL", testCases.get(i), "Result_Status");
						excelUtil.setCellData(sheet, putility.getProperty("DB_NO_ROW_ERROR"), testCases.get(i), "Result_Errors"); 
					}					
					res.close();
				}
			}
			if (flag){
				FileOutputStream fileOut = new FileOutputStream(DATA_FILEPATH);
				wb.write(fileOut);
				fileOut.close();
				Date endTime = new Date();
				System.out.println("Total time taken in secs for loadDataApplyJobOpening : "
					+ utils.calcullateTime(startTime, endTime));
			}
		} catch (Exception e) {
			MyConstants.executeTestCase = false;
			System.out
					.println("Staffing loadDataApplyJobOpening:: Error occured in Apply for job opening query on test case: "
							+ currentTestCase + " :: Exception:" + e);
		} finally {
			if (con != null)
				con.close();
			if (stmt != null)
				stmt.close();
		}
	}
	
	
	public void loadDataManageHire() throws Exception {
		String currentTestCase = "";
		XMLParserUtil xmlUtil = new XMLParserUtil();
		ArrayList<String> testCases = xmlUtil.getAllTestCases(TESTNGPATH);
		boolean flag = false;
		try {
			Date startTime = new Date();
			DBConnect();
			ExcelUtils excelUtil = new ExcelUtils();
			InputStream inp = new FileInputStream(DATA_FILEPATH);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheet("Manage_Hire");
			for (int i = 0; i < testCases.size(); i++) {
				currentTestCase = testCases.get(i);
				String query = excelUtil.getExcelData(sheet, testCases.get(i), "SQL");
				if (query != null && query.trim().length() > 0) {
					flag = true;
					ResultSet res = stmt.executeQuery(query);
					if (res.next()) {
						if (hasColumn(res, "HRS_PERSON_ID"))
							excelUtil.setCellData(sheet, res.getString("HRS_PERSON_ID"), testCases.get(i),
									"APPLID");					
						if (hasColumn(res, "EMPLID"))
							excelUtil.setCellData(sheet, res.getString("EMPLID"), testCases.get(i), "Emplid");
					}
					else{
						excelUtil.setCellData(sheet, "FAIL", testCases.get(i), "Result_Status");
						excelUtil.setCellData(sheet, putility.getProperty("DB_NO_ROW_ERROR"), testCases.get(i), "Result_Errors"); 
					}					
					res.close();
				}
			}
			if (flag){
				FileOutputStream fileOut = new FileOutputStream(DATA_FILEPATH);
				wb.write(fileOut);
				fileOut.close();
				Date endTime = new Date();
				System.out.println("Total time taken in secs for loadDataManageHire : "
					+ utils.calcullateTime(startTime, endTime));
			}
		} catch (Exception e) {
			MyConstants.executeTestCase = false;
			System.out
					.println("Staffing loadDataApplyJobOpening:: Error occured in Apply for job opening query on test case: "
							+ currentTestCase + " :: Exception:" + e);
		} finally {
			if (con != null)
				con.close();
			if (stmt != null)
				stmt.close();
		}
	}	
	
	
	public boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columns = rsmd.getColumnCount();
		for (int x = 1; x <= columns; x++) {
			if (columnName.equals(rsmd.getColumnName(x)))
				return true;
		}
		return false;
	}

}*/